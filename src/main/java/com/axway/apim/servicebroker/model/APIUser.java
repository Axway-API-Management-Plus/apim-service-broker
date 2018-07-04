package com.axway.apim.servicebroker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class APIUser {

	private String id;
	private String organizationId; // (string, optional): The unique identifier for the organization to which the user belongs ,
	private String name; //(string, optional): The user's name ,
	private String description; //(string, optional): A description of the user ,
	private String loginName; //(string, optional): A unique login name for the user ,
	private String email; //(string, optional): An email address for the user ,
	private String phone; //(string, optional): The user's phone number ,
	private String mobile; //(string, optional): The user's mobile number ,
	private String role = "oadmin"; //(string, optional): The user's role, one of: user, oadmin, or admin ,
	private boolean enabled = true; //(boolean, optional): Indicates whether or not the user account is enabled or not ,
	private long createdOn; //(integer, optional): Epoch/Unix time stamp when the organization was created ,
	private String state = "approved"; //(string, optional): The current state of the account, one of: approved, pending ,
	private String type = "internal"; //(string, optional): Indicates the type of user. Possible values: internal, external ,
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
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
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public long getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(long createdOn) {
		this.createdOn = createdOn;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
