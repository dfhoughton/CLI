/*
 * dfh.cli -- a command line argument parsing library for Java
 * 
 * Copyright (C) 2012 David F. Houghton
 * 
 * This software is licensed under the LGPL. Please see accompanying NOTICE file
 * and lgpl.txt.
 */
package dfh.cli.coercions;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import dfh.cli.Coercion;
import dfh.cli.ValidationException;

/**
 * Coerces a file name into a {@link PrintStream}.
 * <p>
 * 
 * @author David F. Houghton - Sep 15, 2012
 * 
 */
public class StreamCoercion extends Coercion<PrintStream> implements
		Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * Maximum number of mappings from print streams to strings they were
	 * coerced from that a {@link StreamCoercion} will retain. These mappings
	 * are retained to provide more useful descriptions of default values in
	 * usage information. Since {@link #C} may be reused in many options, each
	 * of which may have a default, this cache is allocated more space than one
	 * might naively expect. It will not grow beyond this size, however.
	 */
	public static int CACHE_SIZE = 100;
	private Map<Object, String> defaultMap = new HashMap<Object, String>(
			(int) (CACHE_SIZE * 1.75));
	/**
	 * Constant singleton instance of this coercion that can be used in lieu of
	 * {@code new StreamCoercion()}.
	 */
	public static final StreamCoercion C = new StreamCoercion();

	@Override
	public PrintStream coerce(String s) throws ValidationException {
		try {
			PrintStream ps = new PrintStream(s);
			if (defaultMap.size() < CACHE_SIZE)
				defaultMap.put(ps, s);
			return ps;
		} catch (FileNotFoundException e) {
			throw new ValidationException("could not create print stream from "
					+ s + ": " + e.getMessage());
		}
	}

	@Override
	public String argName() {
		return "ps";
	}

	@Override
	public String type() {
		return PrintStream.class.getCanonicalName();
	}

	@Override
	protected String describeDefault(Object def) {
		if (def == System.err)
			return "STDERR";
		if (def == System.out)
			return "STDOUT";
		String s = defaultMap.get(def);
		if (s == null)
			return def.toString();
		return s;
	}
}
