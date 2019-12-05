package com.lab.business;

import java.io.Serializable;

import com.lab.dao.UserDAO;
import com.lab.exception.BusinessException;
import com.lab.model.User;

public class UserBusiness implements Serializable{

	private static final long serialVersionUID = 2740105967902279692L;
	
	public User getUserAuthenticated(User user) throws BusinessException {
		User u = UserDAO.getUserAuthenticated(user);
		if(u==null)
			throw new BusinessException("Username or Password Invalid!");
		u.setAuthenticated(true);
		return u;
	}
}
