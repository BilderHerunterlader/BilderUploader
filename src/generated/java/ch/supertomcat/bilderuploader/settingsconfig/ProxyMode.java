//
// This file was generated by the Eclipse Implementation of JAXB, v4.0.5 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
//


package ch.supertomcat.bilderuploader.settingsconfig;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 * 
 * <p>Java class for ProxyMode</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * <pre>{@code
 * <simpleType name="ProxyMode">
 *   <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     <enumeration value="DIRECT_CONNECTION"/>
 *     <enumeration value="PROXY"/>
 *   </restriction>
 * </simpleType>
 * }</pre>
 * 
 */
@XmlType(name = "ProxyMode")
@XmlEnum
public enum ProxyMode {

    DIRECT_CONNECTION,
    PROXY;

    public String value() {
        return name();
    }

    public static ProxyMode fromValue(String v) {
        return valueOf(v);
    }

}
