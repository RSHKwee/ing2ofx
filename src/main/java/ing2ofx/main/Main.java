package ing2ofx.main;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import library.JarInfo;
import ing2ofx.gui.GUILayout;
import javafx.scene.image.Image;

/**
 * Dit package bevat het hoofdprogramma (main) voor het starten van de GUI van
 * de Werkbank Post21 Scenario generator.
 *
 * @author kweers1
 */

public class Main {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  static String m_MenuTitel = "ING csv to ofx convertor";
  static public String m_creationtime;

  // "Metal", "Nimbus", "CDE/Motif", "Windows", "Windows Classic"
  static String m_LookAndFeel = "Nimbus";

  /**
   * Create the GUI and show it. For thread safety, this method should be invoked
   * from the event-dispatching thread.
   */
  private static void createAndShowGUI() {
    // Set the look and feel.
    initLookAndFeel();

    // Create and set up the window.
    // Image ing_logo = new Image("inglogo.png");
    JFrame frame = new JFrame(m_MenuTitel + " (" + m_creationtime + ")");
    // frame.setIconImage(ing_logo);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Create and set up the content pane.
    GUILayout scenGUI = new GUILayout();
    // GUILayout scenGUI = new GUILayout(m_Configbestand);
    scenGUI.setOpaque(true);
    frame.setContentPane(scenGUI);

    // Display the window.
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
   * @param args 0 Configuratie bestand of posten directory
   * @param args 1 Look and feel, "Nimbus" of "Metal"
   */
  public static void main(String[] argv) {
    m_creationtime = JarInfo.getTimeStr(GUILayout.class);

    switch (argv.length) {
      case 1: {
        // m_Configbestand = argv[0];
        break;
      }
      case 2: {
        // m_LookAndFeel = argv[1];
        break;
      }

      default: {
        // m_Configbestand =
        // "J:\\CARE\\BS\\test\\Regressietest\\ScePosten_Regressie.txt";
      }
        ;
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
