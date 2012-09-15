package dfh.cli.coercions;

import java.io.File;

import dfh.cli.Coercion;
import dfh.cli.ValidationException;

/**
 * Coerces strings into files.
 * <p>
 * 
 * @author David F. Houghton - Sep 15, 2012
 * 
 */
public class FileCoercion extends Coercion<File> {

	@Override
	public File coerce(String s) throws ValidationException {
		return new File(s);
	}

	@Override
	public String argName() {
		return "file";
	}
}
