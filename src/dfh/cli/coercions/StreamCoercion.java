package dfh.cli.coercions;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import dfh.cli.Coercion;
import dfh.cli.ValidationException;

/**
 * Coerces a file name into a {@link PrintStream}.
 * <p>
 * 
 * @author David F. Houghton - Sep 15, 2012
 * 
 */
public class StreamCoercion extends Coercion<PrintStream> {

	@Override
	public PrintStream coerce(String s) throws ValidationException {
		try {
			return new PrintStream(s);
		} catch (FileNotFoundException e) {
			throw new ValidationException("could not create print stream from "
					+ s + ": " + e.getMessage());
		}
	}

	@Override
	public String argName() {
		return "ps";
	}
}
