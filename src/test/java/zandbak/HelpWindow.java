package zandbak;

import java.awt.Desktop;

import java.io.File;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HelpWindow {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

  public static void main(String[] args) {
    // Replace "path/to/help/file" with the actual path to your help file
    File helpFile = new File("F:\\dev\\ing2ofx\\help\\ing2ofx.chm");

    if (helpFile.exists()) {
      try {
        // Open the help file with the default viewer
        Desktop.getDesktop().open(helpFile);
      } catch (IOException e) {
        LOGGER.log(Level.WARNING, e.getMessage());
      }
    } else {
      System.out.println("Help file not found.");
    }
  }
}
