//
// This file was generated by the Eclipse Implementation of JAXB, v4.0.5 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
//


package ch.supertomcat.bilderuploader.filenameparser;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType>
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="filenameSplitter" type="{}FilenameSplitter"/>
 *         <element name="filenamePartParser" type="{}FilenamePartParser" maxOccurs="unbounded" minOccurs="0"/>
 *         <element name="filenameDatePartParser" type="{}FilenameDatePartParser" maxOccurs="unbounded" minOccurs="0"/>
 *         <element name="titleFormatter" type="{}TitleFormatter" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
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
     * Gets the value of the filenameSplitter property.
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
     * Sets the value of the filenameSplitter property.
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
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the filenamePartParser property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getFilenamePartParser().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FilenamePartParser }
     * </p>
     * 
     * 
     * @return
     *     The value of the filenamePartParser property.
     */
    public List<FilenamePartParser> getFilenamePartParser() {
        if (filenamePartParser == null) {
            filenamePartParser = new ArrayList<>();
        }
        return this.filenamePartParser;
    }

    /**
     * Gets the value of the filenameDatePartParser property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the filenameDatePartParser property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getFilenameDatePartParser().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FilenameDatePartParser }
     * </p>
     * 
     * 
     * @return
     *     The value of the filenameDatePartParser property.
     */
    public List<FilenameDatePartParser> getFilenameDatePartParser() {
        if (filenameDatePartParser == null) {
            filenameDatePartParser = new ArrayList<>();
        }
        return this.filenameDatePartParser;
    }

    /**
     * Gets the value of the titleFormatter property.
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
     * Sets the value of the titleFormatter property.
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
