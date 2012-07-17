/*
 * dfh.cli -- a command line argument parsing library for Java
 * 
 * Copyright (C) 2012 David F. Houghton
 * 
 * This software is licensed under the LGPL. Please see accompanying NOTICE file
 * and lgpl.txt.
 */
package dfh.cli;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Base class for repeatable options.
 * <p>
 * 
 * @author David F. Houghton
 * 
 * @param <K>
 *            the sort of value repeated
 */
public abstract class CollectionOption<K, C extends Collection<K>> extends
		Option<C> {
	protected List<String> storageList = new LinkedList<String>();
	protected List<ValidationRule<K>> itemValidationRules = new ArrayList<ValidationRule<K>>();

	@Override
	public void store(String s) {
		storageList.add(s);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addValidationRule(ValidationRule<?> v)
			throws ValidationException {
		Method m = getTestMethod(v);
		Class<?> cz = m.getParameterTypes()[0];
		try {
			if (Collection.class.isAssignableFrom(cz)) {
				validationRules.add((ValidationRule<C>) v);
			} else {
				itemValidationRules.add((ValidationRule<K>) v);
			}
		} catch (Exception e) {
			throw new ValidationException("validation rule for --" + name
					+ " is invalid for value class");
		}
	}

	private Method getTestMethod(ValidationRule<?> v) {
		for (Method m : v.getClass().getMethods()) {
			if (m.getName().equals("test"))
				return m;
		}
		return null;
	}

	@Override
	void terminalValidation() throws ValidationException {
		if (!value().isEmpty()) {
			for (K k : value()) {
				for (ValidationRule<K> v : itemValidationRules) {
					v.test(k);
				}
			}
			for (ValidationRule<C> v : validationRules)
				v.test(value());
		}
	}

}
