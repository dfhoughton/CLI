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

import dfh.cli.IntegerOption.NumType;

/**
 * Option holding repeated integer values.
 * <p>
 * 
 * @author David F. Houghton
 * 
 */
public class IntegerListOption extends CollectionOption<Number, List<Number>> {
	private final NumType it;

	{
		value = new ArrayList<Number>();
	}

	public IntegerListOption(Object cz) {
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
				return "a list of floating point numbers";
			default:
				return "a list of integers";
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
