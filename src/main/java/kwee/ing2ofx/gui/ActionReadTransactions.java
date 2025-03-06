package kwee.ing2ofx.gui;

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
import javax.swing.SwingWorker;

import org.apache.commons.io.FilenameUtils;

import kwee.convertor.ing.convertor.IngTransactions;
import kwee.ofxLibrary.OfxMetaInfo;
import kwee.ofxLibrary.OfxTransaction;
import kwee.convertor.sns.convertor.*;
import kwee.library.ApplicationMessages;
import kwee.logger.MyLogger;

/**
 * Read transactions from file(s).
 * 
 * @author René
 *
 */
public class ActionReadTransactions extends SwingWorker<List<OfxTransaction>, String> {
  private static final Logger LOGGER = MyLogger.getLogger();
  private Object lock = GUILayout.lock;

  private ApplicationMessages bundle = ApplicationMessages.getInstance();

  private List<OfxTransaction> m_OfxTransactions = new LinkedList<OfxTransaction>();
  private Map<String, OfxMetaInfo> m_metainfo = new HashMap<String, OfxMetaInfo>();

  private File[] m_CSVFiles = null;
  private boolean m_TransactionProcessed = false;
//  private File m_Synonym_file;

  private JProgressBar m_ProgressBar;
  private JLabel m_Progresslabel;
  private int m_Processed = -1;
  private int m_Number = 0;

  /**
   * Constructor initialize variables.
   * 
   * @param a_Synonym_file
   * @param a_CSVFiles      List of files to be processed.
   * @param a_ProgressBar
   * @param a_Progresslabel
   */
  public ActionReadTransactions(File[] a_CSVFiles, JProgressBar a_ProgressBar, JLabel a_Progresslabel) {
    m_CSVFiles = a_CSVFiles;
    m_OfxTransactions.clear();
    m_TransactionProcessed = false;
//    m_Synonym_file = a_Synonym_file;

    m_ProgressBar = a_ProgressBar;
    m_Progresslabel = a_Progresslabel;

    m_Processed = -1;
    m_Number = m_CSVFiles.length;
    m_ProgressBar.setMaximum(m_Number);
    m_Progresslabel.setVisible(true);
    m_ProgressBar.setVisible(true);
    verwerkProgress();
  }

  /**
   * Do the processing, read transactions from files.
   * 
   * @return
   */
  @Override
  public List<OfxTransaction> doInBackground() {
    synchronized (lock) {
      for (int i = 0; i < m_CSVFiles.length; i++) {
        String l_File = m_CSVFiles[i].getAbsolutePath();
        String l_ext = FilenameUtils.getExtension(l_File);

        // Read ING Transactions
        if (l_ext.toUpperCase().contains("CSV")) {
          LOGGER.log(Level.INFO, bundle.getMessage("ProcessFile", "ING", l_File));
          IngTransactions l_ingtrans = new IngTransactions(m_CSVFiles[i]);
          l_ingtrans.load();
          m_OfxTransactions.addAll(l_ingtrans.getOfxTransactions());
          LOGGER.log(Level.INFO, bundle.getMessage("ProcessFile", "ING", m_OfxTransactions.size()));

          Map<String, OfxMetaInfo> l_metainfo = l_ingtrans.getOfxMetaInfo();
          m_metainfo = updateMetaInfo(l_metainfo);

          m_TransactionProcessed = true;
        }

        // Read SNS Transactions
        if (l_ext.toUpperCase().contains("XML")) {
          LOGGER.log(Level.INFO, bundle.getMessage("ProcessFile", "SNS", l_File));
          SnsTransactions l_snstrans = new SnsTransactions(m_CSVFiles[i]);
          l_snstrans.load();
          m_OfxTransactions.addAll(l_snstrans.getOfxTransactions());
          LOGGER.log(Level.INFO, bundle.getMessage("ProcessFile", "SNS", m_OfxTransactions.size()));

          Map<String, OfxMetaInfo> l_metainfo = l_snstrans.getOfxMetaInfo();
          m_metainfo = updateMetaInfo(l_metainfo);

          m_TransactionProcessed = true;
        }
        verwerkProgress();
        LOGGER.log(Level.INFO, bundle.getMessage("ProcessedFile", l_File));
      }
    }
    return m_OfxTransactions;
  }

  @Override
  protected void done() {
    m_Progresslabel.setVisible(false);
    m_ProgressBar.setVisible(false);

    LOGGER.log(Level.INFO, "");
    LOGGER.log(Level.INFO, bundle.getMessage("Done"));
  }

  /**
   * Check if Transactions are processed (read from file(s)).
   * 
   * @return If true then Transactions are read.
   */
  public boolean TransactionsProcessed() {
    return m_TransactionProcessed;
  }

  /**
   * Get MetaInfo of processed transactions.
   * 
   * @return MetaInfo
   */
  public Map<String, OfxMetaInfo> getOfxMetaInfo() {
    return m_metainfo;
  }

  /**
   * Merge MetaInfo's
   * 
   * @param a_metainfo
   * @return MetaInfo
   */
  private Map<String, OfxMetaInfo> updateMetaInfo(Map<String, OfxMetaInfo> a_metainfo) {
    Map<String, OfxMetaInfo> l_metainfo = m_metainfo;
    Set<String> keys = a_metainfo.keySet();
    keys.forEach(key -> {
      OfxMetaInfo ll_metainfo = a_metainfo.get(key);
      l_metainfo.put(key, ll_metainfo);
    });
    return l_metainfo;
  }

  /**
   * Display progress processed files.
   */
  private void verwerkProgress() {
    m_Progresslabel.setVisible(true);
    m_ProgressBar.setVisible(true);
    m_Processed++;
    try {
      m_ProgressBar.setValue(m_Processed);
      m_ProgressBar.paintImmediately(m_ProgressBar.getVisibleRect());
      Double v_prog = ((double) m_Processed / (double) m_Number) * 100;
      int v_iprog = v_prog.intValue();

      m_Progresslabel.setText(bundle.getMessage("Progress", v_iprog, m_Processed, m_Number));
      m_Progresslabel.paintImmediately(m_Progresslabel.getVisibleRect());
    } catch (Exception e) {
      // Do nothing
    }
  }
}
