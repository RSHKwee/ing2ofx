package ing2ofx.gui;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import ofxLibrary.OfxDocument;
import ofxLibrary.OfxTransaction;
import ofxLibrary.OfxXmlTransactions;

/**
 * 
 * 
 * @author René
 *
 */
public class ActionPerformScript extends SwingWorker<Void, String> implements MyAppendable {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  private JTextArea area = new JTextArea(30, 50);

  private File[] m_CSVFiles = null;
  private String m_OutputFolder = "";
  private boolean m_SeparateOFX = true;
  private boolean m_Interrest = true;

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
  public ActionPerformScript(List<OfxTransaction> a_OfxTransactions, String a_OutputFolder, boolean a_SeparateOFX,
      boolean a_Interrest) {
    m_OfxTransactions = a_OfxTransactions;
    m_OutputFolder = a_OutputFolder;

    m_SeparateOFX = a_SeparateOFX;
    m_Interrest = a_Interrest;
  }

  @Override
  protected Void doInBackground() throws Exception {
    OfxDocument l_document;
    LOGGER.log(Level.INFO, "Start conversion (java).");

    OfxXmlTransactions l_xmlTransactions = new OfxXmlTransactions(m_OfxTransactions);
    l_xmlTransactions.getAccountTransactions();

    for (int i = 0; i < m_CSVFiles.length; i++) {
      String l_CSVFile = m_CSVFiles[i].getAbsolutePath();
      if (!m_OutputFolder.isBlank()) {
        l_document = new OfxDocument(C_BankCode, new File(l_CSVFile), m_OutputFolder, m_SeparateOFX);
      } else {
        l_document = new OfxDocument(C_BankCode, new File(l_CSVFile), m_SeparateOFX);
      }
      if (m_Interrest) {
        l_document.load("Rente");
      } else {
        l_document.load();
      }
      /*
       * if (m_OutputFile.equalsIgnoreCase("Output filename")) {
       * l_ingtrns.CreateOfxDocument(); } else {
       * l_ingtrns.CreateOfxDocument(m_OutputFile); }
       */
      l_document.CreateOfxDocument();

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
