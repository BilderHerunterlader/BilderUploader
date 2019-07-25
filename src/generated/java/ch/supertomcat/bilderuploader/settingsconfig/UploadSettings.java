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
 * <p>Java-Klasse für UploadSettings complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="UploadSettings">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="uploadedBytes" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="uploadedFiles" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="saveLogs" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="maxFailedCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="autoStartUploads" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="autoRetryAfterUploadsComplete" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UploadSettings", propOrder = {
    "uploadedBytes",
    "uploadedFiles",
    "saveLogs",
    "maxFailedCount",
    "autoStartUploads",
    "autoRetryAfterUploadsComplete"
})
public class UploadSettings {

    @XmlElement(defaultValue = "0")
    protected long uploadedBytes;
    @XmlElement(defaultValue = "0")
    protected long uploadedFiles;
    @XmlElement(defaultValue = "true")
    protected boolean saveLogs;
    @XmlElement(defaultValue = "2")
    protected int maxFailedCount;
    @XmlElement(defaultValue = "false")
    protected boolean autoStartUploads;
    @XmlElement(defaultValue = "false")
    protected boolean autoRetryAfterUploadsComplete;

    /**
     * Ruft den Wert der uploadedBytes-Eigenschaft ab.
     * 
     */
    public long getUploadedBytes() {
        return uploadedBytes;
    }

    /**
     * Legt den Wert der uploadedBytes-Eigenschaft fest.
     * 
     */
    public void setUploadedBytes(long value) {
        this.uploadedBytes = value;
    }

    /**
     * Ruft den Wert der uploadedFiles-Eigenschaft ab.
     * 
     */
    public long getUploadedFiles() {
        return uploadedFiles;
    }

    /**
     * Legt den Wert der uploadedFiles-Eigenschaft fest.
     * 
     */
    public void setUploadedFiles(long value) {
        this.uploadedFiles = value;
    }

    /**
     * Ruft den Wert der saveLogs-Eigenschaft ab.
     * 
     */
    public boolean isSaveLogs() {
        return saveLogs;
    }

    /**
     * Legt den Wert der saveLogs-Eigenschaft fest.
     * 
     */
    public void setSaveLogs(boolean value) {
        this.saveLogs = value;
    }

    /**
     * Ruft den Wert der maxFailedCount-Eigenschaft ab.
     * 
     */
    public int getMaxFailedCount() {
        return maxFailedCount;
    }

    /**
     * Legt den Wert der maxFailedCount-Eigenschaft fest.
     * 
     */
    public void setMaxFailedCount(int value) {
        this.maxFailedCount = value;
    }

    /**
     * Ruft den Wert der autoStartUploads-Eigenschaft ab.
     * 
     */
    public boolean isAutoStartUploads() {
        return autoStartUploads;
    }

    /**
     * Legt den Wert der autoStartUploads-Eigenschaft fest.
     * 
     */
    public void setAutoStartUploads(boolean value) {
        this.autoStartUploads = value;
    }

    /**
     * Ruft den Wert der autoRetryAfterUploadsComplete-Eigenschaft ab.
     * 
     */
    public boolean isAutoRetryAfterUploadsComplete() {
        return autoRetryAfterUploadsComplete;
    }

    /**
     * Legt den Wert der autoRetryAfterUploadsComplete-Eigenschaft fest.
     * 
     */
    public void setAutoRetryAfterUploadsComplete(boolean value) {
        this.autoRetryAfterUploadsComplete = value;
    }

}
