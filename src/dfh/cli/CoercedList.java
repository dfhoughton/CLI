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

/**
 * Option whose value is a list of objects coerced out of strings. See
 * {@link Coercion}.
 * 
 * @param <K>
 *            the type strings are coerced into
 */
class CoercedList<K> extends ListOption<K> implements Serializable {
	private static final long serialVersionUID = 1L;
	protected final Coercion<K> c;

	CoercedList(Coercion<K> c) {
		this.c = c;
		argDescription = c.argName();
		nativeConstraints.addAll(c.constraintDescriptions());
	}

	@Override
	K handle(String s) throws ValidationException {
		return c.coerce(s);
	}

	@Override
	String type() {
		return c.type();
	}

	@Override
	String describeDefault() {
		return c.describeDefault(def);
	}
}
