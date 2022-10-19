package ing2ofx.gui;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import ofxLibrary.OfxDocument;
import ofxLibrary.OfxFilter;
import ofxLibrary.OfxMetaAccounts;
import ofxLibrary.OfxMetaInfo;
import ofxLibrary.OfxPairTransaction;
import ofxLibrary.OfxTransaction;
//import ofxLibrary.OfxXmlTransactions;

/**
 * 
 * 
 * @author René
 *
 */
public class ActionPerformScript extends SwingWorker<Void, String> implements MyAppendable {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  private JTextArea area = new JTextArea(30, 50);

//  private File[] m_Files = null;
  private String m_OutputDir = "";
  private boolean m_SeparateOFX = true;
  private boolean m_Interrest = true;
  private String m_FilterName = "";

  private List<OfxTransaction> m_OfxTransactions = new LinkedList<OfxTransaction>();

  /**
   * Constructor for Java.
   * 
   * @param a_CSVFile      CSV input file.
   * @param a_OutputFile   OFX output file.
   * @param a_OutputFolder OFX output directory.
   * @param a_SeparateOFX  All accounts in separate OFX files or all in one.
   * @param a_Interrest    Only interest transactions in OFX file(s).
   */
  public ActionPerformScript(List<OfxTransaction> a_OfxTransactions, File[] a_files, String a_OutputFolder,
      boolean a_SeparateOFX, boolean a_Interrest) {
    m_OfxTransactions = a_OfxTransactions;
    // m_Files = a_files;
    m_OutputDir = a_OutputFolder;
    m_SeparateOFX = a_SeparateOFX;
    m_Interrest = a_Interrest;

    if (m_OutputDir.isBlank()) {
      if (a_files.length > 0) {
        m_OutputDir = a_files[0].getPath();
      }
    }
    if (m_Interrest) {
      FilterInterestTransactions();
      m_FilterName = "Rente";
    }
    OfxPairTransaction l_pairs = new OfxPairTransaction(m_OfxTransactions);
    m_OfxTransactions = l_pairs.pair();
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
    LOGGER.log(Level.INFO, "Start conversion (java).");

    OfxMetaAccounts l_OfxMetaAccounts = new OfxMetaAccounts(m_OfxTransactions);
    Set<String> l_accounts = l_OfxMetaAccounts.getAccounts();

    if (m_SeparateOFX) {
      l_accounts.forEach(l_account -> {
        List<OfxTransaction> l_OfxTransactions = new LinkedList<OfxTransaction>();
        l_OfxTransactions = l_OfxMetaAccounts.getTransactions(l_account);

        OfxMetaInfo l_info = l_OfxMetaAccounts.getOfxMetaInfo(l_account);
        String l_prefix = l_info.getPrefix();
        String l_filename = "";
        if (!l_prefix.isBlank()) {
          l_filename = m_OutputDir + "\\" + String.join("_", l_prefix, l_account);
          if (!m_FilterName.isBlank()) {
            l_filename = String.join("_", l_filename, m_FilterName);
          }
          l_filename = String.join("_", l_filename, ".ofx");
        } else {
          l_filename = m_OutputDir + "\\" + String.join("_", l_account, ".ofx");
        }

        OfxDocument l_document = new OfxDocument(l_OfxTransactions);
        l_document.CreateOfxDocument(l_filename);
      });
    } else {
      OfxDocument l_document = new OfxDocument(m_OfxTransactions);
      String l_outputfilename = m_OutputDir + "\\AllTransactions.ofx";
      l_document.CreateOfxDocument(l_outputfilename);
    }

    LOGGER.log(Level.INFO, "End conversion.");
    return null;
  }

  @Override
  public void append(String text) {
    area.append(text);
  }

  @Override
  protected void done() {
    LOGGER.log(Level.INFO, "");
    LOGGER.log(Level.INFO, "Done.");
  }
}

interface MyAppendable {
  public void append(String text);
}
