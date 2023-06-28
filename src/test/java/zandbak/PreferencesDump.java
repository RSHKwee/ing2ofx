package zandbak;

import java.lang.reflect.Field;
import java.util.prefs.*;

public class PreferencesDump {
  public static void main(String[] args) throws BackingStoreException {
    Preferences prefs = Preferences.userRoot(); // or Preferences.systemRoot() for system preferences

    dumpPreferences("", prefs);

    String filePath;
    try {
      filePath = getPreferencesFilePath(prefs);
      System.out.println("Preferences file location: " + filePath);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private static void dumpPreferences(String nodeName, Preferences prefs) throws BackingStoreException {
    // Retrieve all preference keys
    String[] keys = prefs.keys();

    // Iterate through the keys and retrieve their values
    for (String key : keys) {
      String value = prefs.get(key, null);
      System.out.println(nodeName + "/" + key + " = " + value);
    }

    // Retrieve all child nodes
    String[] childNodes = prefs.childrenNames();

    // Recursively dump the child nodes
    for (String childNode : childNodes) {
      Preferences childPrefs = prefs.node(childNode);
      String childNodeName = nodeName.isEmpty() ? childNode : nodeName + "/" + childNode;
      dumpPreferences(childNodeName, childPrefs);
    }
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