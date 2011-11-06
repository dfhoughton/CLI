package dfh.cli;

public class BooleanOption extends Option<Boolean> {

	{
		hasArgument = true;
		argumentOptional = true;
	}

	@Override
	public String description() {
		return "a boolean option; default value is " + def;
	}

	@Override
	public void validate() throws ValidationException {
		if (stored == null) {
			if (def == null) {
				value = found;
			} else {
				value = found ? !def : def;
			}
		} else {
			try {
				value = new Boolean(stored);
			} catch (Exception e) {
				throw new ValidationException(e.getMessage());
			}
		}
	}

}
