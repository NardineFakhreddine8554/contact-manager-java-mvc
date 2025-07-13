package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import model.Contact;
import model.Contacts;
import util.ObserverSource;

public class ContactsView extends JFrame implements ObserverSource {
	private JButton sortByFirstNameBtn, sortByLastNameBtn, sortByCityBtn;
	private JButton addContactBtn, viewBtn, updateBtn, deleteBtn;
	private JTextField searchField;
	private JList<Contact> contactJList;
	private DefaultListModel<Contact> listModel;
	private List<Contact> originalContacts;
	private Contacts contactsModel;

	private final Font FONT = new Font("SansSerif", Font.PLAIN, 14);

	public ContactsView() {
		contactsModel = Contacts.getInstance();
		contactsModel.addObserver(this);
		contactsModel.loadContacts();

		originalContacts = new ArrayList<>(contactsModel.getcontactSet());
		originalContacts.sort(Comparator.comparing(Contact::getFirstName));
		setTitle("Gestion des contacts");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(500, 500);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		// Left Panel
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));// top to bottom

		sortByFirstNameBtn = new JButton("Sort by First name");
		sortByFirstNameBtn.setFocusable(false);
		sortByLastNameBtn = new JButton("Sort by Last name");
		sortByLastNameBtn.setFocusable(false);
		sortByCityBtn = new JButton("Sort by City");
		sortByCityBtn.setFocusable(false);
		addContactBtn = new JButton("Add new contact");
		addContactBtn.setFocusable(false);

		Dimension buttonSize = new Dimension(180, 40);
		sortByFirstNameBtn.setMaximumSize(buttonSize);
		sortByLastNameBtn.setMaximumSize(buttonSize);
		sortByCityBtn.setMaximumSize(buttonSize);
		addContactBtn.setMaximumSize(buttonSize);

		addContactBtn.addActionListener(e -> new AddContactForm());

		leftPanel.setBorder(BorderFactory.createEmptyBorder(80, 15, 0, 0)); // top, left, bottom, right
		leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));// add space
		leftPanel.add(sortByFirstNameBtn);
		leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));// add space
		leftPanel.add(sortByLastNameBtn);
		leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));// add space
		leftPanel.add(sortByCityBtn);
		leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));// add space
		leftPanel.add(addContactBtn);

		add(leftPanel, BorderLayout.WEST);

		// Right Panel
		JPanel rightPanel = new JPanel(new BorderLayout());

		rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel centerPanel = new JPanel(new BorderLayout());

		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		searchField = new JTextField("Search", 28);
		searchField.setFocusable(false);
		searchField.setForeground(Color.GRAY);

		// Add focus listener for placeholder behavior
		searchField.addFocusListener(new java.awt.event.FocusAdapter() {
			@Override
			public void focusGained(java.awt.event.FocusEvent e) {
				if (searchField.getText().equals("Search")) {
					searchField.setText("");
					searchField.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				if (searchField.getText().isEmpty()) {
					searchField.setForeground(Color.GRAY);
					searchField.setText("Search");
				}
			}
		});
//		searchPanel.add(new JLabel("Search"));
//		searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
		searchPanel.add(searchField);
		centerPanel.add(searchPanel, BorderLayout.NORTH);

		listModel = new DefaultListModel<Contact>();
		originalContacts.forEach(listModel::addElement);

		contactJList = new JList<>(listModel);
		contactJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contactJList.setFont(FONT);
		JScrollPane scrollPane = new JScrollPane(contactJList);
		centerPanel.add(scrollPane, BorderLayout.CENTER);
		rightPanel.add(centerPanel, BorderLayout.CENTER);

		JPanel lowerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));

		viewBtn = new JButton("View");
		updateBtn = new JButton("Update");
		deleteBtn = new JButton("Delete");

		lowerPanel.add(viewBtn);
		lowerPanel.add(updateBtn);
		lowerPanel.add(deleteBtn);
		rightPanel.add(lowerPanel, BorderLayout.SOUTH);

		add(rightPanel, BorderLayout.CENTER);

		// Listeners
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String query = searchField.getText().toLowerCase();
				listModel.clear();
				for (Contact contact : originalContacts) {
					if (query.isEmpty() || contact.getFirstName().toLowerCase().contains(query)
							|| contact.getLastName().toLowerCase().contains(query)
							|| contact.getCity().toLowerCase().contains(query)) {
						listModel.addElement(contact);
					}
				}
			}
		});

		sortByFirstNameBtn.addActionListener(e -> sortContacts(Comparator.comparing(Contact::getFirstName)));
		sortByLastNameBtn.addActionListener(e -> sortContacts(Comparator.comparing(Contact::getLastName)));
		sortByCityBtn.addActionListener(e -> sortContacts(Comparator.comparing(Contact::getCity)));

		viewBtn.addActionListener(e -> {
			Contact selected = contactJList.getSelectedValue();
			if (selected != null) {
				showContactDetails(selected);
			} else {
				JOptionPane.showMessageDialog(this, "Veuillez sélectionner un contact à afficher.", "Avertissement",
						JOptionPane.WARNING_MESSAGE);
			}
		});
		updateBtn.addActionListener(e -> {
			Contact selected = contactJList.getSelectedValue();

			if (selected != null) {
				new UpdateContactForm(selected);
			} else {
				JOptionPane.showMessageDialog(this, " Veuillez sélectionner un contact à mettre à jour.",
						"Aucun contact sélectionné.", JOptionPane.WARNING_MESSAGE);
			}
		});

		deleteBtn.addActionListener(e -> {
			Contact selected = contactJList.getSelectedValue();
			if (selected != null) {
				int response = JOptionPane.showConfirmDialog(this,
						"Êtes-vous sûr de vouloir supprimer ce contact: " + selected + "?", "Confirmer la suppression",
						JOptionPane.YES_NO_OPTION);
				if (response == JOptionPane.YES_OPTION) {
					contactsModel.removeContact(selected);
					originalContacts.remove(selected);
					listModel.removeElement(selected);
				}
			} else {
				JOptionPane.showMessageDialog(this, " Veuillez sélectionner un contact à supprimer.",
						"Aucun contact sélectionné.", JOptionPane.WARNING_MESSAGE);
			}
		});

		setVisible(true);
	}

	private void sortContacts(Comparator<Contact> comparator) {
		originalContacts.sort(comparator);
		listModel.clear();
		originalContacts.forEach(listModel::addElement);
	}

	private void showContactDetails(Contact selected) {
		JFrame viewFrame = new JFrame("View Contact");
		viewFrame.setSize(350, 300);
		viewFrame.setLocationRelativeTo(this);
		viewFrame.setLayout(new BorderLayout());

		JTextArea contactDetails = new JTextArea();
		contactDetails.setEditable(false);
		contactDetails.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		contactDetails.setBackground(new Color(250, 250, 255));

		StringBuilder phoneNumber = new StringBuilder();
		for (String[] phone : selected.getPhoneNumbers()) {
			phoneNumber.append("    Region: ").append(phone[0]).append(", Number: ").append(phone[1]).append("\n");
		}
		StringBuilder groupNames = new StringBuilder();
		if (selected.getContactGroups() != null && !selected.getContactGroups().isEmpty()) {
			for (var group : selected.getContactGroups()) {
				groupNames.append(" - ").append(group.getName()).append("\n");
			}
		} else {
			groupNames.append("Aucun groupe");
		}

		contactDetails.setText("Prénom : " + selected.getFirstName() + "\n\n" + "Nom : " + selected.getLastName()
				+ "\n\n" + "Ville : " + selected.getCity() + "\n\n" + "Numéros de téléphone :\n" + phoneNumber + "\n"
				+ "Groupes :\n" + groupNames);

		viewFrame.add(new JScrollPane(contactDetails), BorderLayout.CENTER);
		viewFrame.setVisible(true);
	}

	@Override
	public void update() {
		originalContacts = new ArrayList<>(contactsModel.getcontactSet());
		originalContacts.sort(Comparator.comparing(Contact::getFirstName));
		listModel.clear();
		originalContacts.forEach(listModel::addElement);
	}
}
