package powerUp;

import java.awt.Graphics;
import java.awt.Image;
import Commons.Common;
import GameFrames.SpaceShip;

public class Coin extends PowerUp {
	public static final int W = 30, H = 30;
	public static Image coin = Common.createImage(W, H, "resources\\Images\\powerUps\\coin.png");
	public Coin() {
		index = 0;
		w = W;
		h = H;
		r = W;
	}

	@Override
	public void action(SpaceShip p) {
		int coinNumber = p.getScoreOfCoins() + 1;
		p.setScoreOfCoins(coinNumber);
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(coin, (int) x, (int) y, null);
	}

}
