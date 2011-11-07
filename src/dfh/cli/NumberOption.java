package dfh.cli;

public class NumberOption extends Option<Double> {

	@Override
	public String description() {
		if (description == null)
			return "floating point numerical option";
		return description;
	}

	@Override
	public void validate() throws ValidationException {
		if (stored != null) {
			try {
				value = new Double(stored);
			} catch (NumberFormatException e) {
				throw new ValidationException("--" + name
						+ " expects a numerical argument; received " + stored);
			}
		}
	}

}
