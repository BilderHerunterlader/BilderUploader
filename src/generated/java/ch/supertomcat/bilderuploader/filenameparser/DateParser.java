//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2019.06.13 um 10:11:42 PM CEST 
//


package ch.supertomcat.bilderuploader.filenameparser;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für DateParser complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="DateParser">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="datePattern" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dateFormat" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dateLocale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DateParser", propOrder = {
    "datePattern",
    "dateFormat",
    "dateLocale"
})
public class DateParser {

    @XmlElement(required = true)
    protected String datePattern;
    @XmlElement(required = true)
    protected String dateFormat;
    protected String dateLocale;

    /**
     * Ruft den Wert der datePattern-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatePattern() {
        return datePattern;
    }

    /**
     * Legt den Wert der datePattern-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatePattern(String value) {
        this.datePattern = value;
    }

    /**
     * Ruft den Wert der dateFormat-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * Legt den Wert der dateFormat-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateFormat(String value) {
        this.dateFormat = value;
    }

    /**
     * Ruft den Wert der dateLocale-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateLocale() {
        return dateLocale;
    }

    /**
     * Legt den Wert der dateLocale-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateLocale(String value) {
        this.dateLocale = value;
    }

}
