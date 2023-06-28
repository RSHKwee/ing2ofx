package kwee.ing2ofx.gui;

import static org.junit.Assert.*;

import java.io.File;
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

  private String m_OfxEnkel = "\\OFXEnkelExp\\";
  private String m_OfxCombine = "\\OfxCombineExp\\";
  private String m_OutputDir;

  @Before
  public void setUp() throws Exception {
    // Parameters
    m_OrgParam = m_Functions.CopyUserSetting(m_param);
    m_param.set_ClearTransactions(true);
    m_param.set_Synonym_file(StringToFile(c_SynonymFile));

    m_param.set_AcountSeparateOFX(true);
    m_param.set_ConvertDecimalSeparator(false);
    m_param.set_ConvertDateFormat(false);
    m_param.set_SeparatorComma(false);
    m_param.set_Java(true);

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

    JFrame l_frame = new JFrame();
    GUILayout guilayout = new GUILayout(true);
    l_frame.add(guilayout);
    frame = new FrameFixture(l_frame);
    frame.show();

    frame.button("Read transactions").click();
    frame.button("Convert to OFX").click();

    LOGGER.log(Level.INFO, "Ready testGUILayoutING");
    // TODO fail("Not yet implemented");

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

    JFrame l_frame = new JFrame();
    GUILayout guilayout = new GUILayout(true);
    l_frame.add(guilayout);
    frame = new FrameFixture(l_frame);
    frame.show();

    frame.button("Read transactions").click();
    frame.button("Convert to OFX").click();

    LOGGER.log(Level.INFO, "Ready testGUILayoutINGSaving");
    // TODO fail("Not yet implemented");
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

    LOGGER.log(Level.INFO, "Ready testGUILayoutSNS");
    // TODO fail("Not yet implemented");
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

    LOGGER.log(Level.INFO, "Ready testGUILayoutCombine");
    // TODO fail("Not yet implemented");
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

  private File StringToFile(String a_File) {
    File l_file = new File(a_File);
    return l_file;
  }
}
