/*
 * dfh.cli -- a command line argument parsing library for Java
 * 
 * Copyright (C) 2012 David F. Houghton
 * 
 * This software is licensed under the LGPL. Please see accompanying NOTICE file
 * and lgpl.txt.
 */
package dfh.cli.rules;

import java.text.DecimalFormat;

import dfh.cli.ValidationException;
import dfh.cli.ValidationRule;

/**
 * Tests whether a number falls within a given range.
 * <p>
 * <b>Creation date:</b> Nov 7, 2011
 * 
 * @author David Houghton
 * 
 * @param <K>
 *            type of number in range
 */
public class Range<K extends Number> extends ValidationRule<K> {
	private static final long serialVersionUID = 1L;
	protected final double low;
	protected final double high;
	private final String rangeName;
	private final boolean lowInc;
	private final boolean highInc;

	private Range(K low, K high, boolean lowInc, boolean highInc) {
		this.rangeName = (lowInc ? "[" : "(") + low + ", " + high
				+ (highInc ? "]" : ")");
		this.low = low.doubleValue();
		this.high = high.doubleValue();
		this.lowInc = lowInc;
		this.highInc = highInc;
	}

	@Override
	public String description() {
		return "value must be in interval " + rangeName;
	}

	/**
	 * Generates a range of the form {@code [low,high]}, or
	 * {@code x >= low and x
	 * <= high}
	 * 
	 * @param low
	 * @param high
	 * @return a range of the form [low,high]
	 */
	public static <T extends Number> Range<T> incl(T low, T high) {
		return new Range<T>(low, high, true, true);
	}

	/**
	 * Generates a range of the form {@code [low,high)}, or
	 * {@code x >= low and x < high}
	 * 
	 * @param low
	 * @param high
	 * @return a range of the form [low,high)
	 */
	public static <T extends Number> Range<T> lowIncl(T low, T high) {
		return new Range<T>(low, high, true, false);
	}

	/**
	 * Generates a range of the form {@code (low,high]}, or
	 * {@code x > low and x <= high}
	 * 
	 * @param low
	 * @param high
	 * @return a range of the form (low,high]
	 */
	public static <T extends Number> Range<T> highIncl(T low, T high) {
		return new Range<T>(low, high, false, true);
	}

	/**
	 * Generates a range of the form {@code (low,high)}, or
	 * {@code x > low and x < high}
	 * 
	 * @param low
	 * @param high
	 * @return a range of the form (low,high)
	 */
	public static <T extends Number> Range<T> excl(T low, T high) {
		return new Range<T>(low, high, false, false);
	}

	/**
	 * Generates a range of the form {@code (0,&infin;]}, or
	 * {@code x > low and x <= &infin;}
	 * 
	 * @return a range of the form (0,&infin;]
	 */
	public static Range<? extends Number> positive() {
		return new Range<Double>(0D, Double.POSITIVE_INFINITY, false, true) {
			private static final long serialVersionUID = 1L;

			@Override
			public String description() {
				return "value must be > 0";
			}
		};
	}

	/**
	 * Generates a range of the form {@code (low,&infin;]}, or
	 * {@code x > low and x <= &infin;}
	 * 
	 * @param low
	 * @return a range of the form (low,&infin;]
	 */
	public static Range<Double> greater(double low) {
		return new Range<Double>(low, Double.POSITIVE_INFINITY, false, true) {
			private static final long serialVersionUID = 1L;

			@Override
			public String description() {
				return "value must be > " + new DecimalFormat().format(low);
			}
		};
	}

	/**
	 * Generates a range of the form {@code [low,&infin;]}, or
	 * {@code x >= low and x <= &infin;}
	 * 
	 * @param low
	 * @return a range of the form [low,&infin;]
	 */
	public static Range<Double> greaterOrEq(double low) {
		return new Range<Double>(low, Double.POSITIVE_INFINITY, true, true) {
			private static final long serialVersionUID = 1L;

			@Override
			public String description() {
				return "value must be >= " + new DecimalFormat().format(low);
			}
		};
	}

	/**
	 * Generates a range of the form {@code [-&infin;,0)}, or
	 * {@code x >= -&infin; and x < 0}
	 * 
	 * @return a range of the form [-&infin;,0)
	 */
	public static Range<? extends Number> negative() {
		return new Range<Double>(Double.NEGATIVE_INFINITY, 0D, true, false) {
			private static final long serialVersionUID = 1L;

			@Override
			public String description() {
				return "value must be < 0";
			}
		};
	}

	/**
	 * Generates a range of the form {@code [-&infin;,high)}, or
	 * {@code x >= low and x < high}
	 * 
	 * @param high
	 * @return a range of the form [-&infin;,high)
	 */
	public static Range<Double> less(double high) {
		return new Range<Double>(Double.NEGATIVE_INFINITY, high, true, false) {
			private static final long serialVersionUID = 1L;

			@Override
			public String description() {
				return "value must be < " + new DecimalFormat().format(high);
			}
		};
	}

	/**
	 * Generates a range of the form {@code [-&infin;,high]}, or
	 * {@code x >= -&infin; and x <= high}
	 * 
	 * @param high
	 * @return a range of the form [-&infin;,high]
	 */
	public static Range<Double> lessOrEq(double high) {
		return new Range<Double>(Double.NEGATIVE_INFINITY, high, true, true) {
			private static final long serialVersionUID = 1L;

			@Override
			public String description() {
				return "value must be <= " + new DecimalFormat().format(high);
			}
		};
	}

	/**
	 * Generates a range of the form {@code [0,&infin;]}, or
	 * {@code x >= 0 and x <= &infin;}
	 * 
	 * @return a range of the form [0,&infin;]
	 */
	public static Range<? extends Number> nonNegative() {
		return new Range<Double>(0D, Double.POSITIVE_INFINITY, true, true) {
			private static final long serialVersionUID = 1L;

			@Override
			public String description() {
				return "value must be >= 0";
			}
		};
	}

	/**
	 * Generates a range of the form {@code [-&infin;,0]}, or
	 * {@code x >= -&infin; and x <= 0}
	 * 
	 * @return a range of the form [-&infin;,0]
	 */
	public static Range<? extends Number> nonPositive() {
		return new Range<Double>(Double.NEGATIVE_INFINITY, 0D, true, true) {
			private static final long serialVersionUID = 1L;

			@Override
			public String description() {
				return "value must be <= 0";
			}
		};
	}

	/**
	 * Generates the range {@code [0,1]}, the unit interval.
	 * 
	 * @return the range [0,1].
	 */
	public static Range<? extends Number> unit() {
		return incl(0, 1);
	}

	@Override
	public void test(K arg) throws ValidationException {
		double d = arg.doubleValue();
		boolean lowb = lowInc ? d >= low : d > low;
		boolean highb = highInc ? d <= high : d < high;
		if (!(lowb && highb))
			throw new ValidationException(arg + " outside range " + rangeName);
	}

}
