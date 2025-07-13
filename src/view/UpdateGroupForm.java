package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;

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

public class UpdateGroupForm extends JFrame {
	private JTextField groupNameField;
	private JTextField descriptionField;
	private JTable contactTable;
	private DefaultTableModel tableModel;
	private Group group;
	private Contacts contactsModel;

	public UpdateGroupForm(Group groupToUpdate) {
		this.group = groupToUpdate;

		setTitle("Update Group");
		setSize(500, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

		Font font = new Font("SansSerif", Font.PLAIN, 14);

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		groupNameField = new JTextField(group.getName(), 20);
		descriptionField = new JTextField(group.getDescription(), 20);

		formPanel.add(createFieldRow("Group Name:", groupNameField, font));
		formPanel.add(Box.createVerticalStrut(10));
		formPanel.add(createFieldRow("Description:", descriptionField, font));
		formPanel.add(Box.createVerticalStrut(20));

		// Contact table setup
		String[] columnNames = { "Contact Name", "City", "Add to Group" };
		tableModel = new DefaultTableModel(columnNames, 0) {
			public Class<?> getColumnClass(int column) {
				return column == 2 ? Boolean.class : String.class;
			}

			public boolean isCellEditable(int row, int column) {
				return column == 2;
			}
		};

		contactTable = new JTable(tableModel);
		contactTable.setRowHeight(25);
		loadContacts();

		JScrollPane scrollPane = new JScrollPane(contactTable);
		scrollPane.setPreferredSize(new Dimension(400, 250));
		scrollPane.setBorder(BorderFactory.createTitledBorder("Modify Group Contacts"));
		formPanel.add(scrollPane);

		formPanel.add(Box.createVerticalStrut(20));

		// Button panel
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
		JButton saveBtn = new JButton("Save");
		JButton cancelBtn = new JButton("Cancel");

		saveBtn.addActionListener(e -> updateGroup());
		cancelBtn.addActionListener(e -> {
			int opt = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel?", "Cancel",
					JOptionPane.YES_NO_OPTION);
			if (opt == JOptionPane.YES_OPTION) {
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
		tableModel.setRowCount(0);
		contactsModel = Contacts.getInstance();
		contactsModel.loadContacts();

		for (Contact c : contactsModel.getcontactSet()) {
			String fullName = c.getFirstName() + " " + c.getLastName();
			boolean selected = group.getContacts().contains(c);
			tableModel.addRow(new Object[] { fullName, c.getCity(), selected });
		}
	}

	private void updateGroup() {
		String newName = groupNameField.getText().trim();
		String newDesc = descriptionField.getText().trim();

		if (newName.isEmpty() || newDesc.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Both name and description are required.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		ArrayList<Contact> updatedContacts = new ArrayList<>();
		int rowCount = tableModel.getRowCount();
		ArrayList<Contact> allContacts = new ArrayList<>(contactsModel.getcontactSet());

		for (int i = 0; i < rowCount; i++) {
			boolean selected = (Boolean) tableModel.getValueAt(i, 2);
			if (selected) {
				updatedContacts.add(allContacts.get(i));
			}
		}

		Group updatedGroup = new Group(newName, newDesc, updatedContacts);
		if (Groups.getInstance().updateGroup(group, updatedGroup)) {
			JOptionPane.showMessageDialog(this, "Groupe enregistré avec succès !");
			dispose();
		} else {
			JOptionPane.showMessageDialog(this, "Ce Group existe déjà.", "Duplication", JOptionPane.WARNING_MESSAGE);
		}

	}

	private JPanel createFieldRow(String labelText, JTextField field, Font font) {
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
