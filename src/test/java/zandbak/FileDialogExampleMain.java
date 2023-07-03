package zandbak;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JFileChooserFixture;

import javax.swing.*;
import java.io.File;

public class FileDialogExampleMain {
  public static void main(String[] args) {
    // Create and show a JFrame containing a JButton that opens a file dialog
    JFrame frame = GuiActionRunner.execute(() -> {
      JFrame f = new JFrame("File Dialog Example");
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      JButton button = new JButton("Open File");
      button.setName("Open File");
      button.addActionListener(e -> {
        JFileChooser fileChooser = new JFileChooser();
        // fileChooser.setName("Kies Files");
        fileChooser.setMultiSelectionEnabled(true);
        int result = fileChooser.showOpenDialog(f);
        if (result == JFileChooser.APPROVE_OPTION) {
          File selectedFile = fileChooser.getSelectedFile();
          File[] selectedFiles = fileChooser.getSelectedFiles();
          // Process the selected file
          System.out.println("Selected file: " + selectedFile.getAbsolutePath());
          int len = selectedFiles.length;
          for (int i = 0; i < len; i++) {
            System.out.println("Selected files: " + selectedFiles[i].getAbsolutePath());
          }
        }
      });

      f.add(button);
      f.pack();
      f.setVisible(true);

      return f;
    });

    // Use AssertJ Swing to interact with the file dialog
    FrameFixture window = new FrameFixture(frame);
    window.button("Open File").click();

    JFileChooserFixture fileChooser = window.fileChooser();
    fileChooser.setCurrentDirectory(new File("f:/"));
    // fileChooser.fileNameTextBox().setText("file.txt"); // Set the desired file
    // name

    File file1 = new File("f:\\Database20220623.kdb");
    File file2 = new File("f:\\Database_20221221.kdb");
    File file3 = new File("f:\\Database20220913.kdb");
    fileChooser.selectFiles(file1, file2, file3);

    fileChooser.approve();

    // Close the JFrame
    window.close();
  }
}
