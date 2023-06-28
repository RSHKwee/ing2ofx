package zandbak;

import java.io.File;
import java.util.prefs.*;

import java.io.FileOutputStream;

public class PreferencesLocation {
  public static void main(String[] args) throws Exception {
    Preferences prefs = Preferences.userRoot(); // or Preferences.systemRoot() for system preferences

    File tempFile = File.createTempFile("prefs", null); // Create a temporary file

    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
      prefs.exportNode(fos); // Export the preferences node to the temporary file
    }

    String filePath = tempFile.getAbsolutePath(); // Get the absolute path of the temporary file

    System.out.println("Preferences file location: " + filePath);

    tempFile.delete(); // Delete the temporary file
  }
}
