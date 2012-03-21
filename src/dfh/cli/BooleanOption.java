/*
 * dfh.cli -- a command line argument parsing library for Java
 * 
 * Copyright (C) 2012 David F. Houghton
 * 
 * This software is licensed under the LGPL. Please see accompanying NOTICE file
 * and lgpl.txt.
 */
package dfh.cli;

public class BooleanOption extends Option<Boolean> {

	{
		hasArgument = false;
	}

	@Override
	public String description() {
		if (description == null)
			return "a boolean option";
		return description;
	}

	@Override
	public void validate() throws ValidationException {
		if (stored == null) {
			if (def == null) {
				value = found;
			} else {
				value = found ? !def : def;
			}
		} else {
			try {
				value = new Boolean(stored);
			} catch (Exception e) {
				throw new ValidationException(e.getMessage());
			}
		}
	}

}
