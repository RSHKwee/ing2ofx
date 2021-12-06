package ing2ofx.convertor;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

public class IngTransactions {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

  private FileReader m_ingReader;
  private CSVReader m_reader;

  private boolean m_saving = false;
  private char m_separator = ';';
  private Set<String> m_UniqueId = new LinkedHashSet<>();

  private List<IngTransaction> m_Transactions;
  private List<IngSavingTransaction> m_SavingTransactions;
  private List<OfxTransaction> m_OfxTransactions;

  public IngTransactions(File a_file) {
    try {
      m_ingReader = new FileReader(a_file);
      m_reader = new CSVReaderBuilder(m_ingReader)
          .withCSVParser(new CSVParserBuilder().withSeparator(m_separator).build()).build();
      String[] nextLine;
      nextLine = m_reader.readNext();
      if (nextLine.length <= 1) {
        m_separator = ',';
        m_reader = new CSVReaderBuilder(m_ingReader)
            .withCSVParser(new CSVParserBuilder().withSeparator(m_separator).build()).build();
        nextLine = m_reader.readNext();
        if (nextLine.length <= 1) {
          LOGGER.log(Level.INFO, "Unknown separator in file " + a_file.getAbsolutePath());
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
        m_SavingTransactions = new CsvToBeanBuilder<IngSavingTransaction>(m_ingReader).withSeparator(m_separator)
            .withMappingStrategy(beanStrategy).build().parse();
        m_Transactions = null;

        m_SavingTransactions.forEach(l_trans -> {
          OfxTransaction l_ofxtrans;
          l_ofxtrans = Ing2OfxTransaction.convertSavingToOfx(l_trans);
          l_ofxtrans.setFitid(createUniqueId(l_ofxtrans));
          m_OfxTransactions.add(l_ofxtrans);
        });
      } else {
        HeaderColumnNameMappingStrategy<IngTransaction> beanStrategy = new HeaderColumnNameMappingStrategy<IngTransaction>();
        beanStrategy.setType(IngTransaction.class);
        m_Transactions = new CsvToBeanBuilder<IngTransaction>(m_ingReader).withSeparator(m_separator)
            .withMappingStrategy(beanStrategy).build().parse();
        m_SavingTransactions = null;

        m_Transactions.forEach(l_trans -> {
          OfxTransaction l_ofxtrans;
          l_ofxtrans = Ing2OfxTransaction.convertToOfx(l_trans);

          l_ofxtrans.setFitid(createUniqueId(l_ofxtrans));
          m_OfxTransactions.add(l_ofxtrans);
        });
      }
    } catch (IOException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
  }

  public boolean isSavingCsvFile() {
    return m_saving;
  }

  public List<IngTransaction> getIngTransactions() {
    return m_Transactions;
  }

  public List<IngSavingTransaction> getIngSavingTransactions() {
    return m_SavingTransactions;
  }

  public List<OfxTransaction> getOfxTransactions() {
    return m_OfxTransactions;
  }

  private String createUniqueId(OfxTransaction l_ofxtrans) {
    String uniqueid = "";
    String time = "";
    String memo = l_ofxtrans.getMemo();
    /*
     * time = "" matches = re.search("\s([0-9]{2}:[0-9]{2})\s", memo) if matches:
     * time = matches.group(1).replace(":", "")
     */
    Pattern patt = Pattern.compile("\s([0-9]{2}:[0-9]{2})\s");
    Matcher matcher = patt.matcher(memo);
    if (matcher.find()) {
      time = matcher.group(1).replace(":", ""); // you can get it from desired index as well
    }
    String fitid = l_ofxtrans.getDtposted() + l_ofxtrans.getTrnamt().replace(",", "").replace("-", "").replace(".", "");
    if (m_UniqueId.contains(fitid)) {
      // # Make unique by adding time and sequence nr.
      int idcount = 0;
      uniqueid = fitid;
      while (m_UniqueId.contains(uniqueid)) {
        idcount = idcount + 1;
        uniqueid = fitid + time + Integer.toString(idcount);
      }
      ;
    } else {
      m_UniqueId.add(fitid);
    }
    return uniqueid;
  }
}
