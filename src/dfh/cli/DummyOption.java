/*
 * dfh.cli -- a command line argument parsing library for Java
 * 
 * Copyright (C) 2012 David F. Houghton
 * 
 * This software is licensed under the LGPL. Please see accompanying NOTICE file
 * and lgpl.txt.
 */
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
class DummyOption extends Option<String> {
	private static final long serialVersionUID = 1L;

	DummyOption() {
		stored = "";
	}

	DummyOption(String s) {
		stored = s;
	}

	@Override
	String description() {
		return stored;
	}

	@Override
	void validate() throws ValidationException {
	}

}
