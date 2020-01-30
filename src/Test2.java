import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Test2 extends JFrame {
	ImageIcon image;
	JLabel l;

	public static void main(String[] args) {
		new Test2();
	}

	public Test2() {
		image = new ImageIcon("Images\\rocket.png");
		l = new JLabel(image);
		l.setBounds(40, 40, 100, 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setBounds(0, 0, 700, 800);
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

	}
}
