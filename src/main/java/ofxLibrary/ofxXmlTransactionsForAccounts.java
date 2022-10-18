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
      m_NumberOfTransactions = 0;

      LOGGER.log(Level.INFO, "");
      LOGGER.log(Level.INFO, "Process account:           " + account);
      m_OfxTransactions.forEach(transaction -> {
        if (a_AllInOne || (transaction.getAccount().equalsIgnoreCase(account))) {
          ArrayList<String> l_regelstrans = new ArrayList<String>();
          if (m_Saving) {
            if ((transaction.getName().equalsIgnoreCase(a_FilterName)) || a_FilterName.isBlank()) {
              l_regelstrans = OfxXmlTransaction(transaction);
              ArrayList<String> prevregels = m_OfxAcounts.get(account);
              prevregels.addAll(l_regelstrans);
              m_OfxAcounts.put(account, prevregels);
              m_NumberOfTransactions++;
            }
          } else {
            l_regelstrans = OfxXmlTransaction(transaction);
            ArrayList<String> prevregels = m_OfxAcounts.get(account);
            prevregels.addAll(l_regelstrans);
            m_OfxAcounts.put(account, prevregels);
            m_NumberOfTransactions++;
          }
        }
      });
      LOGGER.log(Level.INFO, "Transactions processed: " + Integer.toString(m_NumberOfTransactions));
    });
    LOGGER.log(Level.FINE, "");
  }

  private ArrayList<String> OfxXmlTransaction(OfxTransaction a_transaction) {
    ArrayList<String> l_regels = new ArrayList<String>();
    l_regels.add("               <STMTTRN>");
    l_regels.add("                  <TRNTYPE>" + a_transaction.getTrntype() + "</TRNTYPE>");
    l_regels.add("                  <DTPOSTED>" + a_transaction.getDtposted() + "</DTPOSTED>");
    l_regels.add("                  <TRNAMT>" + a_transaction.getTrnamt() + "</TRNAMT>");
    l_regels.add("                  <FITID>" + a_transaction.getFitid() + "</FITID>");
    l_regels.add("                  <NAME>" + a_transaction.getName() + "</NAME>");
    l_regels.add("                  <BANKACCTTO>");
    l_regels.add("                     <BANKID></BANKID>");
    l_regels.add("                     <ACCTID>" + a_transaction.getAccountto() + "</ACCTID>");
    l_regels.add("                     <ACCTTYPE>CHECKING</ACCTTYPE>");
    l_regels.add("                  </BANKACCTTO>");
    l_regels.add("                  <MEMO>" + a_transaction.getMemo() + "</MEMO>");
    l_regels.add("               </STMTTRN>");
    return l_regels;
  }

}
