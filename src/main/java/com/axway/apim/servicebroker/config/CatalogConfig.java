package com.axway.apim.servicebroker.config;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cloud.servicebroker.model.catalog.Catalog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import com.fasterxml.jackson.databind.ObjectMapper;

//@Configuration
public class CatalogConfig {

//	private static final Logger logger = LoggerFactory.getLogger(CatalogConfig.class);
//
//	@Autowired
//	private ResourceLoader resourceLoader;
//
//	@Autowired
//	private ObjectMapper mapper;
//
//	@Bean
//	public Catalog catalog() {
//		InputStream inputStream = null;
//		Catalog catalog = null;
//		try {
//			inputStream = resourceLoader.getResource("classpath:catalog.json").getInputStream();
//			catalog = mapper.readValue(inputStream, Catalog.class);
//
//		} catch (IOException e) {
//			logger.error("Unable to create catalog", e);
//		} finally {
//			if (inputStream != null)
//				try {
//					inputStream.close();
//				} catch (IOException e) {
//
//				}
//		}
//		System.out.println(catalog);
//		return catalog;
//	}
}
