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

public class StringListOption extends CollectionOption<String, List<String>> {
	{
		value = new ArrayList<String>();
	}

	@Override
	public String description() {
		if (description == null)
			return "a list of strings";
		return description;
	}

	@Override
	public void validate() throws ValidationException {
		for (String s : storageList)
			value.add(s);
	}

}
