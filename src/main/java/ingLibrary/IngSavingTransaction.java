package ingLibrary;

/**
 * Bean package for ING Saving transaction
 * 
 * @author René
 *
 */
//import java.util.logging.Logger;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;

public class IngSavingTransaction extends CsvToBean<Object> {
//  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  /*
   * @formatter:off
   * Spaarrekening 
   * "Datum";"Omschrijving";"Rekening";"Rekening naam";"Tegenrekening";"Af Bij";"Bedrag";"Valuta";"Mutatiesoort";"Mededelingen";"Saldo na mutatie"
   *
   * "Datum"; "Omschrijving"; "Rekening"; "Rekening naam"; "Tegenrekening";
   * "Af Bij"; "Bedrag"; "Valuta"; "Mutatiesoort"; "Mededelingen";
   * "Saldo na mutatie"
   *   
   * @formatter:on
   */
  @CsvBindByName(column = "Datum")
  private String Datum = "";

  @CsvBindByName(column = "Omschrijving")
  private String Omschrijving = "";

  @CsvBindByName(column = "Rekening")
  private String Rekening = "";

  @CsvBindByName(column = "Rekening naam")
  private String RekeningNaam = "";

  @CsvBindByName(column = "Tegenrekening")
  private String Tegenrekening = "";

  @CsvBindByName(column = "Af Bij")
  private String Af_Bij = "";

  @CsvBindByName(column = "Bedrag")
  private String Bedrag = "";

  @CsvBindByName(column = "Valuta")
  private String Valuta = "";

  @CsvBindByName(column = "Mutatiesoort")
  private String Mutatiesoort = "";

  @CsvBindByName(column = "Mededelingen")
  private String Mededelingen = "";

  @CsvBindByName(column = "Saldo na mutatie")
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

}
