//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2019.07.25 um 10:53:56 PM CEST 
//


package ch.supertomcat.bilderuploader.settingsconfig;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für DirectorySettings complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="DirectorySettings">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lastUsedPath" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="lastUsedImportPath" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="lastUsedExportPath" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="generatorOutputSaveDirectory" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="lastUsedGeneratorOutputSaveDirectory" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DirectorySettings", propOrder = {
    "lastUsedPath",
    "lastUsedImportPath",
    "lastUsedExportPath",
    "generatorOutputSaveDirectory",
    "lastUsedGeneratorOutputSaveDirectory"
})
public class DirectorySettings {

    @XmlElement(required = true, nillable = true)
    protected String lastUsedPath;
    @XmlElement(required = true, nillable = true)
    protected String lastUsedImportPath;
    @XmlElement(required = true, nillable = true)
    protected String lastUsedExportPath;
    @XmlElement(required = true, nillable = true)
    protected String generatorOutputSaveDirectory;
    @XmlElement(required = true, nillable = true)
    protected String lastUsedGeneratorOutputSaveDirectory;

    /**
     * Ruft den Wert der lastUsedPath-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastUsedPath() {
        return lastUsedPath;
    }

    /**
     * Legt den Wert der lastUsedPath-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastUsedPath(String value) {
        this.lastUsedPath = value;
    }

    /**
     * Ruft den Wert der lastUsedImportPath-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastUsedImportPath() {
        return lastUsedImportPath;
    }

    /**
     * Legt den Wert der lastUsedImportPath-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastUsedImportPath(String value) {
        this.lastUsedImportPath = value;
    }

    /**
     * Ruft den Wert der lastUsedExportPath-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastUsedExportPath() {
        return lastUsedExportPath;
    }

    /**
     * Legt den Wert der lastUsedExportPath-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastUsedExportPath(String value) {
        this.lastUsedExportPath = value;
    }

    /**
     * Ruft den Wert der generatorOutputSaveDirectory-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGeneratorOutputSaveDirectory() {
        return generatorOutputSaveDirectory;
    }

    /**
     * Legt den Wert der generatorOutputSaveDirectory-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGeneratorOutputSaveDirectory(String value) {
        this.generatorOutputSaveDirectory = value;
    }

    /**
     * Ruft den Wert der lastUsedGeneratorOutputSaveDirectory-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastUsedGeneratorOutputSaveDirectory() {
        return lastUsedGeneratorOutputSaveDirectory;
    }

    /**
     * Legt den Wert der lastUsedGeneratorOutputSaveDirectory-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastUsedGeneratorOutputSaveDirectory(String value) {
        this.lastUsedGeneratorOutputSaveDirectory = value;
    }

}
