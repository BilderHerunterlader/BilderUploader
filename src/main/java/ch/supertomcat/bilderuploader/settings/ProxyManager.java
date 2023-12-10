package ch.supertomcat.bilderuploader.settings;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.Credentials;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.ManagedHttpClientConnectionFactory;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.config.Http1Config;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

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
	private PoolingHttpClientConnectionManager conManager;

	/**
	 * Constructor
	 * 
	 * @param settingsManager Settings Manager
	 */
	public ProxyManager(SettingsManager settingsManager) {
		this.settingsManager = settingsManager;
		// Get the configuration
		readFromSettings();

		initializeConnectionManager();

		settingsManager.addSettingsListener(new BUSettingsListener() {

			@Override
			public void settingsChanged() {
				conManager.setDefaultConnectionConfig(createConnectionConfig());
			}

			@Override
			public void lookAndFeelChanged() {
				// Nothing to do
			}
		});
	}

	/**
	 * Create Connection Config
	 * 
	 * @return Connection Config
	 */
	private ConnectionConfig createConnectionConfig() {
		ConnectionSettings conSettings = settingsManager.getConnectionSettings();
		ConnectionConfig.Builder defaultConnectionConfigBuilder = ConnectionConfig.custom();
		defaultConnectionConfigBuilder.setSocketTimeout(Timeout.ofMilliseconds(conSettings.getSocketTimeout()));
		defaultConnectionConfigBuilder.setConnectTimeout(Timeout.ofMilliseconds(conSettings.getConnectTimeout()));
		return defaultConnectionConfigBuilder.build();
	}

	/**
	 * Initialize Connection Manager
	 */
	private void initializeConnectionManager() {
		/*
		 * Custom Socket and Connection config to fix slow uploads. By default apache httpclient only uses 8192 byte frames for sending, which makes uploads
		 * slow.
		 */
		Http1Config http1Config = Http1Config.custom().setBufferSize(65536).setChunkSizeHint(65536).build();
		ManagedHttpClientConnectionFactory connectionFactory = ManagedHttpClientConnectionFactory.builder().http1Config(http1Config).build();

		PoolingHttpClientConnectionManagerBuilder conManagerBuilder = PoolingHttpClientConnectionManagerBuilder.create();
		conManagerBuilder.setMaxConnTotal(60);
		conManagerBuilder.setMaxConnPerRoute(30);
		conManagerBuilder.setDefaultSocketConfig(SocketConfig.custom().setSndBufSize(65536).build());
		conManagerBuilder.setConnectionFactory(connectionFactory);

		conManagerBuilder.setDefaultConnectionConfig(createConnectionConfig());

		conManager = conManagerBuilder.build();
	}

	/**
	 * @return Connection Manager
	 */
	private PoolingHttpClientConnectionManager getConnectionManager() {
		return conManager;
	}

	/**
	 * Returns a preconfigured RequestConfig Builder
	 * 
	 * @return RequestConfig.Builder
	 */
	public RequestConfig.Builder getDefaultRequestConfigBuilder() {
		RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
		ConnectionSettings conSettings = settingsManager.getConnectionSettings();
		requestConfigBuilder.setConnectionRequestTimeout(Timeout.ofMilliseconds(conSettings.getConnectionRequestTimeout()));
		return requestConfigBuilder;
	}

	private void configureHttpClientBuilder(HttpClientBuilder clientBuilder) {
		if (mode != ProxyMode.DIRECT_CONNECTION) {
			HttpHost proxy = new HttpHost(proxyname, proxyport);
			if (auth) {
				AuthScope authScope = new AuthScope(proxyname, proxyport);
				Credentials credentials = new UsernamePasswordCredentials(proxyuser, proxypassword.toCharArray());

				BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
				credentialsProvider.setCredentials(authScope, credentials);

				clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
			}
			clientBuilder.setProxy(proxy);
		}

		DefaultHttpRequestRetryStrategy retryStrategy = new DefaultHttpRequestRetryStrategy(0, TimeValue.ofSeconds(1L));
		clientBuilder.setRetryStrategy(retryStrategy);

		clientBuilder.setDefaultRequestConfig(getDefaultRequestConfigBuilder().build());
	}

	/**
	 * Returns a preconfigured HttpClientBuilder
	 * 
	 * @return HttpClientBuilder
	 */
	@SuppressWarnings("resource")
	public HttpClientBuilder getHTTPClientBuilder() {
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		clientBuilder.setConnectionManagerShared(true);
		clientBuilder.setConnectionManager(getConnectionManager());
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
