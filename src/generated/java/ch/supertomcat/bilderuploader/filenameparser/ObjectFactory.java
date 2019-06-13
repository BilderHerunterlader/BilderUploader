//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2019.06.13 um 10:11:42 PM CEST 
//


package ch.supertomcat.bilderuploader.filenameparser;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ch.supertomcat.bilderuploader.filenameparser package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ch.supertomcat.bilderuploader.filenameparser
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FilenameParser }
     * 
     */
    public FilenameParser createFilenameParser() {
        return new FilenameParser();
    }

    /**
     * Create an instance of {@link FilenameSplitter }
     * 
     */
    public FilenameSplitter createFilenameSplitter() {
        return new FilenameSplitter();
    }

    /**
     * Create an instance of {@link FilenamePartParser }
     * 
     */
    public FilenamePartParser createFilenamePartParser() {
        return new FilenamePartParser();
    }

    /**
     * Create an instance of {@link FilenameDatePartParser }
     * 
     */
    public FilenameDatePartParser createFilenameDatePartParser() {
        return new FilenameDatePartParser();
    }

    /**
     * Create an instance of {@link TitleFormatter }
     * 
     */
    public TitleFormatter createTitleFormatter() {
        return new TitleFormatter();
    }

    /**
     * Create an instance of {@link DateParser }
     * 
     */
    public DateParser createDateParser() {
        return new DateParser();
    }

    /**
     * Create an instance of {@link VariableReplacement }
     * 
     */
    public VariableReplacement createVariableReplacement() {
        return new VariableReplacement();
    }

    /**
     * Create an instance of {@link PatternReplacement }
     * 
     */
    public PatternReplacement createPatternReplacement() {
        return new PatternReplacement();
    }

}
