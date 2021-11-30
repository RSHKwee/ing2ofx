package sandbox;

import java.io.StringWriter;

import org.python.util.PythonInterpreter;

public class StartPythonScript {

  public StartPythonScript(String a_Script, String[] arguments) {
    PythonInterpreter.initialize(System.getProperties(), System.getProperties(), arguments);
    org.python.util.PythonInterpreter python = new org.python.util.PythonInterpreter();
    StringWriter out = new StringWriter();
    python.setOut(out);
    python.execfile(a_Script);
    String outputStr = out.toString();
    System.out.println(outputStr);
  }
}
