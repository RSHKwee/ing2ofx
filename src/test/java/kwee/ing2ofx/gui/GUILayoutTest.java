package kwee.ing2ofx.gui;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fest.swing.fixture.FrameFixture;

import javax.swing.JFrame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import kwee.ing2ofx.main.Main;
import kwee.ing2ofx.main.UserSetting;
import kwee.library.FileUtils;
import kwee.testlibrary.TestFunctions;

public class GUILayoutTest {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  private FrameFixture frame;

  private String c_SynonymFile = "Synoniem.txt";
  private String c_IngTransFile = "Alle_rekeningen.csv";
  private String c_IngSavingTransFile = "Alle_spaarrekeningen.csv";
  private String c_SNSTransFile = "transactie-historie.xml";

  public UserSetting m_param = Main.m_param;
  private UserSetting m_OrgParam = new UserSetting();
  private TestFunctions m_Functions = new TestFunctions();
  private String m_OutputDir;

  // Expected results in following dirs:
  private String m_OfxEnkelExp = "\\OFX_Enkel\\";
  private String m_OfxCombineExp = "\\OFX_Gecombineerd\\";

  // Generated results in following dirs:
  private String m_OfxEnkel = "\\OFX_Enkel_Gen\\";
  private String m_OfxCombine = "\\OFX_Combine_Gen\\";
  private String m_OfxCombineSyn = "\\OFX_Combine_GenSyn\\";

  @Before
  public void setUp() throws Exception {
    // Parameters
    m_OrgParam = m_Functions.CopyUserSetting(m_param);
    m_param.set_ClearTransactions(true);

    m_param.set_AcountSeparateOFX(true);
    m_param.set_ConvertDecimalSeparator(false);
    m_param.set_ConvertDateFormat(false);
    m_param.set_SeparatorComma(false);
    m_param.set_Java(true);

    m_param.set_Synonym_file(new File(c_SynonymFile));
    m_param.save();
  }

  @After
  public void tearDown() throws Exception {
    m_param = m_Functions.CopyUserSetting(m_OrgParam);
    m_param.save();
    this.frame.cleanUp();
  }

  @Test
  public void testGUILayoutING() {
    String l_FileList;
    LOGGER.log(Level.INFO, "testGUILayoutING");

    // ING Trans
    l_FileList = String.join(";", c_IngTransFile);
    m_param.set_CsvFiles(StringToFiles(l_FileList));
    m_param.set_OutputFolder(m_OutputDir + m_OfxEnkel);
    m_param.save();
    FileUtils.checkCreateDirectory(m_OutputDir + m_OfxEnkel);

    // Start GUI, with prepared Usersettings
    JFrame l_frame = new JFrame();
    GUILayout guilayout = new GUILayout(true);
    l_frame.add(guilayout);
    frame = new FrameFixture(l_frame);
    frame.show();

    frame.button("Read transactions").click();
    frame.button("Convert to OFX").click();

    /*
     * Evaluate results:
     * @formatter:off
     * NL54BKMG0378842587_Alle_rekeningen.ofx
     * NL90KNAB0445266309_Alle_rekeningen.ofx
     * NL94COBA0678011583_Alle_rekeningen.ofx
     * _Saldos_Alle_rekeningen.csv
     * @formatter:on
     */
    boolean bstat = false;
    bstat = AssertXmlFile(m_OfxEnkelExp, m_OfxEnkel, "NL54BKMG0378842587_Alle_rekeningen.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxEnkelExp, m_OfxEnkel, "NL90KNAB0445266309_Alle_rekeningen.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxEnkelExp, m_OfxEnkel, "NL94COBA0678011583_Alle_rekeningen.ofx");
    assertTrue(bstat);

    bstat = AssertFile(m_OfxEnkelExp, m_OfxEnkel, "_Saldos_Alle_rekeningen.csv");
    assertTrue(bstat);

    LOGGER.log(Level.INFO, "Ready testGUILayoutING Status:" + bstat);
  }

  @Test
  public void testGUILayoutINGSaving() {
    String l_FileList;
    LOGGER.log(Level.INFO, "testGUILayoutINGSaving");

    // ING Saving trans
    l_FileList = String.join(";", c_IngSavingTransFile);
    m_param.set_CsvFiles(StringToFiles(l_FileList));
    m_param.set_OutputFolder(m_OutputDir + m_OfxEnkel);
    m_param.save();
    FileUtils.checkCreateDirectory(m_OutputDir + m_OfxEnkel);

    // Start GUI, with prepared Usersettings
    JFrame l_frame = new JFrame();
    GUILayout guilayout = new GUILayout(true);
    l_frame.add(guilayout);
    frame = new FrameFixture(l_frame);
    frame.show();

    frame.button("Read transactions").click();
    frame.button("Convert to OFX").click();

    /*
     * @formatter:off
     * Evaluate results:
     *  NL54BKMG0378842587_K111-12345_Alle_spaarrekeningen.ofx
     *  NL90KNAB0445266309_K555-12345_Alle_spaarrekeningen.ofx
     *  NL94COBA0678011583_K222-12345_Alle_spaarrekeningen.ofx
     *  NL94COBA0678011583_K333-12345_Alle_spaarrekeningen.ofx
     *  NL94COBA0678011583_K444-12345_Alle_spaarrekeningen.ofx
     *  _Saldos_Alle_spaarrekeningen.csv
     * @formatter:on
     */
    boolean bstat = false;
    bstat = AssertXmlFile(m_OfxEnkelExp, m_OfxEnkel, "NL54BKMG0378842587_K111-12345_Alle_spaarrekeningen.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxEnkelExp, m_OfxEnkel, "NL90KNAB0445266309_K555-12345_Alle_spaarrekeningen.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxEnkelExp, m_OfxEnkel, "NL94COBA0678011583_K222-12345_Alle_spaarrekeningen.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxEnkelExp, m_OfxEnkel, "NL94COBA0678011583_K333-12345_Alle_spaarrekeningen.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxEnkelExp, m_OfxEnkel, "NL94COBA0678011583_K444-12345_Alle_spaarrekeningen.ofx");
    assertTrue(bstat);

    bstat = AssertFile(m_OfxEnkelExp, m_OfxEnkel, "_Saldos_Alle_spaarrekeningen.csv");
    assertTrue(bstat);

    LOGGER.log(Level.INFO, "Ready testGUILayoutINGSaving Status:" + bstat);
  }

  @Test
  public void testGUILayoutSNS() {
    String l_FileList;
    LOGGER.log(Level.INFO, "testGUILayoutSNS");
    // SNS Trans
    l_FileList = String.join(";", c_SNSTransFile);
    m_param.set_CsvFiles(StringToFiles(l_FileList));
    m_param.set_OutputFolder(m_OutputDir + m_OfxEnkel);
    m_param.save();
    FileUtils.checkCreateDirectory(m_OutputDir + m_OfxEnkel);

    JFrame l_frame = new JFrame();
    GUILayout guilayout = new GUILayout(true);
    l_frame.add(guilayout);
    frame = new FrameFixture(l_frame);
    frame.show();

    frame.button("Read transactions").click();
    frame.button("Convert to OFX").click();

    /*
     * @formatter:off
     * Evaluate results:
     *  NL20LPLN0892606304_transactie-historie.ofx
     *  NL38RABO0192584529_transactie-historie.ofx
     *  NL45TRIO0953178943_transactie-historie.ofx
     *  _Saldos_transactie-historie.csv
     * @formatter:on
     */
    boolean bstat = false;
    bstat = AssertXmlFile(m_OfxEnkelExp, m_OfxEnkel, "NL20LPLN0892606304_transactie-historie.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxEnkelExp, m_OfxEnkel, "NL38RABO0192584529_transactie-historie.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxEnkelExp, m_OfxEnkel, "NL45TRIO0953178943_transactie-historie.ofx");
    assertTrue(bstat);

    bstat = AssertFile(m_OfxEnkelExp, m_OfxEnkel, "_Saldos_transactie-historie.csv");
    assertTrue(bstat);

    LOGGER.log(Level.INFO, "Ready testGUILayoutSNS Status:" + bstat);
  }

  @Test
  public void testGUILayoutCombine() {
    String l_FileList;
    LOGGER.log(Level.INFO, "testGUILayoutCombine");

    // Combine multiple input files
    l_FileList = String.join(";", c_IngTransFile, c_IngSavingTransFile, c_SNSTransFile);
    m_param.set_CsvFiles(StringToFiles(l_FileList));
    m_param.set_OutputFolder(m_OutputDir + m_OfxCombine);
    m_param.save();
    FileUtils.checkCreateDirectory(m_OutputDir + m_OfxCombine);

    JFrame l_frame = new JFrame();
    GUILayout guilayout = new GUILayout(true);
    l_frame.add(guilayout);
    frame = new FrameFixture(l_frame);
    frame.show();

    frame.button("Read transactions").click();
    frame.button("Convert to OFX").click();

    /*
     * @formatter:off
     * 
     * Evaluate results:
     *  NL20LPLN0892606304_transactie-historie.ofx
     *  NL38RABO0192584529_transactie-historie.ofx
     *  NL45TRIO0953178943_transactie-historie.ofx
     *  NL54BKMG0378842587_Alle_rekeningen.ofx
     *  NL54BKMG0378842587_K111-12345_Alle_spaarrekeningen.ofx
     *  NL75FVLB0105564737_transactie-historie.ofx
     *  NL90KNAB0445266309_Alle_rekeningen.ofx
     *  NL90KNAB0445266309_K555-12345_Alle_spaarrekeningen.ofx
     *  NL94COBA0678011583_Alle_rekeningen.ofx
     *  NL94COBA0678011583_K222-12345_Alle_spaarrekeningen.ofx
     *  NL94COBA0678011583_K333-12345_Alle_spaarrekeningen.ofx
     *  NL94COBA0678011583_K444-12345_Alle_spaarrekeningen.ofx
     *  _Saldos_transactie-historie.csv
     * @formatter:on
     */
    boolean bstat = false;
    bstat = AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL20LPLN0892606304_transactie-historie.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL38RABO0192584529_transactie-historie.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL45TRIO0953178943_transactie-historie.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL54BKMG0378842587_Alle_rekeningen.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL54BKMG0378842587_K111-12345_Alle_spaarrekeningen.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL75FVLB0105564737_transactie-historie.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL90KNAB0445266309_Alle_rekeningen.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL90KNAB0445266309_K555-12345_Alle_spaarrekeningen.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL94COBA0678011583_Alle_rekeningen.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL94COBA0678011583_K222-12345_Alle_spaarrekeningen.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL94COBA0678011583_K333-12345_Alle_spaarrekeningen.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL94COBA0678011583_K444-12345_Alle_spaarrekeningen.ofx");
    assertTrue(bstat);

    bstat = AssertFile(m_OfxCombineExp, m_OfxCombine, "_Saldos_transactie-historie.csv");
    assertTrue(bstat);
    LOGGER.log(Level.INFO, "Ready testGUILayoutCombine Status:" + bstat);
  }

  @Test
  public void testGUILayoutCombineSyn() {
    String l_FileList;
    LOGGER.log(Level.INFO, "testGUILayoutCombineSyn");

    // Combine multiple input files
    l_FileList = String.join(";", c_IngTransFile, c_IngSavingTransFile, c_SNSTransFile);
    m_param.set_CsvFiles(StringToFiles(l_FileList));
    m_param.set_OutputFolder(m_OutputDir + m_OfxCombineSyn);
    m_param.set_Synonym_file(new File(m_OutputDir + "\\" + c_SynonymFile));
    m_param.save();
    FileUtils.checkCreateDirectory(m_OutputDir + m_OfxCombineSyn);

    JFrame l_frame = new JFrame();
    GUILayout guilayout = new GUILayout(true);
    l_frame.add(guilayout);
    frame = new FrameFixture(l_frame);
    frame.show();

    frame.button("Read transactions").click();
    frame.button("Convert to OFX").click();

    /*
     * @formatter:off
     * 
     * Evaluate results:
     *  NL20LPLN0892606304_transactie-historie.ofx
     *  NL38RABO0192584529_transactie-historie.ofx
     *  NL45TRIO0953178943_transactie-historie.ofx
     *  NL54BKMG0378842587_Alle_rekeningen.ofx
     *  NL54BKMG0378842587_K111-12345_Alle_spaarrekeningen.ofx
     *  NL75FVLB0105564737_transactie-historie.ofx
     *  NL90KNAB0445266309_Alle_rekeningen.ofx
     *  NL90KNAB0445266309_K555-12345_Alle_spaarrekeningen.ofx
     *  NL94COBA0678011583_Alle_rekeningen.ofx
     *  NL94COBA0678011583_K222-12345_Alle_spaarrekeningen.ofx
     *  NL94COBA0678011583_K333-12345_Alle_spaarrekeningen.ofx
     *  NL94COBA0678011583_K444-12345_Alle_spaarrekeningen.ofx
     *  _Saldos_transactie-historie.csv
     * @formatter:on
     */
    boolean bstat = false;
    bstat = AssertXmlFile(m_OfxCombineExp, m_OfxCombineSyn, "NL94COBA0678011583_K222-12345_Alle_spaarrekeningen.ofx",
        "Aap_K222-12345_Alle_spaarrekeningen.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxCombineExp, m_OfxCombineSyn, "NL45TRIO0953178943_transactie-historie.ofx",
        "Aap_NL45TRIO0953178943_transactie-historie.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxCombineExp, m_OfxCombineSyn, "NL94COBA0678011583_Alle_rekeningen.ofx",
        "Aap_NL94COBA0678011583_Alle_rekeningen.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxCombineExp, m_OfxCombineSyn, "NL94COBA0678011583_K333-12345_Alle_spaarrekeningen.ofx",
        "Aap_NL94COBA0678011583_K333-12345_Alle_spaarrekeningen.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxCombineExp, m_OfxCombineSyn, "NL94COBA0678011583_K444-12345_Alle_spaarrekeningen.ofx",
        "Aap_NL94COBA0678011583_K444-12345_Alle_spaarrekeningen.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxCombineExp, m_OfxCombineSyn, "NL20LPLN0892606304_transactie-historie.ofx",
        "Basis_NL20LPLN0892606304_transactie-historie.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxCombineExp, m_OfxCombineSyn, "NL54BKMG0378842587_Alle_rekeningen.ofx",
        "Mies_NL54BKMG0378842587_Alle_rekeningen.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxCombineExp, m_OfxCombineSyn, "NL54BKMG0378842587_K111-12345_Alle_spaarrekeningen.ofx",
        "Mies_NL54BKMG0378842587_K111-12345_Alle_spaarrekeningen.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxCombineExp, m_OfxCombineSyn, "NL90KNAB0445266309_Alle_rekeningen.ofx",
        "Noot_NL90KNAB0445266309_Alle_rekeningen.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxCombineExp, m_OfxCombineSyn, "NL90KNAB0445266309_K555-12345_Alle_spaarrekeningen.ofx",
        "Noot_NL90KNAB0445266309_K555-12345_Alle_spaarrekeningen.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxCombineExp, m_OfxCombineSyn, "NL38RABO0192584529_transactie-historie.ofx",
        "Teun_NL38RABO0192584529_transactie-historie.ofx");
    assertTrue(bstat);

    bstat = AssertXmlFile(m_OfxCombineExp, m_OfxCombineSyn, "NL75FVLB0105564737_transactie-historie.ofx",
        "Vuur_NL75FVLB0105564737_transactie-historie.ofx");
    assertTrue(bstat);

    bstat = AssertFile(m_OfxCombineExp, m_OfxCombineSyn, "_Saldos_transactie-historieSyn.csv",
        "_Saldos_transactie-historie.csv");
    assertTrue(bstat);
    LOGGER.log(Level.INFO, "Ready testGUILayoutCombineSyn Status:" + bstat);
  }

  // Local functions/methods
  private String c_StringDelim = ";";

  private File[] StringToFiles(String a_Files) {
    String[] ls_files = a_Files.split(c_StringDelim);

    File[] l_files = new File[ls_files.length];
    for (int i = 0; i < ls_files.length; i++) {
      File ll_file = m_Functions.GetResourceFile(ls_files[i]);
      l_files[i] = ll_file;
      m_OutputDir = ll_file.getParent();
    }
    return l_files;
  }

  private boolean AssertXmlFile(String a_dir1, String a_dir2, String a_filename) {
    boolean bstat = false;
    String l_filename1 = m_OutputDir + "\\" + a_dir1 + "\\" + a_filename;
    String l_filename2 = m_OutputDir + "\\" + a_dir2 + "\\" + a_filename;
    try {
      bstat = m_Functions.compareXmlFiles(l_filename1, l_filename2);
    } catch (IOException e) {
      e.printStackTrace();
      LOGGER.log(Level.INFO, e.getMessage() + "/ Filename: " + a_filename);
    }
    return bstat;
  }

  private boolean AssertXmlFile(String a_dirExp, String a_dir2, String a_filenameExp, String a_filename) {
    boolean bstat = false;
    String l_filename1 = m_OutputDir + "\\" + a_dirExp + "\\" + a_filenameExp;
    String l_filename2 = m_OutputDir + "\\" + a_dir2 + "\\" + a_filename;
    try {
      bstat = m_Functions.compareXmlFiles(l_filename1, l_filename2);
    } catch (IOException e) {
      e.printStackTrace();
      LOGGER.log(Level.INFO, e.getMessage() + "/ Filename: " + a_filename);
    }
    return bstat;
  }

  private boolean AssertFile(String a_dir1, String a_dir2, String a_filename) {
    boolean bstat = false;
    String l_filename1 = m_OutputDir + "\\" + a_dir1 + "\\" + a_filename;
    String l_filename2 = m_OutputDir + "\\" + a_dir2 + "\\" + a_filename;
    bstat = FileUtils.FileContentsEquals(l_filename1, l_filename2);
    return bstat;
  }

  private boolean AssertFile(String a_dirExp, String a_dir2, String a_filenameExp, String a_filename) {
    boolean bstat = false;
    String l_filename1 = m_OutputDir + "\\" + a_dirExp + "\\" + a_filenameExp;
    String l_filename2 = m_OutputDir + "\\" + a_dir2 + "\\" + a_filename;
    bstat = FileUtils.FileContentsEquals(l_filename1, l_filename2);
    return bstat;
  }
}