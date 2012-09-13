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

import dfh.cli.IntegerOption.NumType;

/**
 * Option holding repeated, unique integer values.
 * <p>
 * 
 * @author David F. Houghton
 * 
 */
public class IntegerSetOption extends CollectionOption<Number, Set<Number>> {
	private final NumType it;

	{
		value = new LinkedHashSet<Number>();
	}

	public IntegerSetOption(Object cz) {
		it = NumType.obj2type(cz);
		argDescription = it.arg;
	}

	@Override
	public String description() {
		if (description == null) {
			switch (it) {
			case flt:
			case dbl:
			case bigdec:
				return "a set of floating point numbers";
			default:
				return "a set of integers";
			}
		}
		return description;
	}

	@Override
	public void validate() throws ValidationException {
		for (String stored : storageList) {
			value.add(NumType.parse(it, stored));
		}
	}

}
