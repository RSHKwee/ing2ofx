//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.08 at 10:37:47 AM CEST 
//

package kwee.camt053parser.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for Document complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="Document">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BkToCstmrStmt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}BankToCustomerStatementV02"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Document", propOrder = { "bkToCstmrStmt" })
public class Document {

  @XmlElement(name = "BkToCstmrStmt", required = true)
  protected BankToCustomerStatementV02 bkToCstmrStmt;

  /**
   * Gets the value of the bkToCstmrStmt property.
   * 
   * @return possible object is {@link BankToCustomerStatementV02 }
   * 
   */
  public BankToCustomerStatementV02 getBkToCstmrStmt() {
    return bkToCstmrStmt;
  }

  /**
   * Sets the value of the bkToCstmrStmt property.
   * 
   * @param value allowed object is {@link BankToCustomerStatementV02 }
   * 
   */
  public void setBkToCstmrStmt(BankToCustomerStatementV02 value) {
    this.bkToCstmrStmt = value;
  }

}