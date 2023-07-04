package kwee.testlibrary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import kwee.ing2ofx.main.UserSetting;

import java.net.URL;

public class TestFunctions {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

  public File GetResourceFile(String a_file) {
    File l_File = null;
    URL resourceUrl;
    try {
      resourceUrl = getClass().getClassLoader().getResource(a_file);
      if (resourceUrl != null) {
        // Get the resource directory path
        String resourceDirectory = resourceUrl.getPath();
        l_File = new File(resourceDirectory);
      } else {
        LOGGER.log(Level.INFO, "File not found: " + a_file);
      }
    } catch (Exception e) {
      LOGGER.log(Level.WARNING, e.getMessage());
    }
    return l_File;
  }

  /**
   * Copy UserSetings
   * 
   * @param a_UserSetting User settings object.
   * @return
   */
  public UserSetting CopyUserSetting(UserSetting a_UserSetting) {
    UserSetting l_UserSetting = new UserSetting();
    l_UserSetting.set_AcountSeparateOFX(a_UserSetting.is_AcountSeparateOFX());
    l_UserSetting.set_ClearTransactions(a_UserSetting.is_ClearTransactions());
    l_UserSetting.set_ConfirmOnExit(a_UserSetting.is_ConfirmOnExit());
    l_UserSetting.set_ConvertDateFormat(a_UserSetting.is_ConvertDateFormat());
    l_UserSetting.set_ConvertDecimalSeparator(a_UserSetting.is_ConvertDecimalSeparator());
    l_UserSetting.set_CsvFiles(a_UserSetting.get_CsvFiles());
    l_UserSetting.set_GnuCashExecutable(new File(a_UserSetting.get_GnuCashExecutable()));
    l_UserSetting.set_Interest(a_UserSetting.is_Interest());
    l_UserSetting.set_Java(a_UserSetting.is_Java());
    l_UserSetting.set_Level(a_UserSetting.get_Level());
    l_UserSetting.set_LogDir(a_UserSetting.get_LogDir());
    l_UserSetting.set_LookAndFeel(a_UserSetting.get_LookAndFeel());
    l_UserSetting.set_OutputFolder(a_UserSetting.get_OutputFolder());
    l_UserSetting.set_Savings(a_UserSetting.is_Savings());
    l_UserSetting.set_SeparatorComma(a_UserSetting.is_SeparatorComma());
    l_UserSetting.set_Synonym_file(new File(a_UserSetting.get_Synonym_file()));
    l_UserSetting.set_toDisk(a_UserSetting.is_toDisk());
    return l_UserSetting;
  }

  public boolean compareXmlFiles(String logFile1Path, String logFile2Path) throws IOException {
    BufferedReader reader1 = new BufferedReader(new FileReader(logFile1Path));
    BufferedReader reader2 = new BufferedReader(new FileReader(logFile2Path));

    String line1 = reader1.readLine();
    String line2 = reader2.readLine();

    while (line1 != null && line2 != null) {
      String content1 = extractLogContent(line1);
      String content2 = extractLogContent(line2);

      if (!content1.equals(content2)) {
        LOGGER.log(Level.INFO, "Not Equal: Line 1: " + content1 + " NOT Line 2: " + content2);
        reader1.close();
        reader2.close();
        return false;
      }
      line1 = reader1.readLine();
      line2 = reader2.readLine();
    }

    boolean areFilesEndReached = line1 == null && line2 == null;
    reader1.close();
    reader2.close();
    return areFilesEndReached;
  }

  private static String extractLogContent(String logLine) {
    String[] lfilter = { "<DTSERVER>", "<DTPROFUP>", "<DTACCTUP>", "<!--", "#" };
    // String[] lfilter = { "<!--", "#" };
    String l_logline = logLine;
    for (String listItem : lfilter) {
      if (logLine.contains(listItem)) {
        l_logline = "<REPLACED>";
      }
    }
    return l_logline; // If no space is found, return the entire line
  }

}
