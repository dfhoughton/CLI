/*
 * dfh.cli -- a command line argument parsing library for Java
 * 
 * Copyright (C) 2012 David F. Houghton
 * 
 * This software is licensed under the LGPL. Please see accompanying NOTICE file
 * and lgpl.txt.
 */
package dfh.cli.rules;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import dfh.cli.ValidationException;
import dfh.cli.ValidationRule;

/**
 * Checks to see whether an integer argument is in a specified set.
 * <p>
 * 
 * @author David F. Houghton - Feb 23, 2012
 * 
 */
public class IntSet extends ValidationRule<Integer> {
	private static final long serialVersionUID = 1L;
	private final Set<Integer> set;

	public IntSet(int... ints) {
		set = new HashSet<Integer>(ints.length * 2);
		for (int i : ints)
			set.add(i);
	}

	@Override
	public String description() {
		StringBuilder b = new StringBuilder();
		b.append("value must be in {");
		boolean initial = true;
		for (Integer s : new TreeSet<Integer>(set)) {
			if (initial)
				initial = false;
			else
				b.append(", ");
			b.append(s);
		}
		b.append('}');
		return b.toString();
	}

	@Override
	public void test(Integer arg) throws ValidationException {
		if (!set.contains(arg)) {
			StringBuilder b = new StringBuilder();
			b.append(arg + " not in set {");
			boolean initial = true;
			for (int i : new TreeSet<Integer>(set)) {
				if (initial)
					initial = false;
				else
					b.append(',');
				b.append(i);
			}
			b.append('}');
			throw new ValidationException(b.toString());
		}
	}
}
