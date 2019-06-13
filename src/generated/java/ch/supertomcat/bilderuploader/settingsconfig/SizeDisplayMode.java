//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2019.06.13 um 10:26:28 PM CEST 
//


package ch.supertomcat.bilderuploader.settingsconfig;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für SizeDisplayMode.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="SizeDisplayMode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="AUTO_CHANGE_SIZE"/>
 *     &lt;enumeration value="ONLY_B"/>
 *     &lt;enumeration value="ONLY_KIB"/>
 *     &lt;enumeration value="ONLY_MIB"/>
 *     &lt;enumeration value="ONLY_GIB"/>
 *     &lt;enumeration value="ONLY_TIB"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SizeDisplayMode")
@XmlEnum
public enum SizeDisplayMode {

    AUTO_CHANGE_SIZE,
    ONLY_B,
    ONLY_KIB,
    ONLY_MIB,
    ONLY_GIB,
    ONLY_TIB;

    public String value() {
        return name();
    }

    public static SizeDisplayMode fromValue(String v) {
        return valueOf(v);
    }

}
