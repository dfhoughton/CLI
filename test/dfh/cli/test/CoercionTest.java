package dfh.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Pattern;

import org.junit.Test;

import dfh.cli.Cli;
import dfh.cli.Cli.Res;
import dfh.cli.Coercion;
import dfh.cli.coercions.DateCoercion;
import dfh.cli.coercions.FileCoercion;
import dfh.cli.coercions.StreamCoercion;

public class CoercionTest {
	class RomanNumerals extends Coercion<Integer> {

		@Override
		public Integer coerce(String s) {
			if ("I".equals(s))
				return 1;
			if ("II".equals(s))
				return 2;
			if ("III".equals(s))
				return 3;
			if ("IV".equals(s))
				return 4;
			if ("V".equals(s))
				return 5;
			if ("VI".equals(s))
				return 6;
			if ("VII".equals(s))
				return 7;
			if ("VIII".equals(s))
				return 8;
			if ("IX".equals(s))
				return 9;
			if ("X".equals(s))
				return 10;
			return null;
		}

	}

	@Test
	public void testSimple() {
		Object[][][] spec = { { { "foo", new RomanNumerals() } } };
		Cli cli = new Cli(spec);
		cli.parse("--foo", "X");
		assertTrue(cli.object("foo").equals(10));
	}

	@Test
	public void default1() {
		Object[][][] spec = { { { "foo", new RomanNumerals(), 1 } } };
		Cli cli = new Cli(spec);
		cli.parse();
		assertTrue(cli.object("foo").equals(1));
	}

	@Test
	public void default2() {
		Object[][][] spec = { { { "foo", new RomanNumerals(), "I" } } };
		Cli cli = new Cli(spec);
		cli.parse();
		assertEquals(1, cli.object("foo"));
	}

	@Test
	public void unSet() {
		Object[][][] spec = { { { "foo", new RomanNumerals() } } };
		Cli cli = new Cli(spec);
		cli.parse();
		assertEquals(null, cli.object("foo"));
	}

	@Test
	public void testList() {
		Object[][][] spec = { { { "foo", new RomanNumerals() }, {},
				{ Cli.Res.REPEATABLE } } };
		Cli cli = new Cli(spec);
		cli.parse("--foo", "X", "--foo", "I");
		assertTrue(cli.collection("foo").contains(10));
		assertTrue(cli.collection("foo").contains(1));
		assertEquals(2, cli.collection("foo").size());
	}

	@Test
	public void testSet() {
		Object[][][] spec = { { { "foo", new RomanNumerals() }, {},
				{ Cli.Res.SET } } };
		Cli cli = new Cli(spec);
		cli.parse("--foo", "X", "--foo", "I", "--foo", "I");
		assertTrue(cli.collection("foo").contains(10));
		assertTrue(cli.collection("foo").contains(1));
		assertEquals(2, cli.collection("foo").size());
	}

	@Test
	public void testDate() {
		Object[][][] spec = { { { "foo", new DateCoercion() } }, };
		Cli cli = new Cli(spec, Cli.Mod.THROW_EXCEPTION);
		cli.parse("--foo=20120915");
		Date d = (Date) cli.object("foo");
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		assertEquals(2012, c.get(Calendar.YEAR));
		assertEquals(8, c.get(Calendar.MONTH));
		assertEquals(15, c.get(Calendar.DATE));
	}

	@Test
	public void testDate2() {
		Object[][][] spec = { { { "foo", new DateCoercion() }, {},
				{ Res.REPEATABLE } }, };
		Cli cli = new Cli(spec, Cli.Mod.THROW_EXCEPTION);
		cli.parse("--foo=20120915", "--foo=20120101");
		@SuppressWarnings("unchecked")
		Collection<Date> dates = (Collection<Date>) cli.collection("foo");
		assertEquals(2, dates.size());
	}

	@Test
	public void testDate3() {
		Object[][][] spec = { { { "foo", new DateCoercion() }, {}, { Res.SET } }, };
		Cli cli = new Cli(spec, Cli.Mod.THROW_EXCEPTION);
		cli.parse("--foo=20120915", "--foo=20120101", "--foo=20120101");
		@SuppressWarnings("unchecked")
		Collection<Date> dates = (Collection<Date>) cli.collection("foo");
		assertEquals(2, dates.size());
	}

	@Test
	public void testDate4() {
		Object[][][] spec = { { { "foo", new DateCoercion("yyyyMMdd", "yy") } }, };
		Cli cli = new Cli(spec, Cli.Mod.THROW_EXCEPTION);
		cli.parse("--foo=12");
		Date d = (Date) cli.object("foo");
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		assertEquals(2012, c.get(Calendar.YEAR));
		assertEquals(0, c.get(Calendar.MONTH));
		assertEquals(1, c.get(Calendar.DATE));
	}

	@Test
	public void testDate5() {
		try {
			Object[][][] spec = { { { "foo", new DateCoercion() } }, };
			Cli cli = new Cli(spec, Cli.Mod.THROW_EXCEPTION);
			cli.parse("--foo=20120915a");
			System.err.println(cli.object("foo"));
			fail("should have thrown exception: date string too long");
		} catch (Exception e) {
			assertTrue(Pattern
					.compile(
							"cannot\\s++parse\\s++'20120915a'\\s++as\\s++a\\s++date")
					.matcher(e.getMessage()).find());
		}
	}

	@Test
	public void testDateHelp1() {
		try {
			Object[][][] spec = { { { "foo", new DateCoercion() } }, };
			Cli cli = new Cli(spec, Cli.Mod.THROW_EXCEPTION);
			cli.parse("--help");
			fail("should have thrown an exception");
		} catch (Exception e) {
			assertTrue(Pattern
					.compile(
							"<date>\\s++java\\.util\\.Date;\\s++date\\s++string\\s++must\\s++be\\s++parsable\\s++as\\s++'yyyyMMdd'")
					.matcher(e.getMessage()).find());
		}
	}

	@Test
	public void testDateHelp2() {
		try {
			Object[][][] spec = { { { "foo", new DateCoercion("yyyyMMdd", "yy") } }, };
			Cli cli = new Cli(spec, Cli.Mod.THROW_EXCEPTION);
			cli.parse("--help");
			fail("should have thrown an exception");
		} catch (Exception e) {
			assertTrue(Pattern
					.compile(
							"date\\s++string\\s++must\\s++be\\s++parsable\\s++by\\s++one\\s++of\\s++\\{'yyyyMMdd',\\s++'yy'}")
					.matcher(e.getMessage()).find());
		}
	}

	@Test
	public void file() {
		Object[][][] spec = { { { "foo", new FileCoercion() } } };
		Cli cli = new Cli(spec, Cli.Mod.THROW_EXCEPTION);
		cli.parse();
		assertEquals(null, cli.object("foo"));
	}

	@Test
	public void file2() {
		try {
			File f = File.createTempFile("throw_me_away", null);
			try {
				Object[][][] spec = { { { "foo", new FileCoercion() } } };
				Cli cli = new Cli(spec, Cli.Mod.THROW_EXCEPTION);
				cli.parse("--foo", f.getAbsolutePath());
				assertEquals(f, cli.object("foo"));
			} finally {
				f.delete();
			}
		} catch (IOException e) {
			fail("threw exception: " + e);
		}
	}

	@Test
	public void describeDefault1() {
		try {
			Object[][][] spec = { { { "foo", StreamCoercion.C, System.err } } };
			Cli cli = new Cli(spec, Cli.Mod.THROW_EXCEPTION);
			cli.parse("--help");
			fail("should have throw an exception");
		} catch (Exception e) {
			assertTrue(e.getMessage().indexOf("STDERR") > -1);
		}
	}

	@Test
	public void describeDefault2() throws IOException {
		String f = File.createTempFile("foo", null).getName();
		try {
			Object[][][] spec = { { { "foo", StreamCoercion.C, f } } };
			Cli cli = new Cli(spec, Cli.Mod.THROW_EXCEPTION);
			cli.parse("--help");
			fail("should have throw an exception");
		} catch (Exception e) {
			assertTrue(e.getMessage().indexOf(f) > -1);
			new File(f).delete();
		}
	}
}
