package kwee.convertor.ing.ingLibrary;

import java.util.Date;

//import java.util.logging.Logger;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;

import kwee.library.DateToNumeric;

public class IngTransactionEng extends CsvToBean<Object> {
//  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  /*
 * @formatter:off
 *
 * For semicolon seperared file:
 * "Date";"Name / Description";"Account";"Counterparty";"Code";"Debit/credit";
 * "Amount (EUR)";"Transaction type";"Notifications";"Resulting balance";"Tag"
 *     
 * @formatter:on
 */

  @CsvBindByName(column = "Date")
  private String Datum = "";
  private Date dDatum = new Date();

  @CsvBindByName(column = "Name / Description")
  private String Omschrijving = "";

  @CsvBindByName(column = "Account")
  private String Rekening = "";

  @CsvBindByName(column = "Counterparty")
  private String Tegenrekening = "";

  @CsvBindByName(column = "Code")
  private String Code = "";

  @CsvBindByName(column = "Debit/credit")
  private String Af_Bij = "";

  @CsvBindByName(column = "Amount (EUR)")
  private String Bedrag = "";
  private double dBedrag = 0;

  @CsvBindByName(column = "Transaction type")
  private String Mutatiesoort = "";

  @CsvBindByName(column = "Notifications")
  private String Mededelingen = "";

  @CsvBindByName(column = "Resulting balance")
  private String Saldo_na_mutatie = "";
  private double dSaldo_na_mutatie = 0;

  @CsvBindByName(column = "Tag")
  private String Tag = "";

  public Date getDatum() {
    dDatum = DateToNumeric.String_NumericToDate(Datum);
    return dDatum;
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

  public String getCode() {
    return Code;
  }

  public String getAf_Bij() {
    if (Af_Bij.equalsIgnoreCase("Debit")) {
      Af_Bij = "Af";
    } else if (Af_Bij.equalsIgnoreCase("Credit")) {
      Af_Bij = "Bij";
    }
    return Af_Bij;
  }

  public double getBedrag() {
    dBedrag = Double.valueOf(Bedrag.replace(",", "."));
    return dBedrag;
  }

  public String getMutatiesoort() {
    return Mutatiesoort;
  }

  public String getMededelingen() {
    return Mededelingen;
  }

  public double getSaldo_na_mutatie() {
    dSaldo_na_mutatie = Double.valueOf(Saldo_na_mutatie.replace(",", "."));
    return dSaldo_na_mutatie;
  }

  public String getTag() {
    return Tag;
  }

  public void setDatum(String datum) {
    Datum = datum;
    dDatum = DateToNumeric.String_NumericToDate(Datum);
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

  public void setCode(String code) {
    Code = code;
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
    dBedrag = Double.valueOf(Bedrag.replace(",", "."));
  }

  public void setMutatiesoort(String mutatiesoort) {
    Mutatiesoort = mutatiesoort;
  }

  public void setMededelingen(String mededelingen) {
    Mededelingen = filterMededelingen(mededelingen);
  }

  public void setSaldo_na_mutatie(String saldo_na_mutatie) {
    Saldo_na_mutatie = saldo_na_mutatie;
    dSaldo_na_mutatie = Double.valueOf(Saldo_na_mutatie.replace(",", "."));
  }

  public void setTag(String tag) {
    Tag = tag;
  }

  public boolean equals(IngTransactionEng a_transaction) {
    boolean bstat = false;
    bstat = a_transaction.getDatum().equals(this.Datum);
    bstat = bstat && a_transaction.getOmschrijving().equals(this.Omschrijving);
    bstat = bstat && a_transaction.getRekening().equals(this.Rekening);
    bstat = bstat && a_transaction.getTegenrekening().equals(this.Tegenrekening);
    bstat = bstat && a_transaction.getCode().equals(this.Code);
    bstat = bstat && a_transaction.getAf_Bij().equals(this.Af_Bij);
    bstat = bstat && a_transaction.getMutatiesoort().equals(this.Mutatiesoort);
    bstat = bstat && a_transaction.getMededelingen().equals(Mededelingen);
    bstat = bstat && a_transaction.getTag().equals(this.Tag);

    int cstat = Double.compare(a_transaction.getSaldo_na_mutatie(), this.getSaldo_na_mutatie());
    bstat = bstat && (cstat == 0);
    cstat = Double.compare(a_transaction.getBedrag(), this.getBedrag());
    bstat = bstat && (cstat == 0);
    return bstat;
  }

  // Naam: yyyyyy Omschrijving: xxxxxx IBAN: xxxxxx
  // Naam: yyyyyy en Omschrijving: worden verwijderd
  // Resultaat:
  // "xxxxxx IBAN: xxxxxx"
  //
  /**
   * In mededeling kan de naam van rekeninghouder zijn opgenomen, dit is dubbele
   * informatie en wordt verwijderd. <br>
   * Geldt ook voor naamgeving veld "Opmerking:", wordt ook verwijderd.
   * <p>
   * "Naam: yyyyyy Omschrijving: xxxxxx IBAN: xxxxxx"
   * <p>
   * "Naam: yyyyyy" en "Omschrijving: " worden verwijderd <br>
   * Resultaat: <br>
   * "xxxxxx IBAN: xxxxxx"
   * 
   * @param a_Mededeling
   * @return Gefilterde mededeling
   */
  private String filterMededelingen(String a_Mededeling) {
    String l_Mededeling = a_Mededeling;
    if (l_Mededeling.contains(Omschrijving)) {
      l_Mededeling = l_Mededeling.replaceAll(Omschrijving + " ", "");
    }
    if (l_Mededeling.contains("Omschrijving: ")) {
      l_Mededeling = l_Mededeling.replaceAll("Omschrijving: ", "");
    }
    if (l_Mededeling.contains("Naam: ")) {
      l_Mededeling = l_Mededeling.replaceAll("Naam: ", "");
    }
    return l_Mededeling;
  }

}
