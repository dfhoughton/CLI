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

public class CoercedSet<K> extends SetOption<K> implements Serializable {
	private static final long serialVersionUID = 1L;
	protected final Coercion<K> c;

	public CoercedSet(Coercion<K> c) {
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
