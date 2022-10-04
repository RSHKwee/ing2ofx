package ofxLibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ofxXmlTransactionsForAccounts {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  private String m_BankCode = "";

  private List<OfxTransaction> m_OfxTransactions = new LinkedList<OfxTransaction>();
  private boolean m_Saving = false;

  public Map<String, OfxMetaInfo> m_metainfo = new HashMap<String, OfxMetaInfo>();
  public Map<String, ArrayList<String>> m_OfxAcounts = new LinkedHashMap<String, ArrayList<String>>();

  /**
   * 
   */
  public void OfxXmlTransactionsForAccounts() {
    OfxXmlTransactionsForAccounts(false, "");
  }

  /**
   * 
   * @param a_FilterName
   */
  public void OfxXmlTransactionsForAccounts(String a_FilterName) {
    OfxXmlTransactionsForAccounts(false, a_FilterName);
  }

  /**
   * 
   * @param a_AllInOne
   */
  public void OfxXmlTransactionsForAccounts(boolean a_AllInOne) {
    OfxXmlTransactionsForAccounts(a_AllInOne, "");
  }

  /**
   * 
   * @param a_AllInOne
   * @param a_FilterName
   */
  int m_NumberOfTransactions = 0;

  public void OfxXmlTransactionsForAccounts(boolean a_AllInOne, String a_FilterName) {
    Set<String> accounts = m_metainfo.keySet();
    accounts.forEach(account -> {
      OfxMetaInfo l_metainfo = m_metainfo.get(account);
      ArrayList<String> l_regelshead = new ArrayList<String>();
      l_regelshead = OfxXmlTransactionsHeader(account, l_metainfo.getMinDate(), l_metainfo.getMaxDate());
      m_OfxAcounts.put(account, l_regelshead);
      m_NumberOfTransactions = 0;

      LOGGER.log(Level.INFO, "");
      LOGGER.log(Level.INFO, "Process account:           " + account);
      m_OfxTransactions.forEach(transaction -> {
        if (a_AllInOne || (transaction.getAccount().equalsIgnoreCase(account))) {
          ArrayList<String> l_regelstrans = new ArrayList<String>();
          if (m_Saving) {
            if ((transaction.getName().equalsIgnoreCase(a_FilterName)) || a_FilterName.isBlank()) {
              l_regelstrans = transaction.OfxXmlTransaction();
              ArrayList<String> prevregels = m_OfxAcounts.get(account);
              prevregels.addAll(l_regelstrans);
              m_OfxAcounts.put(account, prevregels);
              m_NumberOfTransactions++;
            }
          } else {
            l_regelstrans = transaction.OfxXmlTransaction();
            ArrayList<String> prevregels = m_OfxAcounts.get(account);
            prevregels.addAll(l_regelstrans);
            m_OfxAcounts.put(account, prevregels);
            m_NumberOfTransactions++;
          }
        }
      });

      ArrayList<String> l_regelsfoot = new ArrayList<String>();
      l_regelsfoot = OfxXmlTransactionsFooter(l_metainfo.getBalanceAfterTransaction(), l_metainfo.getBalanceDate());

      ArrayList<String> prevregels = m_OfxAcounts.get(account);
      prevregels.addAll(l_regelsfoot);
      m_OfxAcounts.put(account, prevregels);
      LOGGER.log(Level.INFO, "Transactions processed: " + Integer.toString(m_NumberOfTransactions));
    });
    LOGGER.log(Level.FINE, "");
  }

}
