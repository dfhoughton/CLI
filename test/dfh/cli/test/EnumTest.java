package dfh.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.regex.Pattern;

import org.junit.Test;

import dfh.cli.Cli;
import dfh.cli.Cli.Mod;

/**
 * Tests functionality of EnumOption and its kin.
 * <p>
 * 
 * @author David F. Houghton - Nov 21, 2012
 * 
 */
public class EnumTest {
	enum foo {
		bar, baz
	}

	@Test
	public void compilationTestSimple() {
		try {
			Cli cli = new Cli(new Object[][][] { { { "foo", foo.class }, } },
					Mod.THROW_EXCEPTION);
			cli.parse("--foo", "bar");
			assertEquals("correct value for --foo", foo.bar, cli.object("foo"));
		} catch (Exception e) {
			fail("threw exception " + e);
		}
	}

	@Test
	public void compilationTestSet() {
		try {
			Cli cli = new Cli(new Object[][][] { { { "foo", foo.class }, {},
					{ Cli.Res.SET } } }, Mod.THROW_EXCEPTION);
			cli.parse("--foo", "bar", "--foo", "baz");
			assertTrue(cli.collection("foo").contains(foo.bar));
			assertTrue(cli.collection("foo").contains(foo.baz));
		} catch (Exception e) {
			fail("threw exception " + e);
		}
	}

	@Test
	public void compilationTestList() {
		try {
			Cli cli = new Cli(new Object[][][] { { { "foo", foo.class }, {},
					{ Cli.Res.REPEATABLE } } }, Mod.THROW_EXCEPTION);
			cli.parse("--foo", "bar", "--foo", "baz", "--foo", "baz");
			assertTrue(cli.collection("foo").contains(foo.bar));
			assertTrue(cli.collection("foo").contains(foo.baz));
			assertEquals(3, cli.collection("foo").size());
		} catch (Exception e) {
			fail("threw exception " + e);
		}
	}

	@Test
	public void defaultValue() {
		try {
			Cli cli = new Cli(
					new Object[][][] { { { "foo", foo.class, foo.bar } } },
					Mod.THROW_EXCEPTION);
			cli.parse();
			assertEquals(foo.bar, cli.object("foo"));
		} catch (Exception e) {
			fail("threw exception " + e);
		}
	}

	@Test
	public void overriddenDefaultValue() {
		try {
			Cli cli = new Cli(
					new Object[][][] { { { "foo", foo.class, foo.bar } } },
					Mod.THROW_EXCEPTION);
			cli.parse("--foo=baz");
			assertEquals(foo.baz, cli.object("foo"));
		} catch (Exception e) {
			fail("threw exception " + e);
		}
	}

	@Test
	public void help() {
		try {
			Cli cli = new Cli(new Object[][][] { {
					{ "foo", foo.class, foo.bar }, { "description" } } },
					Mod.THROW_EXCEPTION);
			cli.parse("--help");
			fail("should have thrown an exception");
		} catch (Exception e) {
			String s = e.getMessage();
			Pattern p = Pattern.compile("description.+\\{bar, baz\\}");
			assertTrue("enumeration present in description", p.matcher(s).find());
		}
	}
}
