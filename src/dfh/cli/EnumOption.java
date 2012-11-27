package dfh.cli;

class EnumOption extends Option<Enum<?>> {
	private static final long serialVersionUID = 1L;
	private final Class<Enum<?>> cz;

	{
		argDescription = "str";
	}

	EnumOption(Class<Enum<?>> cz) {
		this.cz = cz;
		try {
			addValidationRule(new EnumDescriber(cz));
		} catch (ValidationException e) {
		}
	}

	@Override
	String description() {
		if (description == null)
			return "a string";
		return description;
	}

	@Override
	void validate() throws ValidationException {
		if (stored != null) {
			for (Enum<?> e : cz.getEnumConstants()) {
				if (e.name().equals(stored)) {
					value = e;
					break;
				}
			}
			if (value == null)
				throw new ValidationException("unknown value: " + stored);
		}
	}
}
