/*
 * dfh.cli -- a command line argument parsing library for Java
 * 
 * Copyright (C) 2012 David F. Houghton
 * 
 * This software is licensed under the LGPL. Please see accompanying NOTICE file
 * and lgpl.txt.
 */
package dfh.cli.coercions;

import java.io.Serializable;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import dfh.cli.Coercion;
import dfh.cli.ValidationException;

/**
 * Coerces date strings into {@link Date} objects using a collection of
 * {@link SimpleDateFormat} date parsers. The default format is yyyyMMdd.
 * <p>
 * 
 * @author David F. Houghton - Sep 15, 2012
 * 
 */
public class DateCoercion extends Coercion<Date> implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * Constant singleton instance of this coercion that can be used in lieu of
	 * {@code new DateCoercion()}.
	 */
	public static final DateCoercion C = new DateCoercion();
	protected final SimpleDateFormat[] parser;

	/**
	 * Constructs a date coercion from a collection of format strings. A default
	 * format of yyyyMMdd is used if no formats are provided.
	 * 
	 * @param formats
	 *            date format strings
	 */
	public DateCoercion(String... formats) {
		Set<String> set = new LinkedHashSet<String>(formats.length * 2);
		for (String fmt : formats)
			set.add(fmt);
		if (set.isEmpty()) {
			String format = "yyyyMMdd";
			parser = new SimpleDateFormat[1];
			parser[0] = new SimpleDateFormat(format);
		} else {
			List<String> acceptedFormats = new ArrayList<String>(set);
			parser = new SimpleDateFormat[set.size()];
			for (int i = 0; i < parser.length; i++)
				parser[i] = new SimpleDateFormat(acceptedFormats.get(i));
		}
	}

	/**
	 * Constructor for when you already have a list of {@link SimpleDateFormat}
	 * objects on hand.
	 * 
	 * @param c
	 */
	public DateCoercion(Collection<SimpleDateFormat> c) {
		if (c.isEmpty())
			c.add(new SimpleDateFormat("yyyyMMdd"));
		parser = c.toArray(new SimpleDateFormat[c.size()]);
	}

	/**
	 * Variadic argument constructor parallel to
	 * {@link DateCoercion#DateCoercion(String...)}, but with
	 * {@link SimpleDateFormat} objects instead of strings. This factory method
	 * is a workaround for type erasure.
	 * 
	 * @param formats
	 * @return
	 */
	public static DateCoercion build(SimpleDateFormat... formats) {
		List<SimpleDateFormat> list = new ArrayList<SimpleDateFormat>(
				formats.length);
		for (SimpleDateFormat sdf : formats)
			list.add(sdf);
		return new DateCoercion(list);
	}

	@Override
	public Date coerce(String s) throws ValidationException {
		ParsePosition pp = new ParsePosition(0);
		for (SimpleDateFormat sdf : parser) {
			Date d = sdf.parse(s, pp);
			if (!(d == null || pp.getIndex() < s.length()))
				return d;
		}
		throw new ValidationException("cannot parse '" + s + "' as a date");
	}

	@Override
	public String argName() {
		return "date";
	}

	@Override
	public String type() {
		return Date.class.getCanonicalName();
	}

	@Override
	public Collection<String> constraintDescriptions() {
		Collection<String> list = new ArrayList<String>(1);
		if (parser.length == 1)
			list.add("date string must be parsable as '"
					+ parser[0].toPattern() + "'");
		else {
			StringBuilder b = new StringBuilder();
			b.append("date string must be parsable by one of {'");
			boolean initial = true;
			for (SimpleDateFormat sdf : parser) {
				if (initial)
					initial = false;
				else
					b.append("', '");
				b.append(sdf.toPattern());
			}
			b.append("'}");
			list.add(b.toString());
		}
		return list;
	}
}
