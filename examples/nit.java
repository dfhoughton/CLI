import dfh.cli.Cli;
import dfh.cli.coercions.FileCoercion;
import dfh.cli.coercions.RxCoercion;
import dfh.cli.rules.Range;
import dfh.cli.rules.StrRegex;
import dfh.cli.rules.StrSet;

/**
 * An example of a bipartite command structure such as one sees in version
 * control systems -- cvs, svn, hg, git, etc.
 * <p>
 * 
 * @author David F. Houghton - Sep 24, 2012
 * 
 */
public class nit {

	private static boolean verbose;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Object[][][] spec = {
				//
				{ { Cli.Opt.USAGE, "provides minor annoyances" },
						{ "usage.txt" } },//
				{ { Cli.Opt.ARGS, "cmd", "options", Cli.Opt.STAR } },//
				{ { Cli.Opt.NAME, nit.class.getCanonicalName() } },//
				{ { Cli.Opt.VERSION, "0.0.1" } },//
				{ { "verbose", 'v' }, { "whistle while you work" } },//
		};
		Cli cli = new Cli(spec);
		cli.parse(args);
		verbose = cli.bool("verbose");
		String cmd = cli.argument("cmd");
		String[] options = cli.slurpedArguments().toArray(
				new String[cli.slurpedArguments().size()]);
		if ("quux".equals(cmd))
			quux(options);
		else if ("foo".equals(cmd))
			foo(options);
		else if ("bar".equals(cmd))
			bar(options);
		else if ("baz".equals(cmd))
			baz(options);
		else
			cli.die("command must be one of foo, bar, baz, quux");
	}

	private static void baz(String[] options) {
		Object[][][] spec = {
				//
				{ { Cli.Opt.USAGE, "four score and seven years ago" }, },//
				{ { Cli.Opt.ARGS, "file" } },//
				{ { Cli.Opt.NAME, nit.class.getCanonicalName() + " baz" } },//
				{ { "abe", 'a', Double.class, 2 }, { "abify the baz" },
						{ Range.positive() } },//
				{ { "babs", 'b' }, { "baz babbily" } },//
		};
		Cli cli = new Cli(spec);
		cli.parse(options);
		System.out.print("you have bazzed");
		if (verbose)
			System.out.print('!');
		System.out.println();
	}

	private static void bar(String[] options) {
		Object[][][] spec = {
				//
				{ { Cli.Opt.USAGE, "short", "but sweet" }, },//
				{ { Cli.Opt.ARGS, "ping", Cli.Opt.QMARK, "pong", Cli.Opt.QMARK } },//
				{ { Cli.Opt.NAME, nit.class.getCanonicalName() + " bar" } },//
				{ { "chumly", 'c', String.class }, { "manservant" },
						{ new StrRegex("(?i:\\b(jeeves|charles|james)\\b)") } },//
		};
		Cli cli = new Cli(spec);
		cli.parse(options);
		System.out.print("you have barred");
		if (verbose)
			System.out.print('!');
		System.out.println();
	}

	private static void foo(String[] options) {
		Object[][][] spec = {
				//
				{ {
						Cli.Opt.USAGE,
						"There once was a girl who had a little curl",
						" Right in the middle of her forehead.\n "
								+ "When she was good she was very very good\n "
								+ "But when she was bad she was horrid." }, },//
				{ { Cli.Opt.ARGS, "url", Cli.Opt.PLUS } },//
				{ { Cli.Opt.NAME, nit.class.getCanonicalName() + " foo" } },//
				{ { "dalek", 'd', String.class, "a" }, { "robot enemies" },
						{ Cli.Res.REPEATABLE, new StrSet("a", "b", "c", "d") } },//
		};
		Cli cli = new Cli(spec);
		cli.parse(options);
		System.out.print("you have fooed");
		if (verbose)
			System.out.print('!');
		System.out.println();
	}

	private static void quux(String[] options) {
		Object[][][] spec = {
				//
				{ {
						Cli.Opt.USAGE,
						"tomorrow and tomorrow and tomorrow",
						"creeps in this petty pace " + "from day to day "
								+ "to the last syllable of recorded time "
								+ "and all our yesterdays have lighted fools "
								+ "the way to dusty death" }, },//
				{ { Cli.Opt.ARGS, "eenie", "meenie", "mynie", "moe" } },//
				{ { Cli.Opt.NAME, nit.class.getCanonicalName() + " quuz" } },//
				{ { "the-one", "to", FileCoercion.C },
						{ "one of those things" } },//
				{ { "tother", "tt", RxCoercion.C }, { "another one" } },//
		};
		Cli cli = new Cli(spec);
		cli.parse(options);
		System.out.print("you have quuxed");
		if (verbose)
			System.out.print('!');
		System.out.println();
	}

}
