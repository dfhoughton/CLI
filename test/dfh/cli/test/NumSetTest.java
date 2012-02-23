package dfh.cli.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import dfh.cli.Cli;
import dfh.cli.Cli.Mod;
import dfh.cli.rules.IntSet;
import dfh.cli.rules.NumSet;

/**
 * Test for {@link NumSet} and {@link IntSet}.
 * <p>
 * 
 * @author David F. Houghton - Feb 23, 2012
 * 
 */
public class NumSetTest {

	@Test
	public void compilationTest1() {
		try {
			Cli cli = new Cli(new Object[][][] { { { "foo", Integer.class },
					{}, { new IntSet(1, 3) } } }, Mod.THROW_EXCEPTION);
			cli.parse("--foo", "1");
			assertTrue("correct value for --foo", cli.integer("foo").equals(1));
		} catch (Exception e) {
			fail("threw exception " + e);
		}
	}

	@Test
	public void compilationTest2() {
		try {
			Cli cli = new Cli(new Object[][][] { { { "foo", Double.class }, {},
					{ new NumSet(1, 3) } } }, Mod.THROW_EXCEPTION);
			cli.parse("--foo", "1");
			assertTrue("correct value for --foo", cli.number("foo").equals(1D));
		} catch (Exception e) {
			fail("threw exception " + e);
		}
	}

	@Test
	public void compilationTest3() {
		try {
			Cli cli = new Cli(new Object[][][] { { { "foo", Float.class }, {},
					{ new NumSet(1, 3) } } }, Mod.THROW_EXCEPTION);
			cli.parse("--foo", "1");
			assertTrue("correct value for --foo", cli.number("foo").equals(1D));
		} catch (Exception e) {
			fail("threw exception " + e);
		}
	}

	@Test
	public void intSetException() {
		try {
			Cli cli = new Cli(new Object[][][] { { { "foo", Integer.class },
					{}, { new IntSet(1, 3) } } }, Mod.THROW_EXCEPTION);
			cli.parse("--foo", "2");
			fail("should have thrown exception");
		} catch (Exception e) {
			assertTrue("correct error message",
					e.getMessage().indexOf("not in set") > -1);
		}
	}

	@Test
	public void numSetException() {
		try {
			Cli cli = new Cli(new Object[][][] { { { "foo", Double.class }, {},
					{ new NumSet(1.5, 3.1415926) } } }, Mod.THROW_EXCEPTION);
			cli.parse("--foo", "2");
			fail("should have thrown exception");
		} catch (Exception e) {
			assertTrue("correct error message",
					e.getMessage().indexOf("not in set") > -1);
		}
	}

	@Test
	public void numSetDouble() {
		try {
			Cli cli = new Cli(new Object[][][] { { { "foo", Double.class }, {},
					{ new NumSet(1.5, 3.1415926) } } }, Mod.THROW_EXCEPTION);
			cli.parse("--foo", "3.1415926");
			assertTrue("no precision error", cli.number("foo")
					.equals(3.1415926));
		} catch (Exception e) {
			fail("threw exception " + e);
		}
	}

}
