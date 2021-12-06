package ing2ofx.convertor;

import java.util.logging.Logger;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;

public class IngTransaction extends CsvToBean<Object> {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  /*
 * @formatter:off
 * "Datum","Naam / Omschrijving","Rekening","Tegenrekening","Code","Af Bij","Bedrag (EUR)","MutatieSoort","Mededelingen"
 * "Datum";"Naam / Omschrijving";"Rekening";"Tegenrekening";"Code";"Af Bij";"Bedrag (EUR)";"Mutatiesoort";"Mededelingen";"Saldo na mutatie";"Tag"
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

    
   * @formatter:on
   */

  @CsvBindByName(column = "Datum")
  private String Datum;

  @CsvBindByName(column = "Naam / Omschrijving")
  private String Omschrijving;

  @CsvBindByName(column = "Rekening")
  private String Rekening;

  @CsvBindByName(column = "Tegenrekening")
  private String Tegenrekening;

  @CsvBindByName(column = "Code")
  private String Code;

  @CsvBindByName(column = "Af Bij")
  private String Af_Bij;

  @CsvBindByName(column = "Bedrag (EUR)")
  private String Bedrag;

  @CsvBindByName(column = "Mutatiesoort")
  private String Mutatiesoort;

  @CsvBindByName(column = "Mededelingen")
  private String Mededelingen;

  @CsvBindByName(column = "Saldo na mutatie")
  private String Saldo_na_mutatie;

  @CsvBindByName(column = "Tag")
  private String Tag;

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

  public String getCode() {
    return Code;
  }

  public String getAf_Bij() {
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

  public String getTag() {
    return Tag;
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

  public void setCode(String code) {
    Code = code;
  }

  public void setAf_Bij(String af_Bij) {
    Af_Bij = af_Bij;
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

  public void setTag(String tag) {
    Tag = tag;
  }

}
