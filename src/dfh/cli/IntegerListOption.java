package dfh.cli;

import java.util.ArrayList;
import java.util.List;

public class IntegerListOption extends CollectionOption<List<Integer>> {
	{
		value = new ArrayList<Integer>();
	}

	@Override
	public String description() {
		return "a list of integers";
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
