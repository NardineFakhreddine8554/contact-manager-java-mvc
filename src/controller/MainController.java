package controller;

import view.ContactsView;
import view.GroupsView;

public class MainController {

	public void showContactWindow() {
		new ContactsView();// Show Contacts View

	}

	public void showGroupWindow() {
		new GroupsView(); // Show Groups View
	}

}