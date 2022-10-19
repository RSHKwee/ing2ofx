package ofxLibrary;

/**
 * OFX Transactions handling.
 * 
 * @author René
 *
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

//import ofxLibrary.BankTransactions;
//import ing2ofx.convertor.IngTransactions;

public class OfxXmlTransactions {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

  private List<OfxTransaction> m_OfxTransactions = new LinkedList<OfxTransaction>();
  private Map<String, OfxMetaInfo> m_metainfo = new HashMap<String, OfxMetaInfo>();
  private Map<String, ArrayList<String>> m_OfxAcounts = new LinkedHashMap<String, ArrayList<String>>();

  private ArrayList<String> m_Regels = new ArrayList<String>();
  private OfxMetaAccounts m_OfxMetaAccounts;

  /**
   * Constructor
   * 
   * @param a_file CSV file with ING transactions.
   */
  public OfxXmlTransactions(List<OfxTransaction> a_OfxTransactions) {
    m_OfxTransactions = a_OfxTransactions;
    m_OfxMetaAccounts = new OfxMetaAccounts(m_OfxTransactions);
    m_metainfo = m_OfxMetaAccounts.getAccountsOfxMetaInfo();
    m_Regels.clear();
  }

  /**
   * Get the OFX meta information of all processed accounts.
   * 
   * @return OFX meta information of all accounts.
   */
  public Map<String, OfxMetaInfo> getOfxMetaInfo() {
    return m_metainfo;
  }

  /**
   * Return Account information of the processed transactions.
   * 
   * @return Account information
   */
  public Map<String, ArrayList<String>> getAccountTransactions() {
    return m_OfxAcounts;
  }

  /**
   * OFX XML header for OFX transactions of an account and certain period.
   * 
   * @param account Account
   * @param mindate Start date of period
   * @param maxdate End date of period
   * @return List of lines with the XML content for a header
   */
  private ArrayList<String> OfxXmlTransactionsHeader(String bankcode, String account, String mindate, String maxdate) {
    ArrayList<String> l_regels = new ArrayList<String>();
    l_regels.add("      <STMTRS>                            <!-- Begin statement response -->");
    l_regels.add("         <CURDEF>EUR</CURDEF>");
    l_regels.add("         <BANKACCTFROM>                   <!-- Identify the account -->");
    l_regels.add("            <BANKID>" + bankcode + "</BANKID>     <!-- Routing transit or other FI ID -->");
    l_regels.add("            <ACCTID>" + account + "</ACCTID>  <!-- Account number -->");
    l_regels.add("            <ACCTTYPE>CHECKING</ACCTTYPE> <!-- Account type -->");
    l_regels.add("         </BANKACCTFROM>                  <!-- End of account ID -->");
    l_regels.add("         <BANKTRANLIST>                   <!-- Begin list of statement trans. -->");
    l_regels.add("            <DTSTART>" + mindate + "</DTSTART>");
    l_regels.add("            <DTEND>" + maxdate + "</DTEND>");
    return l_regels;
  }

  /**
   * OFX XML footer for OFX transactions of an account and certain period.
   * 
   * @param saldonatran Balance at end of period
   * @param maxdate     End date period
   * @return List of lines with the XML content for a footer
   */
  private ArrayList<String> OfxXmlTransactionsFooter(String saldonatran, String maxdate) {
    ArrayList<String> l_regels = new ArrayList<String>();
    l_regels.add("         </BANKTRANLIST>                   <!-- End list of statement trans. -->");
    l_regels.add("         <LEDGERBAL>                       <!-- Ledger balance aggregate -->");
    l_regels.add("            <BALAMT>" + saldonatran + "</BALAMT>");
    l_regels.add(
        "            <DTASOF>" + maxdate + "2359</DTASOF>  <!-- Bal date: Last date in transactions, 11:59 pm -->");
    l_regels.add("         </LEDGERBAL>                      <!-- End ledger balance -->");
    l_regels.add("      </STMTRS>");
    return l_regels;
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

  int m_NumberOfTransactions = 0;

  /**
   * 
   */
  public ArrayList<String> OfxXmlTransactionsBody() {
    Set<String> accounts = m_metainfo.keySet();
    accounts.forEach(account -> {
      OfxMetaInfo l_metainfo = m_metainfo.get(account);
      ArrayList<String> l_regelshead = new ArrayList<String>();
      l_regelshead = OfxXmlTransactionsHeader(l_metainfo.getBankcode(), account, l_metainfo.getMinDate(),
          l_metainfo.getMaxDate());
      m_OfxAcounts.put(account, l_regelshead);
      m_NumberOfTransactions = 0;

      LOGGER.log(Level.INFO, "");
      LOGGER.log(Level.INFO, "Process account:           " + account);
      m_OfxTransactions.forEach(transaction -> {
        ArrayList<String> l_regelstrans = new ArrayList<String>();

        l_regelstrans = OfxXmlTransaction(transaction);
        ArrayList<String> prevregels = m_OfxAcounts.get(account);
        prevregels.addAll(l_regelstrans);
        m_OfxAcounts.put(account, prevregels);
        m_NumberOfTransactions++;
      });

      ArrayList<String> l_regelsfoot = new ArrayList<String>();
      l_regelsfoot = OfxXmlTransactionsFooter(l_metainfo.getBalanceAfterTransaction(), l_metainfo.getBalanceDate());

      ArrayList<String> prevregels = m_OfxAcounts.get(account);
      prevregels.addAll(l_regelsfoot);
      m_Regels.addAll(prevregels);
      LOGGER.log(Level.INFO, "Transactions processed: " + Integer.toString(m_NumberOfTransactions));
    });
    LOGGER.log(Level.FINE, "");
    return m_Regels;
  }
}
