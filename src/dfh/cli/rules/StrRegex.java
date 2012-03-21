/*
 * dfh.cli -- a command line argument parsing library for Java
 * 
 * Copyright (C) 2012 David F. Houghton
 * 
 * This software is licensed under the LGPL. Please see accompanying NOTICE file
 * and lgpl.txt.
 */
package dfh.cli.rules;

import java.util.regex.Pattern;

import dfh.cli.ValidationException;
import dfh.cli.ValidationRule;

/**
 * A validation rule that checks a string option against a regular expression.
 * 
 * @author David Houghton
 */
public class StrRegex implements ValidationRule<String> {

	private final Pattern re;

	public StrRegex(String regex) {
		this(Pattern.compile(regex));
	}

	public StrRegex(Pattern re) {
		this.re = re;
		if (re == null)
			throw new RuntimeException("regex must be non-null");
	}

	@Override
	public void test(String arg) throws ValidationException {
		if (!re.matcher(arg).matches()) {
			throw new ValidationException('"' + arg + "\" does not match " + re);
		}
	}

}
