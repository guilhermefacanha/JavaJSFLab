package com.lab.exception;

public class BusinessException extends Exception {

	private static final long serialVersionUID = 6015891424532204082L;

	public BusinessException(String string) {
		super(string);
	}

	public BusinessException(Exception e) {
		super(e);
	}

}
