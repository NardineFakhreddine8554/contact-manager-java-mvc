package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
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
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import model.Contact;
import model.Contacts;
import model.Group;
import model.Groups;
import util.ObserverSource;

public class AddContactForm extends JFrame implements ObserverSource {

	private JTextField firstNameField, lastNameField, cityField;
	private JTable phoneTable;
	private JCheckBox noGroup, familyGroup, friendsGroup, coworkersGroup;
	List<JCheckBox> groupCheckBoxes = new ArrayList<>();
	Map<JCheckBox, Group> checkBoxGroupMap = new HashMap<>();
	private Groups GroupsModel;
	private Set<Group> groups = new HashSet<>();

	public AddContactForm() {

		GroupsModel = Groups.getInstance();
		GroupsModel.loadGroups();
		GroupsModel.addObserver(this);
		groups = GroupsModel.getGroupSet();

		setTitle("Add New Contact");
		setSize(500, 500);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null); // Center on screen

		Font font = new Font("SansSerif", Font.PLAIN, 14);

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

		JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

		formPanel.setMaximumSize(new Dimension(400, 100));

		firstNameField = new JTextField();
		lastNameField = new JTextField();
		cityField = new JTextField();

		formPanel.add(createLabel("First Name:", font));
		formPanel.add(firstNameField);
		formPanel.add(createLabel("Last Name:", font));
		formPanel.add(lastNameField);
		formPanel.add(createLabel("City:", font));
		formPanel.add(cityField);
		formPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0)); // top, left, bottom, right
		contentPanel.add(formPanel);

		String[] columns = { "Region Code", "Phone Number" };
		DefaultTableModel model = new DefaultTableModel(columns, 10);
		phoneTable = new JTable(model);
		JScrollPane tableScroll = new JScrollPane(phoneTable);
		tableScroll.setBorder(BorderFactory.createTitledBorder("Phone numbers"));
		contentPanel.add(tableScroll);

		JPanel groupPanel = new JPanel(new GridLayout(2, 2));
		groupPanel.setBorder(BorderFactory.createTitledBorder("Add the contact to Groups"));

		noGroup = new JCheckBox("No Groups", true);
		groupPanel.add(noGroup);

		for (Group group : groups) {
			JCheckBox checkBox = new JCheckBox(group.getName());
			groupCheckBoxes.add(checkBox);
			checkBoxGroupMap.put(checkBox, group); // Link checkbox to Group
			groupPanel.add(checkBox);
		}
		for (JCheckBox cb : groupCheckBoxes) {
			cb.addActionListener(e -> {
				if (cb.isSelected()) {
					noGroup.setSelected(false);
				}
			});
		}

		noGroup.addActionListener(e -> {
			if (noGroup.isSelected()) {
				for (JCheckBox groupBox : groupCheckBoxes) {
					groupBox.setSelected(false);
				}
			}
		});

		contentPanel.add(groupPanel);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));

		JButton saveButton = new JButton("Save");
		JButton cancelButton = new JButton("Cancel");

		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		contentPanel.add(buttonPanel);

		mainPanel.add(contentPanel, BorderLayout.CENTER);
		setContentPane(mainPanel);

		saveButton.addActionListener(e -> saveContact());
		cancelButton.addActionListener(e -> cancelAction());
		setVisible(true);
	}

	private JLabel createLabel(String text, Font font) {
		JLabel label = new JLabel(text);
		label.setFont(font);
		return label;
	}

	private void saveContact() {
		String firstName = firstNameField.getText().trim();
		String lastName = lastNameField.getText().trim();
		String city = cityField.getText().trim();

		if (firstName.isEmpty() || lastName.isEmpty() || city.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Veuillez entrer le prénom, le nom et la ville.", "Erreur",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		List<String[]> phoneNumbers = new ArrayList<>();
		boolean hasPhone = false;
		for (int i = 0; i < phoneTable.getRowCount(); i++) {
			String region = (String) phoneTable.getValueAt(i, 0);
			String number = (String) phoneTable.getValueAt(i, 1);

			if (region != null && !region.trim().isEmpty() && number != null && !number.trim().isEmpty()) {
				hasPhone = true;
				region = region.trim();
				phoneNumbers.add(new String[] { region, number.trim() });
			}
		}

		if (!hasPhone) {
			JOptionPane.showMessageDialog(this, "Veuillez entrer au moins un numéro de téléphone.", "Erreur",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		List<Group> selectedGroups = new ArrayList<>();
		if (!noGroup.isSelected()) {
			for (JCheckBox cb : groupCheckBoxes) {
				if (cb.isSelected()) {
					selectedGroups.add(checkBoxGroupMap.get(cb));

				}
			}

		}
		Contact newContact = new Contact(firstName, lastName, city, phoneNumbers, selectedGroups);

		Contacts contacts = Contacts.getInstance();// get alone instance
		if (contacts.addContact(newContact)) {
			GroupsModel.saveGroups();
			JOptionPane.showMessageDialog(this, "Contact enregistré avec succès!", "Succès",
					JOptionPane.INFORMATION_MESSAGE);
			resetForm();
		} else {
			JOptionPane.showMessageDialog(this, "Ce contact existe déjà.", "Duplication", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void resetForm() {
		firstNameField.setText("");
		lastNameField.setText("");
		cityField.setText("");

		DefaultTableModel model = (DefaultTableModel) phoneTable.getModel();
		for (int i = 0; i < model.getRowCount(); i++) {
			model.setValueAt("", i, 0);
			model.setValueAt("", i, 1);
		}
		for (JCheckBox cb : groupCheckBoxes) {
			noGroup.setSelected(true);
			cb.setSelected(false);
		}

	}

	private void cancelAction() {
		int response = JOptionPane.showConfirmDialog(this, "Vous voulez quitter cette fenêtre ?", "Confirmation",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			dispose();
		}
	}

	@Override
	public void update() {
		closeAddContactFormIfOpen();

	}

	private void closeAddContactFormIfOpen() {
		for (Window window : Window.getWindows()) {
			if (window instanceof AddContactForm) {
				window.dispose(); // Close the currently open AddContactForm window
			}
		}
	}

}
