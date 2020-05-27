package com.axway.apim.servicebroker.config;

import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class CatalogConfig {

	private static final Logger logger = LoggerFactory.getLogger(CatalogConfig.class);


	private ResourceLoader resourceLoader;

	@Autowired
	public CatalogConfig(ResourceLoader resourceLoader){
		this.resourceLoader = resourceLoader;
	}



	@Bean
	public String catalog() {
		InputStream inputStream = null;
		String catalog = null;
		try {
			inputStream = resourceLoader.getResource("classpath:catalog.json").getInputStream();
			catalog = JsonPath.parse(inputStream).jsonString();

		} catch (IOException e) {
			logger.error("Unable to create catalog", e);
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {

				}
		}
		return catalog;
	}
}
