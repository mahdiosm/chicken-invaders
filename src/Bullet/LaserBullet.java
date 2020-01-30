package Bullet;

import java.awt.Graphics;
import java.awt.Image;
import Commons.Common;
import GameFrames.GamePanel;
import collision.Collision;
import levels.Level;

public class LaserBullet extends Bullet {
	private static final long serialVersionUID = 1L;
	long beginTime;

	public LaserBullet(Image image, int x, int y, int speed, double angle, float power, int id) {
		super(image, x, y, speed, angle, power, id);
		r = w / 2;
		xCenter = x + r;
		yCenter = y + 2 * r;
		checkFirstCollision();
//		createNewImage();
		beginTime = System.currentTimeMillis();
	}

//	private void createNewImage() {
//		image = Common.resize(image, w, h).getScaledInstance(w, h, Image.SCALE_SMOOTH);
//	}

	private void checkFirstCollision() {
		Level level = GamePanel.level;
		while (true) {
			if (Collision.laserBullet_firstCollisionCheck(GamePanel.spaceShip, level, this)) {
				y -= 5;
				yCenter -= 5;
				h += 5;
				break;
			} else if (yCenter < 0) {
				break;
			}
			yCenter -= 1;
			y -= 1;
			h += 1;
		}
	}

	@Override
	public void move() {
		if (System.currentTimeMillis() - beginTime >= 200) {
			visible = false;
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(image, (int) x, (int) y, w, h, null);
	}
}
