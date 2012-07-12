/*
 * dfh.cli -- a command line argument parsing library for Java
 * 
 * Copyright (C) 2012 David F. Houghton
 * 
 * This software is licensed under the LGPL. Please see accompanying NOTICE file
 * and lgpl.txt.
 */
package dfh.cli;

/**
 * Test to be applied to particular command line values.
 * <p>
 * <b>Creation date:</b> Nov 4, 2011
 * 
 * @author David Houghton
 * 
 * @param <K>
 *            the type of object parsed out of the argument if validation
 *            succeeds
 */
public abstract class ValidationRule<K> {
	protected boolean quiet = false;

	/**
	 * Applies the validation test to a particular argument.
	 * 
	 * @param arg
	 *            command line argument
	 * @throws ValidationException
	 *             any error message generated if validation fails
	 */
	public abstract void test(K arg) throws ValidationException;

	/**
	 * Keeps rule from contributing its description to the option(s) it applies
	 * to.
	 * 
	 * @return the {@link ValidationRule} itself
	 */
	public ValidationRule<K> shh() {
		quiet = true;
		return this;
	}

	/**
	 * Returns the text this validation rule will contribute to an option
	 * description. This will be the empty string if the rule has been
	 * {@link #shh() shushed} or {@link #description()} has not be overridden.
	 * 
	 * @return the text this validation rule will contribute to an option
	 *         description
	 */
	public String describe() {
		return quiet ? "" : description();
	}

	/**
	 * Returns a description of this validation rule. Override this for
	 * particular rules to give them meaningful descriptions.
	 * 
	 * @return a description of this validation rule
	 */
	protected String description() {
		return "";
	}
}
