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

/**
 * Option holding repeated, unique integer values.
 * <p>
 * 
 * @author David F. Houghton
 * 
 */
public class IntegerSetOption extends CollectionOption<Set<Integer>> {
	{
		value = new LinkedHashSet<Integer>();
	}

	@Override
	public String description() {
		if (description == null)
			return "a set of integers";
		return description;
	}

	@Override
	public void validate() throws ValidationException {
		for (String s : storageList) {
			try {
				value.add(new Integer(s));
			} catch (NumberFormatException e) {
				throw new ValidationException("--" + name
						+ " expects integer arguments; received " + s);
			}
		}
	}

}
