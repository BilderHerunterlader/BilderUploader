//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.05 um 01:18:08 AM CEST 
//


package ch.supertomcat.bilderuploader.hosterconfig;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für RegexAndReplacement complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="RegexAndReplacement">
 *   &lt;complexContent>
 *     &lt;extension base="{}Regex">
 *       &lt;sequence>
 *         &lt;element name="replacement" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegexAndReplacement", propOrder = {
    "replacement"
})
public class RegexAndReplacement
    extends Regex
{

    @XmlElement(required = true)
    protected String replacement;

    /**
     * Ruft den Wert der replacement-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReplacement() {
        return replacement;
    }

    /**
     * Legt den Wert der replacement-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReplacement(String value) {
        this.replacement = value;
    }

}
