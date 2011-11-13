package dfh.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.regex.Pattern;

import org.junit.Test;

import dfh.cli.Cli;
import dfh.cli.Cli.Opt;
import dfh.cli.Modifiers;

public class CliTest {
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
			cli.parse("--foo");
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
			cli.parse();
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
			cli.parse("--foo");
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
			cli.parse();
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
			cli.parse();
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
			cli.parse("--foo", "foo");
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
			{ { "foo", Integer.class, "quux" } },//
			};
			Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION);
			cli.parse("--foo", "3.3");
			fail("did not recognize 3.3 as non-integer");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("errors listed", s.startsWith("ERRORS"));
			assertTrue(
					"correct error message",
					s.indexOf("--foo expects an integer argument; received 3.3") > -1);
		}
	}

	@Test
	public void insufficientArgs() {
		try {
			Object[][][] spec = {
			//
			{ { Opt.ARGS, "foo" } },//
			};
			Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION);
			cli.parse();
			fail("did not recognize absence of argument");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("errors listed", s.startsWith("ERRORS"));
			assertTrue("correct error message",
					s.indexOf("argument 1, <foo>, not defined") > -1);
		}
	}

	@Test
	public void plusArgs() {
		try {
			Object[][][] spec = {
			//
			{ { Opt.ARGS, "foo", Opt.PLUS } },//
			};
			Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION);
			cli.parse();
			fail("did not recognize absence of argument");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("errors listed", s.startsWith("ERRORS"));
			assertTrue("correct error message",
					s.indexOf("argument 1, <foo>, not defined") > -1);
		}
	}

	@Test
	public void slurpyArgs() {
		try {
			Object[][][] spec = {
			//
			{ { Opt.ARGS, "foo", Opt.STAR } },//
			};
			Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION);
			cli.parse();
			assertTrue("no error thrown when arg is slurpy", true);
		} catch (RuntimeException e) {
			fail("should not have thrown error");
			System.err.println(e.getMessage());
		}
	}

	@Test
	public void slurpyArgsPresent() {
		try {
			Object[][][] spec = {
			//
			{ { Opt.ARGS, "foo", Opt.STAR } },//
			};
			Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION);
			cli.parse("bar", "quux");
			assertTrue("no error thrown when arg is slurpy", true);
			assertTrue("correct number of slurped arguments", cli
					.slurpedArguments().size() == 2);
		} catch (RuntimeException e) {
			fail("should not have thrown error");
			System.err.println(e.getMessage());
		}
	}

	@Test
	public void doubleError() {
		try {
			Object[][][] spec = {
			//
			{ { "foo", Double.class } },//
			};
			Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION);
			cli.parse("--foo", "blort");
			fail("did not recognize 'blort' as a non-double");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("errors listed", s.startsWith("ERRORS"));
			assertTrue(
					"correct error message",
					s.indexOf("--foo expects a numerical argument; received blort") > -1);
		}
	}

	@Test
	public void autoHelp() {
		try {
			Object[][][] spec = {
			//
			{ { "foo" } },//
			};
			Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION, Modifiers.HELP);
			cli.parse("--help");
			fail("should have thrown error when help called");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("begins without error messages", s.startsWith("USAGE"));
			assertTrue("lists help option", s.indexOf("--help") > -1);
		}
	}

	@Test
	public void requiredOption() {
		try {
			Object[][][] spec = {
			//
			{ { "foo", Integer.class }, null, { Cli.REQUIRED } },//
			};
			Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION);
			cli.parse();
			fail("should have thrown when required option was absent");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("begins with error messages", s.startsWith("ERRORS"));
			assertTrue(
					"correct error",
					s.indexOf("--foo is required but has no defined value") > -1);
		}
	}

	@Test
	public void booleanRequiredOption() {
		try {
			Object[][][] spec = {
			//
			{ { "foo" }, null, { Cli.REQUIRED } },//
			};
			new Cli(spec, Modifiers.THROW_EXCEPTION);
			fail("should have thrown when boolean option was marked as required");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("begins with error messages", s.startsWith("ERRORS"));
			assertTrue(
					"correct error",
					s.indexOf("--foo is boolean and marked as required; these are incompatible") > -1);
		}
	}

	@Test
	public void stringSet() {
		Object[][][] spec = {
		//
		{ { "foo", String.class }, null, { Cli.SET } },//
		};
		Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION);
		cli.parse("--foo", "bar", "--foo", "quux");
		assertTrue("contains bar", cli.stringCollection("foo").contains("bar"));
		assertTrue("contains quux", cli.stringCollection("foo")
				.contains("quux"));
	}

	@Test
	public void stringList() {
		Object[][][] spec = {
		//
		{ { "foo", String.class }, null, { Cli.REPEATABLE } },//
		};
		Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION);
		cli.parse("--foo", "bar", "--foo", "bar");
		assertTrue("contains bar", cli.stringCollection("foo").contains("bar"));
		assertTrue("contains 2 items", cli.stringCollection("foo").size() == 2);
	}

	@Test
	public void integerSet() {
		Object[][][] spec = {
		//
		{ { "foo", Integer.class }, null, { Cli.SET } },//
		};
		Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION);
		cli.parse("--foo", "1", "--foo", "2");
		assertTrue("contains 1", cli.integerCollection("foo").contains(1));
		assertTrue("contains 2", cli.integerCollection("foo").contains(2));
	}

	@Test
	public void integerList() {
		Object[][][] spec = {
		//
		{ { "foo", Integer.class }, null, { Cli.REPEATABLE } },//
		};
		Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION);
		cli.parse("--foo", "1", "--foo", "1");
		assertTrue("contains 1", cli.integerCollection("foo").contains(1));
		assertTrue("contains 2 items", cli.integerCollection("foo").size() == 2);
	}

	@Test
	public void doubleSet() {
		Object[][][] spec = {
		//
		{ { "foo", Number.class }, null, { Cli.SET } },//
		};
		Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION);
		cli.parse("--foo", "1", "--foo", "2");
		assertTrue("contains 1", cli.numberCollection("foo").contains(1D));
		assertTrue("contains 2", cli.numberCollection("foo").contains(2D));
	}

	@Test
	public void doubleList() {
		Object[][][] spec = {
		//
		{ { "foo", Number.class }, null, { Cli.REPEATABLE } },//
		};
		Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION);
		cli.parse("--foo", "1", "--foo", "1");
		assertTrue("contains 1", cli.numberCollection("foo").contains(1D));
		assertTrue("contains 2 items", cli.numberCollection("foo").size() == 2);
	}

	@Test
	public void repeatedBoolean() {
		try {
			Object[][][] spec = {
			//
			{ { "foo" }, null, { Cli.REPEATABLE } },//
			};
			new Cli(spec, Modifiers.THROW_EXCEPTION);
			fail("should have thrown when boolean option was marked as repeatable");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("begins with error messages", s.startsWith("ERRORS"));
			assertTrue("correct error",
					s.indexOf("boolean options are not repeatable") > -1);
		}
	}

	@Test
	public void bundlingTest1() {
		Object[][][] spec = {
				//
				{ { "foo", 'f' } },//
				{ { "bar", 'b' } },//
		};
		Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION);
		cli.parse("-fb");
		assertTrue("bundled options", cli.bool("f") && cli.bool("b"));
	}

	@Test
	public void bundlingTest2() {
		Object[][][] spec = {
				//
				{ { "foo", 'f' } },//
				{ { "bar", 'b' } },//
		};
		Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION);
		cli.parse("-fb");
		assertTrue("bundled options", cli.bool("foo") && cli.bool("bar"));
	}

	@Test
	public void bundlingTest3() {
		Object[][][] spec = {
				//
				{ { "foo", 'f' } },//
				{ { "bar", 'b', String.class } },//
		};
		Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION);
		cli.parse("-fb", "quux");
		assertTrue("bundled options",
				cli.bool("foo") && "quux".equals(cli.string("bar")));
	}

	@Test
	public void argListing1() {
		try {
			Object[][][] spec = {
			//
			{ { Opt.ARGS, "foo", "bar" } },//
			};
			Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION, Modifiers.HELP);
			cli.parse("--help");
			fail("--help failed to throw exception");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("correct usage generated",
					s.startsWith("USAGE: EXECUTABLE [options] <foo> <bar>"));
		}
	}

	@Test
	public void argListing2() {
		try {
			Object[][][] spec = {
			//
			{ { Opt.ARGS, "foo", "bar", Opt.PLUS } },//
			};
			Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION, Modifiers.HELP);
			cli.parse("--help");
			fail("--help failed to throw exception");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("correct usage generated",
					s.startsWith("USAGE: EXECUTABLE [options] <foo> <bar>+"));
		}
	}

	@Test
	public void argListing3() {
		try {
			Object[][][] spec = {
			//
			{ { Opt.ARGS, "foo", "bar", Opt.STAR } },//
			};
			Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION, Modifiers.HELP);
			cli.parse("--help");
			fail("--help failed to throw exception");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("correct usage generated",
					s.startsWith("USAGE: EXECUTABLE [options] <foo> <bar>*"));
		}
	}

	@Test
	public void argListing4() {
		try {
			Object[][][] spec = {
			//
			};
			Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION, Modifiers.HELP);
			cli.parse("--help");
			fail("--help failed to throw exception");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("correct usage generated",
					s.startsWith("USAGE: EXECUTABLE [options] <arg>*"));
		}
	}

	@Test
	public void argListing5() {
		try {
			Object[][][] spec = {
			//
			{ { Opt.ARGS, Opt.PLUS } },//
			};
			Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION, Modifiers.HELP);
			cli.parse("--help");
			fail("--help failed to throw exception");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("correct usage generated",
					s.startsWith("USAGE: EXECUTABLE [options] <arg>+"));
		}
	}

	@Test
	public void argListing6() {
		try {
			Object[][][] spec = {
			//
			{ { Opt.ARGS, Opt.STAR } },//
			};
			Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION, Modifiers.HELP);
			cli.parse("--help");
			fail("--help failed to throw exception");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("correct usage generated",
					s.startsWith("USAGE: EXECUTABLE [options] <arg>*"));
		}
	}

	@Test
	public void argListing7() {
		try {
			Object[][][] spec = {
			//
			{ { Opt.ARGS } },//
			};
			Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION, Modifiers.HELP);
			cli.parse("--help");
			fail("--help failed to throw exception");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("correct usage generated",
					!s.startsWith("USAGE: EXECUTABLE [options] <arg>"));
		}
	}

	@Test
	public void makeHelp() {
		try {
			Object[][][] spec = {
			//
			{ { Opt.NAME, "foo" } },//
			};
			Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION, Modifiers.HELP);
			cli.parse("--help");
			fail("--help failed to throw exception");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("renamed executable", s.startsWith("USAGE: foo "));
		}
	}

	@Test
	public void makeAbstract() {
		try {
			Object[][][] spec = {
			//
			{ { Opt.USAGE, "foo" } },//
			};
			Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION, Modifiers.HELP);
			cli.parse("--help");
			fail("--help failed to throw exception");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			String[] parts = s.split("foo", 2);
			assertTrue("abstract between usage and opt description",
					parts.length == 2);
			assertTrue("first part is usage",
					parts[0].trim().startsWith("USAGE"));
			assertTrue("second part starts with help", parts[1].trim()
					.startsWith("--help"));
		}
	}

	@Test
	public void makeUsage() {
		try {
			Object[][][] spec = {
			//
			{ { Opt.USAGE, "foo", "bar" } },//
			};
			Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION, Modifiers.HELP);
			cli.parse("--help");
			fail("--help failed to throw exception");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("usage at end", s.trim().endsWith("bar"));
		}
	}

	@Test
	public void makeUsageFromExternal() throws IOException {
		try {
			Object[][][] spec = {
			//
			{ { Opt.USAGE, "foo" }, { "foo" } },//
			};
			Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION, Modifiers.HELP);
			cli.parse("--help");
			fail("--help failed to throw exception");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("usage at end", s.trim().endsWith("bar"));
		}
	}

	@Test
	public void textTest1() {
		try {
			Object[][][] spec = {
					//
					{ { "foo" } },//
					{ { Opt.TEXT } },//
					{ { "bar" } },//
			};
			Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION, Modifiers.HELP);
			cli.parse("--help");
			fail("--help failed to throw exception");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue(
					"added blank line",
					Pattern.compile(
							"--foo\\s++a boolean option\\s*$\\s*$\\s*--bar",
							Pattern.MULTILINE).matcher(s).find());
		}
	}

	@Test
	public void textTest2() {
		try {
			Object[][][] spec = {
					//
					{ { "foo" } },//
					{ { Opt.TEXT, "quux" } },//
					{ { "bar" } },//
			};
			Cli cli = new Cli(spec, Modifiers.THROW_EXCEPTION, Modifiers.HELP);
			cli.parse("--help");
			fail("--help failed to throw exception");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue(
					"added quux line",
					Pattern.compile(
							"--foo\\s++a boolean option\\s*$\\nquux$\\s*--bar",
							Pattern.MULTILINE).matcher(s).find());
		}
	}

	@Test
	public void textTestError1() {
		try {
			Object[][][] spec = {
					//
					{ { "foo" } },//
					{ { Opt.TEXT, "quux", "baz" } },//
					{ { "bar" } },//
			};
			new Cli(spec, Modifiers.THROW_EXCEPTION);
			fail("should have thrown error");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("found error", s.startsWith("ERRORS"));
			assertTrue("correct error",
					s.indexOf("cannot have more than 2 elements") > -1);
		}
	}

	@Test
	public void textTestError3() {
		try {
			Object[][][] spec = {
					//
					{ { "foo" } },//
					{ { Opt.TEXT, "quux" }, { "baz" } },//
					{ { "bar" } },//
			};
			new Cli(spec, Modifiers.THROW_EXCEPTION);
			fail("should have thrown error");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("found error", s.startsWith("ERRORS"));
			assertTrue(
					"correct error",
					s.indexOf("line should consist of a single element array") > -1);
		}
	}

	@Test
	public void commandNameTest1() {
		try {
			Object[][][] spec = {
			//
			{ { "f&oo" } },//
			};
			new Cli(spec, Modifiers.THROW_EXCEPTION);
			fail("should have thrown error");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("found error", s.startsWith("ERRORS"));
			assertTrue("correct error",
					s.indexOf("violates option name pattern") > -1);
		}
	}

	@Test
	public void commandNameTest2() {
		try {
			Object[][][] spec = {
			//
			{ { "" } },//
			};
			new Cli(spec, Modifiers.THROW_EXCEPTION);
			fail("should have thrown error");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("found error", s.startsWith("ERRORS"));
			assertTrue("correct error",
					s.indexOf("violates option name pattern") > -1);
		}
	}

	@Test
	public void commandNameTest3() {
		try {
			Object[][][] spec = {
			//
			{ { "-foo" } },//
			};
			new Cli(spec, Modifiers.THROW_EXCEPTION);
			fail("should have thrown error");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("found error", s.startsWith("ERRORS"));
			assertTrue("correct error",
					s.indexOf("violates option name pattern") > -1);
		}
	}

	@Test
	public void commandNameTest4() {
		try {
			Object[][][] spec = {
			//
			{ { "_foo" } },//
			};
			new Cli(spec, Modifiers.THROW_EXCEPTION);
			fail("should have thrown error");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("found error", s.startsWith("ERRORS"));
			assertTrue("correct error",
					s.indexOf("violates option name pattern") > -1);
		}
	}

	@Test
	public void commandNameTest5() {
		try {
			Object[][][] spec = {
			//
			{ { "foo-" } },//
			};
			new Cli(spec, Modifiers.THROW_EXCEPTION);
			fail("should have thrown error");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("found error", s.startsWith("ERRORS"));
			assertTrue("correct error",
					s.indexOf("violates option name pattern") > -1);
		}
	}

	@Test
	public void commandNameTest6() {
		try {
			Object[][][] spec = {
			//
			{ { "foo_" } },//
			};
			new Cli(spec, Modifiers.THROW_EXCEPTION);
			fail("should have thrown error");
		} catch (RuntimeException e) {
			String s = e.getMessage();
			assertTrue("found error", s.startsWith("ERRORS"));
			assertTrue("correct error",
					s.indexOf("violates option name pattern") > -1);
		}
	}

	@Test
	public void commandNameTest7() {
		try {
			Object[][][] spec = {
			//
			{ { "9" } },//
			};
			new Cli(spec, Modifiers.THROW_EXCEPTION);
		} catch (RuntimeException e) {
			fail("'9' should be acceptable command name");
		}
	}

}
