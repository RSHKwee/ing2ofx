package ing2ofx;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class HelpWindow {
  public static void main(String[] args) {
    // Replace "path/to/help/file" with the actual path to your help file
    File helpFile = new File("F:\\dev\\ing2ofx\\help\\ing2ofx.chm");

    if (helpFile.exists()) {
      try {
        // Open the help file with the default viewer
        Desktop.getDesktop().open(helpFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("Help file not found.");
    }
  }
}
