//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.05 um 01:18:08 AM CEST 
//


package ch.supertomcat.bilderuploader.hosterconfig;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für RegexAndVariableStore complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="RegexAndVariableStore">
 *   &lt;complexContent>
 *     &lt;extension base="{}Regex">
 *       &lt;sequence>
 *         &lt;element name="replacement" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="replacementVariable" type="{}RegexReplacementVariableStore" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegexAndVariableStore", propOrder = {
    "replacement",
    "replacementVariable"
})
public class RegexAndVariableStore
    extends Regex
{

    protected String replacement;
    protected List<RegexReplacementVariableStore> replacementVariable;

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

    /**
     * Gets the value of the replacementVariable property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the replacementVariable property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReplacementVariable().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RegexReplacementVariableStore }
     * 
     * 
     */
    public List<RegexReplacementVariableStore> getReplacementVariable() {
        if (replacementVariable == null) {
            replacementVariable = new ArrayList<RegexReplacementVariableStore>();
        }
        return this.replacementVariable;
    }

}
