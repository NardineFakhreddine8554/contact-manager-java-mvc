package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import model.Contact;
import model.Contacts;
import model.Group;
import model.Groups;

public class AddGroupForm extends JFrame {
	private JTextField groupNameField;
	private JTextField descriptionField;
	private JTable contactTable;
	private DefaultTableModel tableModel;
	private Contacts contactsModel;

	public AddGroupForm() {
		setTitle("Add New Group");
		setSize(500, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

		Font font = new Font("SansSerif", Font.PLAIN, 14);

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		groupNameField = new JTextField(20);
		descriptionField = new JTextField(20);

		formPanel.add(createFieldRow("Group Name:", groupNameField, font));
		formPanel.add(Box.createVerticalStrut(10));
		formPanel.add(createFieldRow("Description:", descriptionField, font));
		formPanel.add(Box.createVerticalStrut(20));

		// Contact table setup
		String[] columnNames = { "Contact Name", "City", "Add to Group" };
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public Class<?> getColumnClass(int column) {
				return column == 2 ? Boolean.class : String.class;
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 2;
			}
		};

		contactTable = new JTable(tableModel);
		contactTable.setRowHeight(25);
		loadContacts();

		JScrollPane scrollPane = new JScrollPane(contactTable);
		scrollPane.setPreferredSize(new Dimension(400, 250));
		scrollPane.setBorder(BorderFactory.createTitledBorder("Select Contacts to Add"));
		formPanel.add(scrollPane);
		formPanel.add(Box.createVerticalStrut(20));

		// Button panel
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
		JButton saveBtn = new JButton("Save Group");
		JButton cancelBtn = new JButton("Cancel");

		saveBtn.addActionListener(e -> saveGroup());
		cancelBtn.addActionListener(e -> {
			int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel?", "Cancel",
					JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				dispose();
			}
		});

		buttonPanel.add(saveBtn);
		buttonPanel.add(cancelBtn);

		add(formPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	private void loadContacts() {
		tableModel.setRowCount(0); // Clear table
		contactsModel = Contacts.getInstance();// all contacts
		contactsModel.loadContacts();
		for (Contact c : contactsModel.getcontactSet()) {
			String name = c.getFirstName() + " " + c.getLastName();
			tableModel.addRow(new Object[] { name, c.getCity(), false });
		}
	}

	private void saveGroup() {
		String name = groupNameField.getText().trim();
		String desc = descriptionField.getText().trim();

		if (name.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Group name is required.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (desc.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Group Description is required.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		List<Contact> selectedContacts = new ArrayList<>();
		int rowCount = tableModel.getRowCount();
		List<Contact> allContacts = new ArrayList<>(contactsModel.getcontactSet());

		for (int i = 0; i < rowCount; i++) {
			boolean selected = (Boolean) tableModel.getValueAt(i, 2);
			if (selected) {
				selectedContacts.add(allContacts.get(i));
			}
		}

		Group group = new Group(name, desc, selectedContacts);
		if (Groups.getInstance().addGroup(group)) {
			contactsModel.saveContacts();
			JOptionPane.showMessageDialog(this, "Groupe enregistré avec succès !");
			dispose();
		} else {
			JOptionPane.showMessageDialog(this, "Ce Group existe déjà.", "Duplication", JOptionPane.WARNING_MESSAGE);
		}

	}

	private JPanel createFieldRow(String labelText, Component field, Font font) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel label = new JLabel(labelText);
		label.setFont(font);
		label.setPreferredSize(new Dimension(120, 25));
		field.setPreferredSize(new Dimension(250, 25));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(label);
		panel.add(field);
		return panel;
	}
}
