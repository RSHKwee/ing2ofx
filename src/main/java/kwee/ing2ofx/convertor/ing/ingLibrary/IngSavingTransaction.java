package kwee.ing2ofx.convertor.ing.ingLibrary;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Bean package for ING Saving transaction
 * 
 * @author René
 *
 */
//import java.util.logging.Logger;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;

import kwee.library.DateToNumeric;
import kwee.logger.MyLogger;

public class IngSavingTransaction extends CsvToBean<Object> {
  private static final Logger LOGGER = MyLogger.getLogger();
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
  private Date dDatum = new Date();

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
  private double dBedrag = 0;

  @CsvBindByName(column = "Valuta")
  private String Valuta = "";

  @CsvBindByName(column = "Mutatiesoort")
  private String Mutatiesoort = "";

  @CsvBindByName(column = "Mededelingen")
  private String Mededelingen = "";

  @CsvBindByName(column = "Saldo na mutatie")
  private String Saldo_na_mutatie = "";
  private double dSaldo_na_mutatie = 0;

  public String getCode() {
    return "xx";
  }

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

  public String getValuta() {
    return Valuta;
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

  public String getRekeningNaam() {
    return RekeningNaam;
  }

  // Setters
  public void setRekeningNaam(String rekeningNaam) {
    RekeningNaam = rekeningNaam;
  }

  public void setValuta(String valuta) {
    Valuta = valuta;
  }

  public void setDatum(Date a_Datum) {
    dDatum = a_Datum;
    Datum = DateToNumeric.dateToNumeric(a_Datum);
  }

  public void setDatum(String datum) {
    Datum = datum;
    SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
    String[] st_elem = datum.split("-");
    if (st_elem.length >= 3) {
      int l_Year = Integer.valueOf(st_elem[0]);
      if (l_Year > 31) {
        try {
          SimpleDateFormat inputFormat1 = new SimpleDateFormat("yyyy-MM-dd");
          dDatum = inputFormat1.parse(Datum);
        } catch (Exception e1) {
          LOGGER.log(Level.WARNING, "Error with " + datum + ": " + e1.getMessage());
        }
      } else {
        try {
          dDatum = inputFormat.parse(Datum);
        } catch (Exception e) {
          LOGGER.log(Level.WARNING, "Error with " + datum + ": " + e.getMessage());
        }
      }
    }
    LOGGER.log(Level.FINE, datum + " -> " + dDatum);
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
    Mededelingen = mededelingen;
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

  public boolean equals(IngSavingTransaction a_transaction) {
    boolean bstat = false;
    bstat = a_transaction.getDatum().equals(this.getDatum());
    bstat = bstat && a_transaction.getOmschrijving().equals(this.getOmschrijving());
    bstat = bstat && a_transaction.getRekening().equals(this.getRekening());
    bstat = bstat && a_transaction.getRekeningNaam().equals(this.getRekeningNaam());
    bstat = bstat && a_transaction.getTegenrekening().equals(this.getTegenrekening());
    bstat = bstat && a_transaction.getAf_Bij().equals(this.getAf_Bij());
    bstat = bstat && a_transaction.getValuta().equals(this.getValuta());
    bstat = bstat && a_transaction.getMutatiesoort().equals(this.Mutatiesoort);
    bstat = bstat && a_transaction.getMededelingen().equals(this.getMededelingen());

    int istat = Double.compare(a_transaction.getBedrag(), this.getBedrag());
    bstat = bstat && (istat == 0);

    istat = Double.compare(a_transaction.getSaldo_na_mutatie(), this.getSaldo_na_mutatie());
    bstat = bstat && (istat == 0);
    return bstat;
  }
}
