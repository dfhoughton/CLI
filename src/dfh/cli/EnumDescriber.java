/*
 * dfh.cli -- a command line argument parsing library for Java
 * 
 * Copyright (C) 2012 David F. Houghton
 * 
 * This software is licensed under the LGPL. Please see accompanying NOTICE file
 * and lgpl.txt.
 */
package dfh.cli;

/**
 * Dummy validation rule that serves only to augment the description of an enum
 * option.
 * <p>
 * 
 * @author David F. Houghton - Nov 21, 2012
 * 
 */
class EnumDescriber extends ValidationRule<Enum<?>> {
	private static final long serialVersionUID = 1L;
	private Class<Enum<?>> cz;

	EnumDescriber(Class<Enum<?>> cz) {
		this.cz = cz;
	}

	@Override
	public String description() {
		StringBuilder b = new StringBuilder();
		b.append("value must be in {");
		boolean initial = true;
		for (Enum<?> e : cz.getEnumConstants()) {
			if (initial)
				initial = false;
			else
				b.append(", ");
			b.append(e);
		}
		b.append('}');
		return b.toString();
	}

	@Override
	public void test(Enum<?> arg) throws ValidationException {
	}
}
