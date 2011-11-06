package dfh.cli;

public class StringOption extends Option<String> {

	@Override
	public String description() {
		return "string option";
	}

	@Override
	public void validate() throws ValidationException {
		if (stored != null) {
			value = stored;
		}
	}
}
