package aeo.services.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;

import aeo.services.util.UtilConstants;

public abstract class RestServiceClient extends JbehaveStoryMapper {

	public static String REST_URL_NAME = "rest.url";
	public static String restUrl = "";
	private String defaultPropFilePath = "test.properties";
	private HttpClient httpClient = null;
	private HttpResponse httpResponse = null;
	private CloseableHttpClient httpClientProxy = null;
	
	private void readDefaultProperties() {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(defaultPropFilePath);
		Properties props = new Properties();
		try {
			props.load(inputStream);
			restUrl = props.getProperty(REST_URL_NAME).trim();
			
	
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
				overrideEnvProperties();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void overrideEnvProperties() {
		String restUrlName = System.getProperty(REST_URL_NAME);
		if (StringUtils.isNotBlank(restUrlName)) {
			restUrl = restUrlName.trim();
		}
	}
	
	public String getResource(String url, String pyldtype, Map<String, String> headerParamMap) throws Exception {
		String encodedUrl = URIUtil.encodeQuery(url);
		return getHttpResourceWOProxy(encodedUrl, pyldtype, headerParamMap);
		
	}

private String getHttpResourceWOProxy(String encodedUrl, String pyldtype, Map<String, String> headerParamMap) throws Exception {
	HttpGet httpGet = new HttpGet(encodedUrl);
	return executeHttpRequest(httpGet, pyldtype, headerParamMap);
}

public String createResource(String url, String payload, String pyldtype, Map<String, String> headerParamMap)
		throws Exception {
	String encodedUrl = URIUtil.encodeQuery(url);
	return createHttpResourceWOProxy(encodedUrl, payload, pyldtype, headerParamMap);
	
	
}

private String createHttpResourceWOProxy(String encodedUrl, String payload, String pyldtype,
		Map<String, String> headerParamMap) throws Exception {
	HttpPost httpPost = new HttpPost(encodedUrl);
	if (StringUtils.equalsIgnoreCase(pyldtype, UtilConstants.JSON)) {
		httpPost.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));
	} else if (StringUtils.equalsIgnoreCase(pyldtype, UtilConstants.XML)) {
		httpPost.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));
	} else if (StringUtils.equalsIgnoreCase(pyldtype, UtilConstants.XML)) {
		httpPost.setEntity(new StringEntity(payload, ContentType.APPLICATION_XML));
	}
	return executeHttpRequest(httpPost, pyldtype, headerParamMap);
}

public String updateResource(String url, String payload, String pyldtype, Map<String, String> headerParamMap)
		throws Exception {
	String encodedUrl = URIUtil.encodeQuery(url);
	return updateHttpResourceWOProxy(encodedUrl, payload, pyldtype, headerParamMap);
	
	
}

private String updateHttpResourceWOProxy(String encodedUrl, String payload, String pyldtype,
		Map<String, String> headerParamMap) throws Exception {
	HttpPut httpPut = new HttpPut(encodedUrl);
	if (StringUtils.equalsIgnoreCase(pyldtype, UtilConstants.JSON)) {
		httpPut.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));
	} else if (StringUtils.equalsIgnoreCase(pyldtype, UtilConstants.XML)) {
		httpPut.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));
	} else if (StringUtils.equalsIgnoreCase(pyldtype, UtilConstants.XML)) {
		httpPut.setEntity(new StringEntity(payload, ContentType.APPLICATION_XML));
	}
	return executeHttpRequest(httpPut, pyldtype, headerParamMap);
}







public String deleteResource(String url, String pyldtype, Map<String, String> headerParamMap) throws Exception {
	String encodedUrl = URIUtil.encodeQuery(url);
	return deleteHttpResourceWOProxy(encodedUrl, pyldtype, headerParamMap);
	
}


private String deleteHttpResourceWOProxy(String encodedUrl, String pyldtype, Map<String, String> headerParamMap) throws Exception {
	HttpDelete httpDel = new HttpDelete(encodedUrl);
	return executeHttpRequest(httpDel, pyldtype, headerParamMap);
}

private String executeHttpRequest(HttpUriRequest request, String format, Map<String, String> headerParamMap
		) {
	String responseStr = "";
	try {
		if (StringUtils.equalsIgnoreCase(format, UtilConstants.JSON)) {
			request.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
			request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
		} else if (StringUtils.equalsIgnoreCase(format, UtilConstants.XML)) {
			request.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_XML.getMimeType());
			request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_XML.getMimeType());
		} else if (StringUtils.equalsIgnoreCase(format, UtilConstants.FORM_URL_ENCODED)) {// okta
																							// verification
			request.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
			request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
			// httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,proxy);
			httpResponse = httpClient.execute(request);
			responseStr = EntityUtils.toString(httpResponse.getEntity());
			System.out.println(httpResponse);
			
		}
		if ((headerParamMap != null) && (headerParamMap.size() > 0)) {
			addHttpReqHeaders(request, headerParamMap);
		}
		httpResponse = httpClient.execute(request);
		responseStr = EntityUtils.toString(httpResponse.getEntity());
	} catch (Exception e) {
		e.printStackTrace();
		request.abort();
		
	}
	
	
	return responseStr;
}






private void addHttpReqHeaders(HttpUriRequest request, Map<String, String> headerParamMap) {
	for (String key : headerParamMap.keySet()) {
		String headerValue = "";
		String value = headerParamMap.get(key);
		if (StringUtils.equalsIgnoreCase(value, UtilConstants.NULL)) {
			headerValue = null;
		} else if (StringUtils.equalsIgnoreCase(value, UtilConstants.EMPTY)) {
			headerValue = "";
		} else {
			headerValue = value;
		}
		// request.setHeader(key, headerValue);
		request.addHeader(key, headerValue);
	//	logger.info(key + ":" + headerValue);
	}
	Header[] headersProxy = request.getAllHeaders();
	Map<String, String> reqHeaderMap=new HashMap<String, String>();
	for (Header header : headersProxy) {
		reqHeaderMap.put(header.getName(), header.getValue());
	}
	
}


public int getHttpResponseCode() {
	return httpResponse.getStatusLine().getStatusCode();
	
}

public Map<String, String> getResponseHeaders() {
	Map<String, String> resHeaderMap = new HashMap<String, String>();
	Header[] headers = httpResponse.getAllHeaders();
	for (Header header : headers) {
		resHeaderMap.put(header.getName(), header.getValue());
	}
	
	
	
	return resHeaderMap;
}

@BeforeScenario
public void setUpHttpClientExample() throws Exception {
	httpClient = new DefaultHttpClient();
	// Added for HttpDeleteWithBody service call
	httpClientProxy = HttpClients.createDefault();
	
}

@AfterScenario
public void tearDownHttpClientExample() throws Exception {
	if (httpClient != null) {
		httpClient.getConnectionManager().shutdown();
		// Added for HttpDeleteWithBody service call
		httpClientProxy.getConnectionManager().shutdown();
	}
	
}





public String getSrvcRestUrlBldr(String resourceCollection) {
	StringBuilder builder = new StringBuilder();
	builder.append(restUrl);
	return builder.append(resourceCollection.trim()).toString();
}



}
