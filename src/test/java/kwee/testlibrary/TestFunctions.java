package kwee.testlibrary;

import java.io.File;
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

}
