package dfh.cli;

import java.util.ArrayList;
import java.util.List;

public class StringListOption extends CollectionOption<List<String>> {
	{
		value = new ArrayList<String>();
	}

	@Override
	public String description() {
		if (description == null)
			return "a list of strings";
		return description;
	}

	@Override
	public void validate() throws ValidationException {
		for (String s : storageList)
			value.add(s);
	}

}
