package GameFrames;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import Commons.Common;
import MainFrame.MainMenu;
import boss.Boss;
import chickens.Chicken;
import chickensGroups.GroupOfBoss;
import levels.BossWave;
import levels.Level;

public class Rocket extends Drawable {
	private static final long serialVersionUID = 1L;
	public static Image rocket;
	final public static int W = 70, H = 70;
	public double dx, dy;
	public double angle;

	public Rocket(int x, int y, double angle) {
		visible = false;
		w = W;
		h = H;
		r = Math.min(W, H);
		this.angle = angle;
		rocket = Common.createImage(w, h, "resources\\\\Images\\\\rocket.png");
		visible = true;
		this.x = x;
		this.y = y;
		xCenter = x + w / 2;
		yCenter = y + h / 2;
		dx = 2 * Math.cos(angle);
		dy = 2 * Math.sin(angle);
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform at = AffineTransform.getTranslateInstance(xCenter - w / 2, yCenter - h / 2);
		at.rotate(angle + Math.PI / 2, w / 2, h / 2);
		g2d.drawImage(rocket, at, null);
	}

	@Override
	public void move() {
		x = (float) (x + dx);
		y = (float) (y + dy);
		xCenter += dx;
		yCenter += dy;
		if (Math.abs(xCenter - MainMenu.Width / 2) <= 1 && Math.abs(yCenter - MainMenu.Height / 2) <= 1) {
			visible = false;
			Level level = GamePanel.level;
			if (level instanceof BossWave) {
				Boss boss = ((GroupOfBoss) level.getGroup()).getBoss();
				boss.setId(getId());
				boss.setHealth(boss.getHealth() - 50);
			} else {
				for (Chicken chicken : level.getGroup().getChickens()) {
					chicken.setId(getId());
					chicken.setHealth(0);
					GamePanel.spaceShip.setScore(GamePanel.spaceShip.getScore() + chicken.getFirstHealth());
				}
			}
		}
	}

}
