package ing2ofx.convertor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/*
   * @formatter:off
   * for each account:

         
    * for trns in csv.transactions:
    *  if trns['account'] == account:
         <STMTRS>                            <!-- Begin statement response -->
            <CURDEF>EUR</CURDEF>
            <BANKACCTFROM>                   <!-- Identify the account -->
               <BANKID>INGBNL2A</BANKID>     <!-- Routing transit or other FI ID -->
               <ACCTID>%(account)s</ACCTID>  <!-- Account number -->
               <ACCTTYPE>CHECKING</ACCTTYPE> <!-- Account type -->
            </BANKACCTFROM>                  <!-- End of account ID -->
            <BANKTRANLIST>                   <!-- Begin list of statement trans. -->
               <DTSTART>%(mindate)s</DTSTART>
               <DTEND>%(maxdate)s</DTEND>""" % {"account": account, "mindate": mindate, "maxdate": maxdate}
         
   * 
   * See OfxTransaction v:
               <STMTTRN>                          <!-- Begin statement response -->
                ......
               </STMTTRN>""" % {"maxdate": maxdate}
   * OfxTransaction ^
   
            </BANKTRANLIST>                   <!-- End list of statement trans. -->
            <LEDGERBAL>                       <!-- Ledger balance aggregate -->
               <BALAMT>""" + saldonatran + """</BALAMT>
               <DTASOF>%(maxdate)s2359</DTASOF>  <!-- Bal date: Last date in transactions, 11:59 pm -->
            </LEDGERBAL>                      <!-- End ledger balance -->
         </STMTRS>""" % {"maxdate": maxdate}
         
   * Each account:
      </STMTTRNRS>                        <!-- End of transaction -->
  
   * @formatter:on
   */
public class OfxTransactions {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  private String C_BankCode = "INGBNL2A";

  private List<OfxTransaction> m_OfxTransactions = new LinkedList<OfxTransaction>();
  public Map<String, OfxMetaInfo> m_metainfo = new HashMap<String, OfxMetaInfo>();
  public Map<String, ArrayList<String>> m_OfxAcounts = new LinkedHashMap<String, ArrayList<String>>();

  private File m_file;

  public OfxTransactions(File a_file) {
    m_file = a_file;
  }

  public void load() {
    IngTransactions l_transactions = new IngTransactions(m_file);
    l_transactions.load();
    m_OfxTransactions = l_transactions.getOfxTransactions();
    m_metainfo = l_transactions.getOfxMetaInfo();
  }

  public Map<String, OfxMetaInfo> getOfxMetaInfo() {
    return m_metainfo;
  }

  public Map<String, ArrayList<String>> getAccountTransactions() {
    return m_OfxAcounts;
  }

  public ArrayList<String> OfxXmlTransactionsHeader(String account, String mindate, String maxdate) {
    ArrayList<String> l_regels = new ArrayList<String>();
    l_regels.add("         <STMTRS>                            <!-- Begin statement response -->");
    l_regels.add("            <CURDEF>EUR</CURDEF>");
    l_regels.add("            <BANKACCTFROM>                   <!-- Identify the account -->");
    l_regels.add("               <BANKID>" + C_BankCode + "</BANKID>     <!-- Routing transit or other FI ID -->");
    l_regels.add("               <ACCTID>" + account + "</ACCTID>  <!-- Account number -->");
    l_regels.add("               <ACCTTYPE>CHECKING</ACCTTYPE> <!-- Account type -->");
    l_regels.add("            </BANKACCTFROM>                  <!-- End of account ID -->");
    l_regels.add("            <BANKTRANLIST>                   <!-- Begin list of statement trans. -->");
    l_regels.add("               <DTSTART>" + mindate + "</DTSTART>");
    l_regels.add("               <DTEND>" + maxdate + "</DTEND>");
    return l_regels;
  }

  public ArrayList<String> OfxXmlTransactionsFooter(String saldonatran, String maxdate) {
    ArrayList<String> l_regels = new ArrayList<String>();
    l_regels.add("            </BANKTRANLIST>                   <!-- End list of statement trans. -->");
    l_regels.add("            <LEDGERBAL>                       <!-- Ledger balance aggregate -->");
    l_regels.add("               <BALAMT>" + saldonatran + "</BALAMT>");
    l_regels.add(
        "               <DTASOF>" + maxdate + "2359</DTASOF>  <!-- Bal date: Last date in transactions, 11:59 pm -->");
    l_regels.add("            </LEDGERBAL>                      <!-- End ledger balance -->");
    l_regels.add("         </STMTRS>");
    return l_regels;
  }

  public void OfxXmlTransactionsForAccounts() {
    OfxXmlTransactionsForAccounts("");
  }

  public void OfxXmlTransactionsForAccounts(String a_FilterName) {
    Set<String> accounts = m_metainfo.keySet();
    accounts.forEach(account -> {
      OfxMetaInfo l_metainfo = m_metainfo.get(account);
      ArrayList<String> l_regelshead = new ArrayList<String>();
      l_regelshead = OfxXmlTransactionsHeader(account, l_metainfo.getMinDate(), l_metainfo.getMaxDate());

      m_OfxAcounts.put(account, l_regelshead);

      m_OfxTransactions.forEach(transaction -> {
        ArrayList<String> l_regelstrans = new ArrayList<String>();
        if ((transaction.getName().equalsIgnoreCase(a_FilterName)) || (a_FilterName.isBlank())) {
          l_regelstrans = transaction.OfxXmlTransaction();
          ArrayList<String> prevregels = m_OfxAcounts.get(account);
          prevregels.addAll(l_regelstrans);
          m_OfxAcounts.put(account, prevregels);
        }
      });

      ArrayList<String> l_regelsfoot = new ArrayList<String>();
      l_regelsfoot = OfxXmlTransactionsFooter(l_metainfo.getBalanceAfterTransaction(), l_metainfo.getMaxDate());

      ArrayList<String> prevregels = m_OfxAcounts.get(account);
      prevregels.addAll(l_regelsfoot);
      m_OfxAcounts.put(account, prevregels);
    });
  }
}
