/*
 * dfh.cli -- a command line argument parsing library for Java
 * 
 * Copyright (C) 2012 David F. Houghton
 * 
 * This software is licensed under the LGPL. Please see accompanying NOTICE file
 * and lgpl.txt.
 */
package dfh.cli;

import java.util.Collection;
import java.util.Collections;

/**
 * A factory class that coerces strings into objects of a particular type.
 * <p>
 * 
 * @author David F. Houghton - Sep 12, 2012
 * 
 * @param <K>
 *            type of object string is coerced into
 */
public abstract class Coercion<K> {
	/**
	 * Returns object coerced out of a string.
	 * 
	 * @param s
	 *            string to coerce an object out of
	 * @return object coerced out of a string
	 * @throws ValidationException
	 *             if the string cannot be coerced into an object of the
	 *             specified type
	 */
	public abstract K coerce(String s) throws ValidationException;

	/**
	 * Override to generate a more explanatory argument name.
	 * 
	 * @return
	 */
	public String argName() {
		return "val";
	}

	/**
	 * @return some description of the object type this coerces strings into
	 */
	public String type() {
		return "object";
	}

	/**
	 * Returns a list of descriptions of constraints on coerceable strings. By
	 * default this returns an empty list. Override this to provide more
	 * informative descriptions of constraints on the input to a coercion.
	 * 
	 * @return a list of descriptions of constraints on coerceable strings
	 */
	public Collection<String> constraintDescriptions() {
		return Collections.emptyList();
	}
	
	/**
	 * @param def
	 * @return stringification of a default value of this coercion
	 */
	protected String describeDefault(Object def) {
		return def.toString();
	}
}
