package zandbak;

import java.util.prefs.Preferences;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.io.*;

public class PreferencesCopyExample {
  public static void main(String[] args) {
    // Create the original Preferences instance
    Preferences originalPrefs = Preferences.userNodeForPackage(PreferencesCopyExample.class);
    originalPrefs.put("key1", "value1");
    originalPrefs.put("key2", "value2");

    // Create a new Preferences instance
    Preferences copiedPrefs = Preferences.userNodeForPackage(PreferencesCopyExample.class).node("copiedNode");

    try {
      // Export the original preferences
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      originalPrefs.exportNode(baos);

      // Import the preferences into the copied instance
      Preferences.importPreferences(new ByteArrayInputStream(baos.toByteArray()));
    } catch (IOException | BackingStoreException e) {
      e.printStackTrace();
    } catch (InvalidPreferencesFormatException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // Test the copied preferences
    System.out.println("Copied preference key1: " + copiedPrefs.get("key1", "default1"));
    System.out.println("Copied preference key2: " + copiedPrefs.get("key2", "default2"));
  }
}
