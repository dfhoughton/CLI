/*
 * dfh.cli -- a command line argument parsing library for Java
 * 
 * Copyright (C) 2012 David F. Houghton
 * 
 * This software is licensed under the LGPL. Please see accompanying NOTICE file
 * and lgpl.txt.
 */
package dfh.cli;

public class StringOption extends Option<String> {
	{
		argDescription = "str";
	}

	@Override
	public String description() {
		if (description == null)
			return "string option";
		return description;
	}

	@Override
	public void validate() throws ValidationException {
		if (stored != null) {
			value = stored;
		}
	}
}
