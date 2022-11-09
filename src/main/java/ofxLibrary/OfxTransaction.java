package ofxLibrary;

//import java.util.logging.Logger;

import com.opencsv.bean.CsvToBean;

public class OfxTransaction extends CsvToBean<Object> {
//  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

  private String bankCode = "";

  private String account = "";
  private String trntype = "";
  private String dtposted = "";
  private String trnamt = "";
  private String fitid = "";
  private String name = "";
  private String accountto = "";
  private String memo = "";
  private int OfxTranPair = -1;
  private String Saldo_na_mutatie = "";

  private String Source = "";
  private boolean saving = false;

  public String getSource() {
    return Source;
  }

  public void setSource(String source) {
    Source = source;
  }

  public boolean isSaving() {
    return saving;
  }

  public void setSaving(boolean saving) {
    this.saving = saving;
  }

  public OfxTransaction(String a_Bankcode) {
    bankCode = a_Bankcode;
  }

  public String getBankCode() {
    return bankCode;
  }

  public String getSaldo_na_mutatie() {
    return Saldo_na_mutatie;
  }

  public void setSaldo_na_mutatie(String saldo_na_mutatie) {
    Saldo_na_mutatie = saldo_na_mutatie;
  }

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

  public int getOfxTranPair() {
    return OfxTranPair;
  }

  public void setBankCode(String bankCode) {
    this.bankCode = bankCode;
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

  public void setOfxTranPair(int OfxTranPair) {
    this.OfxTranPair = OfxTranPair;
  }

  public boolean equals(OfxTransaction a_ofxtransaction) {
    boolean bstat = false;
    bstat = a_ofxtransaction.getBankCode().equals(this.bankCode);
    bstat = bstat && a_ofxtransaction.getAccount().equals(this.account);
    bstat = bstat && a_ofxtransaction.getTrntype().equals(this.trntype);
    bstat = bstat && a_ofxtransaction.getDtposted().equals(this.dtposted);
    bstat = bstat && a_ofxtransaction.getTrnamt().equals(this.trnamt);
    bstat = bstat && a_ofxtransaction.getFitid().equals(this.fitid);
    bstat = bstat && a_ofxtransaction.getName().equals(this.name);
    bstat = bstat && a_ofxtransaction.getAccountto().equals(this.accountto);
    bstat = bstat && a_ofxtransaction.getMemo().equals(this.memo);
    bstat = bstat && a_ofxtransaction.getSaldo_na_mutatie().equals(this.Saldo_na_mutatie);
    return bstat;
  }

}
