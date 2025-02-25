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
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import kwee.library.ApplicationMessages;
import kwee.ofxLibrary.OfxFilter;
import kwee.ofxLibrary.OfxFunctions;
import kwee.ofxLibrary.OfxMetaAccounts;
import kwee.ofxLibrary.OfxMetaInfo;
import kwee.ofxLibrary.OfxPairTransaction;
import kwee.ofxLibrary.OfxTransaction;
import kwee.ofxLibrary.doc.OfxDocument;

/**
 * 
 * 
 * @author René
 *
 */
public class ActionConvertTransactions extends SwingWorker<Void, String> implements MyAppendable {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  private Object lock = GUILayout.lock;
  private JTextArea area = new JTextArea(30, 50);

  private ApplicationMessages bundle = ApplicationMessages.getInstance();

  private String m_OutputDir = "";
  private boolean m_Interrest = true;
  private String m_FilterName = "";
  private String m_Suffix = "";

  private JProgressBar m_ProgressBar;
  private JLabel m_Progresslabel;
  private int m_Processed = 0;
  private int m_Number = -1;

  private List<OfxTransaction> m_OfxTransactions = new LinkedList<OfxTransaction>();
  private Map<String, OfxMetaInfo> m_metainfo = new HashMap<String, OfxMetaInfo>();

  /**
   * Constructor for Java. For conversion transactions to OFX and write to
   * file(s).
   * 
   * @param a_OfxTransactions List of OFX Transactions.
   * @param a_metainfo
   * @param a_files
   * @param a_OutputFolder
   * @param a_SeparateOFX
   * @param a_Interrest
   * @param a_ProgressBar
   * @param a_Progresslabel
   */
  public ActionConvertTransactions(List<OfxTransaction> a_OfxTransactions, Map<String, OfxMetaInfo> a_metainfo,
      File[] a_files, String a_OutputFolder, boolean a_SeparateOFX, boolean a_Interrest, JProgressBar a_ProgressBar,
      JLabel a_Progresslabel) {
    m_OfxTransactions = a_OfxTransactions;
    m_metainfo = a_metainfo;

    m_OutputDir = a_OutputFolder;
    m_Interrest = a_Interrest;
    m_Suffix = "";

    m_ProgressBar = a_ProgressBar;
    m_Progresslabel = a_Progresslabel;

    if (m_OutputDir.isBlank()) {
      if (a_files.length > 0) {
        m_OutputDir = a_files[0].getPath();
      }
    }
    if (m_Interrest) {
      FilterInterestTransactions();
      m_FilterName = "Rente";
    }
  }

  void FilterInterestTransactions() {
    OfxFilter a_OfxFilter = new OfxFilter("Rente");
    List<OfxTransaction> l_OfxTransactions = new LinkedList<OfxTransaction>();
    l_OfxTransactions.clear();
    m_OfxTransactions.forEach(transaction -> {
      if (!a_OfxFilter.filter(transaction)) {
        l_OfxTransactions.add(transaction);
      }
    });
    m_OfxTransactions = l_OfxTransactions;
  }

  @Override
  protected Void doInBackground() throws Exception {
    synchronized (lock) {
      LOGGER.log(Level.INFO, bundle.getMessage("StartConversions"));
      OfxPairTransaction l_pairs = new OfxPairTransaction(m_OfxTransactions, m_ProgressBar, m_Progresslabel);
      m_OfxTransactions = l_pairs.pair();

      m_Progresslabel.setVisible(true);
      m_ProgressBar.setVisible(true);

      OfxMetaAccounts l_OfxMetaAccounts = new OfxMetaAccounts(m_OfxTransactions, m_metainfo);
      Set<String> l_accounts = l_OfxMetaAccounts.getAccounts();

      m_Processed = -1;
      m_Number = l_accounts.size();
      m_ProgressBar.setMaximum(m_Number);
      verwerkProgress();

      m_Suffix = "";
      l_accounts.forEach(l_account -> {
        LOGGER.log(Level.INFO, bundle.getMessage("ConvertFor", l_account));

        OfxMetaInfo l_OfxMetaInfo = l_OfxMetaAccounts.getOfxMetaInfo(l_account);
        Map<String, OfxMetaInfo> l_metainfo = new HashMap<String, OfxMetaInfo>();
        l_metainfo.put(l_account, l_OfxMetaInfo);

        List<OfxTransaction> l_OfxTransactions = new LinkedList<OfxTransaction>(
            l_OfxMetaAccounts.getTransactions(l_account));
        String l_Size = Integer.toString(l_OfxTransactions.size());
        LOGGER.log(Level.INFO, bundle.getMessage("ConvertAccount", l_account, l_Size));
        OfxMetaInfo l_info = l_OfxMetaAccounts.getOfxMetaInfo(l_account);
        String l_prefix = l_info.getPrefix();
        String l_suffix = l_info.getSuffix();
        if (!l_suffix.isBlank()) {
          m_Suffix = l_suffix;
        }
        String l_filename = "";
        if (!l_prefix.isBlank()) {
          l_filename = m_OutputDir + "\\" + String.join("_", l_prefix, l_account, l_suffix);
          if (!m_FilterName.isBlank()) {
            l_filename = String.join("_", l_filename, m_FilterName);
          }
          l_filename = l_filename + ".ofx";
          LOGGER.log(Level.INFO, bundle.getMessage("OFXFilename", l_filename));
        } else {
          l_filename = m_OutputDir + "\\" + String.join("_", l_account, l_suffix) + ".ofx";
          LOGGER.log(Level.INFO, bundle.getMessage("OFXFilename", l_filename));
        }

        OfxDocument l_document = new OfxDocument();
        l_document.populateAccountResponseMessage(l_OfxMetaInfo, l_OfxTransactions);
        l_document.createDocument(l_filename);

        verwerkProgress();
      });

      String l_outputfilename = m_OutputDir + "/_Saldos_" + m_Suffix + ".csv";
      LOGGER.log(Level.INFO, bundle.getMessage("SaldosFilename", l_outputfilename));
      OfxFunctions.dumpMetaInfo(l_outputfilename, m_metainfo);

      m_Progresslabel.setVisible(false);
      m_ProgressBar.setVisible(false);
      LOGGER.log(Level.INFO, bundle.getMessage("ConversionsDone"));
    }
    return null;
  }

  @Override
  public void append(String text) {
    area.append(text);
  }

  @Override
  protected void done() {
    LOGGER.log(Level.INFO, "");
    LOGGER.log(Level.INFO, bundle.getMessage("Done"));
  }

  /**
   * Display progress processed transactions.
   */
  private void verwerkProgress() {
    m_Processed++;
    try {
      m_ProgressBar.setValue(m_Processed);
      Double v_prog = ((double) m_Processed / (double) m_Number) * 100;
      Integer v_iprog = v_prog.intValue();
      m_Progresslabel.setText(bundle.getMessage("ProgressAccounts", v_iprog, m_Processed, m_Number));
    } catch (Exception e) {
      // Do nothing
    }
  }
}

/**
 * 
 * @author René
 *
 */
interface MyAppendable {
  public void append(String text);
}
