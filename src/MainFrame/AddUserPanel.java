package MainFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import Commons.Common;
import Commons.IO;
import Users.User;

public class AddUserPanel extends Panel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String name;
	public static int w, h;
	private JButton add, cancel;
	public JTextField txt;
	public Graphics graphics;

	public AddUserPanel(int w1, int h1) {
		w = w1;
		h = h1;
		init();
	}

	void init() {
		setBounds(MainMenu.Width / 2 - w / 2, MainMenu.Height / 2 - h / 2, w, h);
		setLayout(null);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER && txt.getText() != null && txt.getText().length() > 0) {
					addUser();
				}
				if (e.getKeyChar() >= (char) (32)) {
					txt.setText(txt.getText() + e.getKeyChar());
				}
			}
		});
		addComps();
		revalidate();
	}

	private void addComps() {
		createAddButton();
		createCancelButton();
		createTxtField();
	}

	private void createCancelButton() {
		Dimension dim = new Dimension(w / 2, h / 2);
		cancel = new JButton("CANCEL");
		cancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				doings();
			}
		});
		cancel.setBackground(new Color(100, 200, 150));
		cancel.setPreferredSize(dim);
		cancel.setSize(dim);
		cancel.setLocation(w / 2, h / 2);
		cancel.setFont(new Font("serif", Font.BOLD | Font.ITALIC, 30));
		add(cancel);
	}

	private void createTxtField() {
		Dimension dim = new Dimension(w, h / 2);
		txt = new JTextField();
		txt.setSize(dim);
		txt.setPreferredSize(dim);
		txt.setLocation(0, 0);
		txt.setFont(new Font("arial", 0, 40));
		txt.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				if (txt.getText() == null || txt.getText().equals("")) {
					add.setEnabled(false);
				} else {
					add.setEnabled(true);
				}
			}
		});
		add(txt);
	}

	private void createAddButton() {
		Dimension dim = new Dimension(w / 2, h / 2);
		add = new JButton("ADD");
		add.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				addUser();
			}
		});
		add.setBackground(new Color(100, 200, 150));
		add.setEnabled(false);
		add.setPreferredSize(dim);
		add.setSize(dim);
		add.setLocation(0, h / 2);
		add.setFont(new Font("serif", Font.BOLD | Font.ITALIC, 30));
		add(add);
	}

	private void addUser() {
		((UserPanel) getLastPanel()).users.add(new User(txt.getText()));
		Common.setPanelEnable((JPanel) getLastPanel(), true);
		((UserPanel) getLastPanel()).addUser(txt.getText());
		IO.saveUsersData(((UserPanel) getLastPanel()).users);
		getLastPanel().remove(AddUserPanel.this);
		getLastPanel().requestFocusInWindow();
		getLastPanel().repaint();
	}

	void doings() {
		getLastPanel().remove(AddUserPanel.this);
		Common.setPanelEnable((JPanel) MainFrame.mainFrame.getContentPane(), true);
		MainFrame.mainFrame.getContentPane().revalidate();
		((UserPanel) getLastPanel()).checkUsersNum();
		getLastPanel().requestFocusInWindow();
	}
}
