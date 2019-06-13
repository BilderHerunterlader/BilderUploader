//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2019.06.13 um 10:11:42 PM CEST 
//


package ch.supertomcat.bilderuploader.filenameparser;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für FilenameDatePartParser complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="FilenameDatePartParser">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dateParser" type="{}DateParser" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="variableName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="outputVariableName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FilenameDatePartParser", propOrder = {
    "dateParser"
})
public class FilenameDatePartParser {

    protected List<DateParser> dateParser;
    @XmlAttribute(name = "variableName", required = true)
    protected String variableName;
    @XmlAttribute(name = "outputVariableName")
    protected String outputVariableName;

    /**
     * Gets the value of the dateParser property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dateParser property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDateParser().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DateParser }
     * 
     * 
     */
    public List<DateParser> getDateParser() {
        if (dateParser == null) {
            dateParser = new ArrayList<DateParser>();
        }
        return this.dateParser;
    }

    /**
     * Ruft den Wert der variableName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVariableName() {
        return variableName;
    }

    /**
     * Legt den Wert der variableName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVariableName(String value) {
        this.variableName = value;
    }

    /**
     * Ruft den Wert der outputVariableName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutputVariableName() {
        return outputVariableName;
    }

    /**
     * Legt den Wert der outputVariableName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutputVariableName(String value) {
        this.outputVariableName = value;
    }

}
