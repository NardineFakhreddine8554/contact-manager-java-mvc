package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.MainController;

public class MainWindow extends JFrame {

	public JButton contactsButton;
	public JButton groupsButton;
	public JPanel mainPanel;
	private MainController controller;

	public MainWindow(MainController controller) {
		this.controller = controller;

		setTitle("Projet NFA035 - Gestion des contacts");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 500);
		setLocationRelativeTo(null);

		JPanel contentPane = new JPanel(new BorderLayout());

		// Title
		JLabel titleLabel = new JLabel("Gestion des contacts", JLabel.CENTER);
		Font titleFont = new Font("SansSerif", Font.BOLD, 20);
		titleLabel.setFont(titleFont);
		titleLabel.setForeground(Color.GRAY);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		contentPane.add(titleLabel, BorderLayout.NORTH);

		// Left button panel
		JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
		buttonPanel.setPreferredSize(new Dimension(140, 100));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 10));

		contactsButton = new JButton("Contacts");
		groupsButton = new JButton("Groups");
		contactsButton.setFocusPainted(false);

		buttonPanel.add(contactsButton);
		buttonPanel.add(groupsButton);
		contentPane.add(buttonPanel, BorderLayout.WEST);

		// Main panel
		mainPanel = new JPanel();
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		contentPane.add(mainPanel, BorderLayout.CENTER);

		// Listeners
		contactsButton.addActionListener(e -> controller.showContactWindow());
		groupsButton.addActionListener(e -> controller.showGroupWindow());

		setContentPane(contentPane);
		setVisible(true);
	}

}
