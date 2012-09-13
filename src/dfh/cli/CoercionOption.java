package dfh.cli;

import java.io.File;
import java.util.Date;

/**
 * Option that produces an object of a non-basic type, e.g., a {@link File} or
 * {@link Date}
 * <p>
 * 
 * @author David F. Houghton - Sep 12, 2012
 * 
 * @param <K>
 *            type of object returned by option
 */
public abstract class CoercionOption<K> extends Option<K> {

}
