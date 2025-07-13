package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Group implements Serializable {
	private String name;
	private String description;
	private Set<Contact> contacts;

	public Group(String name, String description, List<Contact> contacts) {
		this.name = name;
		this.description = description;
		this.contacts = new HashSet<>();
		if (contacts != null) {
			for (Contact contact : contacts) {
				addContact(contact);
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Contact> getContacts() {
		return contacts;
	}

	public void addContact(Contact contact) {
		if (contacts.add(contact)) {
			contact.addContactGroup(this);
		}

	}

	public void removeContact(Contact contact) {
		contacts.remove(contact);
		contact.removeContactGroup(this);
	}

	public boolean removeContactDeleted(Contact contact) {
		return contacts.remove(contact);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Group))
			return false;
		Group group = (Group) o;
		return Objects.equals(name, group.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public String toString() {
		return "Group [name=" + name + ", description=" + description + ", members=" + contacts + "]";
	}

}
