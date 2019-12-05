package com.lab.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class ViewUtils {
	public ViewUtils() {
		throw new ExceptionInInitializerError("Should not initialize ViewUtils class");
	}

	public static void addError(String msg) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + msg, ""));
	}
	public static void addInfo(String msg) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info: " + msg, ""));
	}
	public static void addWarn(String msg) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn: " + msg, ""));
	}
}
