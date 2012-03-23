/*
 * dfh.cli -- a command line argument parsing library for Java
 * 
 * Copyright (C) 2012 David F. Houghton
 * 
 * This software is licensed under the LGPL. Please see accompanying NOTICE file
 * and lgpl.txt.
 */
package dfh.cli;

import java.util.ArrayList;
import java.util.List;

/**
 * Option holding repeated integer values.
 * <p>
 * 
 * @author David F. Houghton
 * 
 */
public class IntegerListOption extends CollectionOption<List<Integer>> {
	{
		value = new ArrayList<Integer>();
	}

	@Override
	public String description() {
		if (description == null)
			return "a list of integers";
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
