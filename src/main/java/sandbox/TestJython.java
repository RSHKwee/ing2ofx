package sandbox;

import org.python.util.PythonInterpreter;

import library.OutputToLoggerReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.python.core.*;

public class TestJython {

  public static void main(String[] args) throws PyException {
//    PythonInterpreter interp = new PythonInterpreter();
    // System.out.println("Hello, world from Java");

    // interp.set("args.csvfile",
    // "D:/WorkspaceGnuCash/csv/Alle_rekeningen_01-01-2021_28-02-2021-puntkomma.csv");
    // interp.set("args.outfile",
    // "Alle_rekeningen_01-01-2021_28-02-2021-puntkomma.ofx");
    // interp.set("args.dir", "D:/WorkspaceGnuCash/csv");
    // interp.set("args.delimiter", "true");

    String[] arguments = { "ing2ofxPerAccount.py", "-o", "Alle_rekeningen_01-01-2021_28-02-2021-puntkomma.ofx", "-d",
        "D:/WorkspaceGnuCash/csv", "-s",
        "D:/WorkspaceGnuCash/csv/Alle_rekeningen_01-01-2021_28-02-2021-puntkomma.csv" };

    String[] arguments1 = { "ing2ofxPerAccount.py", "-o", "Alle_rekeningen_01-01-2021_28-02-2021.ofx", "-d", "F:/data",
        "-s", "F:/data/Alle_rekeningen_01-01-2021_28-02-2021.csv" };

    String[] arguments2 = {
        "C:\\Users\\rshkw\\.m2\\repository\\org\\python\\jython-standalone\\2.7.2\\jython-standalone-2.7.2.jar",
        "ing2ofxPerAccount.py", "-o", "Alle_rekeningen_01-03-2021_20-10-2021.ofx", "-d", "D:\\WorkspaceGnuCash\\csv",
        "-s", "D:\\WorkspaceGnuCash\\csv\\Alle_rekeningen_01-03-2021_20-10-2021.csv" };

    File scriptfile = library.FileUtils.getResourceAsFile("scripts/ing2ofxPerAccount.py");
    String cmd = "java -jar C:\\Users\\rshkw\\.m2\\repository\\org\\python\\jython-standalone\\2.7.2\\jython-standalone-2.7.2.jar"
        + " " + scriptfile.getAbsolutePath() + " " + "-o" + " " + "Alle_rekeningen_01-03-2021_20-10-2021.ofx" + " "
        + "-d" + " " + "D:\\WorkspaceGnuCash\\csv" + " " + "-s" + " "
        + "D:\\WorkspaceGnuCash\\csv\\Alle_rekeningen_01-03-2021_20-10-2021.csv";
    // for (int i = 0; i > arguments.length; ++i) {
    // arguments[i] = arguments[i].intern();
    // }
    // PythonInterpreter.initialize(System.getProperties(), System.getProperties(),
    // arguments);

    // StartPythonScript pyscr = new StartPythonScript("ing2ofxPerAccount.py",
    // arguments1);

//    StartPythonScript pyscr2 = new StartPythonScript("ing2ofxPerAccount.py", arguments2);

    OutputToLoggerReader l_reader = new OutputToLoggerReader();
    String l_logging;
    try {
      l_logging = l_reader.getReadOut(cmd);
//      l_logging = l_reader.getReadOut(arguments2);
      String[] ll_log = l_logging.split("\n");
      List<String> l_logList = Arrays.asList(ll_log);
      Set<String> l_uniLog = new LinkedHashSet<String>(l_logList);
    } catch (IOException | InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    /*
     * PythonInterpreter.initialize(System.getProperties(), System.getProperties(),
     * arguments); org.python.util.PythonInterpreter python = new
     * org.python.util.PythonInterpreter(); StringWriter out = new StringWriter();
     * python.setOut(out); python.execfile("ing2ofxPerAccount.py"); String outputStr
     * = out.toString(); System.out.println(outputStr);
     */
    // try (PythonInterpreter python = new PythonInterpreter()) {
    // StringWriter out = new StringWriter();
    // python.setOut(out);
    // python.execfile("ing2ofxPerAccount.py");
    // String outputStr = out.toString();
    // System.out.println(outputStr);
    // }

//    interp.execfile("ing2ofxPerAccount.py");
    // D:/WorkspaceGnuCash/csv/Alle_rekeningen_01-01-2021_28-02-2021-puntkomma.csv
    // -o Alle_rekeningen_01-01-2021_28-02-2021-puntkomma.ofx
    // -d D:/WorkspaceGnuCash/csv -s");
    // interp.set("args",
    // "D:/WorkspaceGnuCash/csv/Alle_rekeningen_01-01-2021_28-02-2021-puntkomma.csv
    // -o Alle_rekeningen_01-01-2021_28-02-2021-puntkomma.ofx "
    // + "-d D:/WorkspaceGnuCash/csv -s");

//    interp.execfile("ing2ofxPerAccount.py");
//        + " \"D:/WorkspaceGnuCash/csv/Alle_rekeningen_01-01-2021_28-02-2021-puntkomma.csv\" +"
//        + " \"-o Alle_rekeningen_01-01-2021_28-02-2021-puntkomma.ofx\"" + " \"-d D:/WorkspaceGnuCash/csv\"" + "\"-s\"");

    System.out.println("Goodbye ");
  }
}
