//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.05 um 01:18:08 AM CEST 
//


package ch.supertomcat.bilderuploader.hosterconfig;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für FormContentType.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="FormContentType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="MultiPartFormData"/>
 *     &lt;enumeration value="XWWWFormURLEncoded"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "FormContentType")
@XmlEnum
public enum FormContentType {

    @XmlEnumValue("MultiPartFormData")
    MULTI_PART_FORM_DATA("MultiPartFormData"),
    @XmlEnumValue("XWWWFormURLEncoded")
    XWWW_FORM_URL_ENCODED("XWWWFormURLEncoded");
    private final String value;

    FormContentType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FormContentType fromValue(String v) {
        for (FormContentType c: FormContentType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
