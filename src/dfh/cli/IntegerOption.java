package dfh.cli;

public class IntegerOption extends Option<Integer> {

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
				value = new Integer(stored);
			} catch (NumberFormatException e) {
				throw new ValidationException("--" + name
						+ " expects an integer argument; received " + stored);
			}
		}
	}

}
