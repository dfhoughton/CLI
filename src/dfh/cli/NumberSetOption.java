/*
 * dfh.cli -- a command line argument parsing library for Java
 * 
 * Copyright (C) 2012 David F. Houghton
 * 
 * This software is licensed under the LGPL. Please see accompanying NOTICE file
 * and lgpl.txt.
 */
package dfh.cli;

import dfh.cli.NumberOption.NumType;

/**
 * Option holding repeated, unique integer values.
 * <p>
 * 
 * @author David F. Houghton
 * 
 */
public class NumberSetOption extends SetOption<Number> {
	private final NumType it;

	public NumberSetOption(Object cz) {
		it = NumType.obj2type(cz);
		argDescription = it.arg;
	}

	@Override
	protected Number handle(String s) throws ValidationException {
		return NumType.parse(it, s);
	}

	@Override
	protected String type() {
		switch (it) {
		case flt:
		case dbl:
		case bigdec:
			return "floating point numbers";
		default:
			return "integers";
		}
	}
}
