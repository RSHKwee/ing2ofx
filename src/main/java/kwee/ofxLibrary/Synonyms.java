package kwee.ofxLibrary;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Synonymes
 * 
 * @author René
 *
 */
public class Synonyms {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  private Map<String, Synonym> m_Synonyms = new HashMap<String, Synonym>();

  /**
   * 
   */
  public Synonyms() {
  }

  /**
   * 
   * @param a_SynonymsFile
   */
  public Synonyms(File a_SynonymsFile) {
    String l_SynonymsFile = a_SynonymsFile.getPath();
    LOGGER.log(Level.FINE, "File read:" + l_SynonymsFile);

    // read file into stream, try-with-resources
    try (Stream<String> stream = Files.lines(Paths.get(l_SynonymsFile))) {
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
          Synonym l_synonym = new Synonym(lseq, l_key, l_syn);
          m_Synonyms.put(l_key, l_synonym);
        }
      });
    } catch (IOException e) {
      // LOGGER.log(Level.SEVERE, Class.class.getName() + ": " + e.getMessage());
    }
  }

  /**
   * 
   * @param a_key
   * @return Synonym
   */
  public String getSynonym(String a_key) {
    String l_Syn = "";
    if (m_Synonyms.containsKey(a_key)) {
      Synonym l_Synonym = m_Synonyms.get(a_key);
      l_Syn = l_Synonym.getSyn();
    }
    return l_Syn;
  }

  /**
   * Get sequence for Account (key)
   * 
   * @param a_key Account
   * @return Sequencenr. account
   */
  public int getSequence(String a_key) {
    int l_Seq = -1;
    if (m_Synonyms.containsKey(a_key)) {
      Synonym l_Synonym = m_Synonyms.get(a_key);
      l_Seq = l_Synonym.getSequence();
    }
    return l_Seq;
  }

}
