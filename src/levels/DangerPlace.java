package levels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import Commons.Common;
import GameFrames.Drawable;

public class DangerPlace extends Drawable {
	public static Image danger;

	public DangerPlace(int x, int y, int r) {
		this.x = x;
		this.y = y;
		this.r = r;
		danger = Common.createImage(5 * r / 3, 25 * r / 36, "resources\\Images\\zones\\danger.png");
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.drawOval((int) x - r, (int) y - r, (int) r * 2, (int) r * 2);
		g.drawImage(danger, (int) x - danger.getWidth(null) / 2, (int) y - danger.getHeight(null) / 2, null);
	}

	@Override
	public void move() {
	}
}
