package ing2ofx.gui;

/**
 * Post 21 Scenario generator
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import library.OutputToLoggerReader;
import logger.MyLogger;
import logger.TextAreaHandler;
import net.miginfocom.swing.MigLayout;

public class GUILayout extends JPanel implements ItemListener {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  private static final long serialVersionUID = 1L;

  // * -3 Loglevel: OFF SEVERE WARNING INFO CONFIG FINE FINER FINEST ALL <br>
  static final String[] c_levels = { "OFF", "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER", "FINEST", "ALL" };
  static final String[] c_DelFolderContents = { "Ja", "Nee" };
  static final String[] c_LogToDisk = { "Ja", "Nee" };

  private Level m_Level = Level.INFO;
  private Boolean m_toDisk = false;

  // Variables
  private String m_RootDir = "c:\\";
  private String newline = "\n";
  private File m_GnuCashExecutable = new File("C:\\Program Files (x86)\\gnucash\\bin\\gnucash.exe");
  private String m_OutputFolder;
  private String m_CsvFile;

  private JTextArea output;

  private JLabel lblGNUCashExe = new JLabel("");
  private JTextField txtOutputFilename;

  /**
   * Defineer GUI layout
   * 
   * @param ActionEvent
   */
  public GUILayout() {
    setLayout(new BorderLayout(0, 0));

    JMenuBar menuBar = new JMenuBar();
    add(menuBar, BorderLayout.NORTH);

    // Defineren Setting menu in menubalk:
    JMenu mnSettings = new JMenu("Settings");
    menuBar.add(mnSettings);

    lblGNUCashExe.setText("GnuCash executable: " + m_GnuCashExecutable.getAbsolutePath());
    // Option Location GnuCash exe
    JMenuItem mntmGnuCashExe = new JMenuItem("GnuCash executable");
    mntmGnuCashExe.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser(m_GnuCashExecutable);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileFilter filter = new FileNameExtensionFilter("EXE File", "exe");
        fileChooser.setFileFilter(filter);
        int option = fileChooser.showOpenDialog(GUILayout.this);
        if (option == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          // mntmGnuCashExe.setText("GnuCash executable: " + file.getName());
          LOGGER.log(Level.INFO, "GnuCash executable: " + file.getAbsolutePath());
          lblGNUCashExe.setText("GnuCash executable: " + file.getAbsolutePath());
          m_GnuCashExecutable = file;
        } else {
          mntmGnuCashExe.setText("Command canceled");
        }
      }
    });
    mnSettings.add(mntmGnuCashExe);

    // Optie log level
    JMenuItem mntmLoglevel = new JMenuItem("Loglevel");
    mntmLoglevel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFrame frame = new JFrame("Loglevel");
        String level = "";
        level = (String) JOptionPane.showInputDialog(frame, "Loglevel?", "INFO", JOptionPane.QUESTION_MESSAGE, null,
            c_levels, m_Level.toString());
        m_Level = Level.parse(level.toUpperCase());
        MyLogger.changeLogLevel(m_Level);
      }
    });
    mnSettings.add(mntmLoglevel);

    // Toevoegen Look and Feel
    JMenu menu = new JMenu("Look and Feel");
    mnSettings.add(menu);

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
      menu.add(item);
    }

    // Do the layout.
    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    add(splitPane);

    JPanel topHalf = new JPanel();
    topHalf.setLayout(new BoxLayout(topHalf, BoxLayout.LINE_AXIS));

    topHalf.setMinimumSize(new Dimension(1, 1));
    topHalf.setPreferredSize(new Dimension(1, 1));
    splitPane.add(topHalf);

    JPanel bottomHalf = new JPanel();
    bottomHalf.setLayout(new BoxLayout(bottomHalf, BoxLayout.X_AXIS));

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

    output.setEditable(false);
    output.setTabSize(4);
    JScrollPane outputPane = new JScrollPane(output, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    bottomHalf.add(outputPane);

    JPanel panel = new JPanel();
    outputPane.setColumnHeaderView(panel);
    panel.setLayout(new MigLayout("", "[129px][65px,grow][111px,grow][105px][87px][76px]", "[23px][][][]"));
    outputPane.setColumnHeaderView(panel);
    outputPane.setSize(300, 500);

    panel.setLayout(
        new MigLayout("", "[46px,grow][][grow][205px,grow]", "[23px][23px][23px][23px][23px][23px][][][][]"));

    panel.setMinimumSize(new Dimension(350, 300));
    panel.setPreferredSize(new Dimension(350, 290));

    JCheckBox chckbxConvertDecimalSeparator = new JCheckBox("Convert decimnal separator to dots (.)");
    chckbxConvertDecimalSeparator.setHorizontalAlignment(SwingConstants.LEFT);
    chckbxConvertDecimalSeparator.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean selected = chckbxConvertDecimalSeparator.isSelected();
        LOGGER.log(Level.CONFIG, "Convert decimal to dots:" + Boolean.toString(selected));
      }
    });
    panel.add(chckbxConvertDecimalSeparator, "cell 0 0");

    JCheckBox chckbxAcountSeparateOFX = new JCheckBox("Accounts in seperate OFX files");
    chckbxAcountSeparateOFX.setHorizontalAlignment(SwingConstants.LEFT);
    chckbxAcountSeparateOFX.setSelected(true);
    chckbxAcountSeparateOFX.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean selected = chckbxAcountSeparateOFX.isSelected();
        LOGGER.log(Level.CONFIG, "Accounts in separate OFX files :" + Boolean.toString(selected));
      }
    });
    panel.add(chckbxAcountSeparateOFX, "cell 1 0");

    JCheckBox chckbxConvertDateFormat = new JCheckBox("Convert dates with dd-mm-yyyy to yyyymmdd");
    chckbxConvertDateFormat.setHorizontalAlignment(SwingConstants.LEFT);
    chckbxConvertDateFormat.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean selected = chckbxConvertDateFormat.isSelected();
        LOGGER.log(Level.CONFIG, "Convert dates with dd-mm-yyyy to yyyymmdd :" + Boolean.toString(selected));
      }
    });
    panel.add(chckbxConvertDateFormat, "cell 0 1");

    JCheckBox chckbxSeperatorComma = new JCheckBox("Seperator comma (\",\") Default semicolon (\";\")");
    chckbxSeperatorComma.setHorizontalAlignment(SwingConstants.LEFT);
    chckbxSeperatorComma.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean selected = chckbxSeperatorComma.isSelected();
        LOGGER.log(Level.CONFIG, "Seperator comma (\",\") Default semicolon (\";\") :" + Boolean.toString(selected));
      }
    });
    panel.add(chckbxSeperatorComma, "cell 1 1");

    txtOutputFilename = new JTextField();
    JCheckBox chckbxOutputFileSameAsInput = new JCheckBox("Output filename same as input");
    chckbxOutputFileSameAsInput.setHorizontalAlignment(SwingConstants.RIGHT);
    chckbxOutputFileSameAsInput.setSelected(true);
    chckbxOutputFileSameAsInput.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean selected = chckbxOutputFileSameAsInput.isSelected();
        LOGGER.log(Level.CONFIG, "Output filename same as input : " + Boolean.toString(selected));
        if (selected) {
          txtOutputFilename.setEnabled(false);
        } else {
          txtOutputFilename.setEnabled(true);
        }
      }
    });
    panel.add(chckbxOutputFileSameAsInput, "cell 0 4");

    txtOutputFilename.setHorizontalAlignment(SwingConstants.LEFT);
    txtOutputFilename.setText("Output filename");
    txtOutputFilename.setEnabled(false);

    panel.add(txtOutputFilename, "cell 1 4");
    txtOutputFilename.setColumns(100);

    JLabel lblOutputFolder = new JLabel("");
    lblOutputFolder.setHorizontalAlignment(SwingConstants.LEFT);
    panel.add(lblOutputFolder, "cell 1 5");

    JButton btnOutputFolder = new JButton("Output folder");
    btnOutputFolder.setHorizontalAlignment(SwingConstants.RIGHT);
    btnOutputFolder.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = fileChooser.showOpenDialog(GUILayout.this);
        if (option == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          LOGGER.log(Level.INFO, "Output folder: " + file.getAbsolutePath());
          lblOutputFolder.setText(file.getAbsolutePath());
          m_OutputFolder = file.getAbsolutePath();
        }
      }
    });
    panel.add(btnOutputFolder, "cell 0 5");

    JLabel lblCSVFile = new JLabel("Select a ING CSV file");
    lblCSVFile.setEnabled(false);
    lblCSVFile.setHorizontalAlignment(SwingConstants.RIGHT);
    panel.add(lblCSVFile, "cell 1 3");

    JButton btnConvert = new JButton("Convert to OFX");
    btnConvert.setEnabled(false);

    JButton btnCSVFile = new JButton("CSV File");
    btnCSVFile.setHorizontalAlignment(SwingConstants.RIGHT);
    btnCSVFile.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileFilter filter = new FileNameExtensionFilter("CSV File", "csv");
        fileChooser.setFileFilter(filter);
        int option = fileChooser.showOpenDialog(GUILayout.this);
        if (option == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          LOGGER.log(Level.INFO, "CSV File: " + file.getAbsolutePath());
          lblCSVFile.setText(file.getAbsolutePath());
          m_CsvFile = file.getAbsolutePath();
          if (chckbxOutputFileSameAsInput.isSelected()) {
            String l_filename;
            l_filename = library.FileUtils.getFileNameWithoutExtension(file) + ".ofx";
            txtOutputFilename.setText(l_filename);
            btnConvert.setEnabled(true);
            lblCSVFile.setEnabled(true);
          }
          lblOutputFolder.setText(file.getParent());
        }
      }
    });
    panel.add(btnCSVFile, "cell 0 3");

    btnConvert.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Run OFX Python script
        // @formatter:off
        /*
         * usage: ing2ofx [-h] [-o, --outfile OUTFILE] [-d, --directory DIR] [-c,
         * --convert] [-b, --convert-date] csvfile
         * 
         * This program converts ING (www.ing.nl) CSV files to OFX format. 
         * The default output filename is the input filename.
         * 
         * positional arguments: csvfile A csvfile to process
         * 
         * optional arguments: 
         * -h, --help show this help message and exit 
         * -o, --outfile OUTFILE Output filename 
         * -d, --directory DIR Directory to store output, default is ./ofx 
         * -c, --convert Convert decimal separator to dots (.), default is false 
         * -b, --convert-date Convert dates with dd-mm-yyyy notation to yyyymmdd
         */
        // @formatter:on
        String[] l_options = new String[4];
        l_options[0] = lblCSVFile.getText();
        int idx = 0;

        if (!txtOutputFilename.getText().equalsIgnoreCase("Output filename")) {
          idx++;
          l_options[idx] = "-o " + txtOutputFilename.getText();
        }

        if (!lblOutputFolder.getText().isBlank()) {
          idx++;
          l_options[idx] = "-d " + lblOutputFolder.getText();
        }

        if (chckbxConvertDecimalSeparator.isSelected()) {
          idx++;
          l_options[idx] = "-c";
        }

        if (chckbxConvertDateFormat.isSelected()) {
          idx++;
          l_options[idx] = "-b";
        }

        String l_Script = library.FileUtils.getResourceFileName("scripts/ing2ofx.py");
        if (chckbxAcountSeparateOFX.isSelected()) {
          l_Script = library.FileUtils.getResourceFileName("scripts/ing2ofxPerAccount.py");
        }

        String l_optionsResize = "python";
        l_optionsResize = l_optionsResize + " " + l_Script;
        for (int i = 1; i <= idx + 1; i++) {
          l_optionsResize = l_optionsResize + " " + l_options[i - 1];
        }

        try {
          OutputToLoggerReader l_reader = new OutputToLoggerReader();
          String l_logging = l_reader.getReadOut(l_optionsResize);
          String[] ll_log = l_logging.split("\n");
          System.out.println(ll_log.toString());
          LOGGER.log(Level.INFO, l_logging);
        } catch (IOException | InterruptedException es) {
          es.printStackTrace();
        }
      }
    });
    panel.add(btnConvert, "cell 1 6");

    JLabel lblNewLabel = new JLabel("    ");
    panel.add(lblNewLabel, "cell 0 7");

    panel.add(lblGNUCashExe, "cell 0 8");

    JButton btnGNUCash = new JButton("Start GnuCash");
    btnGNUCash.setHorizontalAlignment(SwingConstants.RIGHT);
    panel.add(btnGNUCash, "cell 1 8");

    bottomHalf.setMinimumSize(new Dimension(500, 100));
    bottomHalf.setPreferredSize(new Dimension(500, 400));
    splitPane.add(bottomHalf);

  }

  @Override
  public void itemStateChanged(ItemEvent e) {
    // TODO Auto-generated method stub

  }

}
