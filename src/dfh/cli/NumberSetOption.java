/*
 * dfh.cli -- a command line argument parsing library for Java
 * 
 * Copyright (C) 2012 David F. Houghton
 * 
 * This software is licensed under the LGPL. Please see accompanying NOTICE file
 * and lgpl.txt.
 */
package dfh.cli;

import java.util.LinkedHashSet;
import java.util.Set;

public class NumberSetOption extends CollectionOption<Set<Double>> {
	{
		value = new LinkedHashSet<Double>();
	}

	@Override
	public String description() {
		if (description == null)
			return "a set of floating point numbers";
		return description;
	}

	@Override
	public void validate() throws ValidationException {
		for (String s : storageList) {
			try {
				value.add(new Double(s));
			} catch (NumberFormatException e) {
				throw new ValidationException("--" + name
						+ " expects floating point arguments; received " + s);
			}
		}
	}

}
