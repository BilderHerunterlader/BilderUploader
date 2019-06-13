package ch.supertomcat.bilderuploader.settings;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import ch.supertomcat.bilderuploader.settingsconfig.ConnectionSettings;
import ch.supertomcat.bilderuploader.settingsconfig.ProxyMode;
import ch.supertomcat.bilderuploader.settingsconfig.ProxySettings;

/**
 * Class which provides method to get a preconfigured HttpClient, so that
 * in the hole program the proxysettings can be used.
 */
public class ProxyManager {
	/**
	 * Settings Manager
	 */
	private final SettingsManager settingsManager;

	/**
	 * Mode
	 */
	private ProxyMode mode = ProxyMode.DIRECT_CONNECTION;

	/**
	 * Name
	 */
	private String proxyname = "127.0.0.1";

	/**
	 * Port
	 */
	private int proxyport = 0;

	/**
	 * Username
	 */
	private String proxyuser = "";

	/**
	 * Password
	 */
	private String proxypassword = "";

	/**
	 * Flag if authetification is required or not
	 */
	private boolean auth = false;

	/**
	 * Mutli Threaded Connection Manager
	 */
	private final PoolingHttpClientConnectionManager conManager;

	/**
	 * Constructor
	 * 
	 * @param settingsManager Settings Manager
	 */
	public ProxyManager(SettingsManager settingsManager) {
		this.settingsManager = settingsManager;
		// Get the configuration
		readFromSettings();

		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
		registryBuilder.register("http", PlainConnectionSocketFactory.getSocketFactory());
		registryBuilder.register("https", SSLConnectionSocketFactory.getSocketFactory());
		Registry<ConnectionSocketFactory> registry = registryBuilder.build();
		conManager = new PoolingHttpClientConnectionManager(registry);
		conManager.setMaxTotal(60);
		conManager.setDefaultMaxPerRoute(30);

		/*
		 * Custom Socket and Connection config to fix slow uploads. By default apache httpclient only uses 8192 byte frames for sending, which makes uploads
		 * slow.
		 */
		conManager.setDefaultSocketConfig(SocketConfig.custom().setSndBufSize(65536).build());
		conManager.setDefaultConnectionConfig(ConnectionConfig.custom().setBufferSize(65536).setFragmentSizeHint(65536).build());
	}

	/**
	 * Returns a preconfigured RequestConfig Builder
	 * 
	 * @return RequestConfig.Builder
	 */
	public RequestConfig.Builder getDefaultRequestConfigBuilder() {
		RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
		ConnectionSettings conSettings = settingsManager.getConnectionSettings();
		requestConfigBuilder.setSocketTimeout(conSettings.getSocketTimeout());
		requestConfigBuilder.setConnectionRequestTimeout(conSettings.getConnectionRequestTimeout());
		requestConfigBuilder.setConnectTimeout(conSettings.getConnectTimeout());
		requestConfigBuilder.setCookieSpec(CookieSpecs.STANDARD);
		return requestConfigBuilder;
	}

	private void configureHttpClientBuilder(HttpClientBuilder clientBuilder) {
		if (mode != ProxyMode.DIRECT_CONNECTION) {
			HttpHost proxy = new HttpHost(proxyname, proxyport);
			if (auth) {
				AuthScope authScope = new AuthScope(proxyname, proxyport);
				Credentials credentials = new UsernamePasswordCredentials(proxyuser, proxypassword);

				CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
				credentialsProvider.setCredentials(authScope, credentials);

				clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
			}
			clientBuilder.setProxy(proxy);
		}

		DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(0, false);
		clientBuilder.setRetryHandler(retryHandler);

		/*
		 * Custom Socket and Connection config to fix slow uploads. By default apache httpclient only uses 8192 byte frames for sending, which makes uploads
		 * slow.
		 */
		clientBuilder.setDefaultSocketConfig(SocketConfig.custom().setSndBufSize(65536).build());
		clientBuilder.setDefaultConnectionConfig(ConnectionConfig.custom().setBufferSize(65536).setFragmentSizeHint(65536).build());

		clientBuilder.setDefaultRequestConfig(getDefaultRequestConfigBuilder().build());
	}

	/**
	 * Returns a preconfigured HttpClientBuilder
	 * 
	 * @return HttpClientBuilder
	 */
	public HttpClientBuilder getHTTPClientBuilder() {
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		clientBuilder.setConnectionManagerShared(true);
		clientBuilder.setConnectionManager(conManager);
		configureHttpClientBuilder(clientBuilder);
		return clientBuilder;
	}

	/**
	 * Returns a preconfigured HttpClientBuilder, but without usage of the Mutli Threaded Connection Manager
	 * I need this, because of some problems on download and import html files.
	 * 
	 * @return HttpClientBuilder
	 */
	public HttpClientBuilder getNonMultithreadedHTTPClientBuilder() {
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		configureHttpClientBuilder(clientBuilder);
		return clientBuilder;
	}

	/**
	 * Returns a preconfigured HttpClient
	 * 
	 * @return HttpClient
	 */
	public CloseableHttpClient getHTTPClient() {
		return getHTTPClientBuilder().build();
	}

	/**
	 * Returns a preconfigured HttpClient, but without usage of the Mutli Threaded Connection Manager
	 * I need this, because of some problems on download and import html files.
	 * 
	 * @return HttpClient
	 */
	public CloseableHttpClient getNonMultithreadedHTTPClient() {
		return getNonMultithreadedHTTPClientBuilder().build();
	}

	/**
	 * Save the settings
	 */
	public void writeToSettings() {
		ConnectionSettings conSettings = settingsManager.getConnectionSettings();
		ProxySettings proxySettings = conSettings.getProxy();
		proxySettings.setMode(mode);
		proxySettings.setHost(proxyname);
		proxySettings.setPort(proxyport);
		proxySettings.setUser(proxyuser);
		proxySettings.setPassword(proxypassword);
		proxySettings.setAuth(auth);
		settingsManager.writeSettings(true);
	}

	/**
	 * Read the settings
	 */
	public void readFromSettings() {
		ConnectionSettings conSettings = settingsManager.getConnectionSettings();
		ProxySettings proxySettings = conSettings.getProxy();
		mode = proxySettings.getMode();
		proxyname = proxySettings.getHost();
		proxyport = proxySettings.getPort();
		proxyuser = proxySettings.getUser();
		proxypassword = proxySettings.getPassword();
		auth = proxySettings.isAuth();
	}

	/**
	 * Returns the proxyuser
	 * 
	 * @return proxyuser
	 */
	public String getProxyuser() {
		return proxyuser;
	}

	/**
	 * Sets the proxyuser
	 * 
	 * @param proxyuser proxyuser
	 */
	public void setProxyuser(String proxyuser) {
		this.proxyuser = proxyuser;
	}

	/**
	 * Returns the mode
	 * 
	 * @return mode
	 */
	public ProxyMode getMode() {
		return mode;
	}

	/**
	 * Sets the mode
	 * 
	 * @param mode mode
	 */
	public void setMode(ProxyMode mode) {
		this.mode = mode;
	}

	/**
	 * Returns the proxyname
	 * 
	 * @return proxyname
	 */
	public String getProxyname() {
		return proxyname;
	}

	/**
	 * Sets the proxyname
	 * 
	 * @param proxyname proxyname
	 */
	public void setProxyname(String proxyname) {
		this.proxyname = proxyname;
	}

	/**
	 * Returns the proxyport
	 * 
	 * @return proxyport
	 */
	public int getProxyport() {
		return proxyport;
	}

	/**
	 * Sets the proxyport
	 * 
	 * @param proxyport proxyport
	 */
	public void setProxyport(int proxyport) {
		this.proxyport = proxyport;
	}

	/**
	 * Returns the proxypassword
	 * 
	 * @return proxypassword
	 */
	public String getProxypassword() {
		return proxypassword;
	}

	/**
	 * Sets the proxypassword
	 * 
	 * @param proxypassword proxypassword
	 */
	public void setProxypassword(String proxypassword) {
		this.proxypassword = proxypassword;
	}

	/**
	 * Returns the auth
	 * 
	 * @return auth
	 */
	public boolean isAuth() {
		return auth;
	}

	/**
	 * Sets the auth
	 * 
	 * @param auth auth
	 */
	public void setAuth(boolean auth) {
		this.auth = auth;
	}
}
