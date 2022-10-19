package ing2ofx.gui;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;

import ing2ofx.convertor.IngTransactions;
import ofxLibrary.OfxTransaction;
import sns2ofx.convertor.SnsTransactions;

public class ActionReadTransactions {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  private List<OfxTransaction> m_OfxTransactions = new LinkedList<OfxTransaction>();

  private File[] m_CSVFiles = null;
  private boolean m_TransactionProcessed = false;

  public ActionReadTransactions(File[] a_CSVFiles) {
    m_CSVFiles = a_CSVFiles;
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
        m_TransactionProcessed = true;
      }

      // Read SNS Transactions
      if (l_ext.toUpperCase().contains("XML")) {
        LOGGER.log(Level.INFO, "Process SNS file " + l_File);
        SnsTransactions l_snstrans = new SnsTransactions(m_CSVFiles[i]);
        l_snstrans.load();
        m_OfxTransactions.addAll(l_snstrans.getOfxTransactions());
        m_TransactionProcessed = true;
      }
      LOGGER.log(Level.INFO, "Processed file " + l_File);
    }
    return m_OfxTransactions;
  }

  public boolean TransactionsProcessed() {
    return m_TransactionProcessed;
  }
}
