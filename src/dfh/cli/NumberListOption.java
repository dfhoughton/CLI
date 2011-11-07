package dfh.cli;

import java.util.ArrayList;
import java.util.List;

public class NumberListOption extends CollectionOption<List<Double>> {
	{
		value = new ArrayList<Double>();
	}

	@Override
	public String description() {
		if (description == null)
			return "a list of floating point numbers";
		return description;
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
