package ing2ofx.convertor;

import java.util.ArrayList;
//import java.util.logging.Logger;

import com.opencsv.bean.CsvToBean;

public class OfxTransaction extends CsvToBean<Object> {
//  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  /*
 * @formatter:off
 *          
                <ACCTID>%(account)s</ACCTID>  <!-- Account number -->

               ofxfile.write(message_begin)

                for trns in csv.transactions:
                    if trns['account'] == account:
                        message_transaction = """
               <STMTTRN>
                  <TRNTYPE>%(trntype)s</TRNTYPE>
                  <DTPOSTED>%(dtposted)s</DTPOSTED>
                  <TRNAMT>%(trnamt)s</TRNAMT>
                  <FITID>%(fitid)s</FITID>
                  <NAME>%(name)s</NAME>
                  <BANKACCTTO>
                     <BANKID></BANKID>
                     <ACCTID>%(accountto)s</ACCTID>
                     <ACCTTYPE>CHECKING</ACCTTYPE>
                  </BANKACCTTO>
                  <MEMO>%(memo)s</MEMO>
               </STMTTRN>""" % trns
                        ofxfile.write(message_transaction)

 * @formatter:on
 */
  private String account = "";
  private String trntype = "";
  private String dtposted = "";
  private String trnamt = "";
  private String fitid = "";
  private String name = "";
  private String accountto = "";
  private String memo = "";

  public String getAccount() {
    return account;
  }

  public String getTrntype() {
    return trntype;
  }

  public String getDtposted() {
    return dtposted;
  }

  public String getTrnamt() {
    return trnamt;
  }

  public String getFitid() {
    return fitid;
  }

  public String getName() {
    return name;
  }

  public String getAccountto() {
    return accountto;
  }

  public String getMemo() {
    return memo;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public void setTrntype(String trntype) {
    this.trntype = trntype;
  }

  public void setDtposted(String dtposted) {
    this.dtposted = dtposted;
  }

  public void setTrnamt(String trnamt) {
    this.trnamt = trnamt;
  }

  public void setFitid(String fitid) {
    this.fitid = fitid;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAccountto(String accountto) {
    this.accountto = accountto;
  }

  public void setMemo(String memo) {
    this.memo = memo;
  }

  public ArrayList<String> OfxXmlTransaction() {
    ArrayList<String> l_regels = new ArrayList<String>();
    l_regels.add("               <STMTTRN>");
    l_regels.add("                  <TRNTYPE>" + trntype + "</TRNTYPE>");
    l_regels.add("                  <DTPOSTED>" + dtposted + "</DTPOSTED>");
    l_regels.add("                  <TRNAMT>" + trnamt + "</TRNAMT>");
    l_regels.add("                  <FITID>" + fitid + "</FITID>");
    l_regels.add("                  <NAME>" + name + "</NAME>");
    l_regels.add("                  <BANKACCTTO>");
    l_regels.add("                     <BANKID></BANKID>");
    l_regels.add("                     <ACCTID>" + accountto + "</ACCTID>");
    l_regels.add("                     <ACCTTYPE>CHECKING</ACCTTYPE>");
    l_regels.add("                  </BANKACCTTO>");
    l_regels.add("                  <MEMO>" + memo + "</MEMO>");
    l_regels.add("               </STMTTRN>");
    return l_regels;
  }

}
