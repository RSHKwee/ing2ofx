package convertor.ing.convertor;

/**
 * Convert ING transactions to OFX transactions.
 * 
 * @author René
 *
 */
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
//import java.util.regex.Pattern;
//import java.util.regex.Matcher;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import convertor.ing.ingLibrary.IngSavingTransaction;
import convertor.ing.ingLibrary.IngTransaction;
import ofxLibrary.OfxTransaction;
import ofxLibrary.OfxFunctions;
import ofxLibrary.OfxMetaInfo;

import library.FileUtils;

public class IngTransactions {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

  private CSVReader m_reader;
  private File m_File;
  private boolean m_saving = false;
  private char m_separator = ';';
  private Set<String> m_UniqueIds = new LinkedHashSet<>();
  private String m_FileName = "";

  private List<IngTransaction> m_Transactions;
  private List<IngSavingTransaction> m_SavingTransactions;
  private List<OfxTransaction> m_OfxTransactions = new LinkedList<OfxTransaction>();
  private Map<String, OfxMetaInfo> m_metainfo = new HashMap<String, OfxMetaInfo>();
  private File m_Synonym_file;

  /**
   * Constructor.
   * 
   * @param a_file CSV File with ING transactions
   */
  public IngTransactions(File a_file, File a_Synonym_file) {
    m_File = a_file;
    m_FileName = FileUtils.getFileNameWithoutExtension(a_file);
    m_Synonym_file = a_Synonym_file;
  }

  /**
   * Determine type of ING transactions (saving or normal). <br>
   * Process the transactions and convert them to OFX transactions.
   */
  public void load() {
    try {
      m_reader = new CSVReaderBuilder(new FileReader(m_File))
          .withCSVParser(new CSVParserBuilder().withSeparator(m_separator).build()).build();
      String[] nextLine;
      nextLine = m_reader.readNext();
      if (nextLine.length <= 1) {
        m_separator = ',';
        m_reader = new CSVReaderBuilder(new FileReader(m_File))
            .withCSVParser(new CSVParserBuilder().withSeparator(m_separator).build()).build();
        nextLine = m_reader.readNext();
        if (nextLine.length <= 1) {
          LOGGER.log(Level.INFO, "Unknown separator in file " + m_File.getAbsolutePath());
        }
      }
      if (nextLine[1].equalsIgnoreCase("Naam / Omschrijving")) {
        m_saving = false;
      } else {
        m_saving = true;
      }

      if (m_saving) {
        HeaderColumnNameMappingStrategy<IngSavingTransaction> beanStrategy = new HeaderColumnNameMappingStrategy<IngSavingTransaction>();
        beanStrategy.setType(IngSavingTransaction.class);
        m_SavingTransactions = new CsvToBeanBuilder<IngSavingTransaction>(new FileReader(m_File))
            .withSeparator(m_separator).withMappingStrategy(beanStrategy).build().parse();
        m_Transactions = null;

        m_SavingTransactions.forEach(l_trans -> {
          OfxTransaction l_ofxtrans;
          l_ofxtrans = Ing2OfxTransaction.convertSavingToOfx(l_trans);

          String l_fitid = OfxFunctions.createUniqueId(l_ofxtrans, m_UniqueIds);
          m_UniqueIds.add(l_fitid);
          l_ofxtrans.setFitid(l_fitid);

          l_ofxtrans.setSaving(true);
          l_ofxtrans.setSource(m_FileName);

          if (m_metainfo.containsKey(l_ofxtrans.getAccount())) {
            updateOfxMetaInfo(l_ofxtrans, l_trans.getSaldo_na_mutatie());
          } else {
            createOfxMetaInfo(l_ofxtrans, l_trans.getSaldo_na_mutatie());
          }
          m_OfxTransactions.add(l_ofxtrans);
        });
      } else {
        HeaderColumnNameMappingStrategy<IngTransaction> beanStrategy = new HeaderColumnNameMappingStrategy<IngTransaction>();
        beanStrategy.setType(IngTransaction.class);
        m_Transactions = new CsvToBeanBuilder<IngTransaction>(new FileReader(m_File)).withSeparator(m_separator)
            .withMappingStrategy(beanStrategy).build().parse();
        m_SavingTransactions = null;

        m_Transactions.forEach(l_trans -> {
          OfxTransaction l_ofxtrans;
          l_ofxtrans = Ing2OfxTransaction.convertToOfx(l_trans);

          String l_fitid = OfxFunctions.createUniqueId(l_ofxtrans, m_UniqueIds);
          m_UniqueIds.add(l_fitid);
          l_ofxtrans.setFitid(l_fitid);

          l_ofxtrans.setSaving(false);
          l_ofxtrans.setSource(m_FileName);

          if (m_metainfo.containsKey(l_ofxtrans.getAccount())) {
            updateOfxMetaInfo(l_ofxtrans, l_trans.getSaldo_na_mutatie());
          } else {
            createOfxMetaInfo(l_ofxtrans, l_trans.getSaldo_na_mutatie());
          }
          m_OfxTransactions.add(l_ofxtrans);
        });
      }
      LOGGER.log(Level.INFO, "Transactions read: " + Integer.toString(m_OfxTransactions.size()));
    } catch (IOException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
  }

  /**
   * Returns true when the processed transactions are saving transactions.
   * 
   * @return True for Saving transactions
   */
  public boolean isSavingCsvFile() {
    return m_saving;
  }

  /**
   * Return a list of normal transactions or null when savings transactions are
   * processed.
   * 
   * @return List of normal transactions
   */
  public List<IngTransaction> getIngTransactions() {
    return m_Transactions;
  }

  /**
   * Return a list of saving transactions or null when normal transactions are
   * processed.
   * 
   * @return List of saving transactions
   */
  public List<IngSavingTransaction> getIngSavingTransactions() {
    return m_SavingTransactions;
  }

  /**
   * Return a list of OFX transactions.
   * 
   * @return List of saving transactions
   */
  public List<OfxTransaction> getOfxTransactions() {
    return m_OfxTransactions;
  }

  /**
   * Returns meta information of the OFX transactions.
   * 
   * @return OFX Meta information
   */
  public Map<String, OfxMetaInfo> getOfxMetaInfo() {
    return m_metainfo;
  }

  public Set<String> getUniqueIds() {
    return m_UniqueIds;
  }

  /**
   * Update meta information of OFX Transactions.
   * 
   * @param a_OfxTransaction OFX Transaction
   * @param a_SaldoNaMutatie Balance after transaction
   */
  private void updateOfxMetaInfo(OfxTransaction a_OfxTransaction, String a_SaldoNaMutatie) {
    OfxMetaInfo l_meta = m_metainfo.get(a_OfxTransaction.getAccount());
    try {
      String sDtPosted = a_OfxTransaction.getDtposted();
      l_meta.setMaxDate(sDtPosted);
      if (l_meta.getMaxDate().equalsIgnoreCase(sDtPosted)) {
        if (l_meta.getBalanceAfterTransaction().isBlank()) {
          l_meta.setBalanceAfterTransaction(a_SaldoNaMutatie);
        }
      }
      l_meta.setMaxDate(sDtPosted);
      l_meta.setMinDate(sDtPosted);
      if (a_OfxTransaction.isSaving()) {
        if (!a_OfxTransaction.getAccountto().isBlank()) {
          l_meta.setPrefix(a_OfxTransaction.getAccountto());
        }
      }
      l_meta.setMaxDate(sDtPosted);
      l_meta.setMinDate(sDtPosted);
      l_meta.setSuffix(a_OfxTransaction.getSource());
      m_metainfo.put(a_OfxTransaction.getAccount(), l_meta);
    } catch (Exception e) {
    }
  }

  /**
   * Store meta information of OFX Transactions.
   * 
   * @param a_OfxTransaction OFX Transaction
   * @param a_SaldoNaMutatie Balance after transaction
   */
  private void createOfxMetaInfo(OfxTransaction a_OfxTransaction, String a_SaldoNaMutatie) {
    String l_bankcode = a_OfxTransaction.getBankCode();
    OfxMetaInfo l_meta = new OfxMetaInfo(l_bankcode, m_Synonym_file);
    l_meta.setSuffix(a_OfxTransaction.getSource());
    l_meta.setAccount(a_OfxTransaction.getAccount());
    String sDtPosted = a_OfxTransaction.getDtposted();
    l_meta.setMaxDate(sDtPosted);
    l_meta.setBalanceAfterTransaction(a_SaldoNaMutatie);

    l_meta.setMaxDate(sDtPosted);
    l_meta.setMinDate(sDtPosted);
    if (a_OfxTransaction.isSaving()) {
      if (l_meta.getPrefix().isBlank()) {
        if (!a_OfxTransaction.getAccountto().isBlank()) {
          l_meta.setPrefix(a_OfxTransaction.getAccountto());
        }
      }
    }
    m_metainfo.put(a_OfxTransaction.getAccount(), l_meta);
  }
}
