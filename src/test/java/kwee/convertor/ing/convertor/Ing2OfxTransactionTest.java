package kwee.convertor.ing.convertor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;
import kwee.convertor.ing.ingLibrary.IngSavingTransaction;
import kwee.convertor.ing.ingLibrary.IngTransaction;
import kwee.ofxLibrary.OfxTransaction;

public class Ing2OfxTransactionTest extends TestCase {
  static String m_bankcode = "INGBNL2A";

  @Override
  public void setUp() {
  }

  @Override
  public void tearDown() {
  }

  public void testConvertSavingToOfx() {
    IngSavingTransaction l_trans = new IngSavingTransaction();
    /*
     * @formatter:off
     * Datum     Omschrijving                                      Rekening    Rekening naam Tegenrekening       
     * 26-5-2023 Overboeking van betaalrekening NL94COBA0678011583 K 444-12345 sprek 1       NL94COBA0678011583  
     * 
     * Af Bij  Bedrag  Valuta  Mutatiesoort  Mededelingen  Saldo na mutatie
     * Bij     2500    EUR     Inleg                       2750
     * 
     *
     * @formatter:on
     */
    l_trans.setDatum("26-5-2023");
    l_trans.setOmschrijving("Overboeking van betaalrekening NL94COBA0678011583");
    l_trans.setRekening("K 444-12345");
    l_trans.setRekeningNaam("sprek 1");
    l_trans.setTegenrekening("NL94COBA0678011583");
    l_trans.setAf_Bij("Bij");
    l_trans.setBedrag(2500.0);
    l_trans.setValuta("EUR");
    l_trans.setMutatiesoort("Inleg");
    l_trans.setMededelingen("  && dubbele  spaties  worden  enkel");
    l_trans.setSaldo_na_mutatie(2750.0);

    OfxTransaction l_ofxtrans = new OfxTransaction(m_bankcode);
    l_ofxtrans = Ing2OfxTransaction.convertSavingToOfx(l_trans);

    Calendar calendar = Calendar.getInstance();
    calendar.set(2023, 4, 26, 0, 0, 0);
    Date ldate = calendar.getTime();
    LocalDate date1 = ldate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

    Date ldate2 = l_ofxtrans.getDtposted();
    LocalDate date2 = ldate2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    int comparisonResult = date1.compareTo(date2);
    assertTrue(comparisonResult == 0);

    assertTrue(l_ofxtrans.getName().equals(l_trans.getOmschrijving()));
    assertTrue(l_ofxtrans.getMemo().equals("&amp&amp dubbele spaties worden enkel"));
    assertTrue(l_ofxtrans.getAccount().equals("K444-12345"));
    assertTrue(l_ofxtrans.getAccountto().equals(l_trans.getTegenrekening()));
    assertTrue(l_ofxtrans.getTrntype().equals("CREDIT"));
    int istat = l_ofxtrans.getTrnamt().compareTo(new BigDecimal(l_trans.getBedrag()));
    assertTrue(istat == 0);

//    assertTrue(l_ofxtrans.getTrnamt().equals(l_trans.getBedrag()));
    istat = l_ofxtrans.getSaldo_na_mutatie().compareTo(new BigDecimal(l_trans.getSaldo_na_mutatie()));
    assertTrue(istat == 0);
//    assertTrue(l_ofxtrans.getSaldo_na_mutatie().equals(l_trans.getSaldo_na_mutatie()));
  }

  public void testConvertToOfx() {
    IngTransaction l_trans = new IngTransaction();
    /*
 * @formatter:off
 * Datum     Naam / Omschrijving Rekening           Tegenrekening Code  Af Bij  Bedrag (EUR)  
 * 20230523  a Acchouder 7       NL90KNAB0445266309               GT    Af      2000          
 *
 * Mutatiesoort      Mededelingen                                                Saldo na mutatie  Tag
 * Online bankieren  Naar Oranje spaarrekening K55512345 Valutadatum: 23-05-2023 446,38  
 * @formatter:on
 */
    l_trans.setDatum("20230523");
    l_trans.setOmschrijving("a Acchouder 7");
    l_trans.setRekening("NL90KNAB0445266309");
    l_trans.setTegenrekening("");
    l_trans.setCode("GT");
    l_trans.setAf_Bij("Af");
    l_trans.setBedrag(2000.0);
    l_trans.setMutatiesoort("Online bankieren");
    l_trans.setMededelingen("Naar Oranje spaarrekening K55512345 Valutadatum: 23-05-2023");
    l_trans.setSaldo_na_mutatie(446.38);

    OfxTransaction l_ofxtrans = new OfxTransaction(m_bankcode);
    l_ofxtrans = Ing2OfxTransaction.convertToOfx(l_trans);

    Calendar calendar = Calendar.getInstance();
    calendar.set(2023, 4, 23, 0, 0, 0);
    Date ldate = calendar.getTime();
    LocalDate date1 = ldate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

    Date ldate2 = l_ofxtrans.getDtposted();
    LocalDate date2 = ldate2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    int comparisonResult = date1.compareTo(date2);
    assertTrue(comparisonResult == 0);

    assertTrue(l_ofxtrans.getName().equals(l_trans.getOmschrijving()));
    assertTrue(l_ofxtrans.getMemo().equals("Naar Oranje spaarrekening K55512345 Valutadatum: 23-05-2023"));
    assertTrue(l_ofxtrans.getAccount().equals("NL90KNAB0445266309"));
    assertTrue(l_ofxtrans.getAccountto().equals("K55512345"));
    assertTrue(l_ofxtrans.getTrntype().equals("PAYMENT"));
    int istat = l_ofxtrans.getTrnamt().compareTo(new BigDecimal(-2000.0));
    assertTrue(istat == 0);
    istat = l_ofxtrans.getSaldo_na_mutatie().compareTo(new BigDecimal(l_trans.getSaldo_na_mutatie()));
    assertTrue(istat == 0);
    // assertTrue(l_ofxtrans.getTrnamt().equals("-2000"));
    // assertTrue(l_ofxtrans.getSaldo_na_mutatie().equals(l_trans.getSaldo_na_mutatie()));
  }

}
