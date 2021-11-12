package ing2ofx.gui;

/**
 * ING 2 OFX Convertor GUI
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;
import logger.MyLogger;
import logger.TextAreaHandler;

import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

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

	// Variables
	private String m_RootDir = "c:\\";
	private File m_GnuCashExecutable = new File("C:\\Program Files (x86)\\gnucash\\bin\\gnucash.exe");
	private String newline = "\n";

	private JTextArea output;

	private JTextField OutputFilenameField;
	private JTextField OPutputFolderField;
	private JTextField CSVFileField;
	private JTextField txtOutputFilename;
	private final JTextArea textArea = new JTextArea();
	/**
	 * @wbp.nonvisual location=10,319
	 */
	private final JPanel panel_1 = new JPanel();

	/**
	 * Defineer GUI layout
	 */
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
				JFileChooser fileChooser = new JFileChooser();
				int option = fileChooser.showOpenDialog(GUILayout.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					// mntmGnuCashExe.setText("GnuCash executable: " + file.getName());
					LOGGER.log(Level.INFO, "GnuCash executable: " + file.getName());
					m_GnuCashExecutable = file;
				} else {
					mntmGnuCashExe.setText("Command canceled");
				}
			}
		});
		mnSettings.add(mntmGnuCashExe);

		// Do the layout.
		JScrollPane outputPane = new JScrollPane(output, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// add(outputPane, BorderLayout.CENTER);
		add(outputPane);
		outputPane.setMinimumSize(new Dimension(500, 350));
		outputPane.setPreferredSize(new Dimension(500, 350));
		outputPane.setVisible(true);
		// panel_1.add(outputPane);

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
		output.setVisible(true);

		JPanel panel = new JPanel();
		outputPane.setColumnHeaderView(panel);

//    add(panel, BorderLayout.CENTER);
		panel.setLayout(
				new MigLayout("", "[46px,grow][][grow][205px,grow]", "[23px][23px][23px][23px][23px][23px][][][][]"));

		panel.setMinimumSize(new Dimension(350, 300));
		panel.setPreferredSize(new Dimension(350, 290));

		JCheckBox chckbxConvertDecimalSeparator = new JCheckBox("Convert decimnal separator to dots (.)");
		chckbxConvertDecimalSeparator.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(chckbxConvertDecimalSeparator, "cell 0 0,alignx left,aligny top");

		JCheckBox chckbxAcountSeparateOFX = new JCheckBox("Accoounts in seperate OFX files");
		chckbxAcountSeparateOFX.setHorizontalAlignment(SwingConstants.LEFT);
		chckbxAcountSeparateOFX.setSelected(true);
		panel.add(chckbxAcountSeparateOFX, "cell 1 0");

		JCheckBox chckbxConvertDateFormat = new JCheckBox("Convert dates with dd-mm-yyyy to yyyymmdd");
		chckbxConvertDateFormat.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(chckbxConvertDateFormat, "cell 0 1,alignx left,aligny center");

		JCheckBox chckbxSeperatorComma = new JCheckBox("Seperator comma (\",\")");
		chckbxSeperatorComma.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(chckbxSeperatorComma, "cell 0 2");

		JCheckBox chckbxOutputFileSameAsInput = new JCheckBox("Output filename same as input");
		chckbxOutputFileSameAsInput.setHorizontalAlignment(SwingConstants.RIGHT);
		chckbxOutputFileSameAsInput.setSelected(true);
		panel.add(chckbxOutputFileSameAsInput, "cell 0 3");

		txtOutputFilename = new JTextField();
		txtOutputFilename.setHorizontalAlignment(SwingConstants.LEFT);
		txtOutputFilename.setText("Output filename");
		panel.add(txtOutputFilename, "cell 1 3,growx");
		txtOutputFilename.setColumns(10);

		JButton btnOutputFolder = new JButton("Output folder");
		btnOutputFolder.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(btnOutputFolder, "cell 0 4,alignx right,aligny center");

		JLabel lblOutputFolder = new JLabel("");
		lblOutputFolder.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lblOutputFolder, "cell 1 4,alignx left,aligny center");

		JButton btnCSVFile = new JButton("CSV File");
		btnCSVFile.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(btnCSVFile, "cell 0 5,alignx right");

		JLabel lblCSVFile = new JLabel("");
		lblCSVFile.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblCSVFile, "cell 1 5,alignx left,aligny center");

		JButton btnConvert = new JButton("Convert");
		panel.add(btnConvert, "flowx,cell 1 6,alignx left");

		JButton btnGNUCash = new JButton("GnuCash");
		btnGNUCash.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(btnGNUCash, "cell 1 6,alignx right");

		JButton btnExit = new JButton("Exit");
		btnExit.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(btnExit, "cell 1 7,alignx right");

		// JPanel Outputpanel = new JPanel();
		// add(Outputpanel, BorderLayout.SOUTH);

//    Outputpanel.add(outputPane);
		outputPane.setVisible(true);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub

	}

}
