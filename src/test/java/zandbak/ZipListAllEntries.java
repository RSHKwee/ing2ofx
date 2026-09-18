package zandbak;

import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipListAllEntries {
  public static void main(String[] args) {
    String zipFilePath = "D:\\Users\\René\\SynologyDrive\\Documenten\\Administraties\\csv\\transactie-historie__20260915163258.zip";

    try (ZipFile zipFile = new ZipFile(zipFilePath)) {
      Enumeration<? extends ZipEntry> entries = zipFile.entries();

      while (entries.hasMoreElements()) {
        ZipEntry entry = entries.nextElement();
        System.out.println("Entry: " + entry.getName() + " (isDirectory: " + entry.isDirectory() + ")");

        // Optioneel: alleen bestanden lezen (geen mappen)
        if (!entry.isDirectory()) {
          try (InputStream is = zipFile.getInputStream(entry)) {
            String content = new String(is.readAllBytes());
            System.out.println("  -> Inhoud: " + content);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}