package zandbak;

import java.io.*;

public class LogFileTimestampReplacer {
  public static void main(String[] args) {
    String logFilePath = "F:\\dev\\Tools\\ing2ofx\\target\\test-classes\\Nieuwe map\\NL20LPLN0892606304_transactie-historie.ofx";
    String outputFilePath = "F:\\dev\\Tools\\ing2ofx\\target\\test-classes\\Nieuwe map\\NL20LPLN0892606304_transactie-historie_bewerkt.ofx";
    String constantString = "[REPLACED_TIMESTAMP]";

    try {
      BufferedReader reader = new BufferedReader(new FileReader(logFilePath));
      BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));

      String line;
      while ((line = reader.readLine()) != null) {
        // Find the timestamp portion of the line using a regular expression
        String replacedLine = line.replaceAll("\\[\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\]", constantString);

        writer.write(replacedLine);
        writer.newLine();
      }

      reader.close();
      writer.close();

      System.out.println("Timestamps replaced successfully.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
