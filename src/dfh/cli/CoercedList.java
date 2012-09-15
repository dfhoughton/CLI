package dfh.cli;

import java.util.ArrayList;
import java.util.List;

public class CoercedList<K> extends CollectionOption<K, List<K>> {

	protected final Coercion<K> c;
	{
		value = new ArrayList<K>();
	}

	public CoercedList(Coercion<K> c) {
		this.c = c;
		argDescription = c.argName();
		nativeConstraints.addAll(c.constraintDescriptions());
	}

	@Override
	public String description() {
		if (description == null)
			return "a list of " + c.type();
		return description;
	}

	@Override
	public void validate() throws ValidationException {
		for (String stored : storageList) {
			value.add(c.coerce(stored));
		}
	}

}
