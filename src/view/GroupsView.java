package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import model.Contact;
import model.Contacts;
import model.Group;
import model.Groups;
import util.ObserverSource;

public class GroupsView extends JFrame implements ObserverSource {

	private JTable groupTable;
	private DefaultTableModel groupTableModel;
	private JTable contactTable;
	private DefaultTableModel contactTableModel;
	private Groups GroupsModel;
	private Contacts ContactsModel;

	public GroupsView() {
		GroupsModel = Groups.getInstance();
//		ContactsModel = Contacts.getInstance();
//		ContactsModel.addObserver(this);
		GroupsModel.addObserver(this);
		GroupsModel.loadGroups();
//		for (Group c : GroupsModel.getGroupSet()) {
//			System.out.println(c);
//		}
//		System.out.println("Group after removal: " + GroupsModel.getGroupSet());
//		originalContacts = new ArrayList<>(contactsModel.getcontactSet());
		setTitle("Groups");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(500, 500);
		setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		// Left panel: group Table
		groupTableModel = new DefaultTableModel(new String[] { "Group", "Members" }, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		groupTable = new JTable(groupTableModel);
		groupTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane listScrollPane = new JScrollPane(groupTable);
		listScrollPane.setPreferredSize(new Dimension(250, 0));

		// Right panel: contact table
		contactTableModel = new DefaultTableModel(new String[] { "Name", "City" }, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		contactTable = new JTable(contactTableModel);
		JScrollPane tableScrollPane = new JScrollPane(contactTable);

		// Bottom panel: buttons
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton addButton = new JButton("Add");
		JButton updateButton = new JButton("Update");
		JButton deleteButton = new JButton("Delete");
		buttonPanel.add(addButton);
		addButton.addActionListener(e -> new AddGroupForm());
		buttonPanel.add(updateButton);
		buttonPanel.add(deleteButton);

		mainPanel.add(listScrollPane, BorderLayout.WEST);
		mainPanel.add(tableScrollPane, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		setContentPane(mainPanel);

//		generateMockData();
		loadGroupsToList();

		groupTable.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && groupTable.getSelectedRow() != -1) {
				String groupName = groupTableModel.getValueAt(groupTable.getSelectedRow(), 0).toString();
				Group group = Groups.getInstance().getGroupSet().stream().filter(g -> g.getName().equals(groupName))
						.findFirst().orElse(null);

				if (group != null) {
					loadContactsForGroup(group);
				}
			}
		});
		updateButton.addActionListener(e -> {
			int selectedRow = groupTable.getSelectedRow();
			if (selectedRow != -1) {
				String groupName = (String) groupTableModel.getValueAt(selectedRow, 0);
				Group group = Groups.getInstance().getGroupSet().stream().filter(g -> g.getName().equals(groupName))
						.findFirst().orElse(null);
				if (group != null) {
					new UpdateGroupForm(group);
				}
			} else {
				JOptionPane.showMessageDialog(this, "Please select a group to update.");
			}
		});

		deleteButton.addActionListener(e -> {
			int selectedRow = groupTable.getSelectedRow();
			if (selectedRow != -1) {
				String groupName = groupTableModel.getValueAt(selectedRow, 0).toString();
				Group group = Groups.getInstance().getGroupSet().stream().filter(g -> g.getName().equals(groupName))
						.findFirst().get(); // assumes group exists
				int confirm = JOptionPane.showConfirmDialog(GroupsView.this,
						"Êtes-vous sûr de vouloir supprimer le groupe '" + group.getName() + "' ?",
						"Confirmer la suppression", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					GroupsModel.removeGroup(group);
					loadGroupsToList();
					contactTableModel.setRowCount(0);
				}
			} else {
				JOptionPane.showMessageDialog(GroupsView.this, "Veuillez sélectionner un groupe à supprimer.");
			}
		});

		setVisible(true);
	}

	public void loadGroupsToList() {
		groupTableModel.setRowCount(0);
		for (Group group : GroupsModel.getGroupSet()) {
			groupTableModel.addRow(
					new Object[] { group.getName(), group.getContacts() == null ? 0 : group.getContacts().size() });
		}
	}

	private void loadContactsForGroup(Group group) {
		contactTableModel.setRowCount(0);
		for (Contact contact : group.getContacts()) {
			contactTableModel
					.addRow(new Object[] { contact.getFirstName() + " " + contact.getLastName(), contact.getCity() });
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		loadGroupsToList();
		contactTableModel.setRowCount(0); // <- Clear contact table when model update
	}
}
