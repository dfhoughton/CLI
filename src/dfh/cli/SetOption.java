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

public abstract class SetOption<K> extends CollectionOption<K, Set<K>> {
	{
		value = new LinkedHashSet<K>();
	}

	@Override
	public String description() {
		if (description == null)
			return "a set of " + type();
		return description;
	}
}
