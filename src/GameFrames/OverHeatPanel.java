package GameFrames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import MainFrame.MainMenu;
import MainFrame.Panel;

public class OverHeatPanel extends Panel {
//	/**
//	 * 
//	 */
	private static final long serialVersionUID = 1L;
	Image heatAndScore;
	ArrayList<Rectangle> rectangles;
	ArrayList<Color> colors;
	double temp;
	JLabel score;
	private int maxHeat;
	private double cons;

	public OverHeatPanel() {
		try {
			setLocation(0, 0);
			maxHeat = GamePanel.spaceShip.getFinalTemp();
			rectangles = new ArrayList<>();
			colors = new ArrayList<>();
			score = new JLabel(String.valueOf(MainMenu.user.getScore()));
			score.setBounds(10, 0, 150, 50);
			score.setHorizontalTextPosition(SwingConstants.LEFT);
			score.setVerticalAlignment(SwingConstants.CENTER);
			score.setFont(new Font("serif", Font.BOLD, 20));
			score.setForeground(Color.BLACK);
			add(score);
			initRects();
			BufferedImage bi = ImageIO.read(new File("resources\\Images\\HeatAndScoreLabel.png"));
			heatAndScore = bi.getScaledInstance(400, 50, Image.SCALE_SMOOTH);
			Dimension dim = new Dimension(heatAndScore.getWidth(null), heatAndScore.getHeight(null));
			setSize(dim);
			setPreferredSize(dim);
			setLayout(null);
		} catch (IOException e) {
		}
	}

	private void initRects() {
		for (int i = 0; i < 20; i++) {
			Rectangle rectangle = new Rectangle(175 + 10 * i, 10, 7, 30);
			Color color = new Color((int) (12.5 * i), (int) (250 - 12.5 * i), 0);
			rectangles.add(rectangle);
			colors.add(color);

		}

	}

	public void setTemp(double temp) {
		this.temp = temp;
	}

	@Override
	protected void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(heatAndScore, 0, 0, null);
		drawRects(g2d);

		paintChildren(g2d);

	}

	private void drawRects(Graphics2D g2d) {
		int temP = (int) GamePanel.spaceShip.getTemp();
		cons = 1f*GamePanel.spaceShip.getFinalTemp() / 20;
		for (int i = 1; i <= 20; i++) {
			if (cons * i <= temP) {
				g2d.setColor(colors.get(i - 1));
			} else {
				g2d.setColor(new Color(0, 0, 255, 80));
			}
			g2d.fill(rectangles.get(i - 1));
		}
	}

	public int getMaxHeat() {
		return maxHeat;
	}

	public void setMaxHeat(int maxHeat) {
		this.maxHeat = maxHeat;
	}
}
