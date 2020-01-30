package levels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import Commons.Common;
import GameFrames.Drawable;

public class SafePlace extends Drawable {
	public Image safe;

	public SafePlace(int x, int y, int r) {
		this.x = x;
		this.y = y;
		this.r = r;
		safe=Common.createImage(5 * r / 3, 25 * r / 36, "resources\\Images\\zones\\safe.png");
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.GREEN);
		g.drawOval((int) x - r, (int) y - r, (int) r * 2, (int) r * 2);
		g.drawImage(safe, (int) x - safe.getWidth(null) / 2, (int) y - safe.getHeight(null) / 2, null);
	}

	@Override
	public void move() {
	}

}
