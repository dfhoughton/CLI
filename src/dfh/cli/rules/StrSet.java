package dfh.cli.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dfh.cli.ValidationException;
import dfh.cli.ValidationRule;

/**
 * A validation rule that checks a string option against a set of valid values.
 * 
 * @author David Houghton
 */
public class StrSet implements ValidationRule<String> {

	private final Set<String> set;

	public StrSet(String... strings) {
		set = new HashSet<String>(strings.length * 2);
		for (String s : strings)
			set.add(s);
	}

	@Override
	public void test(String arg) throws ValidationException {
		if (!set.contains(arg)) {
			List<String> list = new ArrayList<String>(set);
			Collections.sort(list);
			StringBuilder b = new StringBuilder();
			b.append('"').append(arg).append('"');
			b.append(" not in set {");
			boolean initial = false;
			for (String s : list) {
				if (initial)
					initial = false;
				else
					b.append(", ");
				b.append('"').append(s).append('"');
			}
			b.append('}');
			throw new ValidationException(b.toString());
		}

	}

}
