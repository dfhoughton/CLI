package dfh.cli.rules;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import dfh.cli.ValidationException;
import dfh.cli.ValidationRule;

/**
 * Checks to see whether an integer argument is in a specified set.
 * <p>
 * 
 * @author David F. Houghton - Feb 23, 2012
 * 
 */
public class IntSet implements ValidationRule<Integer> {
	private final Set<Integer> set;

	public IntSet(int... ints) {
		set = new HashSet<Integer>(ints.length);
		for (int i : ints)
			set.add(i);
	}

	@Override
	public void test(Integer arg) throws ValidationException {
		if (!set.contains(arg)) {
			StringBuilder b = new StringBuilder();
			b.append(arg + " not in set {");
			boolean initial = true;
			for (int i : new TreeSet<Integer>(set)) {
				if (initial)
					initial = false;
				else
					b.append(',');
				b.append(i);
			}
			b.append('}');
			throw new ValidationException(b.toString());
		}
	}
}
