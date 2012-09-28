/*
 * dfh.cli -- a command line argument parsing library for Java
 * 
 * Copyright (C) 2012 David F. Houghton
 * 
 * This software is licensed under the LGPL. Please see accompanying NOTICE file
 * and lgpl.txt.
 */
package dfh.cli;

public class CoercedOption<K> extends Option<K> {
	private static final long serialVersionUID = 1L;
	private Coercion<K> c;

	CoercedOption(Coercion<K> c) {
		this.c = c;
		argDescription = c.argName();
		nativeConstraints.addAll(c.constraintDescriptions());
	}

	@Override
	public String description() {
		if (description == null)
			return c.type();
		return description;
	}

	@Override
	public void validate() throws ValidationException {
		if (stored != null)
			value = c.coerce(stored);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setDefault(Object def) throws ValidationException {
		if (def instanceof String)
			this.def = c.coerce(def.toString());
		else
			this.def = (K) def;
	}

	@Override
	protected String describeDefault() {
		return c.describeDefault(def);
	}
}
