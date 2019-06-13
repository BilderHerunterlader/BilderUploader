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
 * <p>Java-Klasse für ProxySettings complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ProxySettings">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mode" type="{}ProxyMode"/>
 *         &lt;element name="host" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="port" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="user" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="auth" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProxySettings", propOrder = {
    "mode",
    "host",
    "port",
    "user",
    "password",
    "auth"
})
public class ProxySettings {

    @XmlElement(required = true, defaultValue = "DIRECT_CONNECTION")
    @XmlSchemaType(name = "string")
    protected ProxyMode mode;
    @XmlElement(required = true, defaultValue = "127.0.0.1")
    protected String host;
    @XmlElement(defaultValue = "0")
    protected int port;
    @XmlElement(required = true, defaultValue = "")
    protected String user;
    @XmlElement(required = true, defaultValue = "")
    protected String password;
    @XmlElement(defaultValue = "false")
    protected boolean auth;

    /**
     * Ruft den Wert der mode-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ProxyMode }
     *     
     */
    public ProxyMode getMode() {
        return mode;
    }

    /**
     * Legt den Wert der mode-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ProxyMode }
     *     
     */
    public void setMode(ProxyMode value) {
        this.mode = value;
    }

    /**
     * Ruft den Wert der host-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHost() {
        return host;
    }

    /**
     * Legt den Wert der host-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHost(String value) {
        this.host = value;
    }

    /**
     * Ruft den Wert der port-Eigenschaft ab.
     * 
     */
    public int getPort() {
        return port;
    }

    /**
     * Legt den Wert der port-Eigenschaft fest.
     * 
     */
    public void setPort(int value) {
        this.port = value;
    }

    /**
     * Ruft den Wert der user-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUser() {
        return user;
    }

    /**
     * Legt den Wert der user-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUser(String value) {
        this.user = value;
    }

    /**
     * Ruft den Wert der password-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Legt den Wert der password-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Ruft den Wert der auth-Eigenschaft ab.
     * 
     */
    public boolean isAuth() {
        return auth;
    }

    /**
     * Legt den Wert der auth-Eigenschaft fest.
     * 
     */
    public void setAuth(boolean value) {
        this.auth = value;
    }

}
