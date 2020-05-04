//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.05 um 01:18:08 AM CEST 
//


package ch.supertomcat.bilderuploader.hosterconfig;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ch.supertomcat.bilderuploader.hosterconfig package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ch.supertomcat.bilderuploader.hosterconfig
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AdditionalField }
     * 
     */
    public AdditionalField createAdditionalField() {
        return new AdditionalField();
    }

    /**
     * Create an instance of {@link UploadResultStep }
     * 
     */
    public UploadResultStep createUploadResultStep() {
        return new UploadResultStep();
    }

    /**
     * Create an instance of {@link RegexAndReplacement }
     * 
     */
    public RegexAndReplacement createRegexAndReplacement() {
        return new RegexAndReplacement();
    }

    /**
     * Create an instance of {@link Param }
     * 
     */
    public Param createParam() {
        return new Param();
    }

    /**
     * Create an instance of {@link AdditionalHeader }
     * 
     */
    public AdditionalHeader createAdditionalHeader() {
        return new AdditionalHeader();
    }

    /**
     * Create an instance of {@link PrepareUploadStep }
     * 
     */
    public PrepareUploadStep createPrepareUploadStep() {
        return new PrepareUploadStep();
    }

    /**
     * Create an instance of {@link Regex }
     * 
     */
    public Regex createRegex() {
        return new Regex();
    }

    /**
     * Create an instance of {@link RegexAndVariableStore }
     * 
     */
    public RegexAndVariableStore createRegexAndVariableStore() {
        return new RegexAndVariableStore();
    }

    /**
     * Create an instance of {@link CookieVariableStore }
     * 
     */
    public CookieVariableStore createCookieVariableStore() {
        return new CookieVariableStore();
    }

    /**
     * Create an instance of {@link GenerateRandom }
     * 
     */
    public GenerateRandom createGenerateRandom() {
        return new GenerateRandom();
    }

    /**
     * Create an instance of {@link Hoster }
     * 
     */
    public Hoster createHoster() {
        return new Hoster();
    }

    /**
     * Create an instance of {@link UploadStep }
     * 
     */
    public UploadStep createUploadStep() {
        return new UploadStep();
    }

    /**
     * Create an instance of {@link PasswordVariable }
     * 
     */
    public PasswordVariable createPasswordVariable() {
        return new PasswordVariable();
    }

    /**
     * Create an instance of {@link RegexReplacementVariableStore }
     * 
     */
    public RegexReplacementVariableStore createRegexReplacementVariableStore() {
        return new RegexReplacementVariableStore();
    }

}
