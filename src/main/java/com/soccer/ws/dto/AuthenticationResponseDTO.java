package com.soccer.ws.dto;

import java.util.List;

public class AuthenticationResponseDTO {

	private static final long serialVersionUID = -6624726180748515507L;
	private String token;
	private String firstName;
	private String lastName;
	private String userName;
	private List<String> roles;

	public AuthenticationResponseDTO() {
		super();
	}

	public AuthenticationResponseDTO(String token, String firstName, String lastName, String userName, List<String> roles) {
		this.setToken(token);
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setUserName(userName);
		this.setRoles(roles);
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
