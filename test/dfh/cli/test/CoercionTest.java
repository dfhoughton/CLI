package dfh.cli.test;

import static org.junit.Assert.*;

import org.junit.Test;

import dfh.cli.Cli;
import dfh.cli.Coercion;

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
		assertTrue(cli.object("foo").equals(1));
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
}
