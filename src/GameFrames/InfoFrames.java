package GameFrames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import MainFrame.MainMenu;
import MainFrame.Panel;

public class InfoFrames extends Panel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Image info, coin, power, heart, rocket;
	JLabel coinC, powerC, heartC, rocketC;
	int startX, startY, h = 40, w = 50;

	public InfoFrames() {
		startX = 0;
		startY = MainMenu.Height - w;
		setLocation(startX, startY);
		setOpaque(false);
		info = InfoFrames.createImage("resources\\Images\\info.png", 370, h + 15);
		heart = createImage("resources\\Images\\heart.png", w, h);
		coin = createImage("resources\\Images\\powerUps\\coin.png", w, h);
		power = createImage("resources\\Images\\power.png", w, h);
		rocket = createImage("resources\\Images\\rocket.png", w, h);
		createLabels();
		Dimension dim = new Dimension(info.getWidth(null), info.getHeight(null));
		setSize(dim);
		setPreferredSize(dim);
		setLayout(null);

	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		drawImages(g2d);
		paintChildren(g2d);
	}

	private void drawImages(Graphics2D g2d) {
		g2d.drawImage(info, startX-5, startY-5, null);
		startY+=5;
		g2d.drawImage(heart, startX, startY, null);
		g2d.drawImage(rocket, startX + 90, startY, null);
		g2d.drawImage(power, startX + 180, startY, null);
		g2d.drawImage(coin, startX + 270, startY, null);
		startY-=5;
	}

	private void createLabels() {
		coinC = new JLabel(String.valueOf(MainMenu.user.getCoin()));
		coinC.setFont(new Font("serif", Font.BOLD, 20));
		coinC.setHorizontalTextPosition(SwingConstants.LEFT);
		coinC.setForeground(Color.BLACK);
		coinC.setBounds(startX + 320, startY, 40, 40);
		add(coinC);

		powerC = new JLabel(String.valueOf(MainMenu.user.getPower()));
		powerC.setFont(new Font("serif", Font.BOLD, 20));
		powerC.setHorizontalTextPosition(SwingConstants.LEFT);
		powerC.setForeground(Color.BLACK);
		powerC.setBounds(startX + 230, startY, 40, 40);
		add(powerC);

		heartC = new JLabel(String.valueOf(MainMenu.user.getHeart()));
		heartC.setFont(new Font("serif", Font.BOLD, 20));
		heartC.setHorizontalTextPosition(SwingConstants.LEFT);
		heartC.setForeground(Color.BLACK);
		heartC.setBounds(startX + 50, startY, 40, 40);
		add(heartC);

		rocketC = new JLabel(String.valueOf(MainMenu.user.getRocket()));
		rocketC.setFont(new Font("serif", Font.BOLD, 20));
		rocketC.setHorizontalTextPosition(SwingConstants.LEFT);
		rocketC.setForeground(Color.BLACK);
		rocketC.setBounds(startX + 140, startY, 40, 40);
		add(rocketC);
	}

	public static Image createImage(String string, int width, int height) {
		BufferedImage bi;
		Image result = null;
		try {
			bi = ImageIO.read(new File(string));
			result = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		} catch (IOException e) {
		}
		return result;

	}
}
