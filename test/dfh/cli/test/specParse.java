package dfh.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import dfh.cli.Cli;
import dfh.cli.Cli.Opt;
import dfh.cli.Modifiers;
import dfh.cli.ValidationException;

public class specParse {
	@Test
	public void minimal() {
		try {
			Object[][][] spec = { { { "foo" } } };
			new Cli(spec);
		} catch (Exception e) {
			fail("could not parse minimal spec");
		}
	}

	@Test
	public void minimalValuePresent() {
		try {
			Object[][][] spec = { { { "foo" } } };
			Cli cli = new Cli(spec);
			cli.parse(new String[] { "--foo" });
			assertTrue(cli.bool("foo"));
		} catch (Exception e) {
			fail("could not parse minimal spec");
		}
	}

	@Test
	public void minimalValueAbsent() {
		try {
			Object[][][] spec = { { { "foo" } } };
			Cli cli = new Cli(spec);
			cli.parse(new String[] {});
			assertFalse(cli.bool("foo"));
		} catch (Exception e) {
			fail("could not parse minimal spec");
		}
	}

	@Test
	public void minimalValuePresentDefault() {
		try {
			Object[][][] spec = { { { "foo", Boolean.class, true } } };
			Cli cli = new Cli(spec);
			cli.parse(new String[] { "--foo" });
			assertFalse(cli.bool("foo"));
		} catch (Exception e) {
			fail("could not parse minimal spec");
		}
	}

	@Test
	public void minimalValueAbsentDefault() {
		try {
			Object[][][] spec = { { { "foo", Boolean.class, true } } };
			Cli cli = new Cli(spec);
			cli.parse(new String[] {});
			assertTrue(cli.bool("foo"));
		} catch (Exception e) {
			fail("could not parse minimal spec");
		}
	}

	@Test
	public void stringAbsent() {
		try {
			Object[][][] spec = { { { "foo", String.class } } };
			Cli cli = new Cli(spec);
			cli.parse(new String[] {});
			assertNull(cli.string("foo"));
		} catch (Exception e) {
			fail("could not parse spec");
		}
	}

	@Test
	public void stringPresent() {
		try {
			Object[][][] spec = { { { "foo", String.class } } };
			Cli cli = new Cli(spec);
			cli.parse(new String[] { "--foo", "foo" });
			assertNotNull(cli.string("foo"));
		} catch (Exception e) {
			fail("could not parse spec");
		}
	}

	@Test
	public void integerError() {
		try {
			Object[][][] spec = {
					//
					{ { "foo", Integer.class, "quux" },
							{ "some nonsense", "bar" } },//
					{ { Opt.NAME, "baz" } },//
					{ { Opt.ARGS } },//
					{ { Opt.USAGE, "waste time" } },//
			};
			Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION);
			cli.parse(new String[] { "--foo", "3.3" });
			fail("did not recognize 3.3 as non-integer");
		} catch (RuntimeException e) {
			System.err.println(e.getMessage());
		}
	}
}
