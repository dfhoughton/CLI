package dfh.cli;

import java.util.ArrayList;
import java.util.List;

public class StringListOption extends CollectionOption<List<String>> {
	{
		value = new ArrayList<String>();
	}

	@Override
	public String description() {
		return "a list of strings";
	}

	@Override
	public void validate() throws ValidationException {
		for (String s : storageList)
			value.add(s);
	}

}
