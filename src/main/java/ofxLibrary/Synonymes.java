package ofxLibrary;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Synonymes {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  private Map<String, Synonyme> m_Synonymes = new HashMap<String, Synonyme>();

  public Synonymes() {
  }

  public Synonymes(File a_SynonymesFile) {
    String l_SynonymesFile = a_SynonymesFile.getPath();
    LOGGER.log(Level.FINE, "File read:" + l_SynonymesFile);

    // read file into stream, try-with-resources
    try (Stream<String> stream = Files.lines(Paths.get(l_SynonymesFile))) {
      stream.forEach(l_line -> {
        String[] l_elems = l_line.split(";");
        if (l_elems.length >= 3) {
          int lseq = -1;
          try {
            lseq = Integer.parseInt(l_elems[0].strip());
          } catch (NumberFormatException ex) {
            // ex.printStackTrace();
          }

          String l_key = l_elems[1].strip();
          String l_syn = l_elems[2].strip();
          Synonyme l_synonyme = new Synonyme(lseq, l_key, l_syn);
          m_Synonymes.put(l_key, l_synonyme);
        }
      });
    } catch (IOException e) {
      // LOGGER.log(Level.SEVERE, Class.class.getName() + ": " + e.getMessage());
    }
  }

  public String GetSynonyme(String a_key) {
    String l_Syn = "";
    if (m_Synonymes.containsKey(a_key)) {
      Synonyme l_Synonyme = m_Synonymes.get(a_key);
      l_Syn = l_Synonyme.getSyn();
    }
    return l_Syn;
  }

  public int GetSequence(String a_key) {
    int l_Seq = -1;
    if (m_Synonymes.containsKey(a_key)) {
      Synonyme l_Synonyme = m_Synonymes.get(a_key);
      l_Seq = l_Synonyme.getSequence();
    }
    return l_Seq;
  }

}
