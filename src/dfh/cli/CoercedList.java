/*
 * dfh.cli -- a command line argument parsing library for Java
 * 
 * Copyright (C) 2012 David F. Houghton
 * 
 * This software is licensed under the LGPL. Please see accompanying NOTICE file
 * and lgpl.txt.
 */
package dfh.cli;

public class CoercedList<K> extends ListOption<K> {

	protected final Coercion<K> c;

	public CoercedList(Coercion<K> c) {
		this.c = c;
		argDescription = c.argName();
		nativeConstraints.addAll(c.constraintDescriptions());
	}

	@Override
	protected K handle(String s) throws ValidationException {
		return c.coerce(s);
	}

	@Override
	protected String type() {
		return c.type();
	}

}
