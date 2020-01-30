package boss;

import java.awt.Graphics;

import java.awt.Image;
import java.util.function.Function;
import Commons.Common;
import GameFrames.GamePanel;
import MyPoint.Point;
import enemy.Enemy;
import network.ServerPlayer;

public class Boss extends Enemy {
	private static final long serialVersionUID = 1L;
	long lastTime;
	public static final int W = 300, H = 400;
	public static Image boss = Common.createImage(W, H, "resources\\Images\\Chickens\\boss.png");
	public BossWeapon weapon;
	int levelNum;
	float health;
	private Function<Point, Point> groupPattern;
	public boolean reached = false;

	public Boss(int levelNum) {
		image = boss;
		w = W;
		h = H;
		r = W / 2;
		this.levelNum = levelNum;
		health = 250 * levelNum;
		if (GamePanel.player != null) {
			health = (int) Math.floor(Math.sqrt(ServerPlayer.numOfClients + 1)) * 250 * levelNum;
		}
		lastTime = System.currentTimeMillis();
		weapon = new BossWeapon();
		weapon.setId(-1);
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(boss, (int) x, (int) y, null);
		if (System.currentTimeMillis() - lastTime >= 500) {
			fire();
		}
	}

	@Override
	public void setCenterPoint(Point point) {
		xCenter = point.getX();
		yCenter = point.getY();
		y = yCenter + r - h;
		x = xCenter - r;
	}

	@Override
	public void move() {
		Point point = groupPattern.apply(new Point(x, y));
		x += point.getX();
		y += point.getY();
		xCenter += point.getX();
		yCenter += point.getY();
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
		if (health <= 0) {
			this.health = 0;
			visible = false;
		}
	}

	public Function<Point, Point> getGroupPattern() {
		return groupPattern;
	}

	public void setGroupPattern(Function<Point, Point> groupPattern) {
		this.groupPattern = groupPattern;
	}

	public void fire() {
		if (reached) {
			weapon.setBeginPoint(new java.awt.Point((int) xCenter, (int) yCenter));
			weapon.add(GamePanel.spaceShip.getBullets());
			lastTime = System.currentTimeMillis();
		}
	}
}
