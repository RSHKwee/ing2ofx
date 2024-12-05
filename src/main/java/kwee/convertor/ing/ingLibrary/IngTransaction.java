package kwee.convertor.ing.ingLibrary;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

//import java.util.logging.Logger;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import kwee.library.DateToNumeric;

public class IngTransaction extends CsvToBean<Object> {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  /*
 * @formatter:off
 * 
 * For comma seperated file:
 * "Datum","Naam / Omschrijving","Rekening","Tegenrekening","Code","Af Bij","Bedrag (EUR)","MutatieSoort","Mededelingen"
 *
 * For semicolon seperared file:
 * "Datum";"Naam / Omschrijving";"Rekening";"Tegenrekening";"Code";"Af Bij";"Bedrag (EUR)";"Mutatiesoort";"Mededelingen";"Saldo na mutatie";"Tag"
 * 
 *     These are the first two lines of an ING Netherlands CSV file:

    "Datum","Naam / Omschrijving","Rekening","Tegenrekening","Code",\
"Af Bij","Bedrag (EUR)","MutatieSoort",\
"Mededelingen"
    "20200213","Kosten OranjePakket met korting","NL42INGB0001085276","","DV",\
"Af","1,25","Diversen",\
"1 jan t/m 31 jan 2020 ING BANK N.V. Valutadatum: 13-02-2020"

     or ";" seperated:
     "Datum";"Naam / Omschrijving";"Rekening";"Tegenrekening";"Code";
     "Af Bij";"Bedrag (EUR)";"Mutatiesoort";"Mededelingen";"Saldo na mutatie";"Tag"
"20210911";"Kosten OranjePakket";"NL12INGB0000123456";"";"DV";"Af";"1,95";"Diversen";"1 aug t/m 31 aug 2021 ING BANK N.V. Valutadatum: 11-09-2021";"5185,32";""
    
   * @formatter:on
   */

  @CsvBindByName(column = "Datum")
  private String Datum = "";
  private Date dDatum = new Date();

  @CsvBindByName(column = "Naam / Omschrijving")
  private String Omschrijving = "";

  @CsvBindByName(column = "Rekening")
  private String Rekening = "";

  @CsvBindByName(column = "Tegenrekening")
  private String Tegenrekening = "";

  @CsvBindByName(column = "Code")
  private String Code = "";

  @CsvBindByName(column = "Af Bij")
  private String Af_Bij = "";

  @CsvBindByName(column = "Bedrag (EUR)")
  private String Bedrag = "";
  private double dBedrag = 0;

  @CsvBindByName(column = "Mutatiesoort")
  private String Mutatiesoort = "";

  @CsvBindByName(column = "Mededelingen")
  private String Mededelingen = "";

  @CsvBindByName(column = "Saldo na mutatie")
  private String Saldo_na_mutatie = "";
  private double dSaldo_na_mutatie = 0;

  @CsvBindByName(column = "Tag")
  private String Tag = "";

  public Date getDatum() {
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
    return Af_Bij;
  }

  public double getBedrag() {
    return dBedrag;
  }

  public String getMutatiesoort() {
    return Mutatiesoort;
  }

  public String getMededelingen() {
    return Mededelingen;
  }

  public double getSaldo_na_mutatie() {
    return dSaldo_na_mutatie;
  }

  public String getTag() {
    return Tag;
  }

  // Setters
  public void setDatum(String datum) {
    Datum = datum;
    dDatum = DateToNumeric.String_NumericToDate(Datum);
  }

  public void setDatum(Date datum) {
    dDatum = datum;
    Datum = DateToNumeric.dateToNumeric(datum);
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
    Af_Bij = af_Bij;
  }

  public void setBedrag(String bedrag) {
    Bedrag = bedrag;
    dBedrag = Double.valueOf(Bedrag.replace(",", "."));
  }

  public void setBedrag(double bedrag) {
    dBedrag = bedrag;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");
    Bedrag = decimalFormat.format(dBedrag).replace(".", ",");
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

  public void setSaldo_na_mutatie(double saldo_na_mutatie) {
    dSaldo_na_mutatie = saldo_na_mutatie;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");
    Saldo_na_mutatie = decimalFormat.format(dSaldo_na_mutatie).replace(".", ",");
  }

  public void setTag(String tag) {
    Tag = tag;
  }

  public boolean equals(IngTransaction a_transaction) {
    boolean bstat = false;
    bstat = a_transaction.getDatum().equals(this.getDatum());
    bstat = bstat && a_transaction.getOmschrijving().equals(this.getOmschrijving());
    bstat = bstat && a_transaction.getRekening().equals(this.getRekening());
    bstat = bstat && a_transaction.getTegenrekening().equals(this.getTegenrekening());
    bstat = bstat && a_transaction.getCode().equals(this.getCode());
    bstat = bstat && a_transaction.getAf_Bij().equals(this.getAf_Bij());
    bstat = bstat && a_transaction.getMutatiesoort().equals(this.getMutatiesoort());
    bstat = bstat && a_transaction.getMededelingen().equals(this.getMededelingen());
    bstat = bstat && a_transaction.getTag().equals(this.getTag());

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
    try {
      if (l_Mededeling.contains("Omschrijving: ")) {
        l_Mededeling = l_Mededeling.replaceAll("Omschrijving: ", "");
      }
      if (l_Mededeling.contains(Omschrijving)) {
        l_Mededeling = l_Mededeling.replaceAll(Omschrijving + " ", "");
      }
      if (l_Mededeling.contains("Naam: ")) {
        l_Mededeling = l_Mededeling.replaceAll("Naam: ", "");
      }
    } catch (Exception e) {
      LOGGER.log(Level.INFO, l_Mededeling);
    }
    return l_Mededeling;
  }

}
