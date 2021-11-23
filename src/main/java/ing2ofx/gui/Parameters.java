package ing2ofx.gui;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Parameters {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

  private String c_GnuCashExe = "GnuCashExe";
  private String c_GnuCashExeValue = "C:\\Program Files (x86)\\gnucash\\bin\\gnucash.exe";

  private String c_Level = "Level";
  private String c_LevelValue = "INFO";

  private String c_toDisk = "ToDisk";
  private String c_AccountSepOfx = "AccountSepOfx";
  private String c_ConvertDecimalSeparator = "ConvertDecimalSeperator";
  private String c_ConvertDateFormat = "ConvertDateFormat";
  private String c_SeperatorComma = "SeperatorComma";
  private String c_OutputFolder = "OutputFolder";
  private String c_CsvFile = "CsvFile";

  private String m_Level;
  private String m_GnuCashExecutable;
  private String m_OutputFolder;
  private String m_CsvFile;

  private boolean m_toDisk = false;
  private boolean m_AcountSeparateOFX = true;
  private boolean m_ConvertDecimalSeparator = false;
  private boolean m_ConvertDateFormat = false;
  private boolean m_SeperatorComma = false;

  private Preferences pref = Preferences.userRoot();

  public Parameters() {
    m_toDisk = pref.getBoolean(c_toDisk, false);

    m_AcountSeparateOFX = pref.getBoolean(c_AccountSepOfx, true);
    m_ConvertDecimalSeparator = pref.getBoolean(c_ConvertDecimalSeparator, false);
    m_ConvertDateFormat = pref.getBoolean(c_ConvertDateFormat, false);
    m_SeperatorComma = pref.getBoolean(c_SeperatorComma, false);

    m_GnuCashExecutable = pref.get(c_GnuCashExe, c_GnuCashExeValue);
    m_OutputFolder = pref.get(c_OutputFolder, "");
    m_CsvFile = pref.get(c_CsvFile, "");
    m_Level = pref.get(c_Level, c_LevelValue);
  }

  public String get_GnuCashExecutable() {
    return m_GnuCashExecutable;
  }

  public void set_GnuCashExecutable(File a_GnuCashExecutable) {
    pref.put(c_GnuCashExe, a_GnuCashExecutable.getAbsolutePath());
    m_GnuCashExecutable = a_GnuCashExecutable.getAbsolutePath();
    save();
  }

  public String get_OutputFolder() {
    return m_OutputFolder;
  }

  public void set_OutputFolder(File a_OutputFolder) {
    pref.put(c_OutputFolder, a_OutputFolder.getAbsolutePath());
    m_OutputFolder = a_OutputFolder.getAbsolutePath();
    save();
  }

  public String get_CsvFile() {
    return m_CsvFile;
  }

  public void set_CsvFile(File a_CsvFile) {
    pref.put(c_CsvFile, a_CsvFile.getAbsolutePath());
    m_CsvFile = a_CsvFile.getAbsolutePath();
    save();
  }

  public boolean is_toDisk() {
    return m_toDisk;
  }

  public void set_toDisk(boolean a_toDisk) {
    pref.putBoolean(c_toDisk, a_toDisk);
    this.m_toDisk = a_toDisk;
    save();
  }

  public Level get_Level() {
    return Level.parse(m_Level);
  }

  public void set_Level(Level a_Level) {
    pref.put(c_Level, a_Level.toString());
    this.m_Level = a_Level.toString();
    save();
  }

  public boolean is_AcountSeparateOFX() {
    return m_AcountSeparateOFX;
  }

  public void set_AcountSeparateOFX(boolean a_AcountSeparateOFX) {
    pref.putBoolean(c_AccountSepOfx, a_AcountSeparateOFX);
    this.m_AcountSeparateOFX = a_AcountSeparateOFX;
    save();
  }

  public boolean is_ConvertDecimalSeparator() {
    return m_ConvertDecimalSeparator;
  }

  public void set_ConvertDecimalSeparator(boolean a_ConvertDecimalSeparator) {
    pref.putBoolean(c_ConvertDecimalSeparator, a_ConvertDecimalSeparator);
    this.m_ConvertDecimalSeparator = a_ConvertDecimalSeparator;
    save();
  }

  public boolean is_ConvertDateFormat() {
    return m_ConvertDateFormat;
  }

  public void set_ConvertDateFormat(boolean a_ConvertDateFormat) {
    pref.putBoolean(c_ConvertDateFormat, a_ConvertDateFormat);
    this.m_ConvertDateFormat = a_ConvertDateFormat;
    save();
  }

  public boolean is_SeperatorComma() {
    return m_SeperatorComma;
  }

  public void set_SeperatorComma(boolean a_SeperatorComma) {
    pref.putBoolean(c_SeperatorComma, a_SeperatorComma);
    this.m_SeperatorComma = a_SeperatorComma;
    save();
  }

  public void save() {
    try {
      pref.sync();
      pref.flush();
    } catch (BackingStoreException e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
  }
}
