package dfh.cli;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * For parsing CLI args.
 * <p>
 * <b>Creation date:</b> Nov 4, 2011
 * 
 * @author David Houghton
 * 
 */
public class Cli {
	private ArrayList<String> argList;
	private TreeMap<String, Object> commands;

	public Cli parse(String[] args) {
		Cli p = new Cli();
		p.commands = new TreeMap<String, Object>();
		p.argList = new ArrayList<String>(args.length);
		boolean endCommands = false;
		String lastCommand = null;
		for (String s : args) {
			if (endCommands)
				p.argList.add(s);
			else {
				if (s.startsWith("-")) {
					if (s.equals("--"))
						endCommands = true;
					else
						lastCommand = s.substring(2);
				} else if (lastCommand == null) {
					endCommands = true;
					p.argList.add(s);
				} else {
					p.commands.put(lastCommand, s);
					lastCommand = null;
				}
			}
		}
		if (lastCommand != null)
			p.commands.put(lastCommand, null);
		return p;
	}

	public Boolean bool(String string) {
		if (commands.containsKey(string)) {
			return new Boolean((String) commands.get(string));
		}
		return null;
	}

	public Integer integer(String string) {
		if (commands.containsKey(string)) {
			return new Integer((String) commands.get(string));
		}
		return null;
	}
}
