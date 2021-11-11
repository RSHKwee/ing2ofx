package ing2ofx.gui;

/**
 * ING 2 OFX Convertor GUI
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;
import logger.MyLogger;
import logger.TextAreaHandler;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;

public class GUILayout extends JPanel implements ItemListener {
  /**
   *
   */

  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  private static final long serialVersionUID = 1L;

  // * -3 Loglevel: OFF SEVERE WARNING INFO CONFIG FINE FINER FINEST ALL <br>
  static final String[] c_levels = { "OFF", "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER", "FINEST", "ALL" };
  static final String[] c_DelFolderContents = { "Ja", "Nee" };
  static final String[] c_LogToDisk = { "Ja", "Nee" };

  private Level m_Level = Level.INFO;
  private Boolean m_toDisk = false;
  private Boolean m_DelFolderContents = false;
  private StringBuffer choices;

  // Variables
  private String m_RootDir = "c:\\";
  private String m_SceDirPrefix = "gen";
  private String m_SceDirJenkinsPrefix = "Jenkins";
  private String newline = "\n";

  private JTextArea output;

  @SuppressWarnings("rawtypes")
  private final JCheckBox chckbxConvertDateFormat = new JCheckBox("Convert dates with dd-mm-yyyy to yyyymmdd");
  private JTextField OutputFilenameField;
  private JTextField OPutputFolderField;
  private JTextField CSVFileField;

  /**
   * Defineer GUI layout
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public GUILayout() {

    setLayout(new BorderLayout(0, 0));

    JMenuBar menuBar = new JMenuBar();
    add(menuBar, BorderLayout.NORTH);

    // Defineren Setting menu in menubalk:
    JMenu mnSettings = new JMenu("Settings");
    menuBar.add(mnSettings);

    // Add Look and Feel
    JMenu menuLookFeel = new JMenu("Look and Feel");
    mnSettings.add(menuLookFeel);

    // Get all the available look and feel that we are going to use for
    // creating the JMenuItem and assign the action listener to handle
    // the selection of menu item to change the look and feel.
    UIManager.LookAndFeelInfo[] lookAndFeels = UIManager.getInstalledLookAndFeels();
    for (UIManager.LookAndFeelInfo lookAndFeelInfo : lookAndFeels) {
      JMenuItem item = new JMenuItem(lookAndFeelInfo.getName());
      item.addActionListener(event -> {
        try {
          // Set the look and feel for the frame and update the UI
          // to use a new selected look and feel.
          UIManager.setLookAndFeel(lookAndFeelInfo.getClassName());
          SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
      menuLookFeel.add(item);
    }

    // Option Location GnuCash exe
    JMenuItem mntmGnuCashExe = new JMenuItem("GnuCash executable");
    mntmGnuCashExe.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFrame frame = new JFrame("GnuCash executable");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          mntmGnuCashExe.setText("File Selected: " + file.getName());
        } else {
          mntmGnuCashExe.setText("Command canceled");
        }

        frame.setSize(560, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        m_SceDirPrefix = JOptionPane.showInputDialog(frame, "Scenario prefix?", m_SceDirPrefix);
      }
    });

    mnSettings.add(mntmGnuCashExe);

    JPanel panel = new JPanel();
    add(panel, BorderLayout.CENTER);
    panel.setLayout(new MigLayout("", "[46px][][grow][205px,grow]", "[23px][23px][][][][][][][][][]"));

    JCheckBox chckbxConvertDecimalSeparator = new JCheckBox("Convert decimnal separator to dots (.)");
    chckbxConvertDecimalSeparator.setHorizontalAlignment(SwingConstants.LEFT);
    panel.add(chckbxConvertDecimalSeparator, "cell 0 0,alignx left,aligny top");
    chckbxConvertDateFormat.setHorizontalAlignment(SwingConstants.LEFT);
    panel.add(chckbxConvertDateFormat, "cell 0 1,alignx center,aligny center");

    JCheckBox chckbxOutputFileSameAsInput = new JCheckBox("Output filename same as input");
    chckbxOutputFileSameAsInput.setSelected(true);
    panel.add(chckbxOutputFileSameAsInput, "cell 0 2");

    JLabel lblOutputFileName = new JLabel("Output filename:");
    lblOutputFileName.setHorizontalAlignment(SwingConstants.CENTER);
    panel.add(lblOutputFileName, "cell 0 3,alignx trailing,aligny top");

    OutputFilenameField = new JTextField();
    panel.add(OutputFilenameField, "cell 2 3,growx");
    OutputFilenameField.setColumns(10);

    JCheckBox chckbxAcountSeparateOFX = new JCheckBox("Accoounts in seperate OFX files");
    chckbxAcountSeparateOFX.setSelected(true);
    panel.add(chckbxAcountSeparateOFX, "cell 0 4");

    JLabel lblOutputFolder = new JLabel("Output folder:");
    lblOutputFolder.setHorizontalAlignment(SwingConstants.RIGHT);
    panel.add(lblOutputFolder, "cell 0 5,alignx trailing");

    OPutputFolderField = new JTextField();
    panel.add(OPutputFolderField, "cell 2 5,growx");
    OPutputFolderField.setColumns(10);

    JLabel lblCSVFile = new JLabel("CSV file:");
    lblCSVFile.setHorizontalAlignment(SwingConstants.RIGHT);
    panel.add(lblCSVFile, "cell 0 6,alignx trailing");

    CSVFileField = new JTextField();
    panel.add(CSVFileField, "cell 2 6,growx");
    CSVFileField.setColumns(10);

    JButton btnConvert = new JButton("Convert");
    panel.add(btnConvert, "cell 2 7");

    JButton btnGNUCash = new JButton("GnuCash");
    panel.add(btnGNUCash, "cell 3 7");

    JButton btnExit = new JButton("Exit");
    panel.add(btnExit, "cell 3 9");

    // Build output area.
    try {
      MyLogger.setup(m_Level, m_RootDir, m_toDisk);
    } catch (IOException es) {
      LOGGER.log(Level.SEVERE, Class.class.getName() + ": " + es.toString());
      es.printStackTrace();
    }
    Logger rootLogger = Logger.getLogger("");
    for (Handler handler : rootLogger.getHandlers()) {
      if (handler instanceof TextAreaHandler) {
        TextAreaHandler textAreaHandler = (TextAreaHandler) handler;
        output = textAreaHandler.getTextArea();
      }
    }

    // output = new JTextArea(12, 10);
    output.setEditable(false);
    output.setTabSize(4);

  }

  @Override
  public void itemStateChanged(ItemEvent e) {
    // TODO Auto-generated method stub

  }

}
