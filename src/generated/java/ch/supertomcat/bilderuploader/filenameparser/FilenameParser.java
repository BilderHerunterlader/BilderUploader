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
 *         &lt;element name="filenameSplitter" type="{}FilenameSplitter"/>
 *         &lt;element name="filenamePartParser" type="{}FilenamePartParser" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="filenameDatePartParser" type="{}FilenameDatePartParser" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="titleFormatter" type="{}TitleFormatter" minOccurs="0"/>
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
    "filenameSplitter",
    "filenamePartParser",
    "filenameDatePartParser",
    "titleFormatter"
})
@XmlRootElement(name = "filenameParser")
public class FilenameParser {

    @XmlElement(required = true)
    protected FilenameSplitter filenameSplitter;
    protected List<FilenamePartParser> filenamePartParser;
    protected List<FilenameDatePartParser> filenameDatePartParser;
    protected TitleFormatter titleFormatter;

    /**
     * Ruft den Wert der filenameSplitter-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link FilenameSplitter }
     *     
     */
    public FilenameSplitter getFilenameSplitter() {
        return filenameSplitter;
    }

    /**
     * Legt den Wert der filenameSplitter-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link FilenameSplitter }
     *     
     */
    public void setFilenameSplitter(FilenameSplitter value) {
        this.filenameSplitter = value;
    }

    /**
     * Gets the value of the filenamePartParser property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the filenamePartParser property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFilenamePartParser().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FilenamePartParser }
     * 
     * 
     */
    public List<FilenamePartParser> getFilenamePartParser() {
        if (filenamePartParser == null) {
            filenamePartParser = new ArrayList<FilenamePartParser>();
        }
        return this.filenamePartParser;
    }

    /**
     * Gets the value of the filenameDatePartParser property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the filenameDatePartParser property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFilenameDatePartParser().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FilenameDatePartParser }
     * 
     * 
     */
    public List<FilenameDatePartParser> getFilenameDatePartParser() {
        if (filenameDatePartParser == null) {
            filenameDatePartParser = new ArrayList<FilenameDatePartParser>();
        }
        return this.filenameDatePartParser;
    }

    /**
     * Ruft den Wert der titleFormatter-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link TitleFormatter }
     *     
     */
    public TitleFormatter getTitleFormatter() {
        return titleFormatter;
    }

    /**
     * Legt den Wert der titleFormatter-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link TitleFormatter }
     *     
     */
    public void setTitleFormatter(TitleFormatter value) {
        this.titleFormatter = value;
    }

}
