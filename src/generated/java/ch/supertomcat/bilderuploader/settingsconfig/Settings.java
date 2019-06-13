//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2019.06.13 um 10:26:28 PM CEST 
//


package ch.supertomcat.bilderuploader.settingsconfig;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *         &lt;element name="directorySettings" type="{}DirectorySettings"/>
 *         &lt;element name="connectionSettings" type="{}ConnectionSettings"/>
 *         &lt;element name="uploadSettings" type="{}UploadSettings"/>
 *         &lt;element name="guiSettings" type="{}GUISettings"/>
 *         &lt;element name="backupDbOnStart" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="logLevel" type="{}LogLevelSetting"/>
 *         &lt;element name="hosterSettings" type="{}HosterSettings" maxOccurs="unbounded" minOccurs="0"/>
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
    "directorySettings",
    "connectionSettings",
    "uploadSettings",
    "guiSettings",
    "backupDbOnStart",
    "logLevel",
    "hosterSettings"
})
@XmlRootElement(name = "settings")
public class Settings {

    @XmlElement(required = true)
    protected DirectorySettings directorySettings;
    @XmlElement(required = true)
    protected ConnectionSettings connectionSettings;
    @XmlElement(required = true)
    protected UploadSettings uploadSettings;
    @XmlElement(required = true)
    protected GUISettings guiSettings;
    @XmlElement(defaultValue = "true")
    protected boolean backupDbOnStart;
    @XmlElement(required = true, defaultValue = "INFO")
    @XmlSchemaType(name = "string")
    protected LogLevelSetting logLevel;
    protected List<HosterSettings> hosterSettings;

    /**
     * Ruft den Wert der directorySettings-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DirectorySettings }
     *     
     */
    public DirectorySettings getDirectorySettings() {
        return directorySettings;
    }

    /**
     * Legt den Wert der directorySettings-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DirectorySettings }
     *     
     */
    public void setDirectorySettings(DirectorySettings value) {
        this.directorySettings = value;
    }

    /**
     * Ruft den Wert der connectionSettings-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ConnectionSettings }
     *     
     */
    public ConnectionSettings getConnectionSettings() {
        return connectionSettings;
    }

    /**
     * Legt den Wert der connectionSettings-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ConnectionSettings }
     *     
     */
    public void setConnectionSettings(ConnectionSettings value) {
        this.connectionSettings = value;
    }

    /**
     * Ruft den Wert der uploadSettings-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link UploadSettings }
     *     
     */
    public UploadSettings getUploadSettings() {
        return uploadSettings;
    }

    /**
     * Legt den Wert der uploadSettings-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link UploadSettings }
     *     
     */
    public void setUploadSettings(UploadSettings value) {
        this.uploadSettings = value;
    }

    /**
     * Ruft den Wert der guiSettings-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link GUISettings }
     *     
     */
    public GUISettings getGuiSettings() {
        return guiSettings;
    }

    /**
     * Legt den Wert der guiSettings-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link GUISettings }
     *     
     */
    public void setGuiSettings(GUISettings value) {
        this.guiSettings = value;
    }

    /**
     * Ruft den Wert der backupDbOnStart-Eigenschaft ab.
     * 
     */
    public boolean isBackupDbOnStart() {
        return backupDbOnStart;
    }

    /**
     * Legt den Wert der backupDbOnStart-Eigenschaft fest.
     * 
     */
    public void setBackupDbOnStart(boolean value) {
        this.backupDbOnStart = value;
    }

    /**
     * Ruft den Wert der logLevel-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link LogLevelSetting }
     *     
     */
    public LogLevelSetting getLogLevel() {
        return logLevel;
    }

    /**
     * Legt den Wert der logLevel-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link LogLevelSetting }
     *     
     */
    public void setLogLevel(LogLevelSetting value) {
        this.logLevel = value;
    }

    /**
     * Gets the value of the hosterSettings property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hosterSettings property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHosterSettings().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HosterSettings }
     * 
     * 
     */
    public List<HosterSettings> getHosterSettings() {
        if (hosterSettings == null) {
            hosterSettings = new ArrayList<HosterSettings>();
        }
        return this.hosterSettings;
    }

}
