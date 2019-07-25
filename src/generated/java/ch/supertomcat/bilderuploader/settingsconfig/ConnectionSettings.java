//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2019.07.25 um 10:53:56 PM CEST 
//


package ch.supertomcat.bilderuploader.settingsconfig;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ConnectionSettings complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ConnectionSettings">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="maxConnections" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="maxConnectionsPerHost" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="connectTimeout" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="socketTimeout" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="connectionRequestTimeout" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="proxy" type="{}ProxySettings"/>
 *         &lt;element name="userAgent" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConnectionSettings", propOrder = {
    "maxConnections",
    "maxConnectionsPerHost",
    "connectTimeout",
    "socketTimeout",
    "connectionRequestTimeout",
    "proxy",
    "userAgent"
})
public class ConnectionSettings {

    @XmlElement(defaultValue = "32")
    protected int maxConnections;
    @XmlElement(defaultValue = "8")
    protected int maxConnectionsPerHost;
    @XmlElement(defaultValue = "60000")
    protected int connectTimeout;
    @XmlElement(defaultValue = "60000")
    protected int socketTimeout;
    @XmlElement(defaultValue = "60000")
    protected int connectionRequestTimeout;
    @XmlElement(required = true)
    protected ProxySettings proxy;
    @XmlElement(required = true, defaultValue = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:60.9) Gecko/20100101 Goanna/4.2 Firefox/60.9 PaleMoon/28.5.0")
    protected String userAgent;

    /**
     * Ruft den Wert der maxConnections-Eigenschaft ab.
     * 
     */
    public int getMaxConnections() {
        return maxConnections;
    }

    /**
     * Legt den Wert der maxConnections-Eigenschaft fest.
     * 
     */
    public void setMaxConnections(int value) {
        this.maxConnections = value;
    }

    /**
     * Ruft den Wert der maxConnectionsPerHost-Eigenschaft ab.
     * 
     */
    public int getMaxConnectionsPerHost() {
        return maxConnectionsPerHost;
    }

    /**
     * Legt den Wert der maxConnectionsPerHost-Eigenschaft fest.
     * 
     */
    public void setMaxConnectionsPerHost(int value) {
        this.maxConnectionsPerHost = value;
    }

    /**
     * Ruft den Wert der connectTimeout-Eigenschaft ab.
     * 
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * Legt den Wert der connectTimeout-Eigenschaft fest.
     * 
     */
    public void setConnectTimeout(int value) {
        this.connectTimeout = value;
    }

    /**
     * Ruft den Wert der socketTimeout-Eigenschaft ab.
     * 
     */
    public int getSocketTimeout() {
        return socketTimeout;
    }

    /**
     * Legt den Wert der socketTimeout-Eigenschaft fest.
     * 
     */
    public void setSocketTimeout(int value) {
        this.socketTimeout = value;
    }

    /**
     * Ruft den Wert der connectionRequestTimeout-Eigenschaft ab.
     * 
     */
    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    /**
     * Legt den Wert der connectionRequestTimeout-Eigenschaft fest.
     * 
     */
    public void setConnectionRequestTimeout(int value) {
        this.connectionRequestTimeout = value;
    }

    /**
     * Ruft den Wert der proxy-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ProxySettings }
     *     
     */
    public ProxySettings getProxy() {
        return proxy;
    }

    /**
     * Legt den Wert der proxy-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ProxySettings }
     *     
     */
    public void setProxy(ProxySettings value) {
        this.proxy = value;
    }

    /**
     * Ruft den Wert der userAgent-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * Legt den Wert der userAgent-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserAgent(String value) {
        this.userAgent = value;
    }

}
