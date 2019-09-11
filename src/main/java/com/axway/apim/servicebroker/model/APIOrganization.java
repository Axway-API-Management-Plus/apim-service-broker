package com.axway.apim.servicebroker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class APIOrganization {

	private String id;
	private String name;
	private String description;
	private String email;
	private String virtualHost;
	private String phone;
	private boolean enabled = true;
	private boolean development = true;
	//custom attribute
	private String service_instance_id;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getVirtualHost() {
		return virtualHost;
	}
	public void setVirtualHost(String virtualHost) {
		this.virtualHost = virtualHost;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isDevelopment() {
		return development;
	}
	public void setDevelopment(boolean development) {
		this.development = development;
	}
	public String getService_instance_id() {
		return service_instance_id;
	}
	public void setService_instance_id(String service_instance_id) {
		this.service_instance_id = service_instance_id;
	}
}
