package ing2ofx.convertor;

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

public class IngTransactions {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

  private CSVReader m_reader;
  private File m_File;
  private boolean m_saving = false;
  private char m_separator = ';';
  private Set<String> m_UniqueId = new LinkedHashSet<>();

  private List<IngTransaction> m_Transactions;
  private List<IngSavingTransaction> m_SavingTransactions;
  private List<OfxTransaction> m_OfxTransactions = new LinkedList<OfxTransaction>();
  private Map<String, OfxMetaInfo> m_metainfo = new HashMap<String, OfxMetaInfo>();

  /**
   * Constructor.
   * 
   * @param a_file CSV File with ING transactions
   */
  public IngTransactions(File a_file) {
    m_File = a_file;
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
      if (nextLine[nextLine.length - 1].equalsIgnoreCase("Tag")) {
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
          l_ofxtrans.setFitid(createUniqueId(l_ofxtrans));
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
          l_ofxtrans.setFitid(createUniqueId(l_ofxtrans));
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
      if (m_saving && (l_meta.getPrefix().isBlank())) {
        if (!a_OfxTransaction.getAccountto().isBlank()) {
          l_meta.setPrefix(a_OfxTransaction.getAccountto());
        }
      }
      l_meta.setMaxDate(sDtPosted);
      l_meta.setMinDate(sDtPosted);

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
    OfxMetaInfo l_meta = new OfxMetaInfo();
    l_meta.setAccount(a_OfxTransaction.getAccount());
    String sDtPosted = a_OfxTransaction.getDtposted();
    l_meta.setMaxDate(sDtPosted);
    if (l_meta.getMaxDate().equalsIgnoreCase(sDtPosted)) {
      if (l_meta.getBalanceAfterTransaction().isBlank()) {
        l_meta.setBalanceAfterTransaction(a_SaldoNaMutatie);
      }
    }
    l_meta.setMaxDate(sDtPosted);
    l_meta.setMinDate(sDtPosted);

    if (l_meta.getPrefix().isBlank()) {
      if (!a_OfxTransaction.getAccountto().isBlank()) {
        l_meta.setPrefix(a_OfxTransaction.getAccountto());
      }
    }
    m_metainfo.put(a_OfxTransaction.getAccount(), l_meta);
  }

  /**
   * Create a unique fitid for an OFX Transaction.
   * 
   * @param l_ofxtrans OFX Transaction
   * @return A unique fitid
   */
  private String createUniqueId(OfxTransaction l_ofxtrans) {
    String uniqueid = "";
    /*
     * @formatter:off
    String memo = l_ofxtrans.getMemo();
    String time = "";
     * time = "" matches = re.search("\s([0-9]{2}:[0-9]{2})\s", memo) if matches:
     * time = matches.group(1).replace(":", "")

    Pattern patt = Pattern.compile("([0-9]{2}:[0-9]{2})");
    Matcher matcher = patt.matcher(memo);
    if (matcher.find()) {
      time = matcher.group(1).replace(":", ""); // you can get it from desired index as well
    }
     * @formatter:on
    */
    String fitid = l_ofxtrans.getDtposted() + l_ofxtrans.getTrnamt().replace(",", "").replace("-", "").replace(".", "");
    uniqueid = fitid;
    if (m_UniqueId.contains(fitid)) {
      // # Make unique by adding time and sequence nr.
      int idcount = 0;
      uniqueid = fitid;
      while (m_UniqueId.contains(uniqueid)) {
        idcount = idcount + 1;
        // uniqueid = fitid + time + Integer.toString(idcount);
        uniqueid = fitid + Integer.toString(idcount);
      }
      m_UniqueId.add(uniqueid);
    } else {
      m_UniqueId.add(fitid);
    }
    return uniqueid;
  }
}
