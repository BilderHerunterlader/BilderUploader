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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fieldName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{}additionalField" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}additionalHeader" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="failureRegex" type="{}Regex" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="filenameRegex" type="{}RegexAndReplacement" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "url",
    "fieldName",
    "additionalField",
    "additionalHeader",
    "failureRegex",
    "filenameRegex"
})
@XmlRootElement(name = "uploadStep")
public class UploadStep {

    @XmlElement(required = true)
    protected String url;
    @XmlElement(required = true)
    protected String fieldName;
    protected List<AdditionalField> additionalField;
    protected List<AdditionalHeader> additionalHeader;
    protected List<Regex> failureRegex;
    protected List<RegexAndReplacement> filenameRegex;

    /**
     * Ruft den Wert der url-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrl() {
        return url;
    }

    /**
     * Legt den Wert der url-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrl(String value) {
        this.url = value;
    }

    /**
     * Ruft den Wert der fieldName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Legt den Wert der fieldName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldName(String value) {
        this.fieldName = value;
    }

    /**
     * Gets the value of the additionalField property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the additionalField property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdditionalField().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AdditionalField }
     * 
     * 
     */
    public List<AdditionalField> getAdditionalField() {
        if (additionalField == null) {
            additionalField = new ArrayList<AdditionalField>();
        }
        return this.additionalField;
    }

    /**
     * Gets the value of the additionalHeader property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the additionalHeader property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdditionalHeader().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AdditionalHeader }
     * 
     * 
     */
    public List<AdditionalHeader> getAdditionalHeader() {
        if (additionalHeader == null) {
            additionalHeader = new ArrayList<AdditionalHeader>();
        }
        return this.additionalHeader;
    }

    /**
     * Gets the value of the failureRegex property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the failureRegex property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFailureRegex().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Regex }
     * 
     * 
     */
    public List<Regex> getFailureRegex() {
        if (failureRegex == null) {
            failureRegex = new ArrayList<Regex>();
        }
        return this.failureRegex;
    }

    /**
     * Gets the value of the filenameRegex property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the filenameRegex property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFilenameRegex().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RegexAndReplacement }
     * 
     * 
     */
    public List<RegexAndReplacement> getFilenameRegex() {
        if (filenameRegex == null) {
            filenameRegex = new ArrayList<RegexAndReplacement>();
        }
        return this.filenameRegex;
    }

}
