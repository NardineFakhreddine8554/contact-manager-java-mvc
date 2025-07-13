package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Contact implements Serializable {
	private String firstName;
	private String lastName;
	private String city;
	private List<String[]> phoneNumbers;
	private Set<Group> contactGroups;

	public Contact(String firstName, String lastName, String city, List<String[]> phoneNumbers,
			List<Group> contactGroups) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.city = city;
		this.phoneNumbers = phoneNumbers;
		this.contactGroups = new HashSet<>(); // Initialize set
		if (contactGroups != null) {
			for (Group group : contactGroups) {
				addContactGroup(group); // Ensure bidirectional linking
			}
		}

	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public List<String[]> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(List<String[]> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public void addPhoneNumber(String[] number) {
		phoneNumbers.add(number);
	}

	public Set<Group> getContactGroups() {
		return contactGroups;
	}

	public void addContactGroup(Group group) {
		if (contactGroups.add(group)) {
			group.addContact(this);// Maintain bidirectional relationship
		}
	}

	public void removeContactGroup(Group group) {
		if (contactGroups.remove(group)) {
			group.removeContact(this);
		}
	}

	@Override
	public String toString() {
		return firstName + " " + lastName + " - " + city;
	}

//	@Override
//	public boolean equals(Object o) {
//		if (this == o)
//			return true;
//		if (o == null || getClass() != o.getClass())
//			return false;
//
//		Contact contact = (Contact) o;
//
//		if (!firstName.equalsIgnoreCase(contact.firstName))
//			return false;
//		if (!lastName.equalsIgnoreCase(contact.lastName))
//			return false;
//		if (phoneNumbers == null || contact.phoneNumbers == null)
//			return false;
//		if (phoneNumbers.size() != contact.phoneNumbers.size())
//			return false;
//
//		for (int i = 0; i < phoneNumbers.size(); i++) {
//			String[] thisPhone = phoneNumbers.get(i);
//			String[] otherPhone = contact.phoneNumbers.get(i);
//
//			if (thisPhone.length != otherPhone.length)
//				return false;
//			for (int j = 0; j < thisPhone.length; j++) {
//				if (!thisPhone[j].equals(otherPhone[j]))
//					return false;
//			}
//		}
//
//		return true;
//	}
//
//	@Override
//	public int hashCode() {
//		int result = (firstName == null ? 0 : firstName.toLowerCase().hashCode());
//		result = 31 * result + (lastName == null ? 0 : lastName.toLowerCase().hashCode());
//
//		if (phoneNumbers != null) {
//			for (String[] phone : phoneNumbers) {
//				for (String part : phone) {
//					result = 31 * result + (part == null ? 0 : part.hashCode());
//				}
//			}
//		}
//
//		return result;
//	}
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Contact contact = (Contact) o;

		if (firstName == null || contact.firstName == null)
			return false;

		return firstName.trim().equalsIgnoreCase(contact.firstName.trim());
	}

	@Override
	public int hashCode() {
		return firstName == null ? 0 : firstName.trim().toLowerCase().hashCode();
	}

}
