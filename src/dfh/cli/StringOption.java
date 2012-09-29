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
 * Option whose value remains a string.
 */
class StringOption extends Option<String> {
	private static final long serialVersionUID = 1L;

	{
		argDescription = "str";
	}

	@Override
	String description() {
		if (description == null)
			return "string option";
		return description;
	}

	@Override
	void validate() throws ValidationException {
		if (stored != null) {
			value = stored;
		}
	}
}
