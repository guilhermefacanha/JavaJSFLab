package com.lab.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable{ 
	
	private static final long serialVersionUID = 6895018722222668796L;
	
	private String username;
	private String password;
	private boolean authenticated;
	
	public boolean equals(Object other) {
		try {
			User u = (User) other;
			return this.username.equals(u.getUsername()) && this.password.equals(u.getPassword());
		} catch (Exception e) {
			return false;
		}
	}

	public boolean hasPermission(String page) {
		//load permissions for the user at login function
		return true;
	}
}
