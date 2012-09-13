package dfh.cli;

import java.math.BigDecimal;
import java.math.BigInteger;

import sun.security.util.BigInt;

public class IntegerOption extends Option<Number> {

	enum NumType {
		shrt("short"), integer("int"), lng("long"), bigint(BigInt.class
				.getName()), flt("float"), dbl("double"), bigdec(
				BigDecimal.class.getName());
		final String s;

		NumType(String s) {
			this.s = s;
		}
	}

	private final NumType it;

	IntegerOption(Object cz) {
		if (cz.equals(Short.class))
			it = NumType.shrt;
		else if (cz.equals(Integer.class))
			it = NumType.integer;
		else if (cz.equals(Long.class))
			it = NumType.lng;
		else if (cz.equals(BigInteger.class))
			it = NumType.bigint;
		else if (cz.equals(Float.class))
			it = NumType.flt;
		else if (cz.equals(Double.class))
			it = NumType.dbl;
		else if (cz.equals(BigDecimal.class))
			it = NumType.bigdec;
		else
			throw new RuntimeException("unexpected class: " + cz);
	}

	@Override
	public String description() {
		if (description == null)
			return "whole number option";
		return description;
	}

	@Override
	public void validate() throws ValidationException {
		if (stored != null) {
			try {
				switch (it) {
				case bigint:
					value = new BigInteger(stored);
					break;
				case integer:
					value = new BigInteger(new Integer(stored).toString());
					break;
				case lng:
					value = new BigInteger(new Long(stored).toString());
					break;
				case shrt:
					value = new BigInteger(new Short(stored).toString());
					break;
				default:
					throw new RuntimeException(
							"CLI is broken; unexpected integer type: " + it);
				}
			} catch (NumberFormatException e) {
				throw new ValidationException("--" + name
						+ " must be parsable as " + it.s + "; received "
						+ stored);
			}
		}
	}

}
