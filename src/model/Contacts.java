package model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.FileUtil;
import util.ObservableSource;

public class Contacts extends ObservableSource {
	private static Contacts instance;// use singleton pattern to get one instance
	private Groups groupsModel;
	private Set<Contact> contactSet = new HashSet<>();

	public static Contacts getInstance() {
		if (instance == null) {
			instance = new Contacts();
		}
		return instance;
	}

	public Set<Contact> getcontactSet() {
		return contactSet;
	}

	public boolean addContact(Contact c) {
		boolean added = contactSet.add(c); // Only adds if it's not a duplicate
		if (added) {
			saveContacts(); // Save only if added successfully
		}
		return added;
	}

	public boolean updateContact(Contact oldContact, Contact newContact) {
		if (contactSet.contains(oldContact)) {
			// Step 1: Remove old contact from all groups
			for (Group group : oldContact.getContactGroups()) {
				for (Group g : Groups.getInstance().getGroupSet()) {
					if (g.equals(group)) {
						g.getContacts().remove(oldContact);
					}
				}
			}

			contactSet.remove(oldContact);

			// Step 2: Try to add new contact
			if (addContact(newContact)) {
				// Step 3: Add new contact to its selected groups
				for (Group group : newContact.getContactGroups()) {
					for (Group g : Groups.getInstance().getGroupSet()) {
						if (g.equals(group)) {
							g.getContacts().add(newContact);
						}
					}
				}

				saveContacts();
				Groups.getInstance().saveGroups(); // If you're persisting groups too
				return true;
			} else {
				// Rollback
				contactSet.add(oldContact);
				for (Group group : oldContact.getContactGroups()) {
					for (Group g : Groups.getInstance().getGroupSet()) {
						if (g.equals(group)) {
							g.getContacts().add(oldContact);
						}
					}
				}
				return false;
			}
		}

		return false;
	}

	public boolean removeContact(Contact c) {
		// Remove this contact from all groups it belongs to
		for (Group group : c.getContactGroups()) {
			for (Group groupSet : Groups.getInstance().getGroupSet()) {
				if (groupSet.equals(group)) {
					groupSet.getContacts().remove(c);
				}
			}
		}

		boolean removed = contactSet.remove(c);
		if (removed) {
			saveContacts();
			Groups.getInstance().saveGroups();
		}
		return removed;

	}

	public void saveContacts() {
		FileUtil.saveToFile("contacts.ser", contactSet);
		setChanged();
		notifyObservers();
	}

	public void loadContacts() {
		Object loaded = FileUtil.loadFromFile("contacts.ser");
		if (loaded instanceof List<?>) {
			contactSet = new HashSet<>((List<Contact>) loaded);
		} else if (loaded instanceof Set<?>) {
			contactSet = (Set<Contact>) loaded;
		} else {
			contactSet = new HashSet<>();
		}
	}

}
