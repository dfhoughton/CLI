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
 * Checks to see whether an number argument is in a specified set. The
 * underlying set is represented as a set of doubles.
 * <p>
 * 
 * @author David F. Houghton - Feb 23, 2012
 * 
 */
public class NumSet extends ValidationRule<Number> {
	private static final long serialVersionUID = 1L;
	private final Set<Double> set;

	public NumSet(double... nums) {
		set = new HashSet<Double>(nums.length * 2);
		for (double i : nums)
			set.add(i);
	}

	@Override
	public String description() {
		StringBuilder b = new StringBuilder();
		b.append("value must be in {");
		boolean initial = true;
		for (Double s : new TreeSet<Double>(set)) {
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
	public void test(Number arg) throws ValidationException {
		if (!set.contains(arg.doubleValue())) {
			StringBuilder b = new StringBuilder();
			b.append(arg + " not in set {");
			boolean initial = true;
			for (double i : new TreeSet<Double>(set)) {
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
