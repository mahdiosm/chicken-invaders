package chickens;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import javax.swing.Timer;
import GameFrames.GamePanel;
import GameFrames.SpaceShip;
import MainFrame.GameMode;
import MyPoint.Point;
import boss.Boss;
import chickensGroups.Group;
import egg.Egg;
import enemy.Enemy;
import network.ServerPlayer;
import powerUp.BulletPowerUp;
import powerUp.Coin;
import powerUp.MaxHeatChanger;
import powerUp.gift.BlueGift;
import powerUp.gift.Gift;
import powerUp.gift.GreenGift;
import powerUp.gift.RedGift;

public class Chicken extends Enemy {
	private boolean entered = false, translating = false;
	protected float health, angle;
	protected int eggPercent, eggDelta = 2;
	private int coinPercent = 5, powerUpPercent = 3, giftPercent = 3;
	protected float destX, destY;
	protected int firstHealth;
	protected Group owner;
	protected AffineTransform at;
	protected Function<Point, Point> selfPattern, groupPattern;
	protected Timer timer;

	{
		visible = false;
	}

	@Override
	public void move() {
		Point p1 = groupPattern.apply(new Point(xCenter, yCenter));
		Point p2 = selfPattern.apply(new Point(xCenter, yCenter));
		x += p1.getX() + p2.getX();
		y += p1.getY() + p2.getY();
		xCenter += p1.getX() + p2.getX();
		yCenter += p1.getY() + p2.getY();
		if (translating) {
			destX += p1.getX() + p2.getX();
			destY += p1.getY() + p2.getY();
			checkReached();
		}
	}

	private void checkReached() {
		if (Math.sqrt(Math.pow(xCenter - destX, 2) + Math.pow(yCenter - destY, 2)) <= 2) {
			setCenterPoint(new Point(destX, destY));
			translating = false;
		}
	}

	public void translateTo(float x, float y) {
		translating = true;
		this.destX = x;
		this.destY = y;
		setPattern(a -> {
			float angle = (float) Math.atan2(y - yCenter, x - xCenter);
			return new Point(Math.cos(angle), Math.sin(angle));
		});
	}

	public Point getPoint() {
		return new Point(x, y);
	}

	public void setPattern(Function<Point, Point> pattern) {
		selfPattern = pattern;
	}

	public void setGroupPattern(Function<Point, Point> pattern) {
		groupPattern = pattern;
	}

	public Group getOwner() {
		return owner;
	}

	public void setOwner(Group owner) {
		this.owner = owner;
	}

	public int getEggDelta() {
		return eggDelta;
	}

	public Chicken setEggDelta(int eggDelta) {
		this.eggDelta = eggDelta;
		return this;
	}

	@SuppressWarnings("unchecked")
	public static Chicken create(int type) {
		String p = "chickens.ChickenLevel";
		Class<Chicken> s = null;
		Chicken chicken = null;
		try {
			s = (Class<Chicken>) Class.forName(p + type);
			Constructor<Chicken> cons = s.getConstructor();
			chicken = (Chicken) cons.newInstance();
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return chicken;
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		if (health <= 0) {
			visible = false;
			if (GameMode.type == GameMode.SINGLE)
				GamePanel.spaceShip.setScore(GamePanel.spaceShip.getScore() + firstHealth);
			else {
				SpaceShip spaceShip = ((ServerPlayer) GamePanel.player).getAllPlayers().get(id);
				spaceShip.setScore(spaceShip.getScore() + firstHealth);
			}
			createPowerUps(giftPercent, powerUpPercent, coinPercent, xCenter, yCenter, id);
		}
		this.health = health;
	}

	public static void createPowerUps(int giftPercent, int powerUpPercent, int coinPercent, float xCenter,
			float yCenter, int id) {
		if (createRandom(giftPercent + powerUpPercent)) {
			if (createRandom(100 * powerUpPercent / (powerUpPercent + giftPercent))) {
				if (createRandom(50)) {
					MaxHeatChanger maxHeatChanger = new MaxHeatChanger();
					maxHeatChanger.setId(id);
					maxHeatChanger.setBeginPoint(xCenter - MaxHeatChanger.W / 2, yCenter - MaxHeatChanger.H / 2);
					GamePanel.spaceShip.getPowerUps().add(maxHeatChanger);
				} else {
					BulletPowerUp bulletPowerUp = new BulletPowerUp();
					bulletPowerUp.setId(id);
					bulletPowerUp.setBeginPoint(xCenter - BulletPowerUp.W / 2, yCenter - BulletPowerUp.H / 2);
					GamePanel.spaceShip.getPowerUps().add(bulletPowerUp);
				}
			} else {
				createRandomGift(xCenter, yCenter, id);
			}
		}
		if (createRandom(coinPercent)) {
			Coin coin = new Coin();
			coin.setId(id);
			coin.setBeginPoint(xCenter - Coin.W / 2, yCenter - Coin.H / 2);
			GamePanel.spaceShip.getPowerUps().add(coin);
		}
	}

	private static void createRandomGift(float xCenter, float yCenter, int id) {
		Gift gift = null;
		if (createRandom(40))
			gift = new RedGift();
		else if (createRandom(50))
			gift = new GreenGift();
		else
			gift = new BlueGift();
		gift.setBeginPoint(xCenter - RedGift.W / 2, yCenter - RedGift.H / 2);
		gift.setId(id);
		GamePanel.spaceShip.getPowerUps().add(gift);
	}

	public static boolean createRandom(int percent) {
		Random random = new Random();
		HashSet<Integer> aHashSet = new HashSet<>();
		for (int i = 0; i < percent; i++) {
			int j = -1;
			while (aHashSet.contains(j)) {
				j = random.nextInt(100);
			}
			aHashSet.add(j);
		}
		int tokhmi = random.nextInt(100);
		return aHashSet.contains(tokhmi);
	}

	protected Chicken setEggPercent(int percent) {
		this.eggPercent = percent;
		return this;
	}

	public int getEggPercent() {
		return eggPercent;
	}

	public void setBeginPoint(float x, float y) {
		this.x = x;
		this.xCenter = x + w / 2;
		this.y = y;
		this.yCenter = y + h / 2;
	}

	public int getFirstHealth() {
		return firstHealth;
	}

	public void setFirstHealth(int firstHealth) {
		this.firstHealth = firstHealth;
	}

	public boolean isEntered() {
		return entered;
	}

	public void setEntered(boolean entered) {
		this.entered = entered;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	@Override
	public void render(Graphics g) {
	}

	public boolean isTranslating() {
		return translating;
	}

	public void setTranslating(boolean translating) {
		this.translating = translating;
	}
}
