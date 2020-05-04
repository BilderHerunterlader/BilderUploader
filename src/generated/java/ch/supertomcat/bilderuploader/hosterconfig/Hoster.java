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
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element name="displayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="displayIcon" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="homePage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="maxFileSize" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="fileNameFilter" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="maxConnections" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="delay" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element ref="{}generateRandom" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}prepareUploadStep" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}uploadStep"/>
 *         &lt;element ref="{}uploadResultStep"/>
 *         &lt;element name="passwordVariable" type="{}PasswordVariable" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "displayName",
    "displayIcon",
    "homePage",
    "maxFileSize",
    "fileNameFilter",
    "maxConnections",
    "delay",
    "generateRandom",
    "prepareUploadStep",
    "uploadStep",
    "uploadResultStep",
    "passwordVariable"
})
@XmlRootElement(name = "hoster")
public class Hoster {

    protected String displayName;
    protected String displayIcon;
    protected String homePage;
    protected Long maxFileSize;
    protected String fileNameFilter;
    protected Integer maxConnections;
    protected Integer delay;
    protected List<GenerateRandom> generateRandom;
    protected List<PrepareUploadStep> prepareUploadStep;
    @XmlElement(required = true)
    protected UploadStep uploadStep;
    @XmlElement(required = true)
    protected UploadResultStep uploadResultStep;
    protected List<PasswordVariable> passwordVariable;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "version", required = true)
    protected String version;

    /**
     * Ruft den Wert der displayName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Legt den Wert der displayName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayName(String value) {
        this.displayName = value;
    }

    /**
     * Ruft den Wert der displayIcon-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayIcon() {
        return displayIcon;
    }

    /**
     * Legt den Wert der displayIcon-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayIcon(String value) {
        this.displayIcon = value;
    }

    /**
     * Ruft den Wert der homePage-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHomePage() {
        return homePage;
    }

    /**
     * Legt den Wert der homePage-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHomePage(String value) {
        this.homePage = value;
    }

    /**
     * Ruft den Wert der maxFileSize-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getMaxFileSize() {
        return maxFileSize;
    }

    /**
     * Legt den Wert der maxFileSize-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setMaxFileSize(Long value) {
        this.maxFileSize = value;
    }

    /**
     * Ruft den Wert der fileNameFilter-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileNameFilter() {
        return fileNameFilter;
    }

    /**
     * Legt den Wert der fileNameFilter-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileNameFilter(String value) {
        this.fileNameFilter = value;
    }

    /**
     * Ruft den Wert der maxConnections-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxConnections() {
        return maxConnections;
    }

    /**
     * Legt den Wert der maxConnections-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxConnections(Integer value) {
        this.maxConnections = value;
    }

    /**
     * Ruft den Wert der delay-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDelay() {
        return delay;
    }

    /**
     * Legt den Wert der delay-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDelay(Integer value) {
        this.delay = value;
    }

    /**
     * Gets the value of the generateRandom property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the generateRandom property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGenerateRandom().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GenerateRandom }
     * 
     * 
     */
    public List<GenerateRandom> getGenerateRandom() {
        if (generateRandom == null) {
            generateRandom = new ArrayList<GenerateRandom>();
        }
        return this.generateRandom;
    }

    /**
     * Gets the value of the prepareUploadStep property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the prepareUploadStep property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPrepareUploadStep().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PrepareUploadStep }
     * 
     * 
     */
    public List<PrepareUploadStep> getPrepareUploadStep() {
        if (prepareUploadStep == null) {
            prepareUploadStep = new ArrayList<PrepareUploadStep>();
        }
        return this.prepareUploadStep;
    }

    /**
     * Ruft den Wert der uploadStep-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link UploadStep }
     *     
     */
    public UploadStep getUploadStep() {
        return uploadStep;
    }

    /**
     * Legt den Wert der uploadStep-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link UploadStep }
     *     
     */
    public void setUploadStep(UploadStep value) {
        this.uploadStep = value;
    }

    /**
     * Ruft den Wert der uploadResultStep-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link UploadResultStep }
     *     
     */
    public UploadResultStep getUploadResultStep() {
        return uploadResultStep;
    }

    /**
     * Legt den Wert der uploadResultStep-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link UploadResultStep }
     *     
     */
    public void setUploadResultStep(UploadResultStep value) {
        this.uploadResultStep = value;
    }

    /**
     * Gets the value of the passwordVariable property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the passwordVariable property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPasswordVariable().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PasswordVariable }
     * 
     * 
     */
    public List<PasswordVariable> getPasswordVariable() {
        if (passwordVariable == null) {
            passwordVariable = new ArrayList<PasswordVariable>();
        }
        return this.passwordVariable;
    }

    /**
     * Ruft den Wert der name-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Legt den Wert der name-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Ruft den Wert der version-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Legt den Wert der version-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

}
