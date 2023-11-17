package kwee.ing2ofx.gui;

/**
 * ing2ofx GUI
 */
import kwee.logger.MyLogger;
import kwee.logger.TextAreaHandler;

import net.miginfocom.swing.MigLayout;
import kwee.ofxLibrary.OfxFunctions;
import kwee.ofxLibrary.OfxMetaInfo;
import kwee.ofxLibrary.OfxTransaction;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
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
import javax.swing.JProgressBar;
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

import com.webcohesion.ofx4j.OFXSettings;
import com.webcohesion.ofx4j.generated.CurrencyEnum;

import javax.swing.JCheckBoxMenuItem;

import kwee.ing2ofx.main.Main;

import kwee.ing2ofx.main.UserSetting;
import kwee.library.AboutWindow;
import kwee.library.ApplicationMessages;
import kwee.library.OutputToLoggerReader;
import kwee.library.ShowPreferences;

public class GUILayout extends JPanel implements ItemListener {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  private static final long serialVersionUID = 1L;
  static final String c_CopyrightYear = "2023";
  static final String c_repoName = "Ing2Ofx";
  public static final Object lock = new Object();

  private ApplicationMessages bundle = ApplicationMessages.getInstance();

  // Loglevels: OFF SEVERE WARNING INFO CONFIG FINE FINER FINEST ALL
  static final String[] c_levels = { "OFF", "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER", "FINEST", "ALL" };
  static final String[] c_LogToDisk = { "Yes", "No" };

  // Replace "path/to/help/file" with the actual path to your help file
  static final String m_HelpFile = "ing2ofx.chm";

  // Variables
  private String m_LogDir = "c:/";
  private boolean m_OutputFolderModified = false;
  private JTextArea output;

  private Map<String, OfxMetaInfo> m_metainfo = new HashMap<String, OfxMetaInfo>();
  private List<OfxTransaction> m_OfxTransactions = new LinkedList<OfxTransaction>();

  // Progress Bar
  private JProgressBar m_ProgressBar = new JProgressBar();
  private JLabel lblProgressLabel = new JLabel(" ");;

  // Preferences
  private UserSetting m_param;
  private boolean m_4Help = false;

  private File m_GnuCashExecutable = new File("C:/Program Files (x86)/gnucash/bin/gnucash.exe");
  private File m_Synonym_file;
  private File m_OutputFolder;
  private File[] m_CsvFiles;

  private boolean m_toDisk = false;
  private Level m_Level = Level.INFO;
  private boolean m_AcountSeparateOFX = true;
  private boolean m_Interest = false;
  private boolean m_ClearTransactions = true;
  private String m_Language = "en";
  private int i = 0;
  private JFrame m_Frame;
  private OFXSettings m_ofxSettings;

  /**
   * Define GUI layout
   * 
   */
  public GUILayout(JFrame frame, boolean a_help) {
    m_Frame = frame;
    m_4Help = a_help;
    do_GUILayout();
  }

  public GUILayout(JFrame frame) {
    m_Frame = frame;
    do_GUILayout();
  }

  public void do_GUILayout() {
    m_param = UserSetting.getInstance();
    bundle.changeLanguage(m_param.get_Language());

    JLabel lblCSVFile = new JLabel(bundle.getMessage("SelectInpFile"));
    JButton btnOutputFolder = new JButton(bundle.getMessage("OutputFolder"));
    JButton btnReadTransactions = new JButton(bundle.getMessage("ReadTransactions"));
    JButton btnConvert = new JButton(bundle.getMessage("ConvertToOFX"));

    btnOutputFolder.setName("OutputFolder");
    btnReadTransactions.setName("ReadTransactions");
    btnConvert.setName("ConvertToOFX");

    // GUI items menubar
    JMenuBar menuBar = new JMenuBar();

    JCheckBoxMenuItem chckbxAcountSeparateOFX = new JCheckBoxMenuItem(bundle.getMessage("AccountsInSeparateOFXFiles"));
    chckbxAcountSeparateOFX.setName("AccountsInSeparateOFXFiles");

    JMenu mnGnuCashExe = new JMenu(bundle.getMessage("GnuCashExecutable"));
    JMenu mnSynonym = new JMenu(bundle.getMessage("SynonymFile"));
    JMenuItem mntmLoglevel = new JMenuItem(bundle.getMessage("Loglevel"));
    JMenuItem mntmLanguages = new JMenuItem(bundle.getMessage("Languages"));

    JCheckBox chckbxInterest = new JCheckBox(bundle.getMessage("OnlyInterestTransaction"));
    chckbxInterest.setName("OnlyInterestTransaction");

    JTextField txtOutputFilename = new JTextField();
    JLabel lblOutputFolder = new JLabel("");

    // Initialize parameters
    if (m_4Help) {
      lblCSVFile.setEnabled(true);
      btnReadTransactions.setEnabled(true);
      txtOutputFilename.setEnabled(true);
      btnConvert.setEnabled(true);
    } else {
      lblCSVFile.setEnabled(false);
      btnReadTransactions.setEnabled(false);
      txtOutputFilename.setEnabled(false);
      btnConvert.setEnabled(false);
    }

    m_GnuCashExecutable = new File(m_param.get_GnuCashExecutable());
    m_Synonym_file = new File(m_param.get_Synonym_file());

    if (!m_param.get_OutputFolder().isBlank()) {
      m_OutputFolder = new File(m_param.get_OutputFolder());
      lblOutputFolder.setText(m_OutputFolder.getAbsolutePath());
    }
    if (!(m_param.get_CsvFiles() == null)) {
      m_CsvFiles = m_param.get_CsvFiles();
    }

    m_Language = m_param.get_Language();
    m_Level = m_param.get_Level();
    m_toDisk = m_param.is_toDisk();
    m_AcountSeparateOFX = m_param.is_AcountSeparateOFX();
    m_Interest = m_param.is_Interest();
    m_LogDir = m_param.get_LogDir();
    m_ClearTransactions = m_param.is_ClearTransactions();

    initOFXSettings();
    LOGGER.log(Level.CONFIG, m_param.print());

    // Define Layout
    setLayout(new BorderLayout(0, 0));
    add(menuBar, BorderLayout.NORTH);

    // Define Setting menu in menubalk:
    JMenu mnSettings = new JMenu(bundle.getMessage("Settings"));
    mnSettings.setEnabled(true);
    menuBar.add(mnSettings);

    // Checkbox Separate OFX files
    chckbxAcountSeparateOFX.setHorizontalAlignment(SwingConstants.LEFT);
    chckbxAcountSeparateOFX.setSelected(m_AcountSeparateOFX);
    chckbxAcountSeparateOFX.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean selected = chckbxAcountSeparateOFX.isSelected();
        m_AcountSeparateOFX = selected;
        m_param.set_AcountSeparateOFX(selected);
        LOGGER.log(Level.CONFIG, bundle.getMessage("AccountsInSeparateOFXFilesSelected", Boolean.toString(selected)));
      }
    });
    mnSettings.add(chckbxAcountSeparateOFX);

    // Savings transactions
    JCheckBoxMenuItem chcmnkbxInterest = new JCheckBoxMenuItem(bundle.getMessage("OnlyInterestTransaction"));
    chcmnkbxInterest.setName("OnlyInterestTransaction");
    chcmnkbxInterest.setHorizontalAlignment(SwingConstants.LEFT);
    chcmnkbxInterest.setSelected(m_Interest);
    chcmnkbxInterest.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean selected = chcmnkbxInterest.isSelected();
        m_Interest = selected;
        m_param.set_Interest(m_Interest);
        LOGGER.log(Level.CONFIG, bundle.getMessage("OnlyInterestTransactionSelected", Boolean.toString(selected)));
      }
    });
    mnSettings.add(chcmnkbxInterest);

    // Option Location GnuCash exe
    mnSettings.add(mnGnuCashExe);

    String l_GnuCashExecutable = bundle.getMessage("InstallGnuCashQuestion");
    if (m_GnuCashExecutable.exists()) {
      l_GnuCashExecutable = m_GnuCashExecutable.getAbsolutePath();
    }
    JMenuItem mntmGnuCashExe = new JMenuItem(l_GnuCashExecutable);
    mntmGnuCashExe.setName("GNUCash Executable");
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
          LOGGER.log(Level.INFO, bundle.getMessage("InstalledGnuCash", file.getAbsolutePath()));
          m_GnuCashExecutable = file;
          m_param.set_GnuCashExecutable(m_GnuCashExecutable);
          mntmGnuCashExe.setText(m_GnuCashExecutable.getAbsolutePath());
        }
      }
    });
    mnGnuCashExe.add(mntmGnuCashExe);

    // Option Location Synonym file
    mnSettings.add(mnSynonym);

    String l_Synonyme = bundle.getMessage("SynonymFileQuestion");
    if (m_Synonym_file.exists()) {
      l_Synonyme = m_Synonym_file.getAbsolutePath();
    }
    JMenuItem mntmSynonym = new JMenuItem(l_Synonyme);
    mntmSynonym.setName("SynonymFile");
    mntmSynonym.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser(m_Synonym_file);
        fileChooser.setName("SynonymFile");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setSelectedFile(m_Synonym_file);
        FileFilter filter = new FileNameExtensionFilter("TXT File", "txt");
        fileChooser.setFileFilter(filter);
        int option = fileChooser.showOpenDialog(GUILayout.this);
        if (option == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          LOGGER.log(Level.INFO, bundle.getMessage("SynonymFileSelected", file.getAbsolutePath()));
          m_Synonym_file = file;
          m_param.set_Synonym_file(m_Synonym_file);
          mntmSynonym.setText(m_Synonym_file.getAbsolutePath());
        }
      }
    });
    mnSynonym.add(mntmSynonym);

    // Language setting
    mntmLanguages.setHorizontalAlignment(SwingConstants.LEFT);
    mntmLanguages.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFrame frame = new JFrame(bundle.getMessage("Language"));
        String language = "nl";
        Set<String> l_languages = bundle.getTranslations();
        String[] la_languages = new String[l_languages.size()];
        i = 0;
        l_languages.forEach(lang -> {
          la_languages[i] = lang;
          i++;
        });

        language = (String) JOptionPane.showInputDialog(frame, bundle.getMessage("Language") + "?", "nl",
            JOptionPane.QUESTION_MESSAGE, null, la_languages, m_Language);
        if (language != null) {
          m_Language = language;
          m_param.set_Language(m_Language);
          m_param.save();

          bundle.changeLanguage(language);
          restartGUI();
          setLocale(m_Language);
        }
      }
    });
    mnSettings.add(mntmLanguages);

    // Option log level
    mntmLoglevel.setHorizontalAlignment(SwingConstants.LEFT);
    mntmLoglevel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFrame frame = new JFrame(bundle.getMessage("Loglevel"));
        String level = "";
        level = (String) JOptionPane.showInputDialog(frame, bundle.getMessage("Loglevel") + "?", "INFO",
            JOptionPane.QUESTION_MESSAGE, null, c_levels, m_Level.toString());
        if (level != null) {
          m_Level = Level.parse(level.toUpperCase());
          m_param.set_Level(m_Level);
          MyLogger.changeLogLevel(m_Level);
        }
      }
    });
    mnSettings.add(mntmLoglevel);

    // Add item Look and Feel
    JMenu menu = new JMenu(bundle.getMessage("LookAndFeel"));
    menu.setName("LookAndFeel");
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
          m_param.set_LookAndFeel(lookAndFeelInfo.getClassName());
        } catch (Exception e) {
          LOGGER.log(Level.WARNING, e.getMessage());
        }
      });
      menu.add(item);
    }

    // Option Logging to Disk
    JCheckBoxMenuItem mntmLogToDisk = new JCheckBoxMenuItem(bundle.getMessage("CreateLogfiles"));
    mntmLogToDisk.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean selected = mntmLogToDisk.isSelected();
        if (selected) {
          JFileChooser fileChooser = new JFileChooser();
          fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
          fileChooser.setSelectedFile(new File(m_LogDir));
          int option = fileChooser.showOpenDialog(GUILayout.this);
          if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            LOGGER.log(Level.INFO, bundle.getMessage("LogFolder", file.getAbsolutePath()));
            m_LogDir = file.getAbsolutePath() + "/";
            m_param.set_LogDir(m_LogDir);
            m_param.set_toDisk(true);
            m_toDisk = selected;
          }
        } else {
          m_param.set_toDisk(false);
          m_toDisk = selected;
        }
        try {
          MyLogger.setup(m_Level, m_LogDir, m_toDisk);
        } catch (IOException es) {
          LOGGER.log(Level.SEVERE, Class.class.getName() + ": " + es.toString());
          es.printStackTrace();
        }
      }
    });
    mnSettings.add(mntmLogToDisk);

    // Enable for Help
    JCheckBoxMenuItem chckbxmntm4Help = new JCheckBoxMenuItem(bundle.getMessage("EnableAllFields"));
    chckbxmntm4Help.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean selected = chckbxmntm4Help.isSelected();
        m_4Help = selected;
        if (m_4Help) {
          lblCSVFile.setEnabled(true);
          btnReadTransactions.setEnabled(true);
          txtOutputFilename.setEnabled(true);
          btnConvert.setEnabled(true);
        } else {
          lblCSVFile.setEnabled(false);
          btnReadTransactions.setEnabled(false);
          txtOutputFilename.setEnabled(false);
          btnConvert.setEnabled(false);
        }
        LOGGER.log(Level.INFO, bundle.getMessage("EnableAllFieldsSelected", Boolean.toString(selected)));
      }
    });
    mnSettings.add(chckbxmntm4Help);

    // Option Preferences
    JMenuItem mntmPreferences = new JMenuItem(bundle.getMessage("Preferences"));
    mntmPreferences.setName("Preferences");
    mntmPreferences.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ShowPreferences showpref = new ShowPreferences(UserSetting.NodePrefName);
        showpref.showAllPreferences();
      }
    });
    mnSettings.add(mntmPreferences);

    // ? item
    JMenu mnHelpAbout = new JMenu("?");
    mnHelpAbout.setHorizontalAlignment(SwingConstants.RIGHT);
    menuBar.add(mnHelpAbout);

    // Help
    JMenuItem mntmHelp = new JMenuItem(bundle.getMessage("Help"));
    mntmHelp.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        File helpFile = new File("help\\" + m_Language + "\\" + m_HelpFile);

        if (helpFile.exists()) {
          try {
            // Open the help file with the default viewer
            Desktop.getDesktop().open(helpFile);
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        } else {
          LOGGER.log(Level.INFO, bundle.getMessage("HelpFileNotFound", helpFile.getAbsolutePath()));
        }
      }
    });
    mnHelpAbout.add(mntmHelp);

    // About
    JMenuItem mntmAbout = new JMenuItem(bundle.getMessage("About"));
    mntmAbout.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        AboutWindow l_window = new AboutWindow(c_repoName, Main.m_creationtime, c_CopyrightYear);
        l_window.setVisible(true);
      }
    });
    mnHelpAbout.add(mntmAbout);

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
      MyLogger.setup(m_Level, m_LogDir, m_toDisk);
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
    lblCSVFile.setHorizontalAlignment(SwingConstants.RIGHT);
    panel.add(lblCSVFile, "cell 1 0");

    JCheckBox chckbxClearTransactons = new JCheckBox(bundle.getMessage("ClearTransactions"));
    chckbxClearTransactons.setName("ClearTransactions");

    JButton btnCSVFile = new JButton(bundle.getMessage("CSVXMLFile"));

    chckbxClearTransactons.setName("ClearTransactions");
    btnCSVFile.setName("CSVXMLFile");
    btnCSVFile.setHorizontalAlignment(SwingConstants.RIGHT);
    btnCSVFile.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileFilter filter = new FileNameExtensionFilter("CSVXMLFile", "csv", "xml");
        fileChooser.setFileFilter(filter);

        File[] l_files = null;
        m_param.set_CsvFiles(m_CsvFiles);
        // l_files[0] = m_CsvFile;
        fileChooser.setSelectedFiles(l_files);
        fileChooser.setMultiSelectionEnabled(true);

        fileChooser.setSelectedFile(m_CsvFiles[0]);
        int option = fileChooser.showOpenDialog(GUILayout.this);
        if (option == JFileChooser.APPROVE_OPTION) {
          File[] files = fileChooser.getSelectedFiles();
          m_CsvFiles = files;
          m_param.set_CsvFiles(m_CsvFiles);

          File file = null;
          String l_filename = "";

          for (int i = 0; i < files.length; i++) {
            file = files[i];
            LOGGER.log(Level.INFO, bundle.getMessage("CSVXMLFileProcessed", file.getAbsolutePath()));
            lblCSVFile.setText(file.getAbsolutePath());
            l_filename = l_filename + kwee.library.FileUtils.getFileNameWithoutExtension(file) + ".ofx" + "; ";
          }
          txtOutputFilename.setText(l_filename);
          txtOutputFilename.setEnabled(true);

          btnReadTransactions.setEnabled(true);
          lblCSVFile.setEnabled(true);
          if (!m_OutputFolderModified) {
            lblOutputFolder.setText(file.getParent());
            m_OutputFolder = new File(file.getParent());
            m_param.set_OutputFolder(m_OutputFolder);
          }
        }
      }
    });
    panel.add(btnCSVFile, "cell 0 0");

    // Clear transactions
    chckbxClearTransactons.setSelected(m_ClearTransactions);
    chckbxClearTransactons.setHorizontalAlignment(SwingConstants.LEFT);
    chckbxClearTransactons.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean selected = chckbxClearTransactons.isSelected();
        m_ClearTransactions = selected;
        m_param.set_ClearTransactions(m_ClearTransactions);
        LOGGER.log(Level.CONFIG, bundle.getMessage("ClearTransactionsSelected", Boolean.toString(selected)));
        LOGGER.log(Level.CONFIG, m_param.print());
      }
    });
    panel.add(chckbxClearTransactons, "cell 1 1");

    // Define output folder
    // Output folder & filename
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
          // String temp = fileChooser.getTe
          lblOutputFolder.setText(file.getAbsolutePath());
          m_OutputFolder = new File(file.getAbsolutePath());
          m_param.set_OutputFolder(m_OutputFolder);
          m_OutputFolderModified = true;
        }
      }
    });
    panel.add(btnOutputFolder, "cell 0 3");

    // Read transactions
    btnReadTransactions.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        m_param.save();
        if (m_ClearTransactions) {
          m_OfxTransactions.clear();
          m_metainfo.clear();
        }
        ActionReadTransactions l_action = new ActionReadTransactions(m_Synonym_file, m_CsvFiles, m_ProgressBar,
            lblProgressLabel);
        l_action.execute();
        List<OfxTransaction> l_OfxTransactions = new LinkedList<OfxTransaction>();
        try {
          l_OfxTransactions.addAll(l_action.get());
        } catch (InterruptedException | ExecutionException e1) {
          LOGGER.log(Level.WARNING, e1.getMessage());
        }

        int l_read = l_OfxTransactions.size();
        l_OfxTransactions = OfxFunctions.uniqueOfxTransactions(m_OfxTransactions, l_OfxTransactions);
        int l_unique = l_OfxTransactions.size();

        String ls_read = Integer.toString(l_read);
        String ls_unique = Integer.toString(l_unique);

        LOGGER.log(Level.INFO, bundle.getMessage("TransactionsRead", ls_read, ls_unique));

        m_OfxTransactions.addAll(l_OfxTransactions);
        LOGGER.log(Level.INFO, bundle.getMessage("GrandTotalTransactionsRead", m_OfxTransactions.size()));

        m_metainfo = OfxFunctions.addMetaInfo(m_metainfo, l_action.getOfxMetaInfo(), l_OfxTransactions);
        btnConvert.setEnabled(l_action.TransactionsProcessed());
      }
    });
    panel.add(btnReadTransactions, "cell 1 2");

    // Output
    lblOutputFolder.setHorizontalAlignment(SwingConstants.LEFT);
    panel.add(lblOutputFolder, "cell 1 3");

    txtOutputFilename.setHorizontalAlignment(SwingConstants.LEFT);
    txtOutputFilename.setText(bundle.getMessage("OutputFile"));

    txtOutputFilename.setColumns(100);
    panel.add(txtOutputFilename, "cell 1 4");

    // Convert button
    btnConvert.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        m_param.save();
        btnConvert.setEnabled(false);

        LOGGER.log(Level.FINE, bundle.getMessage("StartConversions"));
        ActionConvertTransactions l_action = new ActionConvertTransactions(m_OfxTransactions, m_metainfo, m_CsvFiles,
            lblOutputFolder.getText(), chckbxAcountSeparateOFX.isSelected(), chckbxInterest.isSelected(), m_ProgressBar,
            lblProgressLabel);
        l_action.execute();
        LOGGER.log(Level.FINE, bundle.getMessage("ConversionsDone"));
      }
    });
    panel.add(btnConvert, "cell 1 5");

    JLabel lblNewLabel = new JLabel("    ");
    panel.add(lblNewLabel, "cell 0 6");

    // Start GnuCash
    JButton btnGNUCash = new JButton(bundle.getMessage("StartGnuCash"));
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
        LOGGER.log(Level.INFO, bundle.getMessage("StopGnuCash"));
      }
    });
    panel.add(btnGNUCash, "cell 1 7");

    // Progress bars
    lblProgressLabel = new JLabel(" ");
    panel.add(lblProgressLabel, "flowx,cell 1 9, alignx right,aligny top");

    m_ProgressBar.setVisible(false);
    panel.add(m_ProgressBar, "cell 1 8, south");

    // Bottom
    bottomHalf.setMinimumSize(new Dimension(500, 100));
    bottomHalf.setPreferredSize(new Dimension(500, 400));
    splitPane.add(bottomHalf);
  }

  @Override
  public void itemStateChanged(ItemEvent e) {
    LOGGER.log(Level.INFO, "itemStateChanged");
  }

  // private functions
  private void restartGUI() {
    m_Frame.dispose(); // Dispose of the current GUI window or frame
    m_Frame = Main.createAndShowGUI(); // Recreate the main GUI window or frame
  }

  private void initOFXSettings() {
    CurrencyEnum l_currency = CurrencyEnum.EUR;
    Charset l_encoding = Charset.forName("ISO-8859-1");
    Locale l_locale = new Locale("nl", "NL"); // Decimal comma
    if (m_Language.equalsIgnoreCase("nl")) {
      l_locale = new Locale("nl", "NL"); // Decimal comma
    } else if (m_Language.equalsIgnoreCase("en")) {
      l_locale = new Locale("en", "US"); // Decimal point
    }

    m_ofxSettings = OFXSettings.getInstance();
    m_ofxSettings.setCurrency(l_currency);
    m_ofxSettings.setEncoding(l_encoding);
    m_ofxSettings.setLocale(l_locale);
    m_ofxSettings.setWriteAttributesOnNewLine(true);
  }

  private void setLocale(String a_lang) {
    Locale l_locale = new Locale("nl", "NL"); // Decimal comma
    if (a_lang.equalsIgnoreCase("nl")) {
      l_locale = new Locale("nl", "NL"); // Decimal comma
    } else if (m_Language.equalsIgnoreCase("en")) {
      l_locale = new Locale("en", "US"); // Decimal point
    }
    m_ofxSettings.setLocale(l_locale);
  }

}
