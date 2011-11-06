package dfh.cli;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeMap;

/**
 * For parsing CLI ARGS.
 * <p>
 * <b>Creation date:</b> Nov 4, 2011
 * 
 * @author David Houghton
 * 
 */
public class Cli {
	/**
	 * If provided as the first element of a specification array, these types
	 * determine how the remainder of the array will be parsed.
	 * 
	 * @author David Houghton
	 * 
	 */
	public enum Opt {
		/**
		 * executable NAME
		 */
		NAME,
		/**
		 * the elements of this array are the names of arguments
		 */
		ARGS,
		/**
		 * if provided in the {@link #ARGS} argument list, the next String is
		 * the NAME given to any additional arguments
		 */
		STAR,
		/**
		 * At least one slurpy argument required.
		 */
		PLUS,
		/**
		 * The next string will be taken as usage text in addition to whatever
		 * is automatically generated, unless the next argument is
		 * {@link #useResource}.
		 */
		USAGE,
	};

	private List<String> argList;
	private LinkedHashMap<String, Integer> argNames = new LinkedHashMap<String, Integer>();
	private boolean isSlurpy = true;
	private TreeMap<String, Option<?>> options = new TreeMap<String, Option<?>>();
	private List<String> errors = new ArrayList<String>();
	private String name = "EXECUTABLE";
	private String fullUsage = "";
	private boolean argsSpecified = false;
	private boolean nameSpecified = false;
	private boolean usageSpecified = false;
	private boolean slurpRequired = false;
	/**
	 * Option so marked must be specified on command line.
	 */
	public static final int REQUIRED = 1;
	/**
	 * Option so marked defines a member of a list.
	 */
	public static final int REPEATABLE = 2;
	/**
	 * Option so marked defines a unique member of a collection.
	 */
	public static final int SET = 6;

	public Cli(Object[][][] spec) {
		try {
			for (Object[][] cmd : spec)
				parseSpec(cmd);
		} catch (ValidationException e) {
			errors.add(e.getMessage());
		}
		if (!errors.isEmpty())
			usage(1);
	}

	private void parseSpec(Object[][] cmd) throws ValidationException {
		if (cmd.length == 0)
			throw new ValidationException("empty specification row");
		if (cmd[0][0] instanceof Opt) {
			Opt o = (Opt) cmd[0][0];
			switch (o) {
			case NAME:
				if (nameSpecified)
					throw new ValidationException(
							"executable name specified more than once");
				nameSpecified = true;
				if (cmd[0].length > 2)
					throw new ValidationException(
							"too many arguments in NAME specification line");
				if (cmd[0].length == 1)
					throw new ValidationException("no NAME provided");
				if (!(cmd[0][1] instanceof String))
					throw new ValidationException(
							"NAME argument in specification must be a string");
				name = cmd[0][1].toString();
				break;
			case ARGS:
				if (argsSpecified)
					throw new ValidationException(
							"argument list specified more than once");
				argsSpecified = true;
				isSlurpy = false;
				for (int i = 1; i < cmd[0].length; i++) {
					Object next = cmd[i];
					if (next instanceof Opt) {
						if (next == Opt.STAR || next == Opt.PLUS) {
							if (isSlurpy)
								throw new ValidationException(
										"only specify one of " + Opt.STAR
												+ " or " + Opt.PLUS
												+ " once in argument list");
							isSlurpy = true;
							slurpRequired = next == Opt.PLUS;
						} else
							throw new ValidationException("only " + Opt.STAR
									+ " or " + Opt.PLUS
									+ " can be specified in argument list");
					} else if (next instanceof String) {
						if (isSlurpy)
							throw new ValidationException(
									"no argument names should be provided after "
											+ Opt.STAR);
						String argName = (String) next;
						if (argNames.containsKey(argName))
							throw new ValidationException(
									"duplicate argument name: " + argName);
						argNames.put(argName, argNames.size());
					}
				}
				break;
			case USAGE:
				if (usageSpecified)
					throw new ValidationException(
							"usage details specified more than once");
				usageSpecified = true;
				if (cmd[0].length == 2) {
					if (cmd[0][1] instanceof String)
						fullUsage = (String) cmd[0][1];
					else
						throw new ValidationException(
								"usage information must be provided as a String");
				} else if (cmd[1].length == 1) {
					if (cmd[1][0] instanceof String) {
						try {
							InputStream is = Cli.class.getClassLoader()
									.getResourceAsStream((String) cmd[1][0]);
							BufferedInputStream bis = new BufferedInputStream(
									is);
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							byte[] buf = new byte[1024];
							int count;
							while ((count = bis.read(buf)) > -1) {
								baos.write(buf, 0, count);
							}
							bis.close();
							fullUsage = new String(baos.toByteArray());
						} catch (Exception e) {
							throw new ValidationException(
									"unable to load usage details from resource "
											+ e);
						}
					} else
						throw new ValidationException(
								"external resource providing usage information expected to be specified as a URL string");
				} else
					throw new ValidationException(
							"only one element expected in external usage resource specification");
				break;
			}
		} else {
			Option<?> opt = optionForType(cmd[0], cmd.length == 3 ? cmd[2]
					: null);
			for (String s : opt.longNames) {
				registerName(opt, s);
			}
			for (Character ch : opt.shortNames) {
				String s = ch.toString();
				registerName(opt, s);
			}
			if (cmd.length > 1) {
				Object[] descAndName = cmd[1];
				if (descAndName.length > 0) {
					if (descAndName.length > 2)
						throw new ValidationException(
								"description and name element of spec for --"
										+ opt.name
										+ " must contain only a description and an optional argument name");
					opt.setOptionDescription(descAndName[0].toString());
					if (descAndName.length == 2)
						opt.setArgDescription(descAndName[1].toString());
				}
			}
		}
	}

	protected void registerName(Option<?> opt, String s)
			throws ValidationException {
		Option<?> old = options.get(s);
		if (!(old == null || old == opt))
			throw new ValidationException("option " + s
					+ " used for two distinct options: --" + opt.name
					+ " and --" + old.name);
		options.put(s, opt);
	}

	private Option<?> optionForType(Object[] base, Object[] restrictions)
			throws ValidationException {
		boolean isSet = false, isRepeatable = false, isRequired = false;
		if (restrictions != null) {
			for (Object o : restrictions) {
				if (o instanceof Integer) {
					int i = ((Integer) o).intValue();
					isSet = (i | SET) == SET;
					isRepeatable = (i | REPEATABLE) == REPEATABLE;
					isRequired = (i | REQUIRED) == REQUIRED;
				}
			}
		}
		Class<?> cz = Boolean.class;
		Object def = null;
		boolean findDefault = false;
		for (Object o : base) {
			if (findDefault) {
				if (def != null)
					throw new ValidationException(
							"base spec can contain only a single default value after the type specification");
				def = o;
			} else if (o instanceof Class) {
				cz = (Class<?>) o;
				findDefault = true;
			}
		}
		Option<?> opt = null;
		if (cz.equals(Boolean.class)) {
			if (isRepeatable)
				throw new ValidationException(
						"boolean options are not repeatable");
			opt = new BooleanOption();
		} else if (cz.equals(Integer.class)) {
			if (isSet)
				opt = new IntegerSetOption();
			else if (isRepeatable)
				opt = new IntegerListOption();
			else
				opt = new IntegerOption();
		} else if (cz.equals(Number.class)) {
			if (isSet)
				opt = new NumberSetOption();
			else if (isRepeatable)
				opt = new NumberListOption();
			else
				opt = new NumberOption();
		} else if (cz.equals(String.class)) {
			if (isSet)
				opt = new StringSetOption();
			else if (isRepeatable)
				opt = new StringListOption();
			else
				opt = new StringOption();
		} else {
			throw new ValidationException(
					"spec parser cannot parse arguments of type " + cz);
		}
		opt.setRequired(isRequired);
		for (Object o : base) {
			if (o instanceof String || o instanceof Character)
				opt.addName(o);
			else if (o instanceof Class)
				break;
			else {
				throw new ValidationException(
						"spec base must consist of strings or characters, optionally followed by a class, optionally followed by a default value");
			}
		}
		opt.setDefault(def);
		if (restrictions != null) {
			for (Object o : restrictions) {
				if (o == null)
					throw new ValidationException("null found in spec for --"
							+ opt.name);
				if (Integer.class.equals(o))
					continue;
				if (ValidationRule.class == o.getClass())
					opt.addValidationRule((ValidationRule<?>) o);
				else
					throw new ValidationException(
							"third element in spec for --"
									+ opt.name
									+ " must contain only integers or ValidationRule rules");
			}
		}
		return opt;
	}

	public void parse(String[] args) {
		argList = new ArrayList<String>(args.length);
		boolean endCommands = false;
		Option<?> lastCommand = null;
		for (String s : args) {
			try {
				if (endCommands)
					argList.add(s);
				else {
					if (s.startsWith("-")) {
						if (s.equals("--"))
							endCommands = true;
						else
							lastCommand = extractCommands(s);
					} else if (lastCommand == null) {
						endCommands = true;
						argList.add(s);
					} else {
						lastCommand.store(s);
						lastCommand = null;
					}
				}
			} catch (ValidationException e) {
				errors.add(e.getMessage());
			}
		}
		if (argNames.size() > 0) {
			if (argNames.size() < argList.size()) {
				if (!(isSlurpy && !slurpRequired && argNames.size() == argList
						.size() - 1)) {
					List<String> nameList = new ArrayList<String>(
							argNames.keySet());
					for (int i = argList.size(), lim = isSlurpy
							&& !slurpRequired ? argNames.size() - 1 : argNames
							.size(); i < lim; i++) {
						errors.add("argument " + nameList.get(i)
								+ " not defined");
					}
				}
			}
		}
		for (Option<?> opt : new LinkedHashSet<Option<?>>(options.values())) {
			try {
				opt.validate();
				if (opt.isRequired()) {
					if (opt.value == null) {
						if (opt.def == null)
							errors.add("--" + opt.name
									+ " is required but has not been defined");
						else
							opt.assignDefault();
					}
				}
				opt.terminalValidation();
			} catch (ValidationException e) {
				errors.add(e.getMessage());
			}
		}
		if (!errors.isEmpty())
			usage(1);
	}

	public void usage(int status) {
		System.err.println("ERRORS");
		for (String error : errors)
			System.err.printf("\t%s%n", error);
		System.err.println();
		int c1 = 0, c2 = 0;
		for (Option<?> c : options.values()) {
			String optDesc = c.optionDescription();
			String argDescription = c.argDescription();
			if (optDesc.length() > c1)
				c1 = optDesc.length();
			if (argDescription.length() > c2)
				c2 = argDescription.length();
		}
		String format = "\t%" + c1 + "s %" + c2 + "s  %s%n";
		System.err.printf("USAGE: %s", name());
		for (Option<?> c : options.values()) {
			String optDesc = c.optionDescription();
			String argDescription = c.argDescription();
			System.err.printf(format, optDesc, argDescription, c.description());
		}
		System.exit(status);
	}

	private String name() {
		return name;
	}

	private Option<?> extractCommands(String s) throws ValidationException {
		Option<?> c = null;
		if (s.startsWith("--")) {
			c = options.get(s.substring(2));
			if (c == null)
				throw new ValidationException("unknown option " + s);
			c.mark();
		} else {
			String bundle = s.substring(1);
			for (int i = 0; i < bundle.length(); i++) {
				String sc = s.substring(i, i + 1);
				c = options.get(sc);
				if (c == null)
					throw new ValidationException("unknown option -" + sc);
				c.mark();
			}
		}
		return c;
	}

	public Boolean bool(String string) {
		if (options.containsKey(string)) {
			Option<?> opt = options.get(string);
			try {
				BooleanOption bopt = (BooleanOption) opt;
				return bopt.value;
			} catch (ClassCastException e) {
				throw new RuntimeException("--" + opt.name + " not boolean");
			}
		}
		throw new RuntimeException("unknown option --" + string);
	}

	public Integer integer(String string) {
		if (options.containsKey(string)) {
			Option<?> opt = options.get(string);
			try {
				IntegerOption iopt = (IntegerOption) opt;
				return iopt.value;
			} catch (ClassCastException e) {
				throw new RuntimeException("--" + opt.name + " not integer");
			}
		}
		throw new RuntimeException("unknown option --" + string);
	}

	public Collection<Integer> integerCollection(String string) {
		if (options.containsKey(string)) {
			Option<?> opt = options.get(string);
			if (opt instanceof IntegerListOption)
				return ((IntegerListOption) opt).value();
			else if (opt instanceof IntegerSetOption)
				return ((IntegerSetOption) opt).value();
			throw new RuntimeException("--" + opt.name
					+ " not collection of integers");
		}
		throw new RuntimeException("unknown option --" + string);
	}

	public Number number(String string) {
		if (options.containsKey(string)) {
			Option<?> opt = options.get(string);
			if (opt instanceof NumberOption)
				return ((NumberOption) opt).value();
			if (opt instanceof IntegerOption)
				return ((IntegerOption) opt).value();
			throw new RuntimeException("--" + opt.name + " not number");
		}
		throw new RuntimeException("unknown option --" + string);
	}

	public Collection<? extends Number> numberCollection(String string) {
		if (options.containsKey(string)) {
			Option<?> opt = options.get(string);
			if (opt instanceof IntegerListOption)
				return ((IntegerListOption) opt).value();
			if (opt instanceof IntegerSetOption)
				return ((IntegerSetOption) opt).value();
			if (opt instanceof NumberListOption)
				return ((NumberListOption) opt).value();
			if (opt instanceof NumberSetOption)
				return ((NumberSetOption) opt).value();
			throw new RuntimeException("--" + opt.name
					+ " not number collection");
		}
		throw new RuntimeException("unknown option --" + string);
	}

	public String string(String string) {
		if (options.containsKey(string)) {
			Option<?> opt = options.get(string);
			try {
				StringOption sopt = (StringOption) opt;
				return sopt.value();
			} catch (ClassCastException e) {
				throw new RuntimeException("--" + opt.name + " not string");
			}
		}
		throw new RuntimeException("unknown option --" + string);
	}

	public Collection<String> stringCollection(String string) {
		if (options.containsKey(string)) {
			Option<?> opt = options.get(string);
			if (opt instanceof StringListOption)
				return ((StringListOption) opt).value();
			else if (opt instanceof StringSetOption)
				return ((StringSetOption) opt).value();
			throw new RuntimeException("--" + opt.name
					+ " not collection of strings");
		}
		throw new RuntimeException("unknown option --" + string);
	}

	public List<String> argList() {
		return argList;
	}

	public String argument(String name) {
		Integer i = argNames.get(name);
		if (i == null)
			throw new RuntimeException("unknown argument: " + name);
		return argList.get(i);
	}

	public List<String> slurpedArguments() {
		if (!isSlurpy)
			throw new RuntimeException("not slurpy argument list");
		if (argNames.isEmpty())
			return argList;
		int i = argNames.size() - 1;
		return argList.subList(i, argList.size());
	}
}
