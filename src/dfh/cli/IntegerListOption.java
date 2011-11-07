package dfh.cli;

import java.util.ArrayList;
import java.util.List;

public class IntegerListOption extends CollectionOption<List<Integer>> {
	{
		value = new ArrayList<Integer>();
	}

	@Override
	public String description() {
		if (description == null)
			return "a list of integers";
		return description;
	}

	@Override
	public void validate() throws ValidationException {
		for (String s : storageList) {
			try {
				value.add(new Integer(s));
			} catch (NumberFormatException e) {
				throw new ValidationException("--" + name
						+ " expects integer arguments; received " + s);
			}
		}
	}

}
