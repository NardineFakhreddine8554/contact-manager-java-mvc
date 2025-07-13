package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import model.Contact;
import model.Contacts;
import model.Group;
import model.Groups;

public class UpdateContactForm extends JFrame {
	private JTextField firstNameField, lastNameField, cityField;
	private JTable phoneTable;
	private JCheckBox familyBox, friendsBox, coworkersBox, universityBox;
	private JButton saveButton, cancelButton;

	private final Contact selectedContact;
	private final Contacts contactsModel;
	List<JCheckBox> groupCheckBoxes = new ArrayList<>();
	Set<Group> allGroups;
	Set<Group> selectedGroups;
	Map<JCheckBox, Group> checkBoxGroupMap = new HashMap<>();
	private Groups GroupsModel;
	private boolean isModified = false;

	public UpdateContactForm(Contact contact) {

		this.selectedContact = contact;
		this.contactsModel = Contacts.getInstance(); // Singleton or consistent instance

		setTitle("Update Contact");
		setSize(500, 500);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		firstNameField = new JTextField(contact.getFirstName(), 20);
		firstNameField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				markChanged();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				markChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				markChanged();
			}
		});

		lastNameField = new JTextField(contact.getLastName(), 20);
		lastNameField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				markChanged();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				markChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				markChanged();
			}
		});

		cityField = new JTextField(contact.getCity(), 20);
		cityField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				markChanged();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				markChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				markChanged();
			}
		});
		Font font = new Font("SansSerif", Font.PLAIN, 14);
		formPanel.add(createFieldRow("First name:", firstNameField, font));
		formPanel.add(firstNameField);

		formPanel.add(createFieldRow("Last name:", lastNameField, font));
		formPanel.add(lastNameField);

		formPanel.add(createFieldRow("City:", cityField, font));
		formPanel.add(cityField);
		formPanel.add(Box.createVerticalStrut(30));

		// Phone table
		String[] columnNames = { "Region Code", "Phone number" };
		DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
		phoneTable = new JTable(tableModel);

		List<String[]> phoneList = contact.getPhoneNumbers();
		for (String[] phone : contact.getPhoneNumbers()) {
			if (phone.length == 2) {
				System.out.println("Adding: " + phone[0] + " - " + phone[1]);
				tableModel.addRow(phone);
			}

		}
		if (phoneList.size() < 10) {
			for (int i = 0; i < 10 - phoneList.size(); i++) {
				tableModel.addRow(new Object[] { "", "" });
			}
		}
		phoneTable.getModel().addTableModelListener(e -> {
			markChanged();
		});

		JScrollPane scrollPane = new JScrollPane(phoneTable);

		scrollPane.setBorder(BorderFactory.createTitledBorder("Phone numbers (Region | Number)"));
		formPanel.add(scrollPane);

		formPanel.add(Box.createVerticalStrut(30));
		selectedGroups = contact.getContactGroups();
		GroupsModel = Groups.getInstance();
		GroupsModel.loadGroups();
		Set<Group> groups = new HashSet<>();
		groups = GroupsModel.getGroupSet();
		JPanel groupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		groupPanel.setBorder(BorderFactory.createTitledBorder("Modify contact Groups"));
		for (Group group : groups) {
			boolean isSelected = selectedGroups.contains(group);
			JCheckBox checkBox = new JCheckBox(group.getName(), isSelected);
			checkBox.addItemListener(e -> markChanged()); // Detect changes
			groupCheckBoxes.add(checkBox);
			checkBoxGroupMap.put(checkBox, group); // Link checkbox to Group
			groupPanel.add(checkBox);
		}
		formPanel.add(groupPanel);
		add(formPanel, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
		saveButton = new JButton("Save");
		cancelButton = new JButton("Cancel");

		saveButton.addActionListener(e -> {
			if (!isModified) {
				JOptionPane.showMessageDialog(this, "Aucune modification détectée.", "Information",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty()
					|| cityField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Veuillez entrer le prénom, le nom et la ville.", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (getPhoneNumbersFromTable().size() == 0) {
				JOptionPane.showMessageDialog(this, "Veuillez entrer au moins un numéro de téléphone.", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			Contact updated = new Contact(firstNameField.getText(), lastNameField.getText(), cityField.getText(),
					getPhoneNumbersFromTable(), getSelectedGroups());
			if (contactsModel.updateContact(selectedContact, updated)) {
				JOptionPane.showMessageDialog(this, "Contact adaptee avec succès!", "Succès",
						JOptionPane.INFORMATION_MESSAGE);
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Ce contact existe déjà.", "Duplication",
						JOptionPane.WARNING_MESSAGE);
			}

		});

		cancelButton.addActionListener(e -> dispose());

		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);
		add(buttonPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	private List<String[]> getPhoneNumbersFromTable() {

		List<String[]> phoneNumbers = new ArrayList<>();
		if (phoneTable == null) {
			System.err.println("Warning: phoneTable is not initialized.");
			return phoneNumbers;
		}

		// Ensure edits are committed before reading
		if (phoneTable.isEditing()) {
			phoneTable.getCellEditor().stopCellEditing();
		}
		DefaultTableModel model = (DefaultTableModel) phoneTable.getModel(); // phoneTable must not be null!
		for (int i = 0; i < model.getRowCount(); i++) {
			String region = String.valueOf(model.getValueAt(i, 0));
			String number = String.valueOf(model.getValueAt(i, 1));
			if (!region.isEmpty() && !number.isEmpty()) {
				phoneNumbers.add(new String[] { region, number });
			}
		}
		return phoneNumbers;
	}

	private JPanel createFieldRow(String labelText, JTextField field, Font font) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel label = new JLabel(labelText);
		label.setFont(font);
		panel.add(label);
		panel.add(field);
		return panel;
	}

	private List<Group> getSelectedGroups() {
		List<Group> selectedGroups = new ArrayList<>();
		for (JCheckBox cb : groupCheckBoxes) {
			if (cb.isSelected()) {
				selectedGroups.add(checkBoxGroupMap.get(cb));

			}
		}

		return selectedGroups;
	}

	private void markChanged() {
		isModified = true;
	}
}
