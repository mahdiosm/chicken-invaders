package MainFrame;

import java.awt.Color;

import static Commons.Common.createButton;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Commons.Common;
import Commons.IO;
import Users.User;

public class UserPanel extends Panel implements MouseListener, ListSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel usersBoard, buttons;
	Image back;
	JButton quit, add, delete, enter;
	JLabel quitText, addText, deleteText, enterText;
	JList<String> userList;
	DefaultListModel<String> model;
	JScrollPane scrol;
	public ArrayList<User> users;

	public UserPanel() {
		init();

	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(back, 0, 0, null);
		paintChildren(g);
	}

	private void init() {
		createBack();
		setForeground(new Color(0, 0, 0, 0));
		setSize(MainMenu.Width, MainMenu.Height);
		setLayout(null);
		createButtons();
		createUserBoard();
		add(buttons);
		add(usersBoard);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					userList.setSelectedIndex((userList.getSelectedIndex() + users.size() - 1) % users.size());
				}
				if(e.getKeyCode() == KeyEvent.VK_DOWN) {
					userList.setSelectedIndex((userList.getSelectedIndex() + 1) % users.size());
				}
			}
		});
	}

	private void createBack() {
		back = Common.createImage(MainMenu.Width, MainMenu.Height, "resources\\Images\\zerothFrame.jpg");
	}

	private void createButtons() {
		buttons = new JPanel();
		buttons.setSize(MainMenu.Width, MainMenu.Height / 4 + 50);
		buttons.setLayout(null);
		buttons.setLocation(0, MainMenu.Height * 3 / 4 - 50);
		buttons.setBackground(new Color(0, 0, 0, 0));
		createAndAddButtons(buttons);
	}

	private void createAndAddButtons(JPanel p2) {
		int w = 200, h = 70;

		quit = createButton((MainMenu.Width - w) / 2 - 450, (p2.getHeight() - h) / 2, w, h);
		quit.addMouseListener(this);
		quitText = Common.createLabel(w, h, "QUIT");
		quit.add(quitText);

		add = createButton((MainMenu.Width - w) / 2 - 150, (p2.getHeight() - h) / 2, w, h);
		add.addMouseListener(this);
		addText = Common.createLabel(w, h, "ADD");
		add.add(addText);

		delete = createButton((MainMenu.Width - w) / 2 + 150, (p2.getHeight() - h) / 2, w, h);
		delete.addMouseListener(this);
		deleteText = Common.createLabel(w, h, "DELETE");
		delete.add(deleteText);

		enter = createButton((MainMenu.Width - w) / 2 + 450, (p2.getHeight() - h) / 2, w, h);
		enter.addMouseListener(this);
		enterText = Common.createLabel(w, h, "ENTER");
		enter.add(enterText);
		addButtons(p2);
	}

	private void addButtons(JPanel p2) {
		p2.add(add);
		p2.add(enter);
		p2.add(quit);
		p2.add(delete);
		repaint();
	}

	private JPanel createUserBoard() {
		usersBoard = new JPanel();
		usersBoard.setLocation(0, 0);
		usersBoard.setSize(MainMenu.Width, MainMenu.Height * 3 / 4);
		usersBoard.setBackground(new Color(0, 0, 0, 0));
		usersBoard.setLayout(null);
		CreateList(usersBoard);
		addUsers(usersBoard);
		return usersBoard;
	}

	private void CreateList(JPanel usersBoard) {
		model = new DefaultListModel<>();
		userList = new JList<>(model);
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userList.setSelectedIndex(0);
		userList.addListSelectionListener(this);
		userList.setLayoutOrientation(JList.VERTICAL);
		userList.setVisibleRowCount(-1);
		userList.setFont(new Font("arial", Font.BOLD, 50));
//		userList.setFont(myFont.deriveFont(Font.BOLD, 30f));
		userList.setForeground(Color.white);
		userList.setBackground(new Color(0, 0, 0, 0));
		userList.setBorder(null);
		scrol = new JScrollPane(userList);
		scrol.setSize(MainMenu.Width / 2, MainMenu.Height * 3 / 4 - 50);
		scrol.setLocation(50, 50);
		scrol.setBackground(new Color(0, 0, 0, 0));
		scrol.setBorder(null);
		usersBoard.add(scrol);
	}

	private void addUsers(JPanel usersBoard) {
		users = IO.readUsersData();

		for (int i = 0; i < users.size(); i++) {
			model.addElement(users.get(i).getName());
		}
		checkUsersNum();
		userList.setSelectedIndex(0);
	}

	public void addUser(String name) {
		model.addElement(name);
		userList.setSelectedIndex(model.getSize() - 1);
		checkUsersNum();
	}

	public void removeUser(int index) {
		model.removeElementAt(index);
		userList.setSelectedIndex(0);
		checkUsersNum();
	}

	public void checkUsersNum() {
		if (model.getSize() >= 8) {
			Common.setPanelEnable(add, false);
		} else {
			Common.setPanelEnable(add, true);
		}
		if (model.getSize() == 0) {
			Common.setPanelEnable(delete, false);
			Common.setPanelEnable(enter, false);
		} else {
			Common.setPanelEnable(delete, true);
			Common.setPanelEnable(enter, true);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		requestFocusInWindow();
		if (e.getSource() == add && add.isEnabled()) {
			showAddingPanel();
		} else if (e.getSource() == delete && delete.isEnabled()) {
			users.remove(userList.getSelectedIndex());
			IO.saveUsersData(users);
			int index = userList.getSelectedIndex();
			removeUser(userList.getSelectedIndex());
			userList.setSelectedIndex(index - 1);
			repaint();
		} else if (e.getSource() == quit && quit.isEnabled()) {
			MainFrame.mainFrame.dispose();
		} else if (e.getSource() == enter && enter.isEnabled()) {
			MainMenu.user = users.get(userList.getSelectedIndex());
			MainMenu.userIndex = userList.getSelectedIndex();
			MainFrame.ChangeCurrentPanelWith(new MainMenu());
		}
	}

	private void showAddingPanel() {
		AddUserPanel addUserPanel = new AddUserPanel(400, 300);
		addUserPanel.setLastPanel(this);
		Common.setPanelEnable(this, false);
		add(addUserPanel);
		setComponentZOrder(usersBoard, 1);
		setComponentZOrder(addUserPanel, 0);
		addUserPanel.requestFocusInWindow();
		revalidate();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		repaint();
	}
}
