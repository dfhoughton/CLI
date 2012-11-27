package dfh.cli.test;

import static org.junit.Assert.*;

import org.junit.Test;

import dfh.cli.Cli;
import dfh.cli.Cli.Mod;
import dfh.cli.rules.Range;

public class RangeTest {

	@Test
	public void nonNegativeInteger() {
		Object[][][] spec = { { { "foo", Integer.class }, {},
				{ Range.nonNegative() } } };
		Cli cli = new Cli(spec, Mod.THROW_EXCEPTION);
		cli.parse("--foo=1");
	}

	@Test
	public void nonNegativeDouble() {
		Object[][][] spec = { { { "foo", Double.class }, {},
				{ Range.nonNegative() } } };
		Cli cli = new Cli(spec, Mod.THROW_EXCEPTION);
		cli.parse("--foo=1");
	}

	@Test
	public void nonNegativeShort() {
		Object[][][] spec = { { { "foo", Short.class }, {},
				{ Range.nonNegative() } } };
		Cli cli = new Cli(spec, Mod.THROW_EXCEPTION);
		cli.parse("--foo=1");
	}

	@Test
	public void nonNegativeError() {
		Object[][][] spec = { { { "foo", Integer.class }, {},
				{ Range.nonNegative() } } };
		Cli cli = new Cli(spec, Mod.THROW_EXCEPTION);
		try {
			cli.parse("--foo=-1");
			fail("should have thrown an exception");
		} catch (Exception e) {

		}
	}

	@Test
	public void positiveInt() {
		Object[][][] spec = { { { "foo", Integer.class }, {},
				{ Range.positive() } } };
		Cli cli = new Cli(spec, Mod.THROW_EXCEPTION);
		cli.parse("--foo=1");
	}

	@Test
	public void positiveError() {
		Object[][][] spec = { { { "foo", Integer.class }, {},
				{ Range.positive() } } };
		Cli cli = new Cli(spec, Mod.THROW_EXCEPTION);
		try {
			cli.parse("--foo=0");
			fail("should have thrown an exception");
		} catch (Exception e) {

		}
	}

	@Test
	public void inclusive() {
		Object[][][] spec = { { { "foo", Double.class }, {},
				{ Range.incl(.5, 1) } } };
		Cli cli = new Cli(spec, Mod.THROW_EXCEPTION);
		cli.parse("--foo=.5");
	}

	@Test
	public void exclusive() {
		Object[][][] spec = { { { "foo", Double.class }, {},
				{ Range.excl(.5, 1) } } };
		Cli cli = new Cli(spec, Mod.THROW_EXCEPTION);
		try {
			cli.parse("--foo=.5");
			fail("should have thrown an exception");
		} catch (Exception e) {
		}
	}

	@Test
	public void inclusiveInt() {
		Object[][][] spec = { { { "foo", Integer.class }, {},
				{ Range.incl(-1, 1) } } };
		Cli cli = new Cli(spec, Mod.THROW_EXCEPTION);
		cli.parse("--foo=-1");
	}

	@Test
	public void exclusiveInt() {
		Object[][][] spec = { { { "foo", Integer.class }, {},
				{ Range.excl(-2, 1) } } };
		Cli cli = new Cli(spec, Mod.THROW_EXCEPTION);
		try {
			cli.parse("--foo=-2");
			fail("should have thrown an exception");
		} catch (Exception e) {
		}
	}

}
