/*
 * dfh.cli -- a command line argument parsing library for Java
 * 
 * Copyright (C) 2012 David F. Houghton
 * 
 * This software is licensed under the LGPL. Please see accompanying NOTICE file
 * and lgpl.txt.
 */
package dfh.cli;

import java.io.Serializable;

import dfh.cli.NumberOption.NumType;

/**
 * Option holding repeated numeric values.
 * <p>
 * 
 * @author David F. Houghton
 * 
 */
class NumberListOption extends ListOption<Number> implements Serializable {
	private static final long serialVersionUID = 1L;
	private final NumType it;

	NumberListOption(Object cz) {
		it = NumType.obj2type(cz);
		argDescription = it.arg;
	}

	@Override
	Number handle(String s) throws ValidationException {
		return NumType.parse(it, s);
	}

	@Override
	String type() {
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
