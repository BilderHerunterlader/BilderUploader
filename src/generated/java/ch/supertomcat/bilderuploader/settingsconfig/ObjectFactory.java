//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2019.07.25 um 10:53:56 PM CEST 
//


package ch.supertomcat.bilderuploader.settingsconfig;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ch.supertomcat.bilderuploader.settingsconfig package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ch.supertomcat.bilderuploader.settingsconfig
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Settings }
     * 
     */
    public Settings createSettings() {
        return new Settings();
    }

    /**
     * Create an instance of {@link DirectorySettings }
     * 
     */
    public DirectorySettings createDirectorySettings() {
        return new DirectorySettings();
    }

    /**
     * Create an instance of {@link ConnectionSettings }
     * 
     */
    public ConnectionSettings createConnectionSettings() {
        return new ConnectionSettings();
    }

    /**
     * Create an instance of {@link UploadSettings }
     * 
     */
    public UploadSettings createUploadSettings() {
        return new UploadSettings();
    }

    /**
     * Create an instance of {@link GUISettings }
     * 
     */
    public GUISettings createGUISettings() {
        return new GUISettings();
    }

    /**
     * Create an instance of {@link HosterSettings }
     * 
     */
    public HosterSettings createHosterSettings() {
        return new HosterSettings();
    }

    /**
     * Create an instance of {@link ProxySettings }
     * 
     */
    public ProxySettings createProxySettings() {
        return new ProxySettings();
    }

    /**
     * Create an instance of {@link CustomSetting }
     * 
     */
    public CustomSetting createCustomSetting() {
        return new CustomSetting();
    }

    /**
     * Create an instance of {@link WindowSettings }
     * 
     */
    public WindowSettings createWindowSettings() {
        return new WindowSettings();
    }

}
