package dfh.cli;

import java.util.LinkedHashSet;
import java.util.Set;

public class StringSetOption extends CollectionOption<Set<String>> {
	{
		value = new LinkedHashSet<String>();
	}

	@Override
	public String description() {
		if (description == null)
			return "a set of strings";
		return description;
	}

	@Override
	public void validate() throws ValidationException {
		for (String s : storageList)
			value.add(s);
	}

}
