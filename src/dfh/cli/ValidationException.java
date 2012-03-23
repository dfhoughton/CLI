/*
 * dfh.cli -- a command line argument parsing library for Java
 * 
 * Copyright (C) 2012 David F. Houghton
 * 
 * This software is licensed under the LGPL. Please see accompanying NOTICE file
 * and lgpl.txt.
 */
package dfh.cli;

/**
 * Exception thrown when validation fails. The error message will be collected
 * and added to an error list.
 * <p>
 * <b>Creation date:</b> Nov 4, 2011
 * 
 * @author David Houghton
 * 
 */
public class ValidationException extends Exception {
	private static final long serialVersionUID = 1L;

	public ValidationException(String error) {
		super(error);
	}
}
