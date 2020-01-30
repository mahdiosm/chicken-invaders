package egg;

import java.awt.Graphics;
import java.awt.Image;

import Commons.Common;
import MainFrame.MainMenu;
import enemy.Enemy;

public class Egg extends Enemy {
	public static final int W = 30, H = 40;
	public static Image egg = Common.createImage(W, H, "resources\\Images\\egg.png");
	private float delta;

	public Egg(int startX, int startY, float delta) {
		w = W;
		h = H;
		r = 15;
		image = egg;
		this.delta = delta;
		this.x = startX;
		this.y = startY;
		xCenter = x + w / 2;
		yCenter = y + h / 2;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(egg, (int) (xCenter - w / 2), (int) (yCenter - h / 2), null);
	}

	public void setDelta(float delta1) {
		delta = delta1;
	}

	@Override
	public void move() {
		y += delta;
		yCenter += delta;
		if (y >= MainMenu.Height) {
			visible = false;
		}
	}

}
