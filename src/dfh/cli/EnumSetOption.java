package dfh.cli;

class EnumSetOption extends SetOption<Enum<?>> {
	private static final long serialVersionUID = 1L;
	private final Class<Enum<?>> cz;

	{
		argDescription = "str";
	}

	EnumSetOption(Class<Enum<?>> cz) {
		this.cz = cz;
		try {
			addValidationRule(new EnumDescriber(cz));
		} catch (ValidationException e) {
		}
	}

	@Override
	Enum<?> handle(String s) throws ValidationException {
		for (Enum<?> o : cz.getEnumConstants()) {
			if (o.name().equals(s))
				return o;
		}
		throw new ValidationException(s + " not member of enumeration "
				+ cz.getSimpleName());
	}

	@Override
	String type() {
		return "enumerated strings";
	}

}
