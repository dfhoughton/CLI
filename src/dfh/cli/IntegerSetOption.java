/*
 * dfh.cli -- a command line argument parsing library for Java
 * 
 * Copyright (C) 2012 David F. Houghton
 * 
 * This software is licensed under the LGPL. Please see accompanying NOTICE file
 * and lgpl.txt.
 */
package dfh.cli;

import java.math.BigInteger;
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
public class IntegerSetOption extends
		CollectionOption<BigInteger, Set<BigInteger>> {
	private final NumType it;

	{
		value = new LinkedHashSet<BigInteger>();
	}

	public IntegerSetOption(Object cz) {
		if (cz.equals(Short.class))
			it = NumType.shrt;
		else if (cz.equals(Integer.class))
			it = NumType.integer;
		else if (cz.equals(Long.class))
			it = NumType.lng;
		else if (cz.equals(BigInteger.class))
			it = NumType.bigint;
		else
			throw new RuntimeException("unexpected class: " + cz);
	}

	@Override
	public String description() {
		if (description == null)
			return "a set of integers";
		return description;
	}

	@Override
	public void validate() throws ValidationException {
		for (String stored : storageList) {
			try {
				switch (it) {
				case bigint:
					value.add(new BigInteger(stored));
					break;
				case integer:
					value.add(new BigInteger(new Integer(stored).toString()));
					break;
				case lng:
					value.add(new BigInteger(new Long(stored).toString()));
					break;
				case shrt:
					value.add(new BigInteger(new Short(stored).toString()));
					break;
				default:
					throw new RuntimeException(
							"CLI is broken; unexpected integer type: " + it);
				}
			} catch (NumberFormatException e) {
				throw new ValidationException("--" + name
						+ " must be parsable as " + it.s + "; received "
						+ stored);
			}
		}
	}

}
