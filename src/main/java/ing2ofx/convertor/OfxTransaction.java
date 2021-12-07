package ing2ofx.convertor;

import com.opencsv.bean.CsvToBean;

public class OfxTransaction extends CsvToBean<Object> {
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
  private String account;
  private String trntype;
  private String dtposted;
  private String trnamt;
  private String fitid;
  private String name;
  private String accountto;
  private String memo;

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
}