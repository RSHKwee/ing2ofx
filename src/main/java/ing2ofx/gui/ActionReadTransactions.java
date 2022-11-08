package ing2ofx.gui;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;

import convertor.ing.convertor.IngTransactions;
import ofxLibrary.OfxMetaInfo;
import ofxLibrary.OfxTransaction;
import convertor.sns.convertor.*;

/**
 * Read transactions from file(s).
 * 
 * @author René
 *
 */
public class ActionReadTransactions {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

  private List<OfxTransaction> m_OfxTransactions = new LinkedList<OfxTransaction>();
  private Map<String, OfxMetaInfo> m_metainfo = new HashMap<String, OfxMetaInfo>();

  private File[] m_CSVFiles = null;
  private boolean m_TransactionProcessed = false;
  private boolean m_ClearTransactions = true;

  /**
   * Constructor initialize variables.
   * 
   * @param a_CSVFiles          List of files to be processed.
   * @param a_ClearTransactions Clear, or not, the list of transactions.
   * @param a_OfxTransactions   List of processed transactions.
   */
  public ActionReadTransactions(File[] a_CSVFiles, boolean a_ClearTransactions,
      List<OfxTransaction> a_OfxTransactions) {
    m_CSVFiles = a_CSVFiles;
    m_ClearTransactions = a_ClearTransactions;
    if (m_ClearTransactions) {
      m_OfxTransactions.clear();
      m_TransactionProcessed = false;
    } else {
      m_OfxTransactions.addAll(a_OfxTransactions);
      m_TransactionProcessed = true;
    }
  }

  /**
   * Constructor initialize variables.
   * 
   * @param a_CSVFiles List of files to be processed.
   */
  public ActionReadTransactions(File[] a_CSVFiles) {
    m_CSVFiles = a_CSVFiles;
    m_ClearTransactions = true;
    m_OfxTransactions.clear();
    m_TransactionProcessed = false;
  }

  public List<OfxTransaction> execute() {
    for (int i = 0; i < m_CSVFiles.length; i++) {
      String l_File = m_CSVFiles[i].getAbsolutePath();
      String l_ext = FilenameUtils.getExtension(l_File);

      // Read ING Transactions
      if (l_ext.toUpperCase().contains("CSV")) {
        LOGGER.log(Level.INFO, "Process ING file " + l_File);
        IngTransactions l_ingtrans = new IngTransactions(m_CSVFiles[i]);
        l_ingtrans.load();
        m_OfxTransactions.addAll(l_ingtrans.getOfxTransactions());
        LOGGER.log(Level.INFO, "Total of (ING) transactions read: " + m_OfxTransactions.size());

        Map<String, OfxMetaInfo> l_metainfo = l_ingtrans.getOfxMetaInfo();
        m_metainfo = updateMetaInfo(l_metainfo);

        m_TransactionProcessed = true;
      }

      // Read SNS Transactions
      if (l_ext.toUpperCase().contains("XML")) {
        LOGGER.log(Level.INFO, "Process SNS file " + l_File);
        SnsTransactions l_snstrans = new SnsTransactions(m_CSVFiles[i]);
        l_snstrans.load();
        m_OfxTransactions.addAll(l_snstrans.getOfxTransactions());
        LOGGER.log(Level.INFO, "Total of (SNS) transactions read: " + m_OfxTransactions.size());

        Map<String, OfxMetaInfo> l_metainfo = l_snstrans.getOfxMetaInfo();
        m_metainfo = updateMetaInfo(l_metainfo);

        m_TransactionProcessed = true;
      }
      LOGGER.log(Level.INFO, "Processed file " + l_File);
    }
    return m_OfxTransactions;
  }

  public boolean TransactionsProcessed() {
    return m_TransactionProcessed;
  }

  public Map<String, OfxMetaInfo> getOfxMetaInfo() {
    return m_metainfo;
  }

  private Map<String, OfxMetaInfo> updateMetaInfo(Map<String, OfxMetaInfo> a_metainfo) {
    Map<String, OfxMetaInfo> l_metainfo = m_metainfo;
    Set<String> keys = a_metainfo.keySet();
    keys.forEach(key -> {
      if (l_metainfo.containsKey(key)) {
        OfxMetaInfo ll_metainfo = a_metainfo.get(key);
        // TODO: do something
        l_metainfo.put(key, ll_metainfo);
      } else {
        OfxMetaInfo ll_metainfo = a_metainfo.get(key);
        l_metainfo.put(key, ll_metainfo);
      }
    });
    return l_metainfo;
  }
}
