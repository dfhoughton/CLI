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
 * Option holding a set of values.
 * 
 * @param <K>
 *            type of object in set
 */
abstract class SetOption<K> extends CollectionOption<K, Set<K>> {
	private static final long serialVersionUID = 1L;

	{
		value = new LinkedHashSet<K>();
	}

	@Override
	String description() {
		if (description == null)
			return "a set of " + type();
		return description;
	}
}
