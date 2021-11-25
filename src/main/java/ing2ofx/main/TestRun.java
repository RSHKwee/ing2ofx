package ing2ofx.main;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author james
 */
public class TestRun {
  public TestRun() {
    System.out.println("Constructor");
  }

  public String getReadOut(String a_cmd) throws IOException, InterruptedException {
    Runtime rt = Runtime.getRuntime();

    Process p;
    p = rt.exec(a_cmd);

    BufferedReader processOutput = new BufferedReader(new InputStreamReader(p.getInputStream()), 500000);
    BufferedWriter processInput = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
    ReadThread r = new ReadThread(processOutput);
    Thread th = new Thread(r);
    th.start();
    p.waitFor();
    r.stop();
    String s = r.res;

    p.destroy();
    th.join();
    return s;
  }

  public String getReadOut(String[] params) throws IOException, InterruptedException {
    // Runtime rt = Runtime.getRuntime();

    Process p = Runtime.getRuntime().exec(params);
//    p = rt.exec(params);

    BufferedReader processOutput = new BufferedReader(new InputStreamReader(p.getInputStream()), 500000);
    BufferedWriter processInput = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
    BufferedReader processError = new BufferedReader(new InputStreamReader(p.getErrorStream()), 500000);

    ReadThread r = new ReadThread(processOutput);
    ReadThread e = new ReadThread(processError);
    Thread th = new Thread(r);
    th.start();
    p.waitFor();
    r.stop();
    String s = r.res;

    p.destroy();
    th.join();
    return s;
  }

  public class ReadThread implements Runnable {

    BufferedReader reader;
    char[] buf = new char[100000];
    String res = "";
    boolean stop;

    public ReadThread(BufferedReader reader) {
      this.reader = reader;
      stop = false;
    }

    @Override
    public void run() {
      res = "";

      while (!stop) {
        try {
          reader.read(buf);
          res += new String(buf);

        } catch (IOException ex) {
          Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }

    public void stop() {
      stop = true;
    }
  }

  public static void main(String[] arg) throws IOException, URISyntaxException {
    TestRun tt = new TestRun();

    String[] l_optionsResize = new String[] { "python", "D:\\git\\ing2ofx\\src\\main\\resources\\ing2ofxPerAccount.py",
        "D:\\OneDrive\\Documenten\\ErfenisPapa\\csv&ofx_Bestanden\\NL34INGB0000187782_20211001_20211031.csv", "-d",
        "D:\\OneDrive\\Documenten\\ErfenisPapa\\csv&ofx_Bestanden" };

    String l_optionsCmd = "python D:\\git\\ing2ofx\\src\\main\\resources\\scripts\\ing2ofxPerAccount.py "
        + "D:\\OneDrive\\Documenten\\ErfenisPapa\\csv&ofx_Bestanden\\NL34INGB0000187782_20211001_20211031.csv"
        + " -d D:\\OneDrive\\Documenten\\ErfenisPapa\\csv&ofx_Bestanden";

    String l_optionsCmd2 = "D:\\OneDrive\\Documenten\\ErfenisPapa\\csv&ofx_Bestanden\\NL34INGB0000187782_20211001_20211031.csv"
        + " -d D:\\OneDrive\\Documenten\\ErfenisPapa\\csv&ofx_Bestanden";

    // ClassLoader loader = Thread.currentThread().getContextClassLoader();
    // URL res = loader.getResource("scripts/ing2ofxPerAccount.py");
    // File file = new File(res.toURI());
    // String fileName = file.getPath();

    String fileName = library.FileUtils.getResourceFileName("scripts/ing2ofxPerAccount.py");

    l_optionsCmd2 = "python " + fileName + " " + l_optionsCmd2;

    System.out.println("Options: " + l_optionsResize[0] + " " + l_optionsResize[1]);
    System.out.println("Options2: " + l_optionsCmd2);

    String s = "";
    try {
//      s = tt.getReadOut(l_optionsResize);
      s = tt.getReadOut(l_optionsCmd2);
      System.out.println("command: " + s);
      String[] ll_log = s.split("\n");
      for (int i = 0; i < ll_log.length; i++) {
        System.out.println("i:" + i + " " + ll_log[i]);
      }

      s = tt.getReadOut("cmd dir");
      System.out.println("command: " + s);
      String[] ll_log1 = s.split("\n");
      for (int i = 0; i < ll_log1.length; i++) {
        System.out.println("i:" + i + " " + ll_log1[i]);
      }

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // checking the command i list
    System.out.println("command: " + s);
  }

}