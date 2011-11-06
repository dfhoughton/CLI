package dfh.cli;

public class IntegerOption extends Option<Integer> {

	@Override
	public String description() {
		return "whole number option";
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
