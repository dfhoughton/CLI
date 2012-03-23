/*
 * dfh.cli -- a command line argument parsing library for Java
 * 
 * Copyright (C) 2012 David F. Houghton
 * 
 * This software is licensed under the LGPL. Please see accompanying NOTICE file
 * and lgpl.txt.
 */
package dfh.cli;

import java.util.LinkedList;
import java.util.List;

/**
 * Base class for repeatable options.
 * <p>
 * 
 * @author David F. Houghton
 * 
 * @param <K>
 *            the sort of value repeated
 */
public abstract class CollectionOption<K> extends Option<K> {
	protected List<String> storageList = new LinkedList<String>();

	@Override
	public void store(String s) {
		storageList.add(s);
	}
}
