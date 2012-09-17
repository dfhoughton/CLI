package dfh.cli;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberOption extends Option<Number> {

	/**
	 * Helps with parsing and display of numeric options.
	 * <p>
	 * 
	 * @author David F. Houghton - Sep 13, 2012
	 * 
	 */
	enum NumType {
		shrt("short", "int"), integer("int", "int"), lng("long", "int"), bigint(
				BigInteger.class.getName(), "Z"), flt("float", "fp"), dbl(
				"double", "fp"), bigdec(BigDecimal.class.getName(), "R");
		final String s, arg;

		NumType(String s, String arg) {
			this.s = s;
			this.arg = arg;
		}

		static NumType obj2type(Object cz) {
			NumType o;
			if (cz.equals(Short.class))
				o = NumType.shrt;
			else if (cz.equals(Integer.class))
				o = NumType.integer;
			else if (cz.equals(Long.class))
				o = NumType.lng;
			else if (cz.equals(BigInteger.class))
				o = NumType.bigint;
			else if (cz.equals(Float.class))
				o = NumType.flt;
			else if (cz.equals(Double.class))
				o = NumType.dbl;
			else if (cz.equals(BigDecimal.class) || cz.equals(Number.class))
				o = NumType.bigdec;
			else
				throw new RuntimeException("unexpected class: " + cz);
			return o;
		}

		static Number parse(NumType i, String s) throws ValidationException {
			Number n = null;
			if (s != null) {
				try {
					switch (i) {
					case bigint:
						n = new BigInteger(s);
						break;
					case integer:
						n = new Integer(s);
						break;
					case lng:
						n = new Long(s);
						break;
					case shrt:
						n = new Short(s);
						break;
					case flt:
						n = new Float(s);
						break;
					case dbl:
						n = new Double(s);
						break;
					case bigdec:
						n = new BigDecimal(s);
						break;
					default:
						throw new RuntimeException(
								"CLI is broken; unexpected integer type: " + i);
					}
				} catch (NumberFormatException e) {
					throw new ValidationException("must be parsable as " + i.s
							+ "; received " + s);
				}
			}
			return n;
		}

	}

	private final NumType it;

	NumberOption(Object cz) {
		it = NumType.obj2type(cz);
		argDescription = it.arg;
	}

	@Override
	public String description() {
		if (description == null) {
			switch (it) {
			case flt:
			case dbl:
			case bigdec:
				return "floating point number option";
			default:
				return "integer option";
			}
		}
		return description;
	}

	@Override
	public void validate() throws ValidationException {
		value = NumType.parse(it, stored);
	}

}
