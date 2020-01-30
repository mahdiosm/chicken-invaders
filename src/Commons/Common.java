package Commons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import MainFrame.MainMenu;

/**
 * @author mahdiosm
 *
 */
public class Common {
	public static void changePanel(JPanel first, JPanel last) {
		last.setLocation(-MainMenu.Width, 0);
		Timer timer = new Timer(5, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				first.setLocation(first.getX() + 70, first.getY());
				last.setLocation(last.getX() + 70, last.getY());
				if (first.getX() > MainMenu.Width) {
					last.setLocation(0, 0);
					MainMenu.frame.getContentPane().removeAll();
					MainMenu.frame.getContentPane().add(last);
					((Timer) e.getSource()).stop();
					last.repaint();
				}
			}
		});
		timer.start();
	}

	public static JLabel createLabel(int w, int h, String string) {
		JLabel l = new JLabel(string);
		l.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		l.setBounds(0, 0, w, h);
		l.setFont(new Font("serif", 0, 50));
		l.setForeground(Color.BLACK);
		return l;
	}

	public static JButton createButton(int i, int j, int w, int h, ImageIcon icon) {
		JButton button = new JButton();
		button.setBackground(new Color(0, 0, 0, 0));
		button.setBounds(i, j, w, h);
		button.setIcon(icon);
		button.setContentAreaFilled(false);
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setBorderPainted(false);
		button.setIconTextGap(0);
		button.setBorder(null);
		return button;
	}


	public static JButton createButton(int i, int j, int w, int h) {
		JButton button = new JButton();
		button.setBackground(new Color(220, 150, 0));
		button.setBounds(i, j, w, h);
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setBorderPainted(false);
		button.setIconTextGap(0);
		button.setBorder(null);
		return button;
	}
	
	public static Image createImage(int w, int h, String dir){
		BufferedImage bi=null;
		try {
			bi = ImageIO.read(new File(dir));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bi.getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING);
	}
	
	public static BufferedImage resize(Image img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

	public static void setPanelEnable(JComponent p, boolean b) {
		p.setEnabled(b);
		Component[] components = p.getComponents();
		for (Component component : components) {
			if (component instanceof JComponent) {
				setPanelEnable((JComponent) component, b);
			}
		}
	}

	public static void remove(JPanel parent, JPanel child) {
		parent.remove(child);
	}
}
