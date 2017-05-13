package com.tjing.bussiness.support;

public class BusinessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4779006399038736341L;

	public BusinessException(String text) {
		super(text);
	}
}
