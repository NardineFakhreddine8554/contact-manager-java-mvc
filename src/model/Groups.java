package model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.FileUtil;
import util.ObservableSource;

public class Groups extends ObservableSource {
	private static Groups instance; // Singleton pattern
	private Set<Group> groupSet = new HashSet<>();

	public static Groups getInstance() {
		if (instance == null) {
			instance = new Groups();
		}
		return instance;
	}

	public boolean addGroup(Group g) {
		if (groupSet.add(g)) {
			saveGroups();
			return true;
		} else
			return false;
	}

	public boolean updateGroup(Group oldGroup, Group newGroup) {
		if (groupSet.contains(oldGroup)) {
			// Remove old group from all contacts
			for (Contact contact : oldGroup.getContacts()) {
				for (Contact c : Contacts.getInstance().getcontactSet()) {
					if (c.equals(contact)) {
						c.getContactGroups().remove(oldGroup);
					}
				}
			}

			// Remove old group from group set
			groupSet.remove(oldGroup);

			// Try to add new group
			if (groupSet.add(newGroup)) {
				// Add new group to its selected contacts
				for (Contact contact : newGroup.getContacts()) {
					for (Contact c : Contacts.getInstance().getcontactSet()) {
						if (c.equals(contact)) {
							c.getContactGroups().add(newGroup);
						}
					}
				}

				// Save
				saveGroups();
				Contacts.getInstance().saveContacts(); // Save contact updates too
				return true;
			} else {
				// Rollback
				groupSet.add(oldGroup);
				for (Contact contact : oldGroup.getContacts()) {
					for (Contact c : Contacts.getInstance().getcontactSet()) {
						if (c.equals(contact)) {
							c.getContactGroups().add(oldGroup);
						}
					}
				}
				return false;
			}
		}

		return false;
	}

	public void removeGroup(Group g) {
		// Remove this group from all contacts it belongs to
		for (Contact contact : g.getContacts()) {
			for (Contact contactInSet : Contacts.getInstance().getcontactSet()) {
				if (contactInSet.equals(contact)) {
					contactInSet.getContactGroups().remove(g);
				}
			}
		}
		groupSet.remove(g);

		saveGroups();
		Contacts.getInstance().saveContacts(); // Save contact updates too
	}

	public Set<Group> getGroupSet() {
		return groupSet;
	}

	public void saveGroups() {
		FileUtil.saveToFile("groups.ser", groupSet);
		setChanged();
		notifyObservers();
	}

	public Set<Group> loadGroups() {
		Object loaded = FileUtil.loadFromFile("groups.ser");
		if (loaded instanceof List<?>) {
			groupSet = new HashSet<>((List<Group>) loaded);
		} else if (loaded instanceof Set<?>) {
			groupSet = (Set<Group>) loaded;
		} else {
			groupSet = new HashSet<>();
		}
		return groupSet;
	}

}
