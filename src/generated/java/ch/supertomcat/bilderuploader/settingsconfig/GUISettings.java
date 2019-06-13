//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2019.06.13 um 10:26:28 PM CEST 
//


package ch.supertomcat.bilderuploader.settingsconfig;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für GUISettings complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="GUISettings">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="language" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="saveTableColumnSizes" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="saveTableSortOrders" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="colWidthsQueue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tableSortOrdersQueue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sizeDisplayMode" type="{}SizeDisplayMode"/>
 *         &lt;element name="progressDisplayMode" type="{}ProgressDisplayMode"/>
 *         &lt;element name="uploadRate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="lookAndFeel" type="{}LookAndFeelSetting"/>
 *         &lt;element name="uploadsCompleteNotification" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="uploadsCompleteNotificationSeconds" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="selectedHoster" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="selectedBiWiHoster" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="selectedMainTemplate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="selectedFooterTemplate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="titleFilenameParserEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="titleFilenameParserUseAll" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="selectedTitleFilenameParser" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mainWindow" type="{}WindowSettings"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GUISettings", propOrder = {
    "language",
    "saveTableColumnSizes",
    "saveTableSortOrders",
    "colWidthsQueue",
    "tableSortOrdersQueue",
    "sizeDisplayMode",
    "progressDisplayMode",
    "uploadRate",
    "lookAndFeel",
    "uploadsCompleteNotification",
    "uploadsCompleteNotificationSeconds",
    "selectedHoster",
    "selectedBiWiHoster",
    "selectedMainTemplate",
    "selectedFooterTemplate",
    "titleFilenameParserEnabled",
    "titleFilenameParserUseAll",
    "selectedTitleFilenameParser",
    "mainWindow"
})
public class GUISettings {

    @XmlElement(required = true, nillable = true)
    protected String language;
    @XmlElement(defaultValue = "false")
    protected boolean saveTableColumnSizes;
    @XmlElement(defaultValue = "false")
    protected boolean saveTableSortOrders;
    @XmlElement(required = true, defaultValue = "")
    protected String colWidthsQueue;
    @XmlElement(required = true, defaultValue = "")
    protected String tableSortOrdersQueue;
    @XmlElement(required = true, defaultValue = "AUTO_CHANGE_SIZE")
    @XmlSchemaType(name = "string")
    protected SizeDisplayMode sizeDisplayMode;
    @XmlElement(required = true, defaultValue = "PROGRESSBAR_PERCENT")
    @XmlSchemaType(name = "string")
    protected ProgressDisplayMode progressDisplayMode;
    @XmlElement(defaultValue = "true")
    protected boolean uploadRate;
    @XmlElement(required = true, defaultValue = "LAF_OS")
    @XmlSchemaType(name = "string")
    protected LookAndFeelSetting lookAndFeel;
    @XmlElement(defaultValue = "true")
    protected boolean uploadsCompleteNotification;
    @XmlElement(defaultValue = "5")
    protected int uploadsCompleteNotificationSeconds;
    @XmlElement(required = true, nillable = true)
    protected String selectedHoster;
    @XmlElement(required = true, nillable = true)
    protected String selectedBiWiHoster;
    @XmlElement(required = true, nillable = true)
    protected String selectedMainTemplate;
    @XmlElement(required = true, nillable = true)
    protected String selectedFooterTemplate;
    protected boolean titleFilenameParserEnabled;
    protected boolean titleFilenameParserUseAll;
    @XmlElement(required = true, nillable = true)
    protected String selectedTitleFilenameParser;
    @XmlElement(required = true)
    protected WindowSettings mainWindow;

    /**
     * Ruft den Wert der language-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Legt den Wert der language-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLanguage(String value) {
        this.language = value;
    }

    /**
     * Ruft den Wert der saveTableColumnSizes-Eigenschaft ab.
     * 
     */
    public boolean isSaveTableColumnSizes() {
        return saveTableColumnSizes;
    }

    /**
     * Legt den Wert der saveTableColumnSizes-Eigenschaft fest.
     * 
     */
    public void setSaveTableColumnSizes(boolean value) {
        this.saveTableColumnSizes = value;
    }

    /**
     * Ruft den Wert der saveTableSortOrders-Eigenschaft ab.
     * 
     */
    public boolean isSaveTableSortOrders() {
        return saveTableSortOrders;
    }

    /**
     * Legt den Wert der saveTableSortOrders-Eigenschaft fest.
     * 
     */
    public void setSaveTableSortOrders(boolean value) {
        this.saveTableSortOrders = value;
    }

    /**
     * Ruft den Wert der colWidthsQueue-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColWidthsQueue() {
        return colWidthsQueue;
    }

    /**
     * Legt den Wert der colWidthsQueue-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColWidthsQueue(String value) {
        this.colWidthsQueue = value;
    }

    /**
     * Ruft den Wert der tableSortOrdersQueue-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTableSortOrdersQueue() {
        return tableSortOrdersQueue;
    }

    /**
     * Legt den Wert der tableSortOrdersQueue-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTableSortOrdersQueue(String value) {
        this.tableSortOrdersQueue = value;
    }

    /**
     * Ruft den Wert der sizeDisplayMode-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SizeDisplayMode }
     *     
     */
    public SizeDisplayMode getSizeDisplayMode() {
        return sizeDisplayMode;
    }

    /**
     * Legt den Wert der sizeDisplayMode-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SizeDisplayMode }
     *     
     */
    public void setSizeDisplayMode(SizeDisplayMode value) {
        this.sizeDisplayMode = value;
    }

    /**
     * Ruft den Wert der progressDisplayMode-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ProgressDisplayMode }
     *     
     */
    public ProgressDisplayMode getProgressDisplayMode() {
        return progressDisplayMode;
    }

    /**
     * Legt den Wert der progressDisplayMode-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ProgressDisplayMode }
     *     
     */
    public void setProgressDisplayMode(ProgressDisplayMode value) {
        this.progressDisplayMode = value;
    }

    /**
     * Ruft den Wert der uploadRate-Eigenschaft ab.
     * 
     */
    public boolean isUploadRate() {
        return uploadRate;
    }

    /**
     * Legt den Wert der uploadRate-Eigenschaft fest.
     * 
     */
    public void setUploadRate(boolean value) {
        this.uploadRate = value;
    }

    /**
     * Ruft den Wert der lookAndFeel-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link LookAndFeelSetting }
     *     
     */
    public LookAndFeelSetting getLookAndFeel() {
        return lookAndFeel;
    }

    /**
     * Legt den Wert der lookAndFeel-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link LookAndFeelSetting }
     *     
     */
    public void setLookAndFeel(LookAndFeelSetting value) {
        this.lookAndFeel = value;
    }

    /**
     * Ruft den Wert der uploadsCompleteNotification-Eigenschaft ab.
     * 
     */
    public boolean isUploadsCompleteNotification() {
        return uploadsCompleteNotification;
    }

    /**
     * Legt den Wert der uploadsCompleteNotification-Eigenschaft fest.
     * 
     */
    public void setUploadsCompleteNotification(boolean value) {
        this.uploadsCompleteNotification = value;
    }

    /**
     * Ruft den Wert der uploadsCompleteNotificationSeconds-Eigenschaft ab.
     * 
     */
    public int getUploadsCompleteNotificationSeconds() {
        return uploadsCompleteNotificationSeconds;
    }

    /**
     * Legt den Wert der uploadsCompleteNotificationSeconds-Eigenschaft fest.
     * 
     */
    public void setUploadsCompleteNotificationSeconds(int value) {
        this.uploadsCompleteNotificationSeconds = value;
    }

    /**
     * Ruft den Wert der selectedHoster-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSelectedHoster() {
        return selectedHoster;
    }

    /**
     * Legt den Wert der selectedHoster-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSelectedHoster(String value) {
        this.selectedHoster = value;
    }

    /**
     * Ruft den Wert der selectedBiWiHoster-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSelectedBiWiHoster() {
        return selectedBiWiHoster;
    }

    /**
     * Legt den Wert der selectedBiWiHoster-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSelectedBiWiHoster(String value) {
        this.selectedBiWiHoster = value;
    }

    /**
     * Ruft den Wert der selectedMainTemplate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSelectedMainTemplate() {
        return selectedMainTemplate;
    }

    /**
     * Legt den Wert der selectedMainTemplate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSelectedMainTemplate(String value) {
        this.selectedMainTemplate = value;
    }

    /**
     * Ruft den Wert der selectedFooterTemplate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSelectedFooterTemplate() {
        return selectedFooterTemplate;
    }

    /**
     * Legt den Wert der selectedFooterTemplate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSelectedFooterTemplate(String value) {
        this.selectedFooterTemplate = value;
    }

    /**
     * Ruft den Wert der titleFilenameParserEnabled-Eigenschaft ab.
     * 
     */
    public boolean isTitleFilenameParserEnabled() {
        return titleFilenameParserEnabled;
    }

    /**
     * Legt den Wert der titleFilenameParserEnabled-Eigenschaft fest.
     * 
     */
    public void setTitleFilenameParserEnabled(boolean value) {
        this.titleFilenameParserEnabled = value;
    }

    /**
     * Ruft den Wert der titleFilenameParserUseAll-Eigenschaft ab.
     * 
     */
    public boolean isTitleFilenameParserUseAll() {
        return titleFilenameParserUseAll;
    }

    /**
     * Legt den Wert der titleFilenameParserUseAll-Eigenschaft fest.
     * 
     */
    public void setTitleFilenameParserUseAll(boolean value) {
        this.titleFilenameParserUseAll = value;
    }

    /**
     * Ruft den Wert der selectedTitleFilenameParser-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSelectedTitleFilenameParser() {
        return selectedTitleFilenameParser;
    }

    /**
     * Legt den Wert der selectedTitleFilenameParser-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSelectedTitleFilenameParser(String value) {
        this.selectedTitleFilenameParser = value;
    }

    /**
     * Ruft den Wert der mainWindow-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link WindowSettings }
     *     
     */
    public WindowSettings getMainWindow() {
        return mainWindow;
    }

    /**
     * Legt den Wert der mainWindow-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link WindowSettings }
     *     
     */
    public void setMainWindow(WindowSettings value) {
        this.mainWindow = value;
    }

}
