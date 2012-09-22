/*
 * dfh.cli -- a command line argument parsing library for Java
 * 
 * Copyright (C) 2012 David F. Houghton
 * 
 * This software is licensed under the LGPL. Please see accompanying NOTICE file
 * and lgpl.txt.
 */
package dfh.cli;

public class StringSetOption extends SetOption<String> {
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
