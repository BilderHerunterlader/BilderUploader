package ch.supertomcat.bilderuploader.upload;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.text.StringSubstitutor;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.protocol.RedirectLocations;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.http.message.StatusLine;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.supertomcat.bilderuploader.hosterconfig.AdditionalField;
import ch.supertomcat.bilderuploader.hosterconfig.AdditionalHeader;
import ch.supertomcat.bilderuploader.hosterconfig.CookieVariableStore;
import ch.supertomcat.bilderuploader.hosterconfig.FormContentType;
import ch.supertomcat.bilderuploader.hosterconfig.GenerateRandom;
import ch.supertomcat.bilderuploader.hosterconfig.HTTPMethod;
import ch.supertomcat.bilderuploader.hosterconfig.Hoster;
import ch.supertomcat.bilderuploader.hosterconfig.Param;
import ch.supertomcat.bilderuploader.hosterconfig.PasswordVariable;
import ch.supertomcat.bilderuploader.hosterconfig.PrepareResultStep;
import ch.supertomcat.bilderuploader.hosterconfig.PrepareUploadStep;
import ch.supertomcat.bilderuploader.hosterconfig.Regex;
import ch.supertomcat.bilderuploader.hosterconfig.RegexAndReplacement;
import ch.supertomcat.bilderuploader.hosterconfig.RegexAndVariableStore;
import ch.supertomcat.bilderuploader.hosterconfig.RegexReplacementVariableStore;
import ch.supertomcat.bilderuploader.hosterconfig.UploadResultStep;
import ch.supertomcat.bilderuploader.hosterconfig.UploadStep;
import ch.supertomcat.bilderuploader.randomgenerator.RandomGenerator;
import ch.supertomcat.bilderuploader.randomgenerator.UploadedNetRandomGenerator;
import ch.supertomcat.bilderuploader.settings.ProxyManager;
import ch.supertomcat.bilderuploader.settings.SettingsManager;
import ch.supertomcat.bilderuploader.settingsconfig.HosterSettings;
import ch.supertomcat.supertomcatutils.gui.formatter.UnitFormatUtil;
import ch.supertomcat.supertomcatutils.http.HTTPUtil;
import ch.supertomcat.supertomcatutils.queue.QueueTask;
import ch.supertomcat.supertomcatutils.queue.QueueTaskBase;
import ch.supertomcat.supertomcatutils.queue.QueueTaskFactory;

/**
 * Upload Manager
 */
public class UploadManager implements QueueTaskFactory<UploadFile, FileUploadResult> {
	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Proxy Manager
	 */
	private final ProxyManager proxyManager;

	/**
	 * Settings Manager
	 */
	private final SettingsManager settingsManager;

	/**
	 * Constructor
	 * 
	 * @param proxyManager
	 * @param settingsManager
	 */
	public UploadManager(ProxyManager proxyManager, SettingsManager settingsManager) {
		this.proxyManager = proxyManager;
		this.settingsManager = settingsManager;
	}

	@Override
	public QueueTask<UploadFile, FileUploadResult> createTaskCallable(UploadFile task) {
		return new UploadFileQueueTask(task);
	}

	/**
	 * Uploads the given file to the hoster
	 * 
	 * @param hoster Hoster
	 * @param file File
	 * @param fileSize File Size
	 * @param listener Listener or null
	 * @return Upload Result
	 * @throws IOException
	 */
	public FileUploadResult uploadFile(Hoster hoster, File file, long fileSize, UploadProgressListener listener) throws IOException {
		BasicCookieStore cookieStore = new BasicCookieStore();
		try (CloseableHttpClient client = proxyManager.getHTTPClientBuilder().setDefaultCookieStore(cookieStore).build()) {
			listener.statusChanged(UploadFileState.UPLOADING);
			HosterSettings hosterSettings = settingsManager.getHosterSettings(hoster.getName());

			Map<String, String> dymanicVariables = new LinkedHashMap<>();
			dymanicVariables.put("FileName", file.getName());
			dymanicVariables.put("FileSize", Long.toString(fileSize));
			dymanicVariables.put("FileLastModified", Long.toString(file.lastModified()));

			Map<String, String> passwordValues = new LinkedHashMap<>();
			List<PasswordVariable> passwordVariables = hoster.getPasswordVariable();
			if (!passwordVariables.isEmpty()) {
				if (hosterSettings != null) {
					Map<String, Object> hosterSettingsValues = settingsManager.getHosterSettingsValues(hoster.getName());
					for (PasswordVariable passwordVariable : passwordVariables) {
						String variableName = passwordVariable.getVariableName();
						Object variableValue = hosterSettingsValues.get(variableName);
						if (variableValue == null) {
							logger.error("Hoster Settings Value is null: {}", variableName);
						}
						passwordValues.put(variableName, String.valueOf(variableValue));
					}
				} else {
					throw new IOException("Hoster has password variables, but no Hoster Settings exist for Hoster: " + hoster.getName());
				}
			}

			dymanicVariables.putAll(passwordValues);

			generateRandoms(hoster.getGenerateRandom(), dymanicVariables);

			List<String> prepareUploadResultTexts = new ArrayList<>();
			for (PrepareUploadStep prepareUploadStep : hoster.getPrepareUploadStep()) {
				prepareUpload(prepareUploadStep, dymanicVariables, prepareUploadResultTexts, client, cookieStore, listener);
			}

			logger.info("Dynamic Variables after Prepare Upload: {}", dymanicVariables);

			UploadStep uploadStep = hoster.getUploadStep();
			String fileFieldName = uploadStep.getFieldName();
			String filename = file.getName();
			String preparedFilename = prepareFilename(filename, uploadStep.getFilenameRegex());
			dymanicVariables.put("PreparedFileName", preparedFilename);
			String uploadURL = StringSubstitutor.replace(uploadStep.getUrl(), dymanicVariables);
			Map<String, String> uploadFields = prepareAdditionalFields(uploadStep.getAdditionalField(), dymanicVariables);
			Map<String, String> additionalHeaders = prepareAdditionalHeaders(uploadStep.getAdditionalHeader(), dymanicVariables);

			logger.info("Upload Step: URL: {}, FileFieldName: {}, Filename: {}", uploadURL, fileFieldName, preparedFilename);

			ContainerPage uploadContainerPage = executeHTTPPostRequest(uploadURL, fileFieldName, file, preparedFilename, uploadFields, additionalHeaders, listener, client);
			// ContainerPage uploadContainerPage = executeURLConnectionUpload(uploadURL, fileFieldName, file, file.getName(), uploadFields, listener);
			if (uploadContainerPage.isSuccess()) {
				logger.info("Container-Page for Upload Step: URL={}: {}", uploadURL, uploadContainerPage);
				checkForFailure(uploadStep.getFailureRegex(), uploadContainerPage, listener);
			} else {
				logger.info("Container-Page for Upload Step: URL={}: {}", uploadURL, uploadContainerPage);
				listener.statusChanged(UploadFileState.FAILED);
				throw new IOException("Upload Failed: " + uploadContainerPage.getStatusLine());
			}

			logger.info("Dynamic Variables after Upload: {}", dymanicVariables);

			ContainerPage prepareResultContainerPage = null;
			for (PrepareResultStep prepareResultStep : hoster.getPrepareResultStep()) {
				prepareResultContainerPage = prepareResult(prepareResultStep, dymanicVariables, client, listener);
			}

			logger.info("Dynamic Variables after Prepare Result: {}", dymanicVariables);

			UploadResultStep uploadResultStep = hoster.getUploadResultStep();
			List<String> uploadResultTexts;
			if (prepareResultContainerPage != null) {
				uploadResultTexts = getResultTexts(uploadResultStep, prepareResultContainerPage);
			} else {
				uploadResultTexts = getResultTexts(uploadResultStep, uploadContainerPage);
			}

			if (uploadResultTexts.isEmpty()) {
				logger.info("No Upload Result Texts found for Upload Step: URL={}", uploadURL);
				throw new IOException("Upload Failed: No Upload Result Texts found");
			}

			listener.statusChanged(UploadFileState.COMPLETE);
			return new FileUploadResult(prepareUploadResultTexts, uploadResultTexts);
		}
	}

	private void prepareUpload(PrepareUploadStep prepareUploadStep, Map<String, String> dymanicVariables, List<String> prepareUploadResultTexts, CloseableHttpClient client, CookieStore cookieStore,
			UploadProgressListener listener) throws IOException {
		String prepareUploadURL = StringSubstitutor.replace(prepareUploadStep.getUrl(), dymanicVariables);
		HTTPMethod httpMethod = prepareUploadStep.getHttpMethod();
		logger.info("Prepare Upload Step: URL: {}, HTTP-Method: {}", prepareUploadURL, httpMethod);
		ContainerPage prepareUploadContainerPage;
		Map<String, String> additionalHeaders = prepareAdditionalHeaders(prepareUploadStep.getAdditionalHeader(), dymanicVariables);
		if (httpMethod == HTTPMethod.POST) {
			Map<String, String> prepareUploadFields = prepareAdditionalFields(prepareUploadStep.getAdditionalField(), dymanicVariables);
			boolean multiPart = prepareUploadStep.getFormContentType() == FormContentType.MULTI_PART_FORM_DATA;
			prepareUploadContainerPage = executeHTTPPostRequest(prepareUploadURL, multiPart, prepareUploadFields, additionalHeaders, client);
		} else {
			prepareUploadContainerPage = executeHTTPGetRequest(prepareUploadURL, additionalHeaders, client);
		}

		if (prepareUploadContainerPage.isSuccess()) {
			logger.info("Container-Page for Prepare Upload Step: URL={}, HTTP-Method={}: {}", prepareUploadURL, httpMethod, prepareUploadContainerPage);
			checkForFailure(prepareUploadStep.getFailureRegex(), prepareUploadContainerPage, listener);
			prepareUploadResultTexts.addAll(getPrepareUploadResults(prepareUploadStep, prepareUploadContainerPage, dymanicVariables));

			storeCookiesToVariables(prepareUploadStep.getCookieVariableStore(), dymanicVariables, cookieStore);

			logger.info("Dynamic Variables after Prepare Upload Step: URL={}, HTTP-Method={}: {}", prepareUploadURL, httpMethod, dymanicVariables);
		} else {
			logger.error("Container-Page for Prepare Upload Step: URL={}, HTTP-Method={}: {}", prepareUploadURL, httpMethod, prepareUploadContainerPage);
			listener.statusChanged(UploadFileState.FAILED);
			throw new IOException("Prepare Upload Failed: " + prepareUploadContainerPage.getStatusLine());
		}
	}

	private ContainerPage prepareResult(PrepareResultStep prepareResultStep, Map<String, String> dymanicVariables, CloseableHttpClient client, UploadProgressListener listener) throws IOException {
		String prepareResultURL = StringSubstitutor.replace(prepareResultStep.getUrl(), dymanicVariables);
		HTTPMethod httpMethod = prepareResultStep.getHttpMethod();
		logger.info("Prepare Result Step: URL: {}, HTTP-Method: {}", prepareResultURL, httpMethod);
		ContainerPage prepareResultContainerPage;
		Map<String, String> additionalHeaders = prepareAdditionalHeaders(prepareResultStep.getAdditionalHeader(), dymanicVariables);
		if (httpMethod == HTTPMethod.POST) {
			Map<String, String> prepareUploadFields = prepareAdditionalFields(prepareResultStep.getAdditionalField(), dymanicVariables);
			boolean multiPart = prepareResultStep.getFormContentType() == FormContentType.MULTI_PART_FORM_DATA;
			prepareResultContainerPage = executeHTTPPostRequest(prepareResultURL, multiPart, prepareUploadFields, additionalHeaders, client);
		} else {
			prepareResultContainerPage = executeHTTPGetRequest(prepareResultURL, additionalHeaders, client);
		}

		if (prepareResultContainerPage.isSuccess()) {
			logger.info("Container-Page for Prepare Result Step: URL={}, HTTP-Method={}: {}", prepareResultURL, httpMethod, prepareResultContainerPage);
			checkForFailure(prepareResultStep.getFailureRegex(), prepareResultContainerPage, listener);

			logger.info("Dynamic Variables after Prepare Result Step: URL={}, HTTP-Method={}: {}", prepareResultURL, httpMethod, dymanicVariables);
			return prepareResultContainerPage;
		} else {
			logger.error("Container-Page for Prepare Result Step: URL={}, HTTP-Method={}: {}", prepareResultURL, httpMethod, prepareResultContainerPage);
			listener.statusChanged(UploadFileState.FAILED);
			throw new IOException("Prepare Upload Failed: " + prepareResultContainerPage.getStatusLine());
		}
	}

	private void storeCookiesToVariables(List<CookieVariableStore> cookieVariableStoreList, Map<String, String> dymanicVariables, CookieStore cookieStore) {
		for (CookieVariableStore cookieVariableStore : cookieVariableStoreList) {
			boolean cookieFound = false;
			for (Cookie cookie : cookieStore.getCookies()) {
				if (cookie.getName().equals(cookieVariableStore.getCookieName())) {
					dymanicVariables.put(cookieVariableStore.getVariableName(), cookie.getValue());
					cookieFound = true;
					break;
				}
			}

			if (!cookieFound) {
				logger.error("Cookie was not found: {}", cookieVariableStore.getCookieName());
			}
		}
	}

	/**
	 * Generate Randoms
	 * 
	 * @param randoms Randoms
	 * @param dymanicVariables Dynamic Variables
	 * @throws IOException
	 */
	private void generateRandoms(List<GenerateRandom> randoms, Map<String, String> dymanicVariables) throws IOException {
		for (GenerateRandom random : randoms) {
			RandomGenerator randomGenerator;
			if ("UploadedNetRandomGenerator".equals(random.getClassName())) {
				randomGenerator = new UploadedNetRandomGenerator();
			} else {
				throw new IOException("Upload Failed: Random Generator not found: " + random.getClassName());
			}
			String randomValue = randomGenerator.generateRandom(paramListToMap(random.getParam()));
			dymanicVariables.put(random.getVariableName(), randomValue);
		}
	}

	/**
	 * Convert list of params to Map
	 * 
	 * @param params Params
	 * @return Map
	 */
	private Map<String, String> paramListToMap(List<Param> params) {
		Map<String, String> map = new LinkedHashMap<>();
		for (Param param : params) {
			map.put(param.getKey(), param.getValue());
		}
		return map;
	}

	/**
	 * Prepare Additional Fields
	 * 
	 * @param additionalFields Additional Fields
	 * @param dymanicVariables Dynamic Variables
	 * @return Prepared Additional Fields
	 */
	private Map<String, String> prepareAdditionalFields(List<AdditionalField> additionalFields, Map<String, String> dymanicVariables) {
		Map<String, String> fields = new LinkedHashMap<>();
		for (AdditionalField additionalField : additionalFields) {
			String fieldValue = additionalField.getFieldValue();
			// Replace variables in additional field
			fieldValue = StringSubstitutor.replace(fieldValue, dymanicVariables);
			fields.put(additionalField.getFieldName(), fieldValue);
		}
		return fields;
	}

	/**
	 * Prepare Additional Headers
	 * 
	 * @param additionalHeaders Additional Headers
	 * @param dymanicVariables Dynamic Variables
	 * @return Prepared Additional Headers
	 */
	private Map<String, String> prepareAdditionalHeaders(List<AdditionalHeader> additionalHeaders, Map<String, String> dymanicVariables) {
		Map<String, String> fields = new LinkedHashMap<>();
		for (AdditionalHeader additionalHeader : additionalHeaders) {
			String headerValue = additionalHeader.getHeaderValue();
			// Replace variables in header value
			headerValue = StringSubstitutor.replace(headerValue, dymanicVariables);
			fields.put(additionalHeader.getHeaderName(), headerValue);
		}
		return fields;
	}

	/**
	 * Prepare Filename
	 * 
	 * @param filename Original Filename
	 * @param filenameRegexps Filename Expressions
	 * @return Prepared Filename
	 */
	private String prepareFilename(String filename, List<RegexAndReplacement> filenameRegexps) {
		// TODO Precompile Regex
		String prepareFilename = filename;
		for (RegexAndReplacement filenameRegex : filenameRegexps) {
			Pattern pattern = Pattern.compile(filenameRegex.getPattern());
			Matcher matcher = pattern.matcher(filename);
			prepareFilename = matcher.replaceAll(filenameRegex.getReplacement());
		}
		return prepareFilename;
	}

	private List<String> getResultTexts(UploadResultStep uploadResultStep, ContainerPage uploadContainerPage) {
		return getResultTexts(uploadResultStep.getRegex(), uploadContainerPage, null);
	}

	private List<String> getPrepareUploadResults(PrepareUploadStep prepareUploadStep, ContainerPage prepareUploadContainerPage, Map<String, String> variables) {
		return getResultTexts(prepareUploadStep.getRegex(), prepareUploadContainerPage, variables);
	}

	private <T extends Regex> List<String> getResultTexts(List<T> regularExpressions, ContainerPage containerPage, Map<String, String> variables) {
		List<String> uploadResultTexts = new ArrayList<>();
		for (T regex : regularExpressions) {
			String replacement = null;
			List<RegexReplacementVariableStore> replacementVariables = null;
			if (regex instanceof RegexAndReplacement) {
				RegexAndReplacement regexAndReplacement = (RegexAndReplacement)regex;
				replacement = regexAndReplacement.getReplacement();
			} else if (regex instanceof RegexAndVariableStore) {
				RegexAndVariableStore regexAndVariableStore = (RegexAndVariableStore)regex;
				replacement = regexAndVariableStore.getReplacement();
				replacementVariables = regexAndVariableStore.getReplacementVariable();
			}

			// TODO Precompile Regex
			Pattern pattern = Pattern.compile(regex.getPattern());
			Matcher matcher = pattern.matcher(containerPage.getPage());
			while (matcher.find()) {
				Matcher subMatcher = pattern.matcher(matcher.group());
				if (replacement != null) {
					String uploadResultText = subMatcher.replaceAll(replacement);
					uploadResultTexts.add(uploadResultText);
				}

				if (replacementVariables != null && variables != null) {
					for (RegexReplacementVariableStore variableStore : replacementVariables) {
						String variableValue = subMatcher.replaceAll(variableStore.getReplacement());
						variables.put(variableStore.getVariableName(), variableValue);
					}
				}
			}
		}
		return uploadResultTexts;
	}

	private void checkForFailure(List<Regex> failurePatterns, ContainerPage containerPage, UploadProgressListener listener) throws IOException {
		String page = containerPage.getPage();
		for (Regex failurePattern : failurePatterns) {
			// TODO Precompile Regex
			Pattern pattern = Pattern.compile(failurePattern.getPattern());
			Matcher matcher = pattern.matcher(page);
			if (matcher.find()) {
				listener.statusChanged(UploadFileState.FAILED);
				throw new IOException("Upload failed, because failure pattern was found: " + matcher.group());
			}
		}
	}

	/**
	 * Get Redirected URL
	 * 
	 * @param context Context
	 * @return Redirected URL or null
	 */
	private String getRedirectedURL(HttpContext context) {
		String redirectedURL = null;
		HttpClientContext clientContext = HttpClientContext.castOrCreate(context);
		RedirectLocations redirectedLocations = clientContext.getRedirectLocations();
		if (redirectedLocations != null) {
			List<URI> redirectedLocationsURIList = redirectedLocations.getAll();
			if (!redirectedLocationsURIList.isEmpty()) {
				HttpRequest redirectedRequest = clientContext.getRequest();
				try {
					URI redirectedURI = redirectedRequest.getUri();
					if (redirectedURI.isAbsolute()) {
						redirectedURL = redirectedURI.toString();
					} else {
						HttpHost redirectedHttpHost = clientContext.getHttpRoute().getTargetHost();
						redirectedURL = redirectedHttpHost.toURI() + redirectedURI;
					}
				} catch (URISyntaxException e) {
					logger.error("Could not determine redirection", e);
				}
			}
		}
		return redirectedURL;
	}

	/**
	 * Execute HTTP GET Request
	 * 
	 * @param url URL
	 * @param additionalHeaders Additional Headers
	 * @param client HTTP Client
	 * @return Container Page
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public ContainerPage executeHTTPGetRequest(String url, Map<String, String> additionalHeaders, CloseableHttpClient client) throws IOException {
		url = HTTPUtil.encodeURL(url);
		HttpGet method = new HttpGet(url);

		RequestConfig.Builder requestConfigBuilder = proxyManager.getDefaultRequestConfigBuilder();
		requestConfigBuilder.setMaxRedirects(10);
		method.setConfig(requestConfigBuilder.build());
		method.setHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:52.9) Gecko/20100101 Goanna/4.1 Firefox/52.9 PaleMoon/28.0.0.1");

		for (Map.Entry<String, String> entry : additionalHeaders.entrySet()) {
			method.setHeader(entry.getKey(), entry.getValue());
		}

		HttpClientContext context = HttpClientContext.create();

		return client.execute(method, context, response -> {
			StatusLine statusLine = new StatusLine(response);
			int statusCode = statusLine.getStatusCode();

			if (statusCode < 200 || statusCode >= 400) {
				return new ContainerPage(false, "", null, statusLine, Arrays.asList(response.getHeaders()));
			}

			String redirectedURL = getRedirectedURL(context);

			String page;

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				page = EntityUtils.toString(entity);
			} else {
				page = "";
			}
			return new ContainerPage(true, page, redirectedURL, statusLine, Arrays.asList(response.getHeaders()));
		});
	}

	/**
	 * Execute HTTP POST Request
	 * 
	 * @param url URL
	 * @param multiPart True if multipart/form-data should be used, false if application/x-www-form-urlencoded should be used
	 * @param fields Fields
	 * @param additionalHeaders Additional Headers
	 * @param client HTTP Client
	 * @return Container Page
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public ContainerPage executeHTTPPostRequest(String url, boolean multiPart, Map<String, String> fields, Map<String, String> additionalHeaders, CloseableHttpClient client) throws IOException {
		url = HTTPUtil.encodeURL(url);
		HttpPost method = new HttpPost(url);

		RequestConfig.Builder requestConfigBuilder = proxyManager.getDefaultRequestConfigBuilder();
		requestConfigBuilder.setMaxRedirects(10);
		method.setConfig(requestConfigBuilder.build());
		method.setHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:52.9) Gecko/20100101 Goanna/4.1 Firefox/52.9 PaleMoon/28.0.0.1");

		for (Map.Entry<String, String> entry : additionalHeaders.entrySet()) {
			method.setHeader(entry.getKey(), entry.getValue());
		}

		if (multiPart) {
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			for (Map.Entry<String, String> entry : fields.entrySet()) {
				builder.addTextBody(entry.getKey(), entry.getValue());
			}
			HttpEntity multipart = builder.build();
			method.setEntity(multipart);
		} else {
			List<NameValuePair> params = new ArrayList<>();
			for (Map.Entry<String, String> entry : fields.entrySet()) {
				params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			method.setEntity(new UrlEncodedFormEntity(params));
		}

		HttpClientContext context = HttpClientContext.create();

		return client.execute(method, context, response -> {
			StatusLine statusLine = new StatusLine(response);
			int statusCode = statusLine.getStatusCode();

			if (statusCode < 200 || statusCode >= 400) {
				return new ContainerPage(false, "", null, statusLine, Arrays.asList(response.getHeaders()));
			}

			String redirectedURL = getRedirectedURL(context);

			String page;

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				page = EntityUtils.toString(entity);
			} else {
				page = "";
			}
			return new ContainerPage(true, page, redirectedURL, statusLine, Arrays.asList(response.getHeaders()));
		});
	}

	/**
	 * Execute HTTP POST Request
	 * 
	 * @param url URL
	 * @param fileFieldName Field name for file
	 * @param file File
	 * @param fileName File name
	 * @param fields Fields
	 * @param additionalHeaders Additional Headers
	 * @param listener Listener or null
	 * @param client HTTP Client
	 * @return Container Page
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public ContainerPage executeHTTPPostRequest(String url, String fileFieldName, File file, String fileName, Map<String, String> fields, Map<String, String> additionalHeaders,
			UploadProgressListener listener, CloseableHttpClient client) throws IOException {
		url = HTTPUtil.encodeURL(url);
		HttpPost method = new HttpPost(url);

		RequestConfig.Builder requestConfigBuilder = proxyManager.getDefaultRequestConfigBuilder();
		requestConfigBuilder.setMaxRedirects(10);
		method.setConfig(requestConfigBuilder.build());
		method.setHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:52.9) Gecko/20100101 Goanna/4.1 Firefox/52.9 PaleMoon/28.0.0.1");

		for (Map.Entry<String, String> entry : additionalHeaders.entrySet()) {
			method.setHeader(entry.getKey(), entry.getValue());
		}

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		for (Map.Entry<String, String> entry : fields.entrySet()) {
			builder.addTextBody(entry.getKey(), entry.getValue());
		}

		builder.addBinaryBody(fileFieldName, file, ContentType.APPLICATION_OCTET_STREAM, fileName);

		try (HttpEntity multipart = builder.build()) {
			if (listener != null) {
				UploadProgressEntityWrapper uploadProgressEntityWrapper = new UploadProgressEntityWrapper(multipart, listener);
				method.setEntity(uploadProgressEntityWrapper);
			} else {
				method.setEntity(multipart);
			}

			HttpClientContext context = HttpClientContext.create();
			return client.execute(method, context, response -> {
				StatusLine statusLine = new StatusLine(response);
				int statusCode = statusLine.getStatusCode();

				if (statusCode < 200 || statusCode >= 400) {
					return new ContainerPage(false, "", null, statusLine, Arrays.asList(response.getHeaders()));
				}

				String redirectedURL = getRedirectedURL(context);

				String page;

				HttpEntity entity = response.getEntity();
				if (entity != null) {
					page = EntityUtils.toString(entity);
				} else {
					page = "";
				}
				return new ContainerPage(true, page, redirectedURL, statusLine, Arrays.asList(response.getHeaders()));
			});
		}
	}

	/**
	 * Execute URL Connection Upload
	 * 
	 * @param url URL
	 * @param fileFieldName Field name for file
	 * @param file File
	 * @param fileName File name
	 * @param fields Fields
	 * @param listener Listener or null
	 * @return Container Page
	 */
	@SuppressWarnings("resource")
	public ContainerPage executeURLConnectionUpload(String url, String fileFieldName, File file, String fileName, Map<String, String> fields, UploadProgressListener listener) {
		try {
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			for (Map.Entry<String, String> entry : fields.entrySet()) {
				builder.addTextBody(entry.getKey(), entry.getValue());
			}

			builder.addBinaryBody(fileFieldName, file, ContentType.APPLICATION_OCTET_STREAM, fileName);

			HttpEntity multipart = builder.build();

			URL xurl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection)xurl.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("POST");
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);

			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.addRequestProperty("Content-length", multipart.getContentLength() + "");
			conn.addRequestProperty("Content-Type", multipart.getContentType());

			conn.connect();

			OutputStream out;
			if (listener != null) {
				out = new UploadProgressFilterOutputStream(conn.getOutputStream(), listener, multipart.getContentLength());
			} else {
				out = conn.getOutputStream();
			}

			multipart.writeTo(out);

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				String page = readStream(conn.getInputStream());
				return new ContainerPage(true, page, "", null, new ArrayList<>());
			}
		} catch (Exception e) {
			logger.error("xxx", e);
		}
		return new ContainerPage(false, "", "", null, new ArrayList<>());
	}

	private static String readStream(InputStream in) {
		BufferedReader reader = null;
		StringBuilder builder = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return builder.toString();
	}

	private class UploadFileQueueTask extends QueueTaskBase<UploadFile, FileUploadResult> {
		/**
		 * Constructor
		 * 
		 * @param uploadFile
		 */
		public UploadFileQueueTask(UploadFile uploadFile) {
			super(uploadFile);
		}

		@Override
		public FileUploadResult call() throws Exception {
			FileUploadResult result = uploadFile(task.getHoster(), task.getFile(), task.getSize(), new UploadProgressListener() {
				/**
				 * Progress
				 */
				private final UploadFileProgress progress = task.getProgress();

				@Override
				public void progress(long bytesTotal, long bytesCompleted, float percent, double rate) {
					progress.setBytesTotal(bytesTotal);
					progress.setBytesUploaded(bytesCompleted);
					progress.setRate(rate);
					task.progressUpdated();
				}

				@Override
				public void statusChanged(UploadFileState state) {
					task.setStatus(state);
					if (state == UploadFileState.FAILED) {
						task.increaseFailedCount();
						if (task.getFailedCount() >= settingsManager.getUploadSettings().getMaxFailedCount()) {
							task.setDeactivated(true);
						}
					}
				}

				@Override
				public void complete(long bytesTotal, long bytesCompleted, long duration) {
					// TODO Save?
					double rate = -1;
					if (duration > 0) {
						double secondsPassed = duration / 1000.0d;
						if (secondsPassed < 1) {
							secondsPassed = 1;
							// interpolate size to 1 second
							rate = 1000.0d * bytesCompleted / duration;
						} else {
							rate = bytesCompleted / secondsPassed;
						}
					}
					logger.info("Duration: {}ms, Rate: {}", duration, UnitFormatUtil.getBitrateString(rate));
				}
			});
			task.setFileUploadResult(result);
			return result;
		}
	}
}
