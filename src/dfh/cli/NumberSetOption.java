package dfh.cli;

import java.util.LinkedHashSet;
import java.util.Set;

public class NumberSetOption extends CollectionOption<Set<Double>> {
	{
		value = new LinkedHashSet<Double>();
	}

	@Override
	public String description() {
		return "a set of floating point numbers";
	}

	@Override
	public void validate() throws ValidationException {
		for (String s : storageList) {
			try {
				value.add(new Double(s));
			} catch (NumberFormatException e) {
				throw new ValidationException("--" + name
						+ " expects floating point arguments; received " + s);
			}
		}
	}

}
