package convertor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Ing2ofx {

  public Ing2ofx() {

  }

  public void ReadCsvFile(File a_CsvFile) {
    FileReader fr;
    try {
      fr = new FileReader(a_CsvFile);
      BufferedReader br = new BufferedReader(fr);
      String header = br.readLine();

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
}
