package sandbox;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;

public class Transaction extends CsvToBean {
  /*
 * @formatter:off
 * "Datum","Naam / Omschrijving","Rekening","Tegenrekening","Code","Af Bij","Bedrag (EUR)","MutatieSoort","Mededelingen"
 * "Datum";"Naam / Omschrijving";"Rekening";"Tegenrekening";"Code";"Af Bij";"Bedrag (EUR)";"Mutatiesoort";"Mededelingen";"Saldo na mutatie";"Tag"
 * 
 * Spaarrekening 
 * "Datum";"Omschrijving";"Rekening";"Rekening naam";"Tegenrekening";"Af Bij";"Bedrag";"Valuta";"Mutatiesoort";"Mededelingen";"Saldo na mutatie"
 *
   *     These are the first two lines of an ING Netherlands CSV file:

    "Datum","Naam / Omschrijving","Rekening","Tegenrekening","Code",\
"Af Bij","Bedrag (EUR)","MutatieSoort",\
"Mededelingen"
    "20200213","Kosten OranjePakket met korting","NL42INGB0001085276","","DV",\
"Af","1,25","Diversen",\
"1 jan t/m 31 jan 2020 ING BANK N.V. Valutadatum: 13-02-2020"

     or ";" seperated:
     "Datum";"Naam / Omschrijving";"Rekening";"Tegenrekening";"Code";\
     "Af Bij";"Bedrag (EUR)";"Mutatiesoort";"Mededelingen";"Saldo na mutatie";"Tag"
"20210911";"Kosten OranjePakket";"NL12INGB0000123456";"";"DV";"Af";"1,95";"Diversen";"1 aug t/m 31 aug 2021 ING BANK N.V. Valutadatum: 11-09-2021";"5185,32";""

    These fields are from the Statement class:

    id = ""

    # Date transaction was posted to account (booking date)
    date = datetime.now()                          # "Datum"

    memo = ""                                      # "Mededelingen"

    # Amount of transaction
    amount = D(0)

    # additional fields
    payee = ""

    # Date user initiated transaction, if known (transaction date)
    date_user = datetime.now()

    # Check (or other reference) number
    check_no = ""

    # Reference number that uniquely identifies the transaction. Can be used in
    # addition to or instead of a check_no           # fitid
    refnum = ""

    # Transaction type, must be one of TRANSACTION_TYPES # "Code"
    "CREDIT",       # Generic credit
    "DEBIT",        # Generic debit
    "INT",          # Interest earned or paid
    "DIV",          # Dividend
    "FEE",          # FI fee
    "SRVCHG",       # Service charge
    "DEP",          # Deposit
    "ATM",          # ATM debit or credit             # GM
    "POS",          # Point of sale debit or credit   # BA
    "XFER",         # Transfer
    "CHECK",        # Check
    "PAYMENT",      # Electronic payment              # GT
    "CASH",         # Cash withdrawal
    "DIRECTDEP",    # Direct deposit                  # ST
    "DIRECTDEBIT",  # Merchant initiated debit        # IC
    "REPEATPMT",    # Repeating payment/standing order
    "OTHER"         # Other                           # DV OV VZ 

    trntype = "CHECK"

    # Optional BankAccount instance                   # "Tegenrekening"
    bank_account_to = None
    
   * @formatter:on
   */

  String id = "";
  String memo = "";
  String amount = "0";
  String payee = "";

  @CsvBindByName(column = "Datum")
  String date_user = "";
  String check_no = "";
  String refnum = "";
  String trntype = "CHECK";
  String bank_account_to = "";

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getMemo() {
    return memo;
  }

  public void setMemo(String memo) {
    this.memo = memo;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getPayee() {
    return payee;
  }

  public void setPayee(String payee) {
    this.payee = payee;
  }

  public String getDate_user() {
    return date_user;
  }

  public void setDate_user(String date_user) {
    this.date_user = date_user;
  }

  public String getCheck_no() {
    return check_no;
  }

  public void setCheck_no(String check_no) {
    this.check_no = check_no;
  }

  public String getRefnum() {
    return refnum;
  }

  public void setRefnum(String refnum) {
    this.refnum = refnum;
  }

  public String getTrntype() {
    return trntype;
  }

  public void setTrntype(String trntype) {
    this.trntype = trntype;
  }

  public String getBank_account_to() {
    return bank_account_to;
  }

  public void setBank_account_to(String bank_account_to) {
    this.bank_account_to = bank_account_to;
  }

}
