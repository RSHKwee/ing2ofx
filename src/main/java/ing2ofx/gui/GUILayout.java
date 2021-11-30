package ing2ofx.gui;

/**
 * ing2ofx GUI
 */
import logger.MyLogger;
import logger.TextAreaHandler;

import net.miginfocom.swing.MigLayout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.io.File;
import java.io.IOException;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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
import javax.swing.JCheckBoxMenuItem;

import library.UserSetting;
import library.OutputToLoggerReader;

public class GUILayout extends JPanel implements ItemListener {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  private static final long serialVersionUID = 1L;

  // * -3 Loglevel: OFF SEVERE WARNING INFO CONFIG FINE FINER FINEST ALL <br>
  static final String[] c_levels = { "OFF", "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER", "FINEST", "ALL" };
  static final String[] c_LogToDisk = { "Yes", "No" };

  // Variables
  private String m_RootDir = "c:\\";
  // private String newline = "\n";

  // Preferences
  private File m_GnuCashExecutable = new File("C:\\Program Files (x86)\\gnucash\\bin\\gnucash.exe");
  private File m_OutputFolder;
  private File m_CsvFile;

  private boolean m_toDisk = false;
  private Level m_Level = Level.INFO;
  private boolean m_AcountSeparateOFX = true;
  private boolean m_ConvertDecimalSeparator = false;
  private boolean m_ConvertDateFormat = false;
  private boolean m_SeparatorComma = false;
  private boolean m_Interest = true;
  private boolean m_Savings = false;

  // GUI items
  private JTextArea output;
  private JTextField txtOutputFilename = new JTextField();
  private JLabel lblOutputFolder = new JLabel("");
  private JButton btnConvert = new JButton("Convert to OFX");

  /**
   * Define GUI layout
   * 
   */
  public GUILayout() {
    // Initialize
    UserSetting l_param = new UserSetting();
    m_GnuCashExecutable = new File(l_param.get_GnuCashExecutable());

    if (!l_param.get_OutputFolder().isBlank()) {
      m_OutputFolder = new File(l_param.get_OutputFolder());
      lblOutputFolder.setText(m_OutputFolder.getAbsolutePath());
    }
    if (!l_param.get_CsvFile().isBlank()) {
      m_CsvFile = new File(l_param.get_CsvFile());
    }

    m_Level = l_param.get_Level();
    m_toDisk = l_param.is_toDisk();
    m_AcountSeparateOFX = l_param.is_AcountSeparateOFX();
    m_ConvertDecimalSeparator = l_param.is_ConvertDecimalSeparator();
    m_ConvertDateFormat = l_param.is_ConvertDateFormat();
    m_SeparatorComma = l_param.is_SeparatorComma();
    m_Interest = l_param.is_Interest();
    m_Savings = l_param.is_Savings();

    // Define Layout
    setLayout(new BorderLayout(0, 0));
    JMenuBar menuBar = new JMenuBar();
    add(menuBar, BorderLayout.NORTH);

    // Define Setting menu in menubalk:
    JMenu mnSettings = new JMenu("Settings");
    menuBar.add(mnSettings);

    // Checkbox Separate OFX files
    JCheckBoxMenuItem chckbxAcountSeparateOFX = new JCheckBoxMenuItem("Accounts in separate OFX files");
    chckbxAcountSeparateOFX.setHorizontalAlignment(SwingConstants.LEFT);
    chckbxAcountSeparateOFX.setSelected(m_AcountSeparateOFX);
    chckbxAcountSeparateOFX.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean selected = chckbxAcountSeparateOFX.isSelected();
        m_AcountSeparateOFX = selected;
        l_param.set_AcountSeparateOFX(selected);
        LOGGER.log(Level.CONFIG, "Accounts in separate OFX files :" + Boolean.toString(selected));
      }
    });
    mnSettings.add(chckbxAcountSeparateOFX);

    // Checkbox decimal separator
    JCheckBoxMenuItem chckbxConvertDecimalSeparator = new JCheckBoxMenuItem("Convert decimnal separator to dots (.)");
    chckbxConvertDecimalSeparator.setHorizontalAlignment(SwingConstants.LEFT);
    chckbxConvertDecimalSeparator.setSelected(m_ConvertDecimalSeparator);
    chckbxConvertDecimalSeparator.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean selected = chckbxConvertDecimalSeparator.isSelected();
        m_ConvertDecimalSeparator = selected;
        l_param.set_ConvertDecimalSeparator(selected);
        LOGGER.log(Level.CONFIG, "Convert decimal to dots:" + Boolean.toString(selected));
      }
    });
    mnSettings.add(chckbxConvertDecimalSeparator);

    // Checkbox convert date
    JCheckBoxMenuItem chckbxConvertDateFormat = new JCheckBoxMenuItem("Convert dates with dd-mm-yyyy to yyyymmdd");
    chckbxConvertDateFormat.setHorizontalAlignment(SwingConstants.LEFT);
    chckbxConvertDateFormat.setSelected(m_ConvertDateFormat);
    chckbxConvertDateFormat.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean selected = chckbxConvertDateFormat.isSelected();
        m_ConvertDateFormat = selected;
        l_param.set_ConvertDateFormat(selected);
        LOGGER.log(Level.CONFIG, "Convert dates with dd-mm-yyyy to yyyymmdd :" + Boolean.toString(selected));
      }
    });
    mnSettings.add(chckbxConvertDateFormat);

    // Option Location GnuCash exe
    JMenu mnGnuCashExe = new JMenu("GnuCash executable");
    mnSettings.add(mnGnuCashExe);

    String l_GnuCashExecutable = "Install GnuCash?";
    if (m_GnuCashExecutable.exists()) {
      l_GnuCashExecutable = m_GnuCashExecutable.getAbsolutePath();
    }
    JMenuItem mntmGnuCashExe = new JMenuItem(l_GnuCashExecutable);
    mntmGnuCashExe.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser(m_GnuCashExecutable);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setSelectedFile(m_GnuCashExecutable);
        FileFilter filter = new FileNameExtensionFilter("EXE File", "exe");
        fileChooser.setFileFilter(filter);
        int option = fileChooser.showOpenDialog(GUILayout.this);
        if (option == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          LOGGER.log(Level.INFO, "GnuCash executable: " + file.getAbsolutePath());
          m_GnuCashExecutable = file;
          l_param.set_GnuCashExecutable(m_GnuCashExecutable);
          mntmGnuCashExe.setText(m_GnuCashExecutable.getAbsolutePath());
        }
      }
    });
    mnGnuCashExe.add(mntmGnuCashExe);

    // Option log level
    JMenuItem mntmLoglevel = new JMenuItem("Loglevel");
    mntmLoglevel.setHorizontalAlignment(SwingConstants.LEFT);
    mntmLoglevel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFrame frame = new JFrame("Loglevel");
        String level = "";
        level = (String) JOptionPane.showInputDialog(frame, "Loglevel?", "INFO", JOptionPane.QUESTION_MESSAGE, null,
            c_levels, m_Level.toString());
        if (level != null) {
          m_Level = Level.parse(level.toUpperCase());
          l_param.set_Level(m_Level);
          MyLogger.changeLogLevel(m_Level);
        }
      }
    });
    mnSettings.add(mntmLoglevel);

    // Add item Look and Feel
    JMenu menu = new JMenu("Look and Feel");
    menu.setHorizontalAlignment(SwingConstants.LEFT);
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
          l_param.set_LookAndFeel(lookAndFeelInfo.getClassName());
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
      menu.add(item);
    }

    // Option Logging to Disk
    JMenuItem mntmLogToDisk = new JMenuItem("Create logfiles");
    mntmLogToDisk.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFrame frame = new JFrame("Create Logfiles");
        String ToDisk = "";
        ToDisk = (String) JOptionPane.showInputDialog(frame, "Create logfiles?", "No", JOptionPane.QUESTION_MESSAGE,
            null, c_LogToDisk, m_toDisk);
        if (ToDisk == "Yes") {
          m_toDisk = true;
        } else {
          m_toDisk = false;
        }
        l_param.set_toDisk(m_toDisk);
      }
    });
    mnSettings.add(mntmLogToDisk);

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
    panel.setLayout(new MigLayout("", "[129px][65px,grow][111px,grow][105px]", "[23px][][][][][][][][][][]"));
    outputPane.setColumnHeaderView(panel);
    outputPane.setSize(300, 500);

    panel.setLayout(
        new MigLayout("", "[46px,grow][][grow][205px,grow]", "[23px][23px][23px][23px][23px][23px][][][][]"));

    panel.setMinimumSize(new Dimension(350, 300));
    panel.setPreferredSize(new Dimension(350, 290));

    // Choose CSV File
    JLabel lblCSVFile = new JLabel("Select a ING CSV file");
    lblCSVFile.setEnabled(false);
    lblCSVFile.setHorizontalAlignment(SwingConstants.RIGHT);
    panel.add(lblCSVFile, "cell 1 0");

    JButton btnCSVFile = new JButton("CSV File");
    btnCSVFile.setHorizontalAlignment(SwingConstants.RIGHT);
    btnCSVFile.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileFilter filter = new FileNameExtensionFilter("CSV File", "csv");
        fileChooser.setFileFilter(filter);
        fileChooser.setSelectedFile(m_CsvFile);
        int option = fileChooser.showOpenDialog(GUILayout.this);
        if (option == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          LOGGER.log(Level.INFO, "CSV File: " + file.getAbsolutePath());
          lblCSVFile.setText(file.getAbsolutePath());
          m_CsvFile = file;
          l_param.set_CsvFile(file);

          String l_filename;
          l_filename = library.FileUtils.getFileNameWithoutExtension(file) + ".ofx";
          txtOutputFilename.setText(l_filename);
          txtOutputFilename.setEnabled(true);

          btnConvert.setEnabled(true);
          lblCSVFile.setEnabled(true);
          lblOutputFolder.setText(file.getParent());
          m_OutputFolder = new File(file.getParent());
          l_param.set_OutputFolder(m_OutputFolder);
        }
      }
    });
    panel.add(btnCSVFile, "cell 0 0");

    // Define output folder
    JButton btnOutputFolder = new JButton("Output folder");
    btnOutputFolder.setHorizontalAlignment(SwingConstants.RIGHT);
    btnOutputFolder.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setSelectedFile(new File(lblOutputFolder.getText()));
        int option = fileChooser.showOpenDialog(GUILayout.this);
        if (option == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          LOGGER.log(Level.INFO, "Output folder: " + file.getAbsolutePath());
          lblOutputFolder.setText(file.getAbsolutePath());
          m_OutputFolder = new File(file.getAbsolutePath());
          l_param.set_OutputFolder(m_OutputFolder);
        }
      }
    });

    // Savings transactions
    JCheckBox chckbxInterest = new JCheckBox("Only interest transaction");
    chckbxInterest.setSelected(m_Interest);
    chckbxInterest.setHorizontalAlignment(SwingConstants.RIGHT);
    chckbxInterest.setEnabled(m_Savings);
    chckbxInterest.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean selected = chckbxInterest.isSelected();
        m_Interest = selected;
        l_param.set_Interest(m_Interest);
        LOGGER.log(Level.CONFIG, "Save only interest :" + Boolean.toString(selected));
      }
    });

    JCheckBox chckbxSavings = new JCheckBox("Savings transactions");
    chckbxSavings.setSelected(m_Savings);
    chckbxSavings.setHorizontalAlignment(SwingConstants.LEFT);
    chckbxSavings.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean selected = chckbxSavings.isSelected();
        m_Savings = selected;
        if (selected) {
          chckbxInterest.setEnabled(true);
        } else {
          chckbxInterest.setEnabled(false);
        }
        l_param.set_Savings(selected);
        LOGGER.log(Level.CONFIG, "Savings transaction :" + Boolean.toString(selected));
      }
    });
    panel.add(chckbxSavings, "cell 1 1");
    panel.add(chckbxInterest, "cell 1 1");

    // Separation
    JCheckBox chckbxSeparatorComma = new JCheckBox("Seperator comma (\",\") Default semicolon (\";\")");
    chckbxSeparatorComma.setHorizontalAlignment(SwingConstants.LEFT);
    chckbxSeparatorComma.setSelected(m_SeparatorComma);
    chckbxSeparatorComma.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean selected = chckbxSeparatorComma.isSelected();
        m_SeparatorComma = selected;
        l_param.set_SeparatorComma(selected);
        LOGGER.log(Level.CONFIG, "Seperator comma (\",\") Default semicolon (\";\") :" + Boolean.toString(selected));
      }
    });
    panel.add(chckbxSeparatorComma, "cell 1 2");

    panel.add(btnOutputFolder, "cell 0 4");
    lblOutputFolder.setHorizontalAlignment(SwingConstants.LEFT);
    panel.add(lblOutputFolder, "cell 1 4");

    txtOutputFilename.setHorizontalAlignment(SwingConstants.LEFT);
    txtOutputFilename.setText("Output filename");
    txtOutputFilename.setEnabled(false);
    txtOutputFilename.setColumns(100);
    panel.add(txtOutputFilename, "cell 1 5");

    btnConvert.setEnabled(false);
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
         * -s, --separator Separator semicolon is default (true) otherwise comma (false)", action='store_true')
         * -i, --interest Only interest savings transactions (true) otherwise all savings transactions default (false)
         */
        // @formatter:on
        String[] l_options = new String[8];
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
        if (!chckbxSeparatorComma.isSelected()) {
          idx++;
          l_options[idx] = "-s";
        }
        if (chckbxInterest.isSelected()) {
          idx++;
          l_options[idx] = "-i";
        }
        String l_Script = library.FileUtils.getResourceFileName("scripts/ing2ofx.py");
        if (chckbxSavings.isSelected()) {
          // Handling saving transactions
          if (chckbxAcountSeparateOFX.isSelected()) {
            l_Script = library.FileUtils.getResourceFileName("scripts/ing2ofxSpaarPerAccount.py");
          } else {
            l_Script = library.FileUtils.getResourceFileName("scripts/ing2ofxSpaar.py");
          }
        } else {
          // Handling "normal" transactions
          if (chckbxAcountSeparateOFX.isSelected()) {
            l_Script = library.FileUtils.getResourceFileName("scripts/ing2ofxPerAccount.py");
          } else {
            l_Script = library.FileUtils.getResourceFileName("scripts/ing2ofx.py");
          }
        }
        String l_optionsResize = "python";
        try {
          l_optionsResize = library.FileUtils.getResourceFileName("python.exe");
        } catch (Exception ep) {
          // Do nothing
        }
        l_optionsResize = l_optionsResize + " " + l_Script;
        for (int i = 1; i <= idx + 1; i++) {
          l_optionsResize = l_optionsResize + " " + l_options[i - 1];
        }
        LOGGER.log(Level.INFO, "Start: " + l_optionsResize);
        try {
          OutputToLoggerReader l_reader = new OutputToLoggerReader();
          String l_logging = l_reader.getReadOut(l_optionsResize);
          String[] ll_log = l_logging.split("\n");
          List<String> l_logList = Arrays.asList(ll_log);
          Set<String> l_uniLog = new LinkedHashSet<String>(l_logList);
          LOGGER.log(Level.INFO, " ");
          System.out.println(ll_log.toString());
          l_uniLog.forEach(ll -> {
            LOGGER.log(Level.INFO, " " + ll);
          });
        } catch (IOException | InterruptedException es) {
          LOGGER.log(Level.INFO, es.getMessage());
          es.printStackTrace();
        }
        LOGGER.log(Level.INFO, "Script ended.");
      }
    });
    panel.add(btnConvert, "cell 1 6");

    JLabel lblNewLabel = new JLabel("    ");
    panel.add(lblNewLabel, "cell 0 7");

    // Start GnuCash
    JButton btnGNUCash = new JButton("Start GnuCash");
    btnGNUCash.setHorizontalAlignment(SwingConstants.RIGHT);
    btnGNUCash.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        LOGGER.log(Level.INFO, "Start GNUCash: " + m_GnuCashExecutable.getAbsolutePath());
        OutputToLoggerReader l_reader = new OutputToLoggerReader();
        try {
          String l_logging = l_reader.getReadOut(m_GnuCashExecutable.getAbsolutePath());
          LOGGER.log(Level.INFO, l_logging);
        } catch (IOException | InterruptedException e1) {
          e1.printStackTrace();
        }
        LOGGER.log(Level.INFO, "Stop GNUCash");
      }
    });
    panel.add(btnGNUCash, "cell 1 8");

    bottomHalf.setMinimumSize(new Dimension(500, 100));
    bottomHalf.setPreferredSize(new Dimension(500, 400));
    splitPane.add(bottomHalf);
  }

  @Override
  public void itemStateChanged(ItemEvent e) {
    LOGGER.log(Level.INFO, "itemStateChanged");
  }

}
