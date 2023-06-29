package zandbak;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LogFileComparator {
  public static void main(String[] args) {
    String logFile1Path = "F:\\dev\\Tools\\ing2ofx\\target\\test-classes\\OFXEnkel\\NL20LPLN0892606304_transactie-historie.ofx";
    String logFile2Path = "F:\\dev\\Tools\\ing2ofx\\target\\test-classes\\OFXEnkelExp\\NL20LPLN0892606304_transactie-historie.ofx";

    try {
      boolean areLogsEqual = compareLogFiles(logFile1Path, logFile2Path);
      if (areLogsEqual) {
        System.out.println("Log files are equal.");
      } else {
        System.out.println("Log files are not equal.");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static boolean compareLogFiles(String logFile1Path, String logFile2Path) throws IOException {
    BufferedReader reader1 = new BufferedReader(new FileReader(logFile1Path));
    BufferedReader reader2 = new BufferedReader(new FileReader(logFile2Path));

    String line1 = reader1.readLine();
    String line2 = reader2.readLine();

    while (line1 != null && line2 != null) {
      String content1 = extractLogContent(line1);
      String content2 = extractLogContent(line2);

      if (!content1.equals(content2)) {
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
    String[] lfilter = { "<DTSERVER>", "<DTPROFUP>", "<DTACCTUP>", "#" };
    String l_logline = logLine;
    for (String listItem : lfilter) {
      if (logLine.contains(listItem)) {
        l_logline = "<REPLACED>";
      }
    }
    return l_logline; // If no space is found, return the entire line
  }
}
