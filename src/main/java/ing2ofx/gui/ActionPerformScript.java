package ing2ofx.gui;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import library.OutputToLoggerReader;

public class ActionPerformScript extends SwingWorker<Void, String> implements MyAppendable {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  private JTextArea area = new JTextArea(30, 50);

  private String m_CSVFile;
  private String m_OutputFile;
  private String m_OutputFolder;
  private boolean m_SeparateOFX;
  private boolean m_ConvertDecimalSep;
  private boolean m_ConvertDate;
  private boolean m_SeparatorComma;
  private boolean m_SavingTransactions;
  private boolean m_Interrest;

  public ActionPerformScript(String a_CSVFile, String a_OutputFile, String a_OutputFolder, boolean a_SeparateOFX,
      boolean a_ConvertDecimalSep, boolean a_ConvertDate, boolean a_SeparatorComma, boolean a_SavingTransactions,
      boolean a_Interrest) {

    m_CSVFile = a_CSVFile;
    m_OutputFile = a_OutputFile;
    m_OutputFolder = a_OutputFolder;

    m_SeparateOFX = a_SeparateOFX;
    m_ConvertDecimalSep = a_ConvertDecimalSep;
    m_ConvertDate = a_ConvertDate;
    m_SeparatorComma = a_SeparatorComma;
    m_SavingTransactions = a_SavingTransactions;
    m_Interrest = a_Interrest;
  }

  @Override
  protected Void doInBackground() throws Exception {
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
    l_options[0] = m_CSVFile;
    int idx = 0;
    if (!m_OutputFile.equalsIgnoreCase("Output filename")) {
      idx++;
      l_options[idx] = "-o " + m_OutputFile;
    }
    if (!m_OutputFolder.isBlank()) {
      idx++;
      l_options[idx] = "-d " + m_OutputFolder;
    }
    if (m_ConvertDecimalSep) {
      idx++;
      l_options[idx] = "-c";
    }
    if (m_ConvertDate) {
      idx++;
      l_options[idx] = "-b";
    }
    if (!m_SeparatorComma) {
      idx++;
      l_options[idx] = "-s";
    }

    String l_Script = library.FileUtils.getResourceFileName("scripts/ing2ofx.py");
    if (m_SavingTransactions) {
      // Handling saving transactions
      if (m_Interrest) {
        idx++;
        l_options[idx] = "-i";
      }
      if (m_SeparateOFX) {
        l_Script = library.FileUtils.getResourceFileName("scripts/ing2ofxSpaarPerAccount.py");
      } else {
        l_Script = library.FileUtils.getResourceFileName("scripts/ing2ofxSpaar.py");
      }
    } else {
      // Handling "normal" transactions
      if (m_SeparateOFX) {
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
    return null;
  }

  @Override
  public void append(String text) {
    area.append(text);
  }

  @Override
  protected void done() {
    LOGGER.log(Level.INFO, "");
    LOGGER.log(Level.INFO, "Done.");
  }
}

interface MyAppendable {
  public void append(String text);
}
