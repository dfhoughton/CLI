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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * The base class for the various long or short flags one can put on the command
 * line. An {@link Option} handles its own validation, defaults, parsing, and so
 * forth.
 * <p>
 * <b>Creation date:</b> Nov 10, 2011
 * 
 * @author David Houghton
 * 
 * @param <K>
 *            the value associated with the option
 */
abstract class Option<K> implements Serializable {
	private static final long serialVersionUID = 1L;
	protected boolean found = false;
	protected Set<String> longNames = new TreeSet<String>();
	protected Set<Character> shortNames = new TreeSet<Character>();
	protected List<ValidationRule<K>> validationRules = new ArrayList<ValidationRule<K>>();
	protected String name;
	protected String stored;

	protected K value;
	protected Object def;
	protected String argDescription = "val";
	protected String optionDescription;
	protected String description;
	protected boolean hasArgument = true;
	protected boolean required = false;
	protected boolean brief = false;
	/**
	 * Description of constraints native to the option itself, not added by
	 * validation rules.
	 */
	protected List<String> nativeConstraints = new LinkedList<String>();

	boolean isRequired() {
		return required;
	}

	void setRequired(boolean required) {
		this.required = required;
	}

	void setArgDescription(String argDescription) {
		this.argDescription = argDescription;
	}

	void setHasArgument(boolean hasArgument) {
		this.hasArgument = hasArgument;
	}

	@SuppressWarnings("unchecked")
	K value() {
		if (value == null)
			return (K) def;
		return value;
	}

	void addName(Object n) throws ValidationException {
		if (n instanceof String) {
			String s = (String) n;
			if (s.length() == 1)
				shortNames.add(s.charAt(0));
			else
				longNames.add(s);
		} else if (n instanceof Character) {
			shortNames.add((Character) n);
		} else {
			throw new ValidationException("option NAME " + n
					+ " neither string nor character");
		}
		if (name == null)
			name = n.toString();
	}

	void store(String stored) {
		this.stored = stored;
	}

	/**
	 * Mark command as present on the command line. This facilitates discovering
	 * missing a required argument.
	 */
	void mark() {
		this.found = true;
	}

	/**
	 * @return whether the command was on the command line
	 */
	boolean found() {
		return found;
	}

	String optionDescription() {
		if (optionDescription == null) {
			StringBuilder b = new StringBuilder();
			for (String s : longNames) {
				if (b.length() > 0)
					b.append(' ');
				b.append("--").append(s);
			}
			for (Character c : shortNames) {
				if (b.length() > 0)
					b.append(' ');
				b.append('-').append(c);
			}
			optionDescription = b.toString();
		}
		return optionDescription;
	}

	String argDescription() {
		if (hasArgument()) {
			return '<' + argDescription + '>';
		}
		return "";
	}

	boolean hasArgument() {
		return hasArgument;
	}

	abstract String description();

	abstract void validate() throws ValidationException;

	void terminalValidation() throws ValidationException {
		if (value() != null) {
			for (ValidationRule<K> v : validationRules) {
				v.test(value());
			}
		}
	}

	@SuppressWarnings("unchecked")
	K getDefault() {
		return (K) def;
	}

	@SuppressWarnings("unchecked")
	void setDefault(Object def) throws ValidationException {
		try {
			this.def = (K) def;
		} catch (ClassCastException e) {
			throw new ValidationException(
					"wrong type for default value of option --" + name);
		}
	}

	@SuppressWarnings("unchecked")
	void addValidationRule(ValidationRule<?> v) throws ValidationException {
		try {
			validationRules.add((ValidationRule<K>) v);
		} catch (ClassCastException e) {
			throw new ValidationException("validation rule for --" + name
					+ " is invalid for value class");
		}
	}

	@SuppressWarnings("unchecked")
	void assignDefault() {
		value = (K) def;
	}

	void setDescription(String string) {
		this.description = string;
	}

	boolean isBrief() {
		return brief;
	}

	void setBrief(boolean brief) {
		this.brief = brief;
	}

	/**
	 * @return string representation of the default value
	 */
	String describeDefault() {
		return def.toString();
	}
}
