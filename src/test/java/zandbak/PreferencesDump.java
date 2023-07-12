package zandbak;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.*;

public class PreferencesDump {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

  public static void main(String[] args) throws BackingStoreException {
    Preferences prefs = Preferences.userRoot(); // or Preferences.systemRoot() for system preferences

    LOGGER.log(Level.INFO, dumpPreferences("", prefs));

    String filePath;
    try {
      filePath = getPreferencesFilePath(prefs);
      System.out.println("Preferences file location: " + filePath);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      // e.printStackTrace();
    }
  }

  private static String dumpPreferences(String nodeName, Preferences prefs) throws BackingStoreException {
    String lstr = "";
    // Retrieve all preference keys
    String[] keys = prefs.keys();

    // Iterate through the keys and retrieve their values
    for (String key : keys) {
      String value = prefs.get(key, null);
      lstr = lstr + nodeName + " | " + key + " = " + value + "\n ";
      // System.out.println(nodeName + "/" + key + " = " + value);
    }

    // Retrieve all child nodes
    String[] childNodes = prefs.childrenNames();

    // Recursively dump the child nodes
    for (String childNode : childNodes) {
      Preferences childPrefs = prefs.node(childNode);
      String childNodeName = nodeName.isEmpty() ? childNode : nodeName + " / " + childNode;
      lstr = lstr + dumpPreferences(childNodeName, childPrefs) + "\n ";
    }
    return lstr;
  }

  private static String getPreferencesFilePath(Preferences prefs) throws Exception {
    Field prefsFileField = prefs.getClass().getDeclaredField("file");
    prefsFileField.setAccessible(true);

    Object prefsFile = prefsFileField.get(prefs);
    if (prefsFile instanceof java.io.File) {
      return ((java.io.File) prefsFile).getAbsolutePath();
    }
    return null;
  }
}