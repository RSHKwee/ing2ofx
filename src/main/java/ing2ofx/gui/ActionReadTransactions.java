package ing2ofx.gui;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

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
  private File m_Synonym_file;

  private JProgressBar m_ProgressBar;
  private JLabel m_Progresslabel;
  private int m_Processed = 0;
  private int m_Number = -1;

  /**
   * Constructor initialize variables.
   * 
   * @param a_CSVFiles List of files to be processed.
   */
  public ActionReadTransactions(File a_Synonym_file, File[] a_CSVFiles, JProgressBar a_ProgressBar,
      JLabel a_Progresslabel) {
    m_CSVFiles = a_CSVFiles;
    m_OfxTransactions.clear();
    m_TransactionProcessed = false;
    m_Synonym_file = a_Synonym_file;

    m_ProgressBar = a_ProgressBar;
    m_Progresslabel = a_Progresslabel;
  }

  public List<OfxTransaction> execute() {
    m_Processed = -1;
    m_Number = m_CSVFiles.length;
    m_ProgressBar.setMaximum(m_Number);
    m_Progresslabel.setVisible(true);
    m_ProgressBar.setVisible(true);
    verwerkProgress();

    for (int i = 0; i < m_CSVFiles.length; i++) {
      String l_File = m_CSVFiles[i].getAbsolutePath();
      String l_ext = FilenameUtils.getExtension(l_File);

      // Read ING Transactions
      if (l_ext.toUpperCase().contains("CSV")) {
        LOGGER.log(Level.INFO, "Process ING file " + l_File);
        IngTransactions l_ingtrans = new IngTransactions(m_CSVFiles[i], m_Synonym_file);
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
        SnsTransactions l_snstrans = new SnsTransactions(m_CSVFiles[i], m_Synonym_file);
        l_snstrans.load();
        m_OfxTransactions.addAll(l_snstrans.getOfxTransactions());
        LOGGER.log(Level.INFO, "Total of (SNS) transactions read: " + m_OfxTransactions.size());

        Map<String, OfxMetaInfo> l_metainfo = l_snstrans.getOfxMetaInfo();
        m_metainfo = updateMetaInfo(l_metainfo);

        m_TransactionProcessed = true;
      }
      LOGGER.log(Level.INFO, "Processed file " + l_File);
    }
    m_Progresslabel.setVisible(false);
    m_ProgressBar.setVisible(false);
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

  /**
   * Display progress processed files.
   */
  private void verwerkProgress() {
    m_Processed++;
    try {
      m_ProgressBar.setValue(m_Processed);
      Double v_prog = ((double) m_Processed / (double) m_Number) * 100;
      Integer v_iprog = v_prog.intValue();
      m_Progresslabel.setText(v_iprog.toString() + "% (" + m_Processed + " of " + m_Number + " transactions)");
    } catch (Exception e) {
      // Do nothing
    }
  }
}
