package dfh.cli;

import java.util.LinkedHashSet;
import java.util.Set;

public class CoercedSet<K> extends CollectionOption<K, Set<K>> {

	protected final Coercion<K> c;
	{
		value = new LinkedHashSet<K>();
	}

	public CoercedSet(Coercion<K> c) {
		this.c = c;
		argDescription = c.argName();
		nativeConstraints.addAll(c.constraintDescriptions());
	}

	@Override
	public String description() {
		if (description == null)
			return "a set of " + c.type();
		return description;
	}

	@Override
	public void validate() throws ValidationException {
		for (String stored : storageList) {
			value.add(c.coerce(stored));
		}
	}

}
