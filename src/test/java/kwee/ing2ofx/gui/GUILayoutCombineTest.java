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
import kwee.logger.MyLogger;
import kwee.logger.TestLogger;

public class GUILayoutCombineTest extends TestCase {
  private static final Logger LOGGER = MyLogger.getLogger();
  private FrameFixture frame;
  Object lock = GUILayout.lock;

  private String c_SynonymFile = "Synoniem.txt";
  private String c_IngTransFile = "Alle_rekeningen.csv";
  private String c_IngSavingTransFile = "Alle_spaarrekeningen.csv";
  private String c_IngSNSAlleTrans = "SNS_ING_Alle_rekeningen.csv";

  private String c_SNSTransFile = "transactie-historie.xml";
  private String c_SNSINGAlleTrans = "SNS_ING_transactie-historie.xml";

  private UserSetting m_param = UserSetting.getInstance();
  private TestFunctions m_Functions = new TestFunctions();
  private String m_OutputDir;

  // Expected results in following dirs:
  private String m_DirExp_Suffux = "_Exp";

  // Generated results in following dirs:
  private String m_OfxCombine = "OFX_Combine";
  private String m_OfxCombineSyn = "OFX_Combine_Syn";
  private String m_OfxCombineDouble = "OFX_CombineDouble_Syn";
  private String m_OfxCombineSimple = "OFX_CombineSimple";

  /**
   * setUp, store original User parameters and reset parameters for Test purpose. Start GUI
   */
  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    m_param = UserSetting.getInstance();

    // Parameters
    m_param.freeze();
    m_param.set_ClearTransactions(true);

//    m_param.set_AcountSeparateOFX(true);
    m_param.set_ConvertDecimalSeparator(false);
    m_param.set_ConvertDateFormat(false);
    m_param.set_SeparatorComma(false);
    m_param.set_Java(true);
    m_param.set_Level(Level.INFO);
    m_param.set_Language("nl");

    File ll_file = m_Functions.GetResourceFile(c_SynonymFile);
    m_OutputDir = ll_file.getParent();
    m_param.set_Synonym_file(new File(m_OutputDir + "/" + c_SynonymFile));
    m_param.save();

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

    m_param.unfreeze();
    m_param.save();
    this.frame.cleanUp();
    TestLogger.close();
  }

  /**
   * Test handling of Combined ING-, ING Saving- and SNS Transactions.
   */
  @Test
  public void testGUILayoutCombine() {
    LOGGER.log(Level.INFO, "testGUILayoutCombine");
    FileUtils.checkCreateDirectory(m_OutputDir + "/" + m_OfxCombine);

    frame.button("OutputFolder").click();
    JFileChooserFixture fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir + "/" + m_OfxCombine));
    fileChooser.approve();

    frame.button("CSVXMLFile").click();
    fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir));

    // Select multiple files
    File file1 = new File(m_OutputDir + "/" + c_IngTransFile);
    File file2 = new File(m_OutputDir + "/" + c_IngSavingTransFile);
    File file3 = new File(m_OutputDir + "/" + c_SNSTransFile);
    fileChooser.selectFiles(file1, file2, file3);
    fileChooser.approve();
    frame.button("ReadTransactions").click();

    frame.button("ConvertToOFX").click();

    // Evaluate results:
    synchronized (lock) {
      String logOutput = TestLogger.getOutput();

      assertTrue(logOutput.contains("Gelezen transacties: 157, na verwijdering doublures: 157"));
      assertTrue(logOutput.contains("Eindtotaal van gelezen transacties: 157"));

      AssertXmlFile(m_OfxCombine, "Aap_K222-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombine, "Aap_NL45TRIO0953178943_transactie-historie.ofx");
      AssertXmlFile(m_OfxCombine, "Aap_NL94COBA0678011583_Alle_rekeningen.ofx");
      AssertXmlFile(m_OfxCombine, "Aap_NL94COBA0678011583_K333-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombine, "Aap_NL94COBA0678011583_K444-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombine, "Basis_NL20LPLN0892606304_transactie-historie.ofx");
      AssertXmlFile(m_OfxCombine, "Mies_NL54BKMG0378842587_Alle_rekeningen.ofx");
      AssertXmlFile(m_OfxCombine, "Mies_NL54BKMG0378842587_K111-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombine, "Noot_NL90KNAB0445266309_Alle_rekeningen.ofx");
      AssertXmlFile(m_OfxCombine, "Noot_NL90KNAB0445266309_K555-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombine, "Teun_NL38RABO0192584529_transactie-historie.ofx");
      AssertXmlFile(m_OfxCombine, "Vuur_NL75FVLB0105564737_transactie-historie.ofx");
      AssertFile(m_OfxCombine, "_Saldos_transactie-historie.csv");

      LOGGER.log(Level.INFO, "Ready testGUILayoutCombine");
    }
  }

  /**
   * Test handling of Combined ING-, ING Saving- and SNS Transactions and make use of a Synonym file.
   */
  @Test
  public void testGUILayoutCombineSyn() {
    LOGGER.log(Level.INFO, "testGUILayoutCombineSyn");
    FileUtils.checkCreateDirectory(m_OutputDir + "/" + m_OfxCombineSyn);

    // Define Synonym file
    frame.menuItem("SynonymFile").click();
    JFileChooserFixture fileChoosersyn = frame.fileChooser();
    fileChoosersyn.setCurrentDirectory(new File(m_OutputDir));
    fileChoosersyn.fileNameTextBox().setText(c_SynonymFile);
    fileChoosersyn.approve();

    frame.button("OutputFolder").click();
    JFileChooserFixture fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir + "/" + m_OfxCombineSyn));
    fileChooser.approve();

    frame.button("CSVXMLFile").click();
    fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir));

    // Select multiple files
    File file1 = new File(m_OutputDir + "/" + c_IngTransFile);
    File file2 = new File(m_OutputDir + "/" + c_IngSavingTransFile);
    File file3 = new File(m_OutputDir + "/" + c_SNSTransFile);
    fileChooser.selectFiles(file1, file2, file3);
    fileChooser.approve();
    frame.button("ReadTransactions").click();

    frame.button("ConvertToOFX").click();

    // Evaluate results
    synchronized (lock) {
      String logOutput = TestLogger.getOutput();

      assertTrue(logOutput.contains("Gelezen transacties: 157, na verwijdering doublures: 157"));
      assertTrue(logOutput.contains("Eindtotaal van gelezen transacties: 157"));

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

    JCheckBoxFixture checkBox = frame.checkBox("ClearTransactions");
    checkBox.uncheck();

    frame.button("OutputFolder").click();
    JFileChooserFixture fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir + "/" + m_OfxCombineDouble));
    fileChooser.approve();

    frame.button("CSVXMLFile").click();
    fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir));
    // Select multiple files
    File file1 = new File(m_OutputDir + "/" + c_IngTransFile);
    File file2 = new File(m_OutputDir + "/" + c_IngSavingTransFile);
    File file3 = new File(m_OutputDir + "/" + c_SNSTransFile);
    fileChooser.selectFiles(file1, file2, file3);
    fileChooser.approve();
    frame.button("ReadTransactions").click();

    LOGGER.log(Level.INFO, "Read again (double).....");
    frame.button("CSVXMLFile").click();
    fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir));

    file1 = new File(m_OutputDir + "/" + c_IngTransFile);
    file2 = new File(m_OutputDir + "/" + c_IngSavingTransFile);
    file3 = new File(m_OutputDir + "/" + c_SNSTransFile);
    fileChooser.selectFiles(file1, file2, file3);
    fileChooser.approve();
    frame.button("ReadTransactions").click();

    frame.button("ConvertToOFX").click();

    // Evaluate results:
    synchronized (lock) {
      // Logmessages
      String logOutput = TestLogger.getOutput();
      assertTrue(logOutput.contains("Gelezen transacties: 157, na verwijdering doublures: 157"));
      assertTrue(logOutput.contains("Gelezen transacties: 157, na verwijdering doublures: 0"));
      assertTrue(logOutput.contains("Eindtotaal van gelezen transacties: 157"));

      // Generated files
      AssertXmlFile(m_OfxCombineDouble, "Aap_K222-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombineDouble, "Aap_NL45TRIO0953178943_transactie-historie.ofx");
      AssertXmlFile(m_OfxCombineDouble, "Aap_NL94COBA0678011583_Alle_rekeningen.ofx");
      AssertXmlFile(m_OfxCombineDouble, "Aap_NL94COBA0678011583_K333-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombineDouble, "Aap_NL94COBA0678011583_K444-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombineDouble, "Basis_NL20LPLN0892606304_transactie-historie.ofx");
      AssertXmlFile(m_OfxCombineDouble, "Mies_NL54BKMG0378842587_Alle_rekeningen.ofx");
      AssertXmlFile(m_OfxCombineDouble, "Mies_NL54BKMG0378842587_K111-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombineDouble, "Noot_NL90KNAB0445266309_Alle_rekeningen.ofx");
      AssertXmlFile(m_OfxCombineDouble, "Noot_NL90KNAB0445266309_K555-12345_Alle_spaarrekeningen.ofx");
      AssertXmlFile(m_OfxCombineDouble, "Teun_NL38RABO0192584529_transactie-historie.ofx");
      AssertXmlFile(m_OfxCombineDouble, "Vuur_NL75FVLB0105564737_transactie-historie.ofx");
      AssertFile(m_OfxCombineDouble, "_Saldos_transactie-historie.csv");
      LOGGER.log(Level.INFO, "Ready testGUILayoutCombine");
    }
  }

  @Test
  public void testGUILayoutSimple() {
    LOGGER.log(Level.INFO, "testGUILayoutSimple");
    FileUtils.checkCreateDirectory(m_OutputDir + "/" + m_OfxCombineSimple);

    JCheckBoxFixture checkBox = frame.checkBox("ClearTransactions");
    checkBox.uncheck();

    frame.button("OutputFolder").click();
    JFileChooserFixture fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir + "/" + m_OfxCombineSimple));
    fileChooser.approve();

    frame.button("CSVXMLFile").click();
    fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir));
    // Select multiple files
    File file1 = new File(m_OutputDir + "/" + c_IngSNSAlleTrans);
    File file2 = new File(m_OutputDir + "/" + c_SNSINGAlleTrans);

    fileChooser.selectFiles(file1, file2);
    fileChooser.approve();
    frame.button("ReadTransactions").click();

    frame.button("ConvertToOFX").click();

    // Evaluate results:
    synchronized (lock) {
      // Logmessages
      String logOutput = TestLogger.getOutput();
      assertTrue(logOutput.contains("Gelezen transacties: 4, na verwijdering doublures: 4"));
      assertTrue(logOutput.contains("Eindtotaal van gelezen transacties: 4"));

      // Generated files
      AssertXmlFile(m_OfxCombineSimple, "Aap_NL94COBA0678011583_SNS_ING_Alle_rekeningen.ofx");
      AssertXmlFile(m_OfxCombineSimple, "Basis_NL20LPLN0892606304_SNS_ING_transactie-historie.ofx");
      AssertFile(m_OfxCombineSimple, "_Saldos_SNS_ING_transactie-historie.csv");
      LOGGER.log(Level.INFO, "Ready testGUILayoutSimple");
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
