package dfh.cli;

public class CoercedOption<K> extends Option<K> {

	private Coercion<K> c;

	CoercedOption(Coercion<K> c) {
		this.c = c;
		argDescription = c.argName();
		nativeConstraints.addAll(c.constraintDescriptions());
	}

	@Override
	public String description() {
		if (description == null)
			return c.type();
		return description;
	}

	@Override
	public void validate() throws ValidationException {
		value = c.coerce(stored);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setDefault(Object def) throws ValidationException {
		if (def instanceof String)
			this.def = c.coerce(def.toString());
		else
			this.def = (K) def;
	}

}
