package com.thy.aodb.scheduler.support;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.client.RestTemplate;

public class RestTemplateFactory implements FactoryBean<RestTemplate>, InitializingBean {
	
	protected transient Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	private RestTemplate restTemplate;
	
	private int readTimeout;
	private int connectionTimeout;
	
	private String rootURL;
	private int httpPort;
	private String schema;
	
	private String username;
	private String password;

	@Override
	public RestTemplate getObject() {
		return restTemplate;
	}

	@Override
	public Class<RestTemplate> getObjectType() {
		return RestTemplate.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() {
		try {
			LOGGER.debug("Initializing Rest Template... : {} , {} , {}", getSchema(), getRootURL(), getHttpPort());
			HttpHost host = new HttpHost(getRootURL(), getHttpPort(), getSchema());
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(getUsername(), getPassword()));
			CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
	        HttpComponentsClientHttpRequestFactoryBasicAuth requestFactory  = new HttpComponentsClientHttpRequestFactoryBasicAuth(httpClient, host);
	        requestFactory.setReadTimeout(getReadTimeout());
			requestFactory.setConnectTimeout(getConnectionTimeout());
	     	restTemplate =  new RestTemplate(requestFactory);
	     	LOGGER.debug("Rest Template Initialized Successfully...");
		}catch(Exception e){
			LOGGER.error("Rest Template Initialization FAILED...", e);
		}
    }

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public String getRootURL() {
		return rootURL;
	}

	public void setRootURL(String rootURL) {
		this.rootURL = rootURL;
	}

	public int getHttpPort() {
		return httpPort;
	}

	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
