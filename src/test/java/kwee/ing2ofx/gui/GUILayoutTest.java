package kwee.ing2ofx.gui;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JFileChooserFixture;
import org.assertj.swing.fixture.JCheckBoxFixture;

import javax.swing.JFrame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import kwee.ing2ofx.main.Main;
import kwee.ing2ofx.main.UserSetting;
import kwee.library.FileUtils;
import kwee.logger.TestLogger;
import kwee.testlibrary.TestFunctions;

public class GUILayoutTest extends TestCase {
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

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();

    // Parameters
    m_OrgParam = m_Functions.CopyUserSetting(m_param);
    m_param.set_ClearTransactions(true);

    m_param.set_AcountSeparateOFX(true);
    m_param.set_ConvertDecimalSeparator(false);
    m_param.set_ConvertDateFormat(false);
    m_param.set_SeparatorComma(false);
    m_param.set_Java(true);
    m_param.set_Level(Level.INFO);

    File ll_file = m_Functions.GetResourceFile(c_SynonymFile);
    m_OutputDir = ll_file.getParent();
    m_param.set_Synonym_file(new File(c_SynonymFile));
    m_param.save();

    // Start GUI, with prepared Usersettings
    if (frame != null) {
      frame.cleanUp();
    }

    JFrame l_frame = new JFrame();
    GUILayout guilayout = new GUILayout(true);
    l_frame.add(guilayout);
    frame = new FrameFixture(l_frame);
    frame.show();

    TestLogger.setup(Level.INFO);
  }

  @Override
  @After
  public void tearDown() throws Exception {
    super.tearDown();

    m_param = m_Functions.CopyUserSetting(m_OrgParam);
    m_param.save();
    this.frame.cleanUp();
    TestLogger.close();
  }

  @Test
  public void testGUILayoutING() {
    LOGGER.log(Level.INFO, "testGUILayoutING");
    FileUtils.checkCreateDirectory(m_OutputDir + m_OfxCombineSyn);

    frame.button("CSV/XML File").click();
    JFileChooserFixture fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir));
    fileChooser.fileNameTextBox().setText(c_IngTransFile); // Set the desired file name
    fileChooser.approve();

    frame.button("Read transactions").click();
    frame.button("Convert to OFX").click();

    // Evaluate results:
    boolean bstat = false;
    String logOutput = TestLogger.getOutput();

    bstat = logOutput.contains("Transactions read: 124, after doubles removed: 124");
    assertTrue(bstat);

    bstat = logOutput.contains("Grand total of transactions read: 124");
    assertTrue(bstat);

    AssertXmlFile(m_OfxEnkelExp, m_OfxEnkel, "NL54BKMG0378842587_Alle_rekeningen.ofx");
    AssertXmlFile(m_OfxEnkelExp, m_OfxEnkel, "NL90KNAB0445266309_Alle_rekeningen.ofx");
    AssertXmlFile(m_OfxEnkelExp, m_OfxEnkel, "NL94COBA0678011583_Alle_rekeningen.ofx");
    AssertFile(m_OfxEnkelExp, m_OfxEnkel, "_Saldos_Alle_rekeningen.csv");

    LOGGER.log(Level.INFO, "Ready testGUILayoutING Status:" + bstat);
  }

  @Test
  public void testGUILayoutINGSaving() {
    LOGGER.log(Level.INFO, "testGUILayoutINGSaving");

    // ING Saving trans
    FileUtils.checkCreateDirectory(m_OutputDir + m_OfxEnkel);

    frame.button("CSV/XML File").click();
    JFileChooserFixture fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir));
    fileChooser.fileNameTextBox().setText(c_IngSavingTransFile); // Set the desired file name
    fileChooser.approve();

    frame.button("Read transactions").click();
    frame.button("Convert to OFX").click();

    // Evaluate results
    boolean bstat = false;
    String logOutput = TestLogger.getOutput();

    bstat = logOutput.contains("Transactions read: 24, after doubles removed: 24");
    assertTrue(bstat);

    bstat = logOutput.contains("Grand total of transactions read: 24");
    assertTrue(bstat);

    AssertXmlFile(m_OfxEnkelExp, m_OfxEnkel, "NL54BKMG0378842587_K111-12345_Alle_spaarrekeningen.ofx");
    AssertXmlFile(m_OfxEnkelExp, m_OfxEnkel, "NL90KNAB0445266309_K555-12345_Alle_spaarrekeningen.ofx");
    AssertXmlFile(m_OfxEnkelExp, m_OfxEnkel, "NL94COBA0678011583_K222-12345_Alle_spaarrekeningen.ofx");
    AssertXmlFile(m_OfxEnkelExp, m_OfxEnkel, "NL94COBA0678011583_K333-12345_Alle_spaarrekeningen.ofx");
    AssertXmlFile(m_OfxEnkelExp, m_OfxEnkel, "NL94COBA0678011583_K444-12345_Alle_spaarrekeningen.ofx");
    AssertFile(m_OfxEnkelExp, m_OfxEnkel, "_Saldos_Alle_spaarrekeningen.csv");

    LOGGER.log(Level.INFO, "Ready testGUILayoutINGSaving Status:" + bstat);
  }

  @Test
  public void testGUILayoutSNS() {
    LOGGER.log(Level.INFO, "testGUILayoutSNS");
    FileUtils.checkCreateDirectory(m_OutputDir + m_OfxEnkel);

    frame.button("CSV/XML File").click();
    JFileChooserFixture fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir));
    fileChooser.fileNameTextBox().setText(c_SNSTransFile); // Set the desired file name
    fileChooser.approve();

    frame.button("Read transactions").click();
    frame.button("Convert to OFX").click();

    // Evaluate results:
    boolean bstat = false;
    String logOutput = TestLogger.getOutput();

    bstat = logOutput.contains("Transactions read: 9, after doubles removed: 9");
    assertTrue(bstat);

    bstat = logOutput.contains("Grand total of transactions read: 9");
    assertTrue(bstat);

    AssertXmlFile(m_OfxEnkelExp, m_OfxEnkel, "NL20LPLN0892606304_transactie-historie.ofx");
    AssertXmlFile(m_OfxEnkelExp, m_OfxEnkel, "NL38RABO0192584529_transactie-historie.ofx");
    AssertXmlFile(m_OfxEnkelExp, m_OfxEnkel, "NL45TRIO0953178943_transactie-historie.ofx");
    AssertFile(m_OfxEnkelExp, m_OfxEnkel, "_Saldos_transactie-historie.csv");

    LOGGER.log(Level.INFO, "Ready testGUILayoutSNS Status:" + bstat);
  }

  @Test
  public void testGUILayoutCombine() {
    LOGGER.log(Level.INFO, "testGUILayoutCombine");

    // Combine multiple input files
    FileUtils.checkCreateDirectory(m_OutputDir + m_OfxCombine);

    frame.button("CSV/XML File").click();
    JFileChooserFixture fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir));

    // Select multiple files
    File file1 = new File(m_OutputDir + "\\" + c_IngTransFile);
    File file2 = new File(m_OutputDir + "\\" + c_IngSavingTransFile);
    File file3 = new File(m_OutputDir + "\\" + c_SNSTransFile);
    fileChooser.selectFiles(file1, file2, file3);
    fileChooser.approve();

    frame.button("Read transactions").click();
    frame.button("Convert to OFX").click();

    // Evaluate results:
    boolean bstat = false;
    String logOutput = TestLogger.getOutput();

    bstat = logOutput.contains("Transactions read: 157, after doubles removed: 157");
    assertTrue(bstat);

    bstat = logOutput.contains("Grand total of transactions read: 157");
    assertTrue(bstat);

    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL20LPLN0892606304_transactie-historie.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL38RABO0192584529_transactie-historie.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL45TRIO0953178943_transactie-historie.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL54BKMG0378842587_Alle_rekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL54BKMG0378842587_K111-12345_Alle_spaarrekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL75FVLB0105564737_transactie-historie.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL90KNAB0445266309_Alle_rekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL90KNAB0445266309_K555-12345_Alle_spaarrekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL94COBA0678011583_Alle_rekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL94COBA0678011583_K222-12345_Alle_spaarrekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL94COBA0678011583_K333-12345_Alle_spaarrekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL94COBA0678011583_K444-12345_Alle_spaarrekeningen.ofx");
    AssertFile(m_OfxCombineExp, m_OfxCombine, "_Saldos_transactie-historie.csv");
    LOGGER.log(Level.INFO, "Ready testGUILayoutCombine Status:" + bstat);
  }

  @Test
  public void testGUILayoutCombineSyn() {
    LOGGER.log(Level.INFO, "testGUILayoutCombineSyn");

    // Combine multiple input files
    FileUtils.checkCreateDirectory(m_OutputDir + m_OfxCombineSyn);

    frame.menuItem("Synonym file").click();
    JFileChooserFixture fileChoosersyn = frame.fileChooser();
    fileChoosersyn.setCurrentDirectory(new File(m_OutputDir));
    fileChoosersyn.fileNameTextBox().setText(c_SynonymFile);
    fileChoosersyn.approve();

    frame.button("CSV/XML File").click();
    JFileChooserFixture fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir));

    // Select multiple files
    File file1 = new File(m_OutputDir + "\\" + c_IngTransFile);
    File file2 = new File(m_OutputDir + "\\" + c_IngSavingTransFile);
    File file3 = new File(m_OutputDir + "\\" + c_SNSTransFile);
    fileChooser.selectFiles(file1, file2, file3);
    fileChooser.approve();

    frame.button("Read transactions").click();
    frame.button("Convert to OFX").click();

    // Evaluate
    boolean bstat = false;
    String logOutput = TestLogger.getOutput();

    bstat = logOutput.contains("Transactions read: 157, after doubles removed: 157");
    assertTrue(bstat);

    bstat = logOutput.contains("Grand total of transactions read: 157");
    assertTrue(bstat);

    AssertXmlFile(m_OfxCombineExp, m_OfxCombineSyn, "NL94COBA0678011583_K222-12345_Alle_spaarrekeningen.ofx",
        "Aap_K222-12345_Alle_spaarrekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombineSyn, "NL45TRIO0953178943_transactie-historie.ofx",
        "Aap_NL45TRIO0953178943_transactie-historie.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombineSyn, "NL94COBA0678011583_Alle_rekeningen.ofx",
        "Aap_NL94COBA0678011583_Alle_rekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombineSyn, "NL94COBA0678011583_K333-12345_Alle_spaarrekeningen.ofx",
        "Aap_NL94COBA0678011583_K333-12345_Alle_spaarrekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombineSyn, "NL94COBA0678011583_K444-12345_Alle_spaarrekeningen.ofx",
        "Aap_NL94COBA0678011583_K444-12345_Alle_spaarrekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombineSyn, "NL20LPLN0892606304_transactie-historie.ofx",
        "Basis_NL20LPLN0892606304_transactie-historie.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombineSyn, "NL54BKMG0378842587_Alle_rekeningen.ofx",
        "Mies_NL54BKMG0378842587_Alle_rekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombineSyn, "NL54BKMG0378842587_K111-12345_Alle_spaarrekeningen.ofx",
        "Mies_NL54BKMG0378842587_K111-12345_Alle_spaarrekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombineSyn, "NL90KNAB0445266309_Alle_rekeningen.ofx",
        "Noot_NL90KNAB0445266309_Alle_rekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombineSyn, "NL90KNAB0445266309_K555-12345_Alle_spaarrekeningen.ofx",
        "Noot_NL90KNAB0445266309_K555-12345_Alle_spaarrekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombineSyn, "NL38RABO0192584529_transactie-historie.ofx",
        "Teun_NL38RABO0192584529_transactie-historie.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombineSyn, "NL75FVLB0105564737_transactie-historie.ofx",
        "Vuur_NL75FVLB0105564737_transactie-historie.ofx");
    AssertFile(m_OfxCombineExp, m_OfxCombineSyn, "_Saldos_transactie-historieSyn.csv",
        "_Saldos_transactie-historie.csv");
    LOGGER.log(Level.INFO, "Ready testGUILayoutCombineSyn Status:" + bstat);
  }

  @Test
  public void testGUILayoutDouble() {
    LOGGER.log(Level.INFO, "testGUILayoutDouble");

    // Combine multiple input files
    FileUtils.checkCreateDirectory(m_OutputDir + m_OfxCombine);

    JCheckBoxFixture checkBox = frame.checkBox("Clear transactions");
    checkBox.uncheck();

    frame.button("CSV/XML File").click();
    JFileChooserFixture fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir));

    // Select multiple files
    File file1 = new File(m_OutputDir + "\\" + c_IngTransFile);
    File file2 = new File(m_OutputDir + "\\" + c_IngSavingTransFile);
    File file3 = new File(m_OutputDir + "\\" + c_SNSTransFile);
    fileChooser.selectFiles(file1, file2, file3);
    fileChooser.approve();

    frame.button("Read transactions").click();

    LOGGER.log(Level.INFO, "Nogmaals inlezen.....");
    frame.button("CSV/XML File").click();
    fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir));

    file1 = new File(m_OutputDir + "\\" + c_IngTransFile);
    file2 = new File(m_OutputDir + "\\" + c_IngSavingTransFile);
    file3 = new File(m_OutputDir + "\\" + c_SNSTransFile);
    fileChooser.selectFiles(file1, file2, file3);
    fileChooser.approve();

    frame.button("Read transactions").click();

    frame.button("Convert to OFX").click();

    // Evaluate results:
    boolean bstat = false;
    String logOutput = TestLogger.getOutput();

    bstat = logOutput.contains("Transactions read: 157, after doubles removed: 157");
    assertTrue(bstat);

    bstat = logOutput.contains("Transactions read: 157, after doubles removed: 0");
    assertTrue(bstat);

    bstat = logOutput.contains("Grand total of transactions read: 157");
    assertTrue(bstat);

    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL20LPLN0892606304_transactie-historie.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL38RABO0192584529_transactie-historie.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL45TRIO0953178943_transactie-historie.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL54BKMG0378842587_Alle_rekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL54BKMG0378842587_K111-12345_Alle_spaarrekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL75FVLB0105564737_transactie-historie.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL90KNAB0445266309_Alle_rekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL90KNAB0445266309_K555-12345_Alle_spaarrekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL94COBA0678011583_Alle_rekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL94COBA0678011583_K222-12345_Alle_spaarrekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL94COBA0678011583_K333-12345_Alle_spaarrekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL94COBA0678011583_K444-12345_Alle_spaarrekeningen.ofx");
    AssertFile(m_OfxCombineExp, m_OfxCombine, "_Saldos_transactie-historie.csv");
    LOGGER.log(Level.INFO, "Ready testGUILayoutCombine Status:" + bstat);
  }

  @Test
  public void testGUILayoutOneByOne() {
    LOGGER.log(Level.INFO, "testGUILayoutOneByOne");

    // Combine multiple input files
    FileUtils.checkCreateDirectory(m_OutputDir + m_OfxCombine);

    JCheckBoxFixture checkBox = frame.checkBox("Clear transactions");
    checkBox.uncheck();

    frame.button("CSV/XML File").click();
    JFileChooserFixture fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir));
    fileChooser.fileNameTextBox().setText(c_IngTransFile);
    fileChooser.approve();
    frame.button("Read transactions").click();

    frame.button("CSV/XML File").click();
    fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir));
    fileChooser.fileNameTextBox().setText(c_IngSavingTransFile);
    fileChooser.approve();
    frame.button("Read transactions").click();

    frame.button("CSV/XML File").click();
    fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir));
    fileChooser.fileNameTextBox().setText(c_SNSTransFile);
    fileChooser.approve();
    frame.button("Read transactions").click();

    frame.button("Convert to OFX").click();

    // Evaluate results:
    boolean bstat = false;
    String logOutput = TestLogger.getOutput();

    bstat = logOutput.contains("Transactions read: 124, after doubles removed: 124");
    assertTrue(bstat);

    bstat = logOutput.contains("Transactions read: 24, after doubles removed: 24");
    assertTrue(bstat);

    bstat = logOutput.contains("Transactions read: 9, after doubles removed: 9");
    assertTrue(bstat);

    bstat = logOutput.contains("Grand total of transactions read: 157");
    assertTrue(bstat);

    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL20LPLN0892606304_transactie-historie.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL38RABO0192584529_transactie-historie.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL45TRIO0953178943_transactie-historie.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL54BKMG0378842587_Alle_rekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL54BKMG0378842587_K111-12345_Alle_spaarrekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL75FVLB0105564737_transactie-historie.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL90KNAB0445266309_Alle_rekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL90KNAB0445266309_K555-12345_Alle_spaarrekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL94COBA0678011583_Alle_rekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL94COBA0678011583_K222-12345_Alle_spaarrekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL94COBA0678011583_K333-12345_Alle_spaarrekeningen.ofx");
    AssertXmlFile(m_OfxCombineExp, m_OfxCombine, "NL94COBA0678011583_K444-12345_Alle_spaarrekeningen.ofx");
    AssertFile(m_OfxCombineExp, m_OfxCombine, "_Saldos_transactie-historie.csv");
    LOGGER.log(Level.INFO, "Ready testGUILayoutCombine Status:" + bstat);
  }

  // File asserts
  private void AssertXmlFile(String a_dir1, String a_dir2, String a_filename) {
    boolean bstat = false;
    String l_filename1 = m_OutputDir + "\\" + a_dir1 + "\\" + a_filename;
    String l_filename2 = m_OutputDir + "\\" + a_dir2 + "\\" + a_filename;
    try {
      bstat = m_Functions.compareXmlFiles(l_filename1, l_filename2);
      if (!bstat) {
        LOGGER.log(Level.INFO, "Files not equal " + a_dir1 + "\\" + a_filename + " and " + a_dir2 + "\\" + a_filename);
      }
    } catch (IOException e) {
      e.printStackTrace();
      LOGGER.log(Level.INFO, e.getMessage() + "/ Filename: " + a_filename);
    }
    assertTrue(bstat);
  }

  private void AssertXmlFile(String a_dirExp, String a_dir2, String a_filenameExp, String a_filename) {
    boolean bstat = false;
    String l_filename1 = m_OutputDir + "\\" + a_dirExp + "\\" + a_filenameExp;
    String l_filename2 = m_OutputDir + "\\" + a_dir2 + "\\" + a_filename;
    try {
      bstat = m_Functions.compareXmlFiles(l_filename1, l_filename2);
      if (!bstat) {
        LOGGER.log(Level.INFO,
            "Files not equal " + a_dirExp + "\\" + a_filenameExp + " and " + a_dir2 + "\\" + a_filename);
      }
    } catch (IOException e) {
      e.printStackTrace();
      LOGGER.log(Level.INFO, e.getMessage() + "/ Filename: " + a_filename);
    }
    assertTrue(bstat);
  }

  private void AssertFile(String a_dir1, String a_dir2, String a_filename) {
    boolean bstat = false;
    String l_filename1 = m_OutputDir + "\\" + a_dir1 + "\\" + a_filename;
    String l_filename2 = m_OutputDir + "\\" + a_dir2 + "\\" + a_filename;
    bstat = FileUtils.FileContentsEquals(l_filename1, l_filename2);
    if (!bstat) {
      LOGGER.log(Level.INFO, "Files not equal " + a_dir1 + "\\" + a_filename + " and " + a_dir2 + "\\" + a_filename);
    }
    assertTrue(bstat);
  }

  private void AssertFile(String a_dirExp, String a_dir2, String a_filenameExp, String a_filename) {
    boolean bstat = false;
    String l_filename1 = m_OutputDir + "\\" + a_dirExp + "\\" + a_filenameExp;
    String l_filename2 = m_OutputDir + "\\" + a_dir2 + "\\" + a_filename;
    bstat = FileUtils.FileContentsEquals(l_filename1, l_filename2);
    if (!bstat) {
      LOGGER.log(Level.INFO,
          "Files not equal " + a_dirExp + "\\" + a_filenameExp + " and " + a_dir2 + "\\" + a_filename);
    }
    assertTrue(bstat);
  }
}
