package com.lab.dao;

import java.io.Serializable;
import java.util.List;

import org.jboss.weld.util.collections.ImmutableList;

import com.lab.model.User;

public class UserDAO implements Serializable{

	private static final long serialVersionUID = -3104439259348297150L;
	
	private static List<User> listUsers = ImmutableList.of(User.builder().username("Test1").password("test1@mytest.com").build(), 
															User.builder().username("Test2").password("test2@mytest.com").build());
	
	public UserDAO() {
		throw new ExceptionInInitializerError("Should not initialize UserDAO class");
	}
	
	public static User getUserAuthenticated(User user) {
		if(listUsers.contains(user)) {
			return user;
		}
		else
			return null;
	}

}
