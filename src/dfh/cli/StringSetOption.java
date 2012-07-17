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

public class StringSetOption extends CollectionOption<String, Set<String>> {
	{
		value = new LinkedHashSet<String>();
	}

	@Override
	public String description() {
		if (description == null)
			return "a set of strings";
		return description;
	}

	@Override
	public void validate() throws ValidationException {
		for (String s : storageList)
			value.add(s);
	}

}
