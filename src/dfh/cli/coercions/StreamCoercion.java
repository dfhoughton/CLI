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

	/**
	 * Constant singleton instance of this coercion that can be used in lieu of
	 * {@code new StreamCoercion()}.
	 */
	public static final StreamCoercion C = new StreamCoercion();

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

	@Override
	public String type() {
		return PrintStream.class.getCanonicalName();
	}
}
