package zandbak;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MainFrame extends JFrame {
  /**
   * 
   */
  private static final long serialVersionUID = 2195348893060154192L;

  public MainFrame() {
    super("Main Window");

    // Create a button to open a new window
    JButton button = new JButton("Open New Window");

    // Add an action listener to the button
    button.addActionListener(e -> {
      // Create a new window
      JFrame newFrame = new JFrame("New Window");

      // Add a label to the new window
      JLabel label = new JLabel("This is a new window.");
      newFrame.add(label);

      // Set the size of the new window and make it visible
      newFrame.setSize(200, 150);
      newFrame.setVisible(true);
    });

    // Add the button to the main window
    add(button);

    // Set the size of the main window and make it visible
    setSize(400, 300);
    setVisible(true);
  }

  public static void main(String[] args) {
    new MainFrame();
  }
}
