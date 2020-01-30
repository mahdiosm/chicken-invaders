package powerUp;

import java.awt.Graphics;
import java.awt.Image;

import Commons.Common;
import GameFrames.SpaceShip;

public class MaxHeatChanger extends PowerUp {
	public static final int W = 40, H = 40;
	public static Image maxHeatChanger = Common.createImage(W, H, "resources\\Images\\powerUps\\maxHeatChanger.png");

	public MaxHeatChanger() {
		index = 1;
		w = W;
		h = H;
		r = W;
	}

	@Override
	public void action(SpaceShip p) {
		p.setFinalTemp(p.getFinalTemp() + 5);
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(maxHeatChanger, (int) x, (int) y, null);
	}
}
