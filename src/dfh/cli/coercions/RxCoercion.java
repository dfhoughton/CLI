package dfh.cli.coercions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import dfh.cli.Coercion;
import dfh.cli.ValidationException;

/**
 * Coerces strings into regular expressions ({@link Pattern}).
 * <p>
 * 
 * @author David F. Houghton - Sep 15, 2012
 * 
 */
public class RxCoercion extends Coercion<Pattern> {

	/**
	 * Constant singleton instance of this coercion that can be used in lieu of
	 * {@code new RxCoercion()}.
	 */
	public static final RxCoercion C = new RxCoercion();

	@Override
	public Pattern coerce(String s) throws ValidationException {
		try {
			return Pattern.compile(s);
		} catch (PatternSyntaxException e) {
			throw new ValidationException(e.getMessage());
		}
	}

	@Override
	public String argName() {
		return "rx";
	}

	@Override
	public Collection<String> constraintDescriptions() {
		Collection<String> list = new ArrayList<String>(1);
		list.add("value must parse as regex");
		return list;
	}
}
