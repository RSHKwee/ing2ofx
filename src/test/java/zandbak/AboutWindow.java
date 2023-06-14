package zandbak;

import javax.swing.*;

public class AboutWindow extends JFrame {
  /**
  * 
  */
  private static final long serialVersionUID = 1096860784404657289L;

  public AboutWindow() {
    super("About");

    // Create a text area with information about your program
    JTextArea textArea = new JTextArea("MyProgram v1.0\n\nCopyright © 2023");

    // Set the text area to be uneditable
    textArea.setEditable(false);

    // Add the text area to the window
    add(textArea);

    // Set the size of the window and make it visible
    setSize(200, 150);
    setVisible(true);
  }

  public static void main(String[] args) {
    new AboutWindow();
  }
}