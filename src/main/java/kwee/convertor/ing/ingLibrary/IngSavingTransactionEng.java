package kwee.convertor.ing.ingLibrary;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

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
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  /*
   * @formatter:off
   * Spaarrekening 
   * Date;Description;Account;Account name;Counterparty;Debit/credit;Amount;
   * Currency;Transaction type;Notifications;Resulting balance
   *   
   * @formatter:on
   */
  @CsvBindByName(column = "Date")
  private String Datum = "";
  private Date dDatum = new Date();

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
  private double dBedrag = 0;

  @CsvBindByName(column = "Currency")
  private String Valuta = "";

  @CsvBindByName(column = "Transaction type")
  private String Mutatiesoort = "";

  @CsvBindByName(column = "Notifications")
  private String Mededelingen = "";

  @CsvBindByName(column = "Resulting balance")
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
    if (Af_Bij.equalsIgnoreCase("Debit")) {
      Af_Bij = "Af";
    } else if (Af_Bij.equalsIgnoreCase("Credit")) {
      Af_Bij = "Bij";
    }
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
    Mededelingen = mededelingen;
  }

  public void setSaldo_na_mutatie(String saldo_na_mutatie) {
    Saldo_na_mutatie = saldo_na_mutatie;
    dSaldo_na_mutatie = Double.valueOf(Saldo_na_mutatie.replace(",", "."));
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
    bstat = bstat && a_transaction.getMutatiesoort().equals(this.getMutatiesoort());
    bstat = bstat && a_transaction.getMededelingen().equals(this.getMededelingen());
    int cstat = Double.compare(a_transaction.getBedrag(), this.getBedrag());
    bstat = bstat && (cstat == 0);
    cstat = Double.compare(a_transaction.getSaldo_na_mutatie(), this.getSaldo_na_mutatie());
    bstat = bstat && (cstat == 0);
    return bstat;
  }
}
