/*
 * dfh.cli -- a command line argument parsing library for Java
 * 
 * Copyright (C) 2012 David F. Houghton
 * 
 * This software is licensed under the LGPL. Please see accompanying NOTICE file
 * and lgpl.txt.
 */
package dfh.cli;

import java.io.Serializable;

public class StringListOption extends ListOption<String> implements
		Serializable {
	private static final long serialVersionUID = 1L;

	{
		argDescription = "str";
	}

	@Override
	protected String handle(String s) {
		return s;
	}

	@Override
	protected String type() {
		return "strings";
	}

}
