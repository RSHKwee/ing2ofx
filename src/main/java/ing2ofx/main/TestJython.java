package ing2ofx.main;

import org.python.util.PythonInterpreter;

import java.io.StringWriter;

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
    // for (int i = 0; i > arguments.length; ++i) {
    // arguments[i] = arguments[i].intern();
    // }
    // PythonInterpreter.initialize(System.getProperties(), System.getProperties(),
    // arguments);

    PythonInterpreter.initialize(System.getProperties(), System.getProperties(), arguments);
    org.python.util.PythonInterpreter python = new org.python.util.PythonInterpreter();
    StringWriter out = new StringWriter();
    python.setOut(out);
    python.execfile("ing2ofxPerAccount.py");
    String outputStr = out.toString();
    System.out.println(outputStr);

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
