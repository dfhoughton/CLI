package dfh.cli;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Option holding a numeric value.
 * 
 */
class NumberOption extends Option<Number> {
	private static final long serialVersionUID = 1L;

	/**
	 * Helps with parsing and display of numeric options.
	 * <p>
	 * 
	 * @author David F. Houghton - Sep 13, 2012
	 * 
	 */
	enum NumType {
		byt("byte", "byte"), shrt("short", "int"), integer("int", "int"), lng(
				"long", "int"), bigint(BigInteger.class.getName(), "Z"), flt(
				"float", "fp"), dbl("double", "fp"), bigdec(BigDecimal.class
				.getName(), "R");
		final String s, arg;

		NumType(String s, String arg) {
			this.s = s;
			this.arg = arg;
		}

		/**
		 * Used in parsing spec. Converts the spect type to a {@link NumType}.
		 * 
		 * @param cz
		 *            spec object
		 * @return
		 */
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
			else if (cz.equals(Byte.class))
				o = NumType.byt;
			else
				throw new RuntimeException("unexpected class: " + cz);
			return o;
		}

		/**
		 * Coerces a numeric option argument into a number.
		 * 
		 * @param i
		 *            numeric type governing parsing
		 * @param s
		 *            string to be parsed into a number
		 * @return option value
		 * @throws ValidationException
		 */
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
					case byt:
						n = new Byte(s);
						break;
					default:
						throw new RuntimeException(
								"CLI is broken; unexpected numeric type: " + i);
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
	String description() {
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
	void validate() throws ValidationException {
		value = NumType.parse(it, stored);
	}

	@Override
	void setDefault(Object def) throws ValidationException {
		if ((it == NumType.bigdec || it == NumType.bigint)
				&& def instanceof String) {
			try {
				this.def = it == NumType.bigdec ? new BigDecimal(def.toString())
						: new BigInteger(def.toString());
			} catch (NumberFormatException e) {
				throw new ValidationException(def
						+ " invalid default value for --" + name + " of type "
						+ it.s);
			}
		} else
			super.setDefault(def);
	}

}
