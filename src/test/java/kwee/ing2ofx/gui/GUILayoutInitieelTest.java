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

import javax.swing.JFrame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import junit.framework.TestCase;

import kwee.ing2ofx.main.Main;
import kwee.ing2ofx.main.UserSetting;
import kwee.library.FileUtils;
import kwee.logger.TestLogger;

public class GUILayoutInitieelTest extends TestCase {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  private FrameFixture frame;
  Object lock = GUILayout.lock;

  private String c_SynonymFile = "Synoniem.txt";
  private String c_IngTransFile = "Alle_rekeningen.csv";

  // private String c_IngTransEngFile = "Alle_rekeningen_eng.csv";
  // private String c_IngSavingEngTransFile = "Alle_spaarrekeningen_eng.csv";

  private UserSetting m_param = UserSetting.getInstance();
  private TestFunctions m_Functions = new TestFunctions();
  private String m_OutputDir = "";

  // Expected results in following dirs:
  private String m_DirExp_Suffux = "_Exp";

  // Generated results in following dirs:
  private String m_OfxEnkelING = "OFX_ING_Init";

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
    m_param.remove_Synonym_file();

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
   * Test handling of ING Transactions, csv to OFX.
   */
  @Test
  public void testGUILayoutING() {
    LOGGER.log(Level.INFO, "testGUILayoutING");
    FileUtils.checkCreateDirectory(m_OutputDir + "/" + m_OfxEnkelING);

    frame.button("CSVXMLFile").click();
    JFileChooserFixture fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir));
    fileChooser.fileNameTextBox().setText(c_IngTransFile); // Set the desired file name
    fileChooser.approve();
    frame.button("ReadTransactions").click();

    frame.button("OutputFolder").click();
    fileChooser = frame.fileChooser();
    fileChooser.setCurrentDirectory(new File(m_OutputDir + "/" + m_OfxEnkelING + "/"));
    fileChooser.approve();

    frame.button("ConvertToOFX").click();

    // Evaluate results:
    synchronized (lock) {
      String logOutput = TestLogger.getOutput();

      assertTrue(logOutput.contains("Gelezen transacties: 124, na verwijdering doublures: 124"));
      assertTrue(logOutput.contains("Eindtotaal van gelezen transacties: 124"));

      AssertXmlFile(m_OfxEnkelING, "NL54BKMG0378842587_Alle_rekeningen.ofx");
      AssertXmlFile(m_OfxEnkelING, "NL90KNAB0445266309_Alle_rekeningen.ofx");
      AssertXmlFile(m_OfxEnkelING, "NL94COBA0678011583_Alle_rekeningen.ofx");
      AssertFile(m_OfxEnkelING, "_Saldos_Alle_rekeningen.csv");

      LOGGER.log(Level.INFO, "Ready testGUILayoutING");
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
