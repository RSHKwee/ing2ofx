package zandbak;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLComparator {
  public static void main(String[] args) {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();

      // Parse the first XML file
      Document xml1 = builder
          .parse("F:\\dev\\Tools\\ing2ofx\\target\\test-classes\\OFXEnkel\\NL20LPLN0892606304_transactie-historie.ofx");

      // Parse the second XML file
      Document xml2 = builder.parse(
          "F:\\dev\\Tools\\ing2ofx\\target\\test-classes\\OFXEnkelExp\\NL20LPLN0892606304_transactie-historie.ofx");

      // Compare the XML files
      boolean isEqual = compareXML(xml1.getDocumentElement(), xml2.getDocumentElement());

      if (isEqual) {
        System.out.println("XML files are equal.");
      } else {
        System.out.println("XML files are not equal.");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static boolean compareXML(Node node1, Node node2) {
    // Check if the node is an element
    if (node1.getNodeType() == Node.ELEMENT_NODE && node2.getNodeType() == Node.ELEMENT_NODE) {
      Element elem1 = (Element) node1;
      Element elem2 = (Element) node2;

      // Check if the elements have the same tag name
      if (elem1.getTagName().equals(elem2.getTagName())) {
        // Check if the tag is relevant for comparison
        if (!elem1.getTagName().equals("ignoreTag")) {
          // Compare the text content of the elements
          if (!elem1.getTextContent().replace("/\s\s+/g", " ")
              .equals(elem2.getTextContent().replace("/\s\s+/g", " "))) {
            String lstr1 = elem1.getTextContent().replace("/\s\s+/g", " ");
            String lstr2 = elem2.getTextContent().replace("/\s\s+/g", " ");
            return false;
          }
        }

        // Recursively compare child nodes
        NodeList children1 = elem1.getChildNodes();
        NodeList children2 = elem2.getChildNodes();

        if (children1.getLength() != children2.getLength()) {
          return false;
        }

        for (int i = 0; i < children1.getLength(); i++) {
          if (!compareXML(children1.item(i), children2.item(i))) {
            return false;
          }
        }

        return true;
      }
    }

    return false;
  }
}
