/*
 * dfh.cli -- a command line argument parsing library for Java
 * 
 * Copyright (C) 2012 David F. Houghton
 * 
 * This software is licensed under the LGPL. Please see accompanying NOTICE file
 * and lgpl.txt.
 */
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

	/**
	 * Constant singleton instance of this coercion that can be used in lieu of
	 * {@code new FileCoercion()}.
	 */
	public static final FileCoercion C = new FileCoercion();

	@Override
	public File coerce(String s) throws ValidationException {
		return new File(s);
	}

	@Override
	public String argName() {
		return "file";
	}

	@Override
	public String type() {
		return File.class.getCanonicalName();
	}
}
