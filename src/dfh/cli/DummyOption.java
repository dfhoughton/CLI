package dfh.cli;

/**
 * Holds a line of text to be inserted among the command descriptions in the
 * usage information.
 * <p>
 * <b>Creation date:</b> Nov 13, 2011
 * 
 * @author David Houghton
 * 
 */
public class DummyOption extends Option<String> {

	public DummyOption(String string) {
		stored = string;
	}

	@Override
	public String description() {
		return stored;
	}

	@Override
	public void validate() throws ValidationException {
	}

}
