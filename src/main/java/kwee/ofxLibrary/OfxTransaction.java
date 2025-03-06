package kwee.ofxLibrary;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

// import kwee.logger.MyLogger;

//import java.util.logging.Logger;

//import com.opencsv.bean.CsvToBean;

public class OfxTransaction {
//  private static final Logger LOGGER = MyLogger.getLogger();

  private String bankCode = "";

  private String account = "";
  private String trntype = "";
  private Date dtposted = new Date();
  private BigDecimal trnamt = new BigDecimal(0.0);
  private String fitid = "";
  private String name = "";
  private String accountto = "";
  private String memo = "";
  private int OfxTranPair = -1;
  private BigDecimal Saldo_na_mutatie = new BigDecimal(0.0);

  private String Source = "";
  private boolean saving = false;

  public OfxTransaction() {
  }

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

  public BigDecimal getSaldo_na_mutatie() {
    return Saldo_na_mutatie;
  }

  public void setSaldo_na_mutatie(BigDecimal saldo_na_mutatie) {
    Saldo_na_mutatie = saldo_na_mutatie;
  }

  public String getAccount() {
    return account;
  }

  public String getTrntype() {
    return trntype;
  }

  public Date getDtposted() {
    return dtposted;
  }

  public BigDecimal getTrnamt() {
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

  public void setDtposted(Date dtposted) {
    this.dtposted = dtposted;
  }

  public void setTrnamt(BigDecimal trnamt) {
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
    bstat = a_ofxtransaction.getBankCode().equals(this.getBankCode());
    bstat = bstat && a_ofxtransaction.getFitid().equals(this.getFitid());
    bstat = bstat && a_ofxtransaction.getAccount().equals(this.getAccount());
    bstat = bstat && a_ofxtransaction.getName().equals(this.getName());
    bstat = bstat && a_ofxtransaction.getAccountto().equals(this.getAccountto());
    bstat = bstat && a_ofxtransaction.getTrntype().equals(this.getTrntype());
    bstat = bstat && a_ofxtransaction.getMemo().equals(this.getMemo());

    int cdstat = this.trnamt.compareTo(a_ofxtransaction.getTrnamt());
    bstat = bstat && (cdstat == 0);

    cdstat = this.Saldo_na_mutatie.compareTo(a_ofxtransaction.getSaldo_na_mutatie());
    bstat = bstat && (cdstat == 0);

    Date ldate = this.getDtposted();
    LocalDate date1 = ldate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    Date ldate2 = a_ofxtransaction.getDtposted();
    LocalDate date2 = ldate2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    cdstat = date1.compareTo(date2);
    bstat = bstat && (cdstat == 0);

    return bstat;
  }

  @Override
  public String toString() {
    String l_str = "";
    l_str = String.join(";", this.bankCode, this.fitid, this.dtposted.toString(), this.account, this.name,
        this.accountto, this.trntype, this.trnamt.toString(), this.memo, this.Saldo_na_mutatie.toString());

    return l_str;
  }

}
