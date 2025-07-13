package util;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class ObservableSource {
	boolean changed;
	java.util.List<ObserverSource> observers;

	public ObservableSource() {
		changed = false;
		observers = new ArrayList<ObserverSource>();
	}

	public void setChanged() {
		changed = true;
	}

	public void addObserver(ObserverSource ob) {
		observers.add(ob);
	}

	public void notifyObservers() {
//		if (!changed) return;

		for (Iterator<ObserverSource> it = observers.iterator(); it.hasNext();)
			((ObserverSource) it.next()).update();
		changed = false;
		System.out.println("notify observers");
	}
//	Iterator<ObserverSource> it = observers.iterator();
//	while (it.hasNext()) {
//	    it.next().update();
//	}
}