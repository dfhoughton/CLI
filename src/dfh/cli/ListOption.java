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

public abstract class ListOption<K> extends CollectionOption<K, List<K>> {
	{
		value = new ArrayList<K>();
	}

	@Override
	public String description() {
		if (description == null)
			return "a list of " + type();
		return description;
	}
}
