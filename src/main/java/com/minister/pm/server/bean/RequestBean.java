package com.minister.pm.server.bean;

import com.minister.pm.core.Context;

/**
 *
 * @author ljx
 * @Date Feb 21, 2019 12:08:49 AM
 *
 */
public class RequestBean {
	
	private Context context;

	private HttpMethod method;
	private String Host;
	private String url;
	private String protocol;
	private String Connection;
	private String UserAgent;
	private String Accept;
	private String AcceptEncoding;
	private String AcceptLanguage;
	private String data;

	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = HttpMethod.valueOf(method);
	}

	public String getHost() {
		return Host;
	}

	public void setHost(String host) {
		this.Host = host;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getConnection() {
		return Connection;
	}

	public void setConnection(String connection) {
		this.Connection = connection;
	}

	public String getUserAgent() {
		return UserAgent;
	}

	public void setUserAgent(String userAgent) {
		this.UserAgent = userAgent;
	}

	public String getAccept() {
		return Accept;
	}

	public void setAccept(String accept) {
		this.Accept = accept;
	}

	public String getAcceptEncoding() {
		return AcceptEncoding;
	}

	public void setAcceptEncoding(String acceptEncoding) {
		this.AcceptEncoding = acceptEncoding;
	}

	public String getAcceptLanguage() {
		return AcceptLanguage;
	}

	public void setAcceptLanguage(String acceptLanguage) {
		this.AcceptLanguage = acceptLanguage;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public Context getContext() {
		return context;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(method).append(" ").append(url).append(" ").append(protocol).append("\n");
		sb.append("Host: ").append(Host).append("\n");
		sb.append("Connection: ").append(Connection).append("\n");
		sb.append("User-Agent: ").append(UserAgent).append("\n");
		sb.append("Accept: ").append(Accept).append("\n");
		sb.append("Accept-Encoding: ").append(AcceptEncoding).append("\n");
		sb.append("Accept-Language: ").append(AcceptLanguage).append("\n");
		sb.append("\n");
		if (data != null)
			sb.append(data).append("\n");
		return sb.toString();
	}
}
