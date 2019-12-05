package com.lab.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.lab.business.UserBusiness;
import com.lab.exception.BusinessException;
import com.lab.model.User;
import com.lab.util.ViewUtils;

import lombok.Getter;
import lombok.Setter;

@SessionScoped
@Named
public class UserAuthenticationController implements Serializable {

	private static final long serialVersionUID = 568135254116655105L;

	@Getter
	@Setter
	private User authenticatedUser = new User();

	@Inject
	private UserBusiness userBusiness;

	public String authenticate() {
		try {
			authenticatedUser = userBusiness.getUserAuthenticated(authenticatedUser);
			return "home.xhtml?faces-redirect=true";
		} catch (BusinessException e) {
			ViewUtils.addError(e.getMessage());
			return null;
		}
	}

	public String logout() {
		authenticatedUser = new User();
		return "login.xhtml?faces-redirect=true";
	}
}
