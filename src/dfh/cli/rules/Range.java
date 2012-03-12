package dfh.cli.rules;

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
 */
public class Range<K extends Number> implements ValidationRule<K> {

	private final double low;
	private final double high;
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

	/**
	 * Generates a range of the form <code>[low,high]</code>, or
	 * <code>x &gt;= low and x &lt;= high</code>
	 * 
	 * @param low
	 * @param high
	 * @return a range of the form [low,high]
	 */
	public static <T extends Number> Range<T> incl(T low, T high) {
		return new Range<T>(low, high, true, true);
	}

	/**
	 * Generates a range of the form <code>[low,high)</code>, or
	 * <code>x &gt;= low and x &lt; high</code>
	 * 
	 * @param low
	 * @param high
	 * @return a range of the form [low,high)
	 */
	public static <T extends Number> Range<T> lowIncl(T low, T high) {
		return new Range<T>(low, high, true, false);
	}

	/**
	 * Generates a range of the form <code>(low,high]</code>, or
	 * <code>x &gt; low and x &lt;= high</code>
	 * 
	 * @param low
	 * @param high
	 * @return a range of the form (low,high]
	 */
	public static <T extends Number> Range<T> highIncl(T low, T high) {
		return new Range<T>(low, high, false, true);
	}

	/**
	 * Generates a range of the form <code>(low,high)</code>, or
	 * <code>x &gt; low and x &lt; high</code>
	 * 
	 * @param low
	 * @param high
	 * @return a range of the form (low,high)
	 */
	public static <T extends Number> Range<T> excl(T low, T high) {
		return new Range<T>(low, high, false, false);
	}

	/**
	 * Generates a range of the form <code>(0,&infin;]</code>, or
	 * <code>x &gt; low and x &lt;= &infin;</code>
	 * 
	 * @return a range of the form (0,&infin;]
	 */
	public static Range<? extends Number> positive() {
		return highIncl(0, Double.POSITIVE_INFINITY);
	}

	/**
	 * Generates a range of the form <code>(low,&infin;]</code>, or
	 * <code>x &gt; low and x &lt;= &infin;</code>
	 * 
	 * @param low
	 * @return a range of the form (low,&infin;]
	 */
	public static Range<Double> greater(double low) {
		return highIncl(low, Double.POSITIVE_INFINITY);
	}

	/**
	 * Generates a range of the form <code>[low,&infin;]</code>, or
	 * <code>x &gt;= low and x &lt;= &infin;</code>
	 * 
	 * @param low
	 * @return a range of the form [low,&infin;]
	 */
	public static Range<Double> greaterOrEq(double low) {
		return incl(low, Double.POSITIVE_INFINITY);
	}

	/**
	 * Generates a range of the form <code>[-&infin;,0)</code>, or
	 * <code>x &gt;= -&infin; and x &lt; 0</code>
	 * 
	 * @return a range of the form [-&infin;,0)
	 */
	public static Range<? extends Number> negative() {
		return lowIncl(Double.NEGATIVE_INFINITY, 0);
	}

	/**
	 * Generates a range of the form <code>[-&infin;,high)</code>, or
	 * <code>x &gt;= low and x &lt; high</code>
	 * 
	 * @param high
	 * @return a range of the form [-&infin;,high)
	 */
	public static Range<Double> less(double high) {
		return lowIncl(Double.NEGATIVE_INFINITY, high);
	}

	/**
	 * Generates a range of the form <code>[-&infin;,high]</code>, or
	 * <code>x &gt;= -&infin; and x &lt;= high</code>
	 * 
	 * @param high
	 * @return a range of the form [-&infin;,high]
	 */
	public static Range<Double> lessOrEq(double high) {
		return incl(Double.NEGATIVE_INFINITY, high);
	}

	/**
	 * Generates a range of the form <code>[0,&infin;]</code>, or
	 * <code>x &gt;= 0 and x &lt;= &infin;</code>
	 * 
	 * @return a range of the form [0,&infin;]
	 */
	public static Range<? extends Number> nonNegative() {
		return incl(0, Double.POSITIVE_INFINITY);
	}

	/**
	 * Generates a range of the form <code>[-&infin;,0]</code>, or
	 * <code>x &gt;= -&infin; and x &lt;= 0</code>
	 * 
	 * @return a range of the form [-&infin;,0]
	 */
	public static Range<? extends Number> nonPositive() {
		return incl(Double.NEGATIVE_INFINITY, 0);
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
