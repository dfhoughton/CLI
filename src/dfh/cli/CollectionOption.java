package dfh.cli;

import java.util.LinkedList;
import java.util.List;

public abstract class CollectionOption<K> extends Option<K> {
	protected List<String> storageList = new LinkedList<String>();

	@Override
	public void store(String s) {
		storageList.add(s);
	}
}
