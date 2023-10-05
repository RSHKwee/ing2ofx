package kwee.convertor.ing.ingLibrary;

/**
 * Bean package for ING Saving transaction English heading
 * 
 * @author René
 *
 */
//import java.util.logging.Logger;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;

public class IngSavingTransactionEng extends CsvToBean<Object> {
//  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  /*
   * @formatter:off
   * Spaarrekening 
   * "Datum";"Omschrijving";"Rekening";"Rekening naam";"Tegenrekening";"Af Bij";"Bedrag";"Valuta";"Mutatiesoort";"Mededelingen";"Saldo na mutatie"
   * Date;Description;Account;Account name;Counterparty;Debit/credit;Amount;
   * Currency;Transaction type;Notifications;Resulting balance

   *
   *
   * "Datum"; "Omschrijving"; "Rekening"; "Rekening naam"; "Tegenrekening";
   * "Af Bij"; "Bedrag"; "Valuta"; "Mutatiesoort"; "Mededelingen";
   * "Saldo na mutatie"
   *   
   * @formatter:on
   */
  @CsvBindByName(column = "Date")
  private String Datum = "";

  @CsvBindByName(column = "Description")
  private String Omschrijving = "";

  @CsvBindByName(column = "Account")
  private String Rekening = "";

  @CsvBindByName(column = "Account name")
  private String RekeningNaam = "";

  @CsvBindByName(column = "Counterparty")
  private String Tegenrekening = "";

  @CsvBindByName(column = "Debit/credit")
  private String Af_Bij = "";

  @CsvBindByName(column = "Amount")
  private String Bedrag = "";

  @CsvBindByName(column = "Currency")
  private String Valuta = "";

  @CsvBindByName(column = "Transaction type")
  private String Mutatiesoort = "";

  @CsvBindByName(column = "Notifications")
  private String Mededelingen = "";

  @CsvBindByName(column = "Resulting balance")
  private String Saldo_na_mutatie = "";

  public String getCode() {
    return "xx";
  }

  public String getDatum() {
    return Datum;
  }

  public String getOmschrijving() {
    return Omschrijving;
  }

  public String getRekening() {
    return Rekening;
  }

  public String getTegenrekening() {
    return Tegenrekening;
  }

  public String getValuta() {
    return Valuta;
  }

  public String getAf_Bij() {
    if (Af_Bij.equalsIgnoreCase("Debit")) {
      Af_Bij = "Af";
    } else if (Af_Bij.equalsIgnoreCase("Credit")) {
      Af_Bij = "Bij";
    }
    return Af_Bij;
  }

  public String getBedrag() {
    return Bedrag;
  }

  public String getMutatiesoort() {
    return Mutatiesoort;
  }

  public String getMededelingen() {
    return Mededelingen;
  }

  public String getSaldo_na_mutatie() {
    return Saldo_na_mutatie;
  }

  public String getRekeningNaam() {
    return RekeningNaam;
  }

  public void setRekeningNaam(String rekeningNaam) {
    RekeningNaam = rekeningNaam;
  }

  public void setValuta(String valuta) {
    Valuta = valuta;
  }

  public void setDatum(String datum) {
    Datum = datum;
  }

  public void setOmschrijving(String omschrijving) {
    Omschrijving = omschrijving;
  }

  public void setRekening(String rekening) {
    Rekening = rekening;
  }

  public void setTegenrekening(String tegenrekening) {
    Tegenrekening = tegenrekening;
  }

  public void setCode(String valuta) {
    Valuta = valuta;
  }

  public void setAf_Bij(String af_Bij) {
    if (af_Bij.equalsIgnoreCase("Debit")) {
      Af_Bij = "Af";
    } else if (af_Bij.equalsIgnoreCase("Credit")) {
      Af_Bij = "Bij";
    }
  }

  public void setBedrag(String bedrag) {
    Bedrag = bedrag;
  }

  public void setMutatiesoort(String mutatiesoort) {
    Mutatiesoort = mutatiesoort;
  }

  public void setMededelingen(String mededelingen) {
    Mededelingen = mededelingen;
  }

  public void setSaldo_na_mutatie(String saldo_na_mutatie) {
    Saldo_na_mutatie = saldo_na_mutatie;
  }

  public boolean equals(IngSavingTransaction a_transaction) {
    boolean bstat = false;
    bstat = a_transaction.getDatum().equals(this.Datum);
    bstat = a_transaction.getOmschrijving().equals(this.Omschrijving);
    bstat = a_transaction.getRekening().equals(this.Rekening);
    bstat = a_transaction.getRekeningNaam().equals(this.RekeningNaam);
    bstat = a_transaction.getTegenrekening().equals(this.Tegenrekening);
    bstat = a_transaction.getAf_Bij().equals(this.Af_Bij);
    bstat = a_transaction.getBedrag().equals(this.Bedrag);
    bstat = a_transaction.getValuta().equals(this.Valuta);
    bstat = a_transaction.getMutatiesoort().equals(this.Mutatiesoort);
    bstat = a_transaction.getMededelingen().equals(Mededelingen);
    bstat = a_transaction.getSaldo_na_mutatie().equals(this.Saldo_na_mutatie);
    return bstat;
  }
}
