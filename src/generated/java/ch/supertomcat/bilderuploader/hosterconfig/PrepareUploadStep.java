//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.10 um 11:15:05 AM CEST 
//


package ch.supertomcat.bilderuploader.hosterconfig;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="httpMethod" type="{}HTTPMethod" minOccurs="0"/>
 *         &lt;element name="formContentType" type="{}FormContentType" minOccurs="0"/>
 *         &lt;element ref="{}additionalField" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}additionalHeader" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="failureRegex" type="{}Regex" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="regex" type="{}RegexAndVariableStore" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="cookieVariableStore" type="{}CookieVariableStore" maxOccurs="unbounded" minOccurs="0"/>
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
    "httpMethod",
    "formContentType",
    "additionalField",
    "additionalHeader",
    "failureRegex",
    "regex",
    "cookieVariableStore"
})
@XmlRootElement(name = "prepareUploadStep")
public class PrepareUploadStep {

    @XmlElement(required = true)
    protected String url;
    @XmlSchemaType(name = "string")
    protected HTTPMethod httpMethod;
    @XmlSchemaType(name = "string")
    protected FormContentType formContentType;
    protected List<AdditionalField> additionalField;
    protected List<AdditionalHeader> additionalHeader;
    protected List<Regex> failureRegex;
    protected List<RegexAndVariableStore> regex;
    protected List<CookieVariableStore> cookieVariableStore;

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
     * Ruft den Wert der httpMethod-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link HTTPMethod }
     *     
     */
    public HTTPMethod getHttpMethod() {
        return httpMethod;
    }

    /**
     * Legt den Wert der httpMethod-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link HTTPMethod }
     *     
     */
    public void setHttpMethod(HTTPMethod value) {
        this.httpMethod = value;
    }

    /**
     * Ruft den Wert der formContentType-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link FormContentType }
     *     
     */
    public FormContentType getFormContentType() {
        return formContentType;
    }

    /**
     * Legt den Wert der formContentType-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link FormContentType }
     *     
     */
    public void setFormContentType(FormContentType value) {
        this.formContentType = value;
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
     * Gets the value of the regex property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the regex property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRegex().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RegexAndVariableStore }
     * 
     * 
     */
    public List<RegexAndVariableStore> getRegex() {
        if (regex == null) {
            regex = new ArrayList<RegexAndVariableStore>();
        }
        return this.regex;
    }

    /**
     * Gets the value of the cookieVariableStore property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cookieVariableStore property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCookieVariableStore().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CookieVariableStore }
     * 
     * 
     */
    public List<CookieVariableStore> getCookieVariableStore() {
        if (cookieVariableStore == null) {
            cookieVariableStore = new ArrayList<CookieVariableStore>();
        }
        return this.cookieVariableStore;
    }

}
