package dfh.cli;

/**
 * Constants that can be used to control the parsing of command options.
 * <p>
 * <b>Creation date:</b> Nov 7, 2011
 * 
 * @author David Houghton
 * 
 */
public enum Modifiers {
	/**
	 * Auto-generate help text. See {@link Cli#PREFERRED_HELP_FLAGS} and
	 * {@link Cli#AUXILIARY_HELP_FLAGS}. If none of these is available, a
	 * {@link ValidationException} is thrown.
	 */
	HELP,
	/**
	 * Auto-generate version command. See {@link Cli#PREFERRED_VERSION_FLAGS}.
	 * If none of these is available, a {@link ValidationException} is thrown.
	 */
	VERSION,
	/**
	 * Throw a {@link RuntimeException} when option parsing or validation fails
	 * rather than calling {@link System#exit(int)}.
	 */
	THROW_EXCEPTION
}
