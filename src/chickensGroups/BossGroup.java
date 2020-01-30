package chickensGroups;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;
import GameFrames.GamePanel;
import GameFrames.SpaceShip;
import MainFrame.GameMode;
import MainFrame.MainMenu;
import MyPoint.Point;
import boss.Boss;
import chickens.Chicken;
import levels.Level;
import network.ServerPlayer;

public class BossGroup extends GroupOfBoss {
	private static final long serialVersionUID = 1L;
	boolean showedMessage = false;
	long beginTimeOfMessage;
	int levelNum;

	public BossGroup() {
		this.levelNum = Level.levelCode / 10;
		entered = false;
		boss = new Boss(levelNum);
		delta = 5;
		pattern = a -> new Point(0, delta);
		takePosition();
		setChickensPattern();
	}

	@Override
	public void startEggProbebility() {
	}

	@Override
	public void checkEntered() {
		if (Math.sqrt(Math.pow(y - MainMenu.Height / 2 + 100, 2)) <= delta) {
			entered = true;
			boss.reached = true;
			y = MainMenu.Height / 2 - 100;
		}
	}

	@Override
	protected void takePosition() {
		x = 300;
		y = -500;
		boss.setCenterPoint(new Point(MainMenu.Width / 2, y));
	}

	@Override
	public void move() {
		if (!entered) {
			checkEntered();
			Point point = pattern.apply(new Point(x, y));
			x += point.getX();
			y += point.getY();
			boss.move();
		}
	}

	private boolean firstGift = false;

	@Override
	public boolean finished() {
		if (!boss.isVisible() && !firstGift) {
			beginTimeOfMessage = System.currentTimeMillis();
			firstGift = true;
			SpaceShip.bullets.removeBulletsWithId(-1);
			Random random = new Random();
			for (int i = 0; i < 5; i++) {
				Chicken.createPowerUps(50, 50, 0, random.nextInt(600) + MainMenu.Width / 2 - 300, 0, boss.getId());
			}
			setCoinsZero();
			if (GameMode.type == GameMode.SINGLE) {
				GamePanel.spaceShip.setScore(GamePanel.spaceShip.getScore() + 250 * levelNum);
				GamePanel.spaceShip.setRocket(GamePanel.spaceShip.getRocket() + 1);
			} else {
				SpaceShip spaceShip = ServerPlayer.otherPlayers.get(id);
				spaceShip.setScore(spaceShip.getScore() + 250 * levelNum);
				spaceShip.setRocket(spaceShip.getRocket() + 1);
			}
		}
		return showedMessage;
	}

	private void setCoinsZero() {
		if (GameMode.type == GameMode.SINGLE) {
			GamePanel.spaceShip.setScore(GamePanel.spaceShip.getScore() + GamePanel.spaceShip.getScoreOfCoins() * 3);
			GamePanel.spaceShip.setScoreOfCoins(0);
		} else {
			SpaceShip spaceShip = ServerPlayer.otherPlayers.get(id);
			spaceShip.setScore(spaceShip.getScore() + spaceShip.getScoreOfCoins() * 3);
			spaceShip.setScoreOfCoins(0);
		}
	}

	@Override
	public void render(Graphics g) {
		if (boss.isVisible()) {
			if (entered) {
				g.setColor(Color.white);
				g.fillRect(MainMenu.Width / 2 - 250, 50, 500, 20);
				float x = boss.getHealth() / (250 * levelNum);
				g.setColor(new Color((int) (255 - x * 254), (int) (x * 254), 0));
				g.fillRect(MainMenu.Width / 2 - 250, 50, (int) (500 * x), 20);
			}
			boss.render(g);
		} else if (!showedMessage) {
			if (beginTimeOfMessage != 0 && System.currentTimeMillis() - beginTimeOfMessage >= 3000) {
				showedMessage = true;
			}
			g.setFont(new Font("serif", Font.BOLD, 60));
			g.setColor(Color.green);
			g.drawString("CONGRATULATIONS", MainMenu.Width / 2 - 300, MainMenu.Height / 2 - 50);
		}
	}

	@Override
	protected void setChickensPattern() {
		boss.setGroupPattern(this.pattern);
	}
}
