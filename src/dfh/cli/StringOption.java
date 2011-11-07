package dfh.cli;

public class StringOption extends Option<String> {

	@Override
	public String description() {
		if (description == null)
			return "string option";
		return description;
	}

	@Override
	public void validate() throws ValidationException {
		if (stored != null) {
			value = stored;
		}
	}
}
