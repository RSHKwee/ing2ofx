package ing2ofx.main;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import library.JarInfo;
import ing2ofx.gui.GUILayout;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;

/**
 * Main program for ING csv to OFX convertor
 * 
 * @author kweers1
 */

public class Main {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  static String m_MenuTitel = "ING csv to ofx convertor";
  static public String m_creationtime;
  static String m_LookAndFeel = "Nimbus";
  static UserSetting m_param = new UserSetting();

  /**
   * Create the GUI and show it. For thread safety, this method should be invoked
   * from the event-dispatching thread.
   */
  private static void createAndShowGUI() {
    // Set the look and feel.
    initLookAndFeel();

    // Create and set up the window.
    JFrame frame = new JFrame(m_MenuTitel + " (" + m_creationtime + ")");
    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    frame.addWindowListener(new WindowListener() {
      @Override
      public void windowClosing(WindowEvent e) {
        JFrame frame = (JFrame) e.getSource();

        int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit the application?",
            "Exit Application", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
          m_param.save();
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
      }

      @Override
      public void windowOpened(WindowEvent e) {
      }

      @Override
      public void windowClosed(WindowEvent e) {
      }

      @Override
      public void windowIconified(WindowEvent e) {
      }

      @Override
      public void windowDeiconified(WindowEvent e) {
      }

      @Override
      public void windowActivated(WindowEvent e) {
      }

      @Override
      public void windowDeactivated(WindowEvent e) {
      }
    });
    frame.setVisible(true);

    // Image ING logo;
    try {
      URL iconURL = Main.class.getClassLoader().getResource("ingLogo.png");
      // iconURL is null when not found
      ImageIcon icon = new ImageIcon(iconURL);
      frame.setIconImage(icon.getImage());
    } catch (Exception e) {
      LOGGER.log(Level.FINE, "ING Logo not found.");
    }

    // Create and set up the content pane.
    GUILayout scenGUI = new GUILayout();
    scenGUI.setOpaque(true);
    frame.setContentPane(scenGUI);

    // Display the window.
    frame.validate();
    frame.pack();
    frame.setSize(900, 700);
    frame.setLocation(50, 50);
    frame.setVisible(true);

    LOGGER.log(Level.INFO, " Build time ING Csv to OFX convertor : " + m_creationtime);
  }

  /**
   * Initialiseer Look and Feel
   */
  private static void initLookAndFeel() {
    try {
      for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        LOGGER.fine(" Look And Feel :" + info.getName());
        if (m_LookAndFeel.equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (Exception e) {
      // If Nimbus is not available, you can set the GUI to another look and feel.
    }
  }

  /**
   * Main start de GUI
   *
   * @param args 0 Look and feel, "Nimbus" of "Metal"
   */
  public static void main(String[] argv) {
    m_LookAndFeel = m_param.get_LookAndFeel();
    m_creationtime = JarInfo.getTimeStr(GUILayout.class);

    switch (argv.length) {
      case 1: {
        m_LookAndFeel = argv[1];
        break;
      }

      default: {
      }
    }

    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        createAndShowGUI();
      }
    });
  }
}
