/**
 * 
 */
package kwee.convertor.ing.convertor;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Assert;
import junit.framework.TestCase;

import kwee.convertor.ing.ingLibrary.IngSavingTransaction;
import kwee.convertor.ing.ingLibrary.IngTransaction;
import kwee.ing2ofx.gui.TestFunctions;
import kwee.ing2ofx.main.UserSetting;
import kwee.ofxLibrary.OfxMetaInfo;
import kwee.ofxLibrary.OfxTransaction;
import kwee.library.DateToNumeric;

/**
 * @author René
 *
 */
public class IngTransactionsTest extends TestCase {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

  private String c_IngTransFile = "ING_Enkele.csv";
  private String c_IngSavingTransFile = "ING_Enkele_spaar.csv";
  private String c_SynonymFile = "Synoniem.txt";

  private File m_IngFile;
  private File m_IngSavingFile;

  private List<IngTransaction> m_Transactions_exp = new LinkedList<IngTransaction>();
  private List<IngSavingTransaction> m_SavingTransactions_exp = new LinkedList<IngSavingTransaction>();
  private List<OfxTransaction> m_OfxTransactions_exp = new LinkedList<OfxTransaction>();

  private Map<String, OfxMetaInfo> m_metainfo_exp = new HashMap<String, OfxMetaInfo>();
  private UserSetting m_usersetting = UserSetting.getInstance();
  private TestFunctions m_Functions = new TestFunctions();

  /**
   * @throws java.lang.Exception
   */
  @Override
  public void setUp() throws Exception {
    m_IngSavingFile = m_Functions.GetResourceFile(c_IngSavingTransFile);
    m_IngFile = m_Functions.GetResourceFile(c_IngTransFile);

    m_usersetting.freeze();
    m_usersetting.set_Synonym_file(m_Functions.GetResourceFile(c_SynonymFile));
  }

  /**
   * @throws java.lang.Exception
   */
  @Override
  public void tearDown() throws Exception {
    m_usersetting.unfreeze();
  }

  /**
   * Test method for
   * {@link kwee.convertor.ing.convertor.IngTransactions#IngTransactions(java.io.File, java.io.File)}.
   */
  public void testIngTransactions() {
    IngTransactions l_ingSavingtrans = new IngTransactions(m_IngSavingFile);
    assertNotNull(l_ingSavingtrans);

    IngTransactions l_ingtrans = new IngTransactions(m_IngFile);
    assertNotNull(l_ingtrans);
  }

  /**
   * Test method for {@link kwee.convertor.ing.convertor.IngTransactions#load()}.
   */
  public void testLoad() {
    IngTransactions l_transSaving = new IngTransactions(m_IngSavingFile);
    l_transSaving.load();
    List<IngSavingTransaction> l_SavingTransactions = l_transSaving.getIngSavingTransactions();
    int l_nrSavingTrans = l_SavingTransactions.size();
    assertEquals(l_nrSavingTrans, 3);

    IngTransactions l_trans = new IngTransactions(m_IngFile);
    l_trans.load();
    List<IngTransaction> l_Transactions = l_trans.getIngTransactions();
    int l_nrTrans = l_Transactions.size();
    assertEquals(l_nrTrans, 2);
  }

  /**
   * Test method for
   * {@link kwee.convertor.ing.convertor.IngTransactions#isSavingCsvFile()}.
   */
  public void testIsSavingCsvFile() {
    IngTransactions l_transSaving = new IngTransactions(m_IngSavingFile);
    l_transSaving.load();
    assertEquals(l_transSaving.isSavingCsvFile(), true);

    IngTransactions l_trans = new IngTransactions(m_IngFile);
    l_trans.load();
    assertEquals(l_trans.isSavingCsvFile(), false);
  }

  /**
   * Test method for
   * {@link kwee.convertor.ing.convertor.IngTransactions#getIngTransactions()}.
   */
  public void testGetIngTransactions() {
    IngTransactions l_trans = new IngTransactions(m_IngFile);
    l_trans.load();
    List<IngSavingTransaction> l_SavingTransactions = l_trans.getIngSavingTransactions();
    assertTrue(l_SavingTransactions.isEmpty());

    List<IngTransaction> l_Transactions = l_trans.getIngTransactions();
    loadINGTransactionExp();

    assertTrue(compareINGTrans(l_Transactions, m_Transactions_exp));
    LOGGER.log(Level.FINE, "Result:" + compareINGTrans(l_Transactions, m_Transactions_exp));
  }

  /**
   * Test method for
   * {@link kwee.convertor.ing.convertor.IngTransactions#getIngSavingTransactions()}.
   */
  public void testGetIngSavingTransactions() {
    IngTransactions l_transSaving = new IngTransactions(m_IngSavingFile);
    l_transSaving.load();
    List<IngTransaction> l_Transactions = l_transSaving.getIngTransactions();
    assertTrue(l_Transactions.isEmpty());

    List<IngSavingTransaction> l_SavingTransactions = l_transSaving.getIngSavingTransactions();
    loadINGSavingTransactionExp();
    assertTrue(compareINGSavingTrans(l_SavingTransactions, m_SavingTransactions_exp));
    LOGGER.log(Level.FINE, "Result:" + compareINGSavingTrans(l_SavingTransactions, m_SavingTransactions_exp));
  }

  /**
   * Test method for
   * {@link kwee.convertor.ing.convertor.IngTransactions#getOfxTransactions()}.
   */
  public void testGetOfxTransactions() {
    IngTransactions l_transSaving = new IngTransactions(m_IngSavingFile);
    l_transSaving.load();

    IngTransactions l_trans = new IngTransactions(m_IngFile);
    l_trans.load();

    // Build OFX transactions list
    List<OfxTransaction> l_OfxTransactions = new LinkedList<OfxTransaction>();
    l_OfxTransactions.clear();
    l_OfxTransactions.addAll(l_trans.getOfxTransactions());
    l_OfxTransactions.addAll(l_transSaving.getOfxTransactions());

    loadOFXTransactionExp();
    assertTrue(compareOFXTrans(l_OfxTransactions, m_OfxTransactions_exp));
    LOGGER.log(Level.FINE, "Result:" + compareOFXTrans(l_OfxTransactions, m_OfxTransactions_exp));
  }

  /**
   * Test method for
   * {@link kwee.convertor.ing.convertor.IngTransactions#getOfxMetaInfo()}.
   */
  public void testGetOfxMetaInfo() {
    IngTransactions l_transSaving = new IngTransactions(m_IngSavingFile);
    l_transSaving.load();

    IngTransactions l_trans = new IngTransactions(m_IngFile);
    l_trans.load();

    Map<String, OfxMetaInfo> l_metasaving = new HashMap<String, OfxMetaInfo>();
    l_metasaving = l_transSaving.getOfxMetaInfo();

    Map<String, OfxMetaInfo> l_meta = new HashMap<String, OfxMetaInfo>();
    l_meta = l_trans.getOfxMetaInfo();

    loadOFXMetaInfoExp();

    OfxMetaInfo l_metainfo = l_metasaving.get("K444-12345");
    assertTrue(l_metainfo.equals(m_metainfo_exp.get("K444-12345")));

    l_metainfo = l_meta.get("NL90KNAB0445266309");
    assertTrue(l_metainfo.equals(m_metainfo_exp.get("NL90KNAB0445266309")));
  }

  /**
   * Test method for
   * {@link kwee.convertor.ing.convertor.IngTransactions#getUniqueIds()}.
   */
  public void testGetUniqueIds() {
    Set<String> l_UniqueIds_exp = new LinkedHashSet<>();
    Set<String> l_UniqueIds = new LinkedHashSet<>();

    IngTransactions l_transSaving = new IngTransactions(m_IngSavingFile);
    l_transSaving.load();
    l_UniqueIds = l_transSaving.getUniqueIds();

    l_UniqueIds_exp.clear();
    l_UniqueIds_exp.add("20230526250000");
    l_UniqueIds_exp.add("2023052525000");
    l_UniqueIds_exp.add("20230501075");
    l_UniqueIds.removeAll(l_UniqueIds_exp);
    // boolean bstat = l_UniqueIds.isEmpty();
    Assert.assertTrue(l_UniqueIds.isEmpty());

    IngTransactions l_trans = new IngTransactions(m_IngFile);
    l_trans.load();
    l_UniqueIds = l_trans.getUniqueIds();
    l_UniqueIds_exp.clear();
    l_UniqueIds_exp.add("20230523200000");
    l_UniqueIds_exp.add("2023052382198");
    l_UniqueIds.removeAll(l_UniqueIds_exp);
    // bstat = l_UniqueIds.isEmpty();
    Assert.assertTrue(l_UniqueIds.isEmpty());
  }

  /**
   * Define Expected results for ING transactions.
   */
  private void loadINGTransactionExp() {
    m_Transactions_exp.add(addIngTransaction("20230523", "a Acchouder 7", "NL90KNAB0445266309", "", "GT", "Af",
        "2000,00", "Online bankieren", "Naar Oranje spaarrekening K55512345 Valutadatum: 23-05-2023", "446,38", ""));
    m_Transactions_exp.add(addIngTransaction("20230523", "Acchouder 8", "NL90KNAB0445266309", "NL58INGB0702712760",
        "VZ", "Bij", "821,98", "Verzamelbetaling",
        "Naam: Acchouder 8 Omschrijving: P0000200000000000231148665230523 PWJ 30-04-23/30-04-23 Acchouder 7 IBAN: NL58INGB0702712760 Kenmerk: DK13 110520EA-01120651634 Valutadatum: 23-05-2023",
        "2446,38", ""));
  }

  /**
   * Define Expected results for ING saving transactions.
   */
  private void loadINGSavingTransactionExp() {
    m_SavingTransactions_exp
        .add(addIngSavingTransaction("2023-05-26", "Overboeking van betaalrekening NL94COBA0678011583", "K 444-12345",
            "sprek 1", "NL94COBA0678011583", "Bij", 2500.00, "EUR", "Inleg", "", 2750.00));
    m_SavingTransactions_exp
        .add(addIngSavingTransaction("2023-05-25", "Overboeking naar betaalrekening NL94COBA0678011583", "K 444-12345",
            "sprek 1", "NL94COBA0678011583", "Af", 250.00, "EUR", "Opname", "", 250.00));
    m_SavingTransactions_exp.add(addIngSavingTransaction("2023-05-01", "Rente", "K 444-12345", "sprek 1", "", "Bij",
        0.75, "EUR", "Rente", "", 1650.75));
  }

  /*
   * @formatter:off
   * INGBNL2A;20230523200000;20230523;NL90KNAB0445266309;a Acchouder 7;K55512345;PAYMENT;-2000,00;Naar Oranje spaarrekening K55512345 Valutadatum: 23-05-2023;446,38;
   * INGBNL2A;2023052382198;20230523;NL90KNAB0445266309;Acchouder 8;NL58INGB0702712760;CREDIT;821,98;P0000200000000000231148665230523 PWJ 30-04-23/30-04-23 Acchouder 7 IBAN: NL58INGB0702712760 Kenmerk: DK13 110520EA-01120651634 Valutadatum: 23-05-2023;2446,38;
   * INGBNL2A;20230526250000;20230526;K444-12345;Overboeking van betaalrekening NL94COBA0678011583;NL94COBA0678011583;CREDIT;2500,00;;2750,00;
   * INGBNL2A;2023052525000;20230525;K444-12345;Overboeking naar betaalrekening NL94COBA0678011583;NL94COBA0678011583;DEBIT;-250,00;;250,00;
   * INGBNL2A;20230501075;20230501;K444-12345;Rente;;CREDIT;0,75;;1650,75;
   * @formatter:on
  */
  /**
   * Define Expected results for OFX transactions.
   */
  private void loadOFXTransactionExp() {
    m_OfxTransactions_exp
        .add(addOfxTransaction("INGBNL2A", "20230523200000", "20230523", "NL90KNAB0445266309", "a Acchouder 7",
            "K55512345", "PAYMENT", -2000.00, "Naar Oranje spaarrekening K55512345 Valutadatum: 23-05-2023", 446.38));
    m_OfxTransactions_exp.add(addOfxTransaction("INGBNL2A", "2023052382198", "20230523", "NL90KNAB0445266309",
        "Acchouder 8", "NL58INGB0702712760", "CREDIT", 821.98,
        "P0000200000000000231148665230523 PWJ 30-04-23/30-04-23 Acchouder 7 IBAN: NL58INGB0702712760 Kenmerk: DK13 110520EA-01120651634 Valutadatum: 23-05-2023",
        2446.38));
    m_OfxTransactions_exp.add(addOfxTransaction("INGBNL2A", "20230526250000", "20230526", "K444-12345",
        "Overboeking van betaalrekening NL94COBA0678011583", "NL94COBA0678011583", "CREDIT", 2500.00, "", 2750.00));
    m_OfxTransactions_exp.add(addOfxTransaction("INGBNL2A", "2023052525000", "20230525", "K444-12345",
        "Overboeking naar betaalrekening NL94COBA0678011583", "NL94COBA0678011583", "DEBIT", -250.00, "", 250.00));
    m_OfxTransactions_exp.add(addOfxTransaction("INGBNL2A", "20230501075", "20230501", "K444-12345", "Rente", "",
        "CREDIT", 0.75, "", 1650.75));
  }

  /**
   * Define Expected results for Meta information.
   */
  private void loadOFXMetaInfoExp() {
    m_metainfo_exp.put("NL90KNAB0445266309",
        addOfxMetaInfo("INGBNL2A", "NL90KNAB0445266309", "", "ING_Enkele", "20230523", "20230523", 446.38));
    m_metainfo_exp.put("K444-12345", addOfxMetaInfo("INGBNL2A", "K444-12345", "Aap_NL94COBA0678011583",
        "ING_Enkele_spaar", "20230501", "20230526", 2750.00));
  }

//K444-12345;Aap_NL94COBA0678011583;ING_Enkele_spaar;20230501;20230526;2750.0
  // ING Transactions
  private IngTransaction addIngTransaction(String a_datum, String a_omschrijving, String a_rekening,
      String a_tegenrekening, String a_code, String a_af_bij, String a_bedrag, String a_mutatiesoort,
      String a_mededelingen, String a_saldoNaMutatie, String a_tag) {
    IngTransaction l_trans = new IngTransaction();
    // "Datum";"Naam / Omschrijving";"Rekening";"Tegenrekening";"Code";"Af
    // Bij";"Bedrag (EUR)";"Mutatiesoort";
    // "Mededelingen";"Saldo na mutatie";"Tag"
    l_trans.setDatum(a_datum);
    l_trans.setOmschrijving(a_omschrijving);
    l_trans.setRekening(a_rekening);
    l_trans.setTegenrekening(a_tegenrekening);
    l_trans.setCode(a_code);
    l_trans.setAf_Bij(a_af_bij);
    l_trans.setBedrag(a_bedrag);
    l_trans.setMutatiesoort(a_mutatiesoort);
    l_trans.setMededelingen(a_mededelingen);
    l_trans.setSaldo_na_mutatie(a_saldoNaMutatie);
    l_trans.setTag(a_tag);
    return l_trans;
  }

  // ING Saving transactions
  private IngSavingTransaction addIngSavingTransaction(String a_datum, String a_omschrijving, String a_rekening,
      String a_Rekeningnaam, String a_tegenrekening, String a_af_bij, double a_bedrag, String a_valuta,
      String a_mutatiesoort, String a_mededelingen, double a_saldoNaMutatie) {
    IngSavingTransaction l_trans = new IngSavingTransaction();
    // Datum Omschrijving Rekening Rekening naam Tegenrekening Af Bij Bedrag Valuta
    // Mutatiesoort Mededelingen Saldo na mutatie
    l_trans.setDatum(a_datum);
    l_trans.setOmschrijving(a_omschrijving);
    l_trans.setRekening(a_rekening);
    l_trans.setRekeningNaam(a_Rekeningnaam);
    l_trans.setTegenrekening(a_tegenrekening);
    l_trans.setAf_Bij(a_af_bij);
    l_trans.setBedrag(a_bedrag);
    l_trans.setValuta(a_valuta);
    l_trans.setMutatiesoort(a_mutatiesoort);
    l_trans.setMededelingen(a_mededelingen);
    l_trans.setSaldo_na_mutatie(a_saldoNaMutatie);
    return l_trans;
  }

  // Load OfxTransaction
  private OfxTransaction addOfxTransaction(String a_bankcode, String a_fitid, String a_dtposted, String a_account,
      String a_name, String a_accountto, String a_trntype, double a_trnamt, String a_memo, double a_Saldo_na_mutatie) {
    OfxTransaction l_trans = new OfxTransaction();
    l_trans.setBankCode(a_bankcode);
    l_trans.setFitid(a_fitid);

    Date dtp = DateToNumeric.String_NumericToDate(a_dtposted);
    l_trans.setDtposted(dtp);
    l_trans.setAccount(a_account);
    l_trans.setName(a_name);
    l_trans.setAccountto(a_accountto);
    l_trans.setTrntype(a_trntype);
    l_trans.setTrnamt(new BigDecimal(a_trnamt));
    l_trans.setMemo(a_memo);
    l_trans.setSaldo_na_mutatie(new BigDecimal(a_Saldo_na_mutatie));
    return l_trans;
  }

  // Load OFX Meta info
  private OfxMetaInfo addOfxMetaInfo(String a_bankcode, String a_account, String a_prefix, String a_suffix,
      String a_mindate, String a_maxdate, double a_balanceafter) {
    OfxMetaInfo l_metaInfo = new OfxMetaInfo(a_bankcode);

    // getAccount(), getPrefix(), getSuffix(), getMinDate(), getMaxDate(),
    // getBalanceAfterTransaction());
    l_metaInfo.setAccount(a_account);
    l_metaInfo.setPrefix(a_prefix);
    l_metaInfo.setSuffix(a_suffix);
    l_metaInfo.setMinDate(a_mindate);
    l_metaInfo.setMaxDate(a_maxdate);
    l_metaInfo.setBalanceAfterTransaction(a_balanceafter);

    return l_metaInfo;
  }

  /**
   * Compare list of ING Transactions with expected.
   * 
   * @param a_trans1 List of ING transactions.
   * @param a_trans2 List of expected ING transactions.
   * @return
   */
  private boolean compareINGTrans(List<IngTransaction> a_trans1, List<IngTransaction> a_trans2) {
    boolean bstat = false;
    if (a_trans1.size() == a_trans2.size()) {
      for (int i = 0; i < a_trans1.size(); i++) {
        bstat = a_trans1.get(i).equals(a_trans2.get(i));
      }
    }
    return bstat;
  }

  /**
   * Compare list of ING Saving Transactions with expected.
   * 
   * @param a_trans1 List of ING Saving transactions.
   * @param a_trans2 List of expected ING Saving transactions.
   * @return
   */
  private boolean compareINGSavingTrans(List<IngSavingTransaction> a_trans1, List<IngSavingTransaction> a_trans2) {
    boolean bstat = false;
    if (a_trans1.size() == a_trans2.size()) {
      for (int i = 0; i < a_trans1.size(); i++) {
        bstat = a_trans1.get(i).equals(a_trans2.get(i));
      }
    }
    return bstat;
  }

  /**
   * Compare with expected, OFX Transactions
   * 
   * @param a_trans1 OFX Transaction 1
   * @param a_trans2 OFX Transaction 2
   * @return Result, OFX transactions are equal (true) or if not equal then result
   *         is False.
   */
  private boolean compareOFXTrans(List<OfxTransaction> a_trans1, List<OfxTransaction> a_trans2) {
    boolean bstat = false;
    if (a_trans1.size() == a_trans2.size()) {
      for (int i = 0; i < a_trans1.size(); i++) {
        bstat = a_trans1.get(i).equals(a_trans2.get(i));
      }
    }
    return bstat;
  }
}
