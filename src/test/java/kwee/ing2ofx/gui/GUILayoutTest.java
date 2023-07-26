package kwee.ing2ofx.gui;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.Robot;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JFileChooserFixture;
import org.assertj.swing.launcher.ApplicationLauncher;
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
  Object lock = GUILayout.lock;

  private String c_SynonymFile = "Synoniem.txt";
  private String c_IngTransFile = "Alle_rekeningen.csv";
  private String c_IngSavingTransFile = "Alle_spaarrekeningen.csv";
  private String c_SNSTransFile = "transactie-historie.xml";

  private UserSetting m_OrgParam = new UserSetting();
  private TestFunctions m_Functions = new TestFunctions();
  private String m_OutputDir;

  // Expected results in following dirs:
  private String m_DirExp_Suffux = "_Exp";

  // Generated results in following dirs:
  private String m_OfxEnkelING = "OFX_ING";
  private String m_OfxEnkelINGSaving = "OFX_INGSaving";
  private String m_OfxEnkelSNS = "OFX_SNS";
  private String m_OfxCombine = "OFX_Combine";
  private String m_OfxCombineSyn = "OFX_Combine_Syn";
  private String m_OfxCombineDouble = "OFX_CombineDouble_Syn";
  private String m_OfxCombineOneByOne = "OFX_CombineOneByOne_Syn";

  /**
   * setUp, store original User parameters and reset parameters for Test purpose.
   * Start GUI
   */
  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();

    // Parameters
    m_OrgParam = Main.m_param.copy();
    Main.m_param.set_ClearTransactions(true);

    Main.m_param.set_AcountSeparateOFX(true);
    Main.m_param.set_ConvertDecimalSeparator(false);
    Main.m_param.set_ConvertDateFormat(false);
    Main.m_param.set_SeparatorComma(false);
    Main.m_param.set_Java(true);
    Main.m_param.set_Level(Level.INFO);

    File ll_file = m_Functions.GetResourceFile(c_SynonymFile);
    m_OutputDir = ll_file.getParent();
    Main.m_param.set_Synonym_file(new File(c_SynonymFile));
    Main.m_param.save();

    // Launch your application or obtain a reference to an existing Swing frame
    ApplicationLauncher.application(kwee.ing2ofx.main.Main.class).start();
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }

    // Create a FrameFixture instance
    JFrame l_frame = Main.createAndShowGUI();
    l_frame.setName("DEFAULT");

    // Get the robot associated with the FrameFixture
    Robot robot = BasicRobot.robotWithCurrentAwtHierarchy();
    frame = new FrameFixture(robot, l_frame);

    TestLogger.setup(Level.INFO);
  }

  /**
   * Reset User parameters to original settings. Close GUI.
   */
  @Override
  @After
  public void tearDown() throws Exception {
    super.tearDown();

    Main.m_param = m_OrgParam.copy();
    Main.m_param.save();
    this.frame.cleanUp();
    TestLogger.close();
  }

  /**
   * Test handling of ING Transactions, csv to OFX.
   */
  @Test
  public void testGUILayoutING() {
    LOGGER.log(Level.INFO, "testGUILayoutING");
    FileUtils.checkCreateDirectory(m_OutputDir + "/" + m_OfxEnkelING);

    frame.button("CSV/XML File").click();
    JFileChooserFixture fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir));
    fileChooser.fileNameTextBox().setText(c_IngTransFile); // Set the desired file name
    fileChooser.approve();
    frame.button("Read transactions").click();

    frame.button("Output folder").click();
    fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir + "/" + m_OfxEnkelING + "/"));
    fileChooser.approve();

    frame.button("Convert to OFX").click();

    // Evaluate results:
    synchronized (lock) {
      String logOutput = TestLogger.getOutput();

      assertTrue(logOutput.contains("Transactions read: 124, after doubles removed: 124"));
      assertTrue(logOutput.contains("Grand total of transactions read: 124"));

      AssertXmlFile(m_OfxEnkelING, "NL54BKMG0378842587_Alle_rekeningen.ofx");
      AssertXmlFile(m_OfxEnkelING, "NL90KNAB0445266309_Alle_rekeningen.ofx");
      AssertXmlFile(m_OfxEnkelING, "NL94COBA0678011583_Alle_rekeningen.ofx");
      AssertFile(m_OfxEnkelING, "_Saldos_Alle_rekeningen.csv");

      LOGGER.log(Level.INFO, "Ready testGUILayoutING");
    }
  }

  /**
   * Test handling of ING Saving transactions, csv to OFX.
   */
  @Test
  public void testGUILayoutINGSaving() {
    LOGGER.log(Level.INFO, "testGUILayoutINGSaving");
    FileUtils.checkCreateDirectory(m_OutputDir + "/" + m_OfxEnkelINGSaving);

    frame.button("Output folder").click();
    JFileChooserFixture fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir + "/" + m_OfxEnkelINGSaving));
    fileChooser.approve();

    frame.button("CSV/XML File").click();
    fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir));
    fileChooser.fileNameTextBox().setText(c_IngSavingTransFile); // Set the desired file name
    fileChooser.approve();
    frame.button("Read transactions").click();

    frame.button("Convert to OFX").click();

    // Evaluate results
    synchronized (lock) {
      String logOutput = TestLogger.getOutput();

      assertTrue(logOutput.contains("Transactions read: 24, after doubles removed: 24"));
      assertTrue(logOutput.contains("Grand total of transactions read: 24"));

      AssertXmlFile(m_OfxEnkelINGSaving, "NL54BKMG0378842587_K111-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxEnkelINGSaving, "NL90KNAB0445266309_K555-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxEnkelINGSaving, "NL94COBA0678011583_K222-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxEnkelINGSaving, "NL94COBA0678011583_K333-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxEnkelINGSaving, "NL94COBA0678011583_K444-12345_Alle_spaarrekeningen.ofx");
      AssertFile(m_OfxEnkelINGSaving, "_Saldos_Alle_spaarrekeningen.csv");

      LOGGER.log(Level.INFO, "Ready testGUILayoutINGSaving");
    }
  }

  /**
   * Test handling of SNS Transactions, CAMT0.53 to OFX.
   */
  @Test
  public void testGUILayoutSNS() {
    LOGGER.log(Level.INFO, "testGUILayoutSNS");
    FileUtils.checkCreateDirectory(m_OutputDir + "/" + m_OfxEnkelSNS);

    frame.button("Output folder").click();
    JFileChooserFixture fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir + "/" + m_OfxEnkelSNS));
    fileChooser.approve();

    frame.button("CSV/XML File").click();
    fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir));
    fileChooser.fileNameTextBox().setText(c_SNSTransFile); // Set the desired file name
    fileChooser.approve();
    frame.button("Read transactions").click();

    frame.button("Convert to OFX").click();

    // Evaluate results:
    synchronized (lock) {
      String logOutput = TestLogger.getOutput();

      assertTrue(logOutput.contains("Transactions read: 9, after doubles removed: 9"));
      assertTrue(logOutput.contains("Grand total of transactions read: 9"));

      AssertXmlFile(m_OfxEnkelSNS, "NL20LPLN0892606304_transactie-historie.ofx");
      AssertXmlFile(m_OfxEnkelSNS, "NL38RABO0192584529_transactie-historie.ofx");
      AssertXmlFile(m_OfxEnkelSNS, "NL45TRIO0953178943_transactie-historie.ofx");
      AssertFile(m_OfxEnkelSNS, "_Saldos_transactie-historie.csv");

      LOGGER.log(Level.INFO, "Ready testGUILayoutSNS");
    }
  }

  /**
   * Test handling of Combined ING-, ING Saving- and SNS Transactions.
   */
  @Test
  public void testGUILayoutCombine() {
    LOGGER.log(Level.INFO, "testGUILayoutCombine");
    FileUtils.checkCreateDirectory(m_OutputDir + "/" + m_OfxCombine);

    frame.button("Output folder").click();
    JFileChooserFixture fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir + "/" + m_OfxCombine));
    fileChooser.approve();

    frame.button("CSV/XML File").click();
    fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir));

    // Select multiple files
    File file1 = new File(m_OutputDir + "/" + c_IngTransFile);
    File file2 = new File(m_OutputDir + "/" + c_IngSavingTransFile);
    File file3 = new File(m_OutputDir + "/" + c_SNSTransFile);
    fileChooser.selectFiles(file1, file2, file3);
    fileChooser.approve();
    frame.button("Read transactions").click();

    frame.button("Convert to OFX").click();

    // Evaluate results:
    synchronized (lock) {
      String logOutput = TestLogger.getOutput();

      assertTrue(logOutput.contains("Transactions read: 157, after doubles removed: 157"));
      assertTrue(logOutput.contains("Grand total of transactions read: 157"));

      AssertXmlFile(m_OfxCombine, "NL20LPLN0892606304_transactie-historie.ofx");
      AssertXmlFile(m_OfxCombine, "NL38RABO0192584529_transactie-historie.ofx");
      AssertXmlFile(m_OfxCombine, "NL45TRIO0953178943_transactie-historie.ofx");
      AssertXmlFile(m_OfxCombine, "NL54BKMG0378842587_Alle_rekeningen.ofx");
      AssertXmlFile(m_OfxCombine, "NL54BKMG0378842587_K111-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombine, "NL75FVLB0105564737_transactie-historie.ofx");
      AssertXmlFile(m_OfxCombine, "NL90KNAB0445266309_Alle_rekeningen.ofx");
      AssertXmlFile(m_OfxCombine, "NL90KNAB0445266309_K555-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombine, "NL94COBA0678011583_Alle_rekeningen.ofx");
      AssertXmlFile(m_OfxCombine, "NL94COBA0678011583_K222-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombine, "NL94COBA0678011583_K333-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombine, "NL94COBA0678011583_K444-12345_Alle_spaarrekeningen.ofx");
      AssertFile(m_OfxCombine, "_Saldos_transactie-historie.csv");
      LOGGER.log(Level.INFO, "Ready testGUILayoutCombine");
    }
  }

  /**
   * Test handling of Combined ING-, ING Saving- and SNS Transactions and make use
   * of a Synonym file.
   */
  @Test
  public void testGUILayoutCombineSyn() {
    LOGGER.log(Level.INFO, "testGUILayoutCombineSyn");
    FileUtils.checkCreateDirectory(m_OutputDir + "/" + m_OfxCombineSyn);

    // Define Synonym file
    frame.menuItem("Synonym file").click();
    JFileChooserFixture fileChoosersyn = frame.fileChooser();
    fileChoosersyn.setCurrentDirectory(new File(m_OutputDir));
    fileChoosersyn.fileNameTextBox().setText(c_SynonymFile);
    fileChoosersyn.approve();

    frame.button("Output folder").click();
    JFileChooserFixture fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir + "/" + m_OfxCombineSyn));
    fileChooser.approve();

    frame.button("CSV/XML File").click();
    fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir));

    // Select multiple files
    File file1 = new File(m_OutputDir + "/" + c_IngTransFile);
    File file2 = new File(m_OutputDir + "/" + c_IngSavingTransFile);
    File file3 = new File(m_OutputDir + "/" + c_SNSTransFile);
    fileChooser.selectFiles(file1, file2, file3);
    fileChooser.approve();
    frame.button("Read transactions").click();

    frame.button("Convert to OFX").click();

    // Evaluate results
    synchronized (lock) {
      String logOutput = TestLogger.getOutput();

      assertTrue(logOutput.contains("Transactions read: 157, after doubles removed: 157"));
      assertTrue(logOutput.contains("Grand total of transactions read: 157"));

      AssertXmlFile(m_OfxCombineSyn, "Aap_K222-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombineSyn, "Aap_NL45TRIO0953178943_transactie-historie.ofx");
      AssertXmlFile(m_OfxCombineSyn, "Aap_NL94COBA0678011583_Alle_rekeningen.ofx");
      AssertXmlFile(m_OfxCombineSyn, "Aap_NL94COBA0678011583_K333-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombineSyn, "Aap_NL94COBA0678011583_K444-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombineSyn, "Basis_NL20LPLN0892606304_transactie-historie.ofx");
      AssertXmlFile(m_OfxCombineSyn, "Mies_NL54BKMG0378842587_Alle_rekeningen.ofx");
      AssertXmlFile(m_OfxCombineSyn, "Mies_NL54BKMG0378842587_K111-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombineSyn, "Noot_NL90KNAB0445266309_Alle_rekeningen.ofx");
      AssertXmlFile(m_OfxCombineSyn, "Noot_NL90KNAB0445266309_K555-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombineSyn, "Teun_NL38RABO0192584529_transactie-historie.ofx");
      AssertXmlFile(m_OfxCombineSyn, "Vuur_NL75FVLB0105564737_transactie-historie.ofx");
      AssertFile(m_OfxCombineSyn, "_Saldos_transactie-historie.csv");
      LOGGER.log(Level.INFO, "Ready testGUILayoutCombineSyn");
    }
  }

  /**
   * Test double transactions, doubles are filtered.
   */
  @Test
  public void testGUILayoutDouble() {
    LOGGER.log(Level.INFO, "testGUILayoutDouble");
    FileUtils.checkCreateDirectory(m_OutputDir + "/" + m_OfxCombineDouble);

    JCheckBoxFixture checkBox = frame.checkBox("Clear transactions");
    checkBox.uncheck();

    frame.button("Output folder").click();
    JFileChooserFixture fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir + "/" + m_OfxCombineDouble));
    fileChooser.approve();

    frame.button("CSV/XML File").click();
    fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir));
    // Select multiple files
    File file1 = new File(m_OutputDir + "/" + c_IngTransFile);
    File file2 = new File(m_OutputDir + "/" + c_IngSavingTransFile);
    File file3 = new File(m_OutputDir + "/" + c_SNSTransFile);
    fileChooser.selectFiles(file1, file2, file3);
    fileChooser.approve();
    frame.button("Read transactions").click();

    LOGGER.log(Level.INFO, "Read again (double).....");
    frame.button("CSV/XML File").click();
    fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir));

    file1 = new File(m_OutputDir + "/" + c_IngTransFile);
    file2 = new File(m_OutputDir + "/" + c_IngSavingTransFile);
    file3 = new File(m_OutputDir + "/" + c_SNSTransFile);
    fileChooser.selectFiles(file1, file2, file3);
    fileChooser.approve();
    frame.button("Read transactions").click();

    frame.button("Convert to OFX").click();

    // Evaluate results:
    synchronized (lock) {
      // Logmessages
      String logOutput = TestLogger.getOutput();
      assertTrue(logOutput.contains("Transactions read: 157, after doubles removed: 157"));
      assertTrue(logOutput.contains("Transactions read: 157, after doubles removed: 0"));
      assertTrue(logOutput.contains("Grand total of transactions read: 157"));

      // Generated files
      AssertXmlFile(m_OfxCombineDouble, "NL20LPLN0892606304_transactie-historie.ofx");
      AssertXmlFile(m_OfxCombineDouble, "NL38RABO0192584529_transactie-historie.ofx");
      AssertXmlFile(m_OfxCombineDouble, "NL45TRIO0953178943_transactie-historie.ofx");
      AssertXmlFile(m_OfxCombineDouble, "NL54BKMG0378842587_Alle_rekeningen.ofx");
      AssertXmlFile(m_OfxCombineDouble, "NL54BKMG0378842587_K111-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombineDouble, "NL75FVLB0105564737_transactie-historie.ofx");
      AssertXmlFile(m_OfxCombineDouble, "NL90KNAB0445266309_Alle_rekeningen.ofx");
      AssertXmlFile(m_OfxCombineDouble, "NL90KNAB0445266309_K555-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombineDouble, "NL94COBA0678011583_Alle_rekeningen.ofx");
      AssertXmlFile(m_OfxCombineDouble, "NL94COBA0678011583_K222-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombineDouble, "NL94COBA0678011583_K333-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombineDouble, "NL94COBA0678011583_K444-12345_Alle_spaarrekeningen.ofx");
      AssertFile(m_OfxCombineDouble, "_Saldos_transactie-historie.csv");
      LOGGER.log(Level.INFO, "Ready testGUILayoutCombine");
    }
  }

  /**
   * Test handling of Combined ING-, ING Saving- and SNS Transactions, are entered
   * one by one.
   */
  @Test
  public void testGUILayoutOneByOne() {
    LOGGER.log(Level.FINE, "testGUILayoutOneByOne");

    // Combine multiple input files
    FileUtils.checkCreateDirectory(m_OutputDir + "/" + m_OfxCombineOneByOne);

    JCheckBoxFixture checkBox = frame.checkBox("Clear transactions");
    checkBox.uncheck();

    frame.button("Output folder").click();
    JFileChooserFixture fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir + "/" + m_OfxCombineOneByOne));
    fileChooser.approve();

    frame.button("CSV/XML File").click();
    fileChooser = frame.fileChooser();
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
    synchronized (lock) {
      String logOutput = TestLogger.getOutput();

      assertTrue(logOutput.contains("Transactions read: 124, after doubles removed: 124"));
      assertTrue(logOutput.contains("Transactions read: 24, after doubles removed: 24"));
      assertTrue(logOutput.contains("Transactions read: 9, after doubles removed: 9"));
      assertTrue(logOutput.contains("Grand total of transactions read: 157"));

      AssertXmlFile(m_OfxCombineOneByOne, "NL20LPLN0892606304_transactie-historie.ofx");
      AssertXmlFile(m_OfxCombineOneByOne, "NL38RABO0192584529_transactie-historie.ofx");
      AssertXmlFile(m_OfxCombineOneByOne, "NL45TRIO0953178943_transactie-historie.ofx");
      AssertXmlFile(m_OfxCombineOneByOne, "NL54BKMG0378842587_Alle_rekeningen.ofx");
      AssertXmlFile(m_OfxCombineOneByOne, "NL54BKMG0378842587_K111-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombineOneByOne, "NL75FVLB0105564737_transactie-historie.ofx");
      AssertXmlFile(m_OfxCombineOneByOne, "NL90KNAB0445266309_Alle_rekeningen.ofx");
      AssertXmlFile(m_OfxCombineOneByOne, "NL90KNAB0445266309_K555-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombineOneByOne, "NL94COBA0678011583_Alle_rekeningen.ofx");
      AssertXmlFile(m_OfxCombineOneByOne, "NL94COBA0678011583_K222-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombineOneByOne, "NL94COBA0678011583_K333-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombineOneByOne, "NL94COBA0678011583_K444-12345_Alle_spaarrekeningen.ofx");
      AssertFile(m_OfxCombineOneByOne, "_Saldos_transactie-historie.csv");
      LOGGER.log(Level.INFO, "Ready testGUILayoutCombine");
    }
  }

  // File asserts
  /**
   * File assert for XML files
   * 
   * @param a_dir      Test dir prefix
   * @param a_filename Filename
   */
  private void AssertXmlFile(String a_dir, String a_filename) {
    boolean bstat = false;
    String l_filename1 = m_OutputDir + "/" + a_dir + m_DirExp_Suffux + "/" + a_filename;
    String l_filename2 = m_OutputDir + "/" + a_dir + "/" + a_filename;
    try {
      bstat = m_Functions.compareXmlFiles(l_filename1, l_filename2);
      if (!bstat) {
        LOGGER.log(Level.INFO,
            "Files not equal " + a_dir + m_DirExp_Suffux + "/" + a_filename + " and " + a_dir + "/" + a_filename);
      }
    } catch (IOException e) {
      // e.printStackTrace();
      LOGGER.log(Level.INFO, e.getMessage() + "File " + a_dir + "/" + a_filename + " and " + a_dir + m_DirExp_Suffux);
    }
    assertTrue(bstat);
  }

  /**
   * File assert for log files
   * 
   * @param a_dir      Test dir prefix
   * @param a_filename Filename
   */
  private void AssertFile(String a_dir, String a_filename) {
    boolean bstat = false;
    String l_filename1 = m_OutputDir + "/" + a_dir + m_DirExp_Suffux + "/" + a_filename;
    String l_filename2 = m_OutputDir + "/" + a_dir + "/" + a_filename;
    bstat = FileUtils.FileContentsEquals(l_filename1, l_filename2);
    if (!bstat) {
      LOGGER.log(Level.INFO,
          "Files not equal " + a_dir + m_DirExp_Suffux + "/" + a_filename + " and " + a_dir + "/" + a_filename);
    }
    assertTrue(bstat);
  }
}
