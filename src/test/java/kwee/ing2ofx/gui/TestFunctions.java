package kwee.ing2ofx.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import kwee.logger.MyLogger;

import java.net.URL;

public class TestFunctions {
  private static final Logger LOGGER = MyLogger.getLogger();

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

  public boolean compareXmlFiles(String logFile1Path, String logFile2Path) throws IOException {
    BufferedReader reader1 = new BufferedReader(new FileReader(logFile1Path));
    BufferedReader reader2 = new BufferedReader(new FileReader(logFile2Path));

    String line1 = reader1.readLine();
    String line2 = reader2.readLine();

    while (line1 != null && line2 != null) {
      String content1 = extractLogContent(line1);
      String content2 = extractLogContent(line2);

      if (!content1.equals(content2)) {
        LOGGER.log(Level.FINE, "Not Equal: Line 1: " + content1 + " NOT Line 2: " + content2);
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
