package dfh.cli;

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
	 */
	public abstract K coerce(String s);

	/**
	 * Returns {@link Option} that provides objects of the type this
	 * {@link Coercion} generates.
	 * 
	 * @return {@link Option} that provides objects of the type this
	 *         {@link Coercion} generates
	 */
	public abstract CoercionOption<K> option();
}
