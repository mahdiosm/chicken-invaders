package network;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import Bullet.Bullet;
import GameFrames.GamePanel;
import GameFrames.Rocket;
import GameFrames.SpaceShip;
import Weapons.Weapon;
import boss.Boss;
import chickens.Chicken;
import chickensGroups.GroupOfBoss;
import egg.Egg;
import enemy.Enemy;
import levels.Level;
import powerUp.PowerUp;

public class InfoForDraw implements Serializable {
	private static final long serialVersionUID = 1L;

	// informations of other players
	int[] spaceShipsX = new int[0], spaceShipsY = new int[0], kindOfColor = new int[0];
	boolean[] spaceShipsVisible = new boolean[0];

	// informations of HeatPanel
	int score, heat;

	// informations of InfoPanel
	int coin, power, rocket, heart;
	boolean visible;

	// informations of enemies
	int[] enemiesX = new int[0], enemiesY = new int[0], kindOfEnemy = new int[0];

	// informations of bullets
	int[] bulletsXCenter = new int[0], bulletsYCenter = new int[0], bulletsWidth = new int[0],
			bulletsHeigth = new int[0], kindOfBullet = new int[0];
	float[] bulletsAngle = new float[0];

	// informations of powerUps
	int[] powerUpsX = new int[0], powerUpsY = new int[0], kindOfPowerUp = new int[0];

	// informations of rockets
	int[] rocketsXCenter = new int[0], rocketsYCenter = new int[0];
	float[] rocketsAngle = new float[0];

	transient Executor executor = Executors.newCachedThreadPool();
	transient GetInfo getInfo = new GetInfo();
	transient ServerPlayer serverPlayer;
	transient SpaceShip otherSpaceShip;

	public InfoForDraw(ServerPlayer serverPlayer) {
		this.serverPlayer = serverPlayer;
	}

	public void determineInfo(ServerPlayer player, SpaceShip otherSpaceShip) {
		this.otherSpaceShip = otherSpaceShip;
		executor.execute(getInfo);
	}

	class GetInfo implements Runnable {

		@Override
		public void run() {
			getSpaceShipsInfo();
			getHeatPanelInfo();
			getInfoPanelInfo();
			getCheckensInfo();
			getBulletsInfo();
			getPowerUpsInfo();
			getRocketsInfo();
		}

		private void getRocketsInfo() {
			ArrayList<Rocket> rockets = GamePanel.spaceShip.getRockets().getRockets();
			int size = rockets.size();
			rocketsXCenter = new int[size];
			rocketsYCenter = new int[size];
			rocketsAngle = new float[size];
			for (int i = 0; i < size; i++) {
				Rocket rocket = rockets.get(i);
				rocketsXCenter[i] = (int) rocket.getCenterPoint().getX();
				rocketsYCenter[i] = (int) rocket.getCenterPoint().getY();
				rocketsAngle[i] = (float) rocket.angle;
			}
		}

		private void getInfoPanelInfo() {
			visible = otherSpaceShip.isVisible();
			heart = otherSpaceShip.getHeart();
			power = otherSpaceShip.getPower();
			rocket = otherSpaceShip.getRocket();
			coin = otherSpaceShip.getScoreOfCoins();
		}

		private void getPowerUpsInfo() {
			Vector<PowerUp> powerUps = GamePanel.spaceShip.getPowerUps();
			int size = GamePanel.spaceShip.getPowerUps().size();
			powerUpsX = new int[size];
			powerUpsY = new int[size];
			kindOfPowerUp = new int[size];
			for (int i = 0; i < size; i++) {
				powerUpsX[i] = (int) powerUps.get(i).getX();
				powerUpsY[i] = (int) powerUps.get(i).getY();
				kindOfPowerUp[i] = powerUps.get(i).getIndex();
			}
		}

		private void getBulletsInfo() {
			ArrayList<Bullet> bullets = GamePanel.spaceShip.getBullets().getBullets();
			int size = bullets.size();
			bulletsXCenter = new int[size];
			bulletsWidth = new int[size];
			bulletsHeigth = new int[size];
			bulletsYCenter = new int[size];
			bulletsAngle = new float[size];
			kindOfBullet = new int[size];
			for (int i = 0; i < size; i++) {
				Bullet bullet = bullets.get(i);
				bulletsXCenter[i] = (int) bullet.getCenterPoint().getX();
				bulletsYCenter[i] = (int) bullet.getCenterPoint().getY();
				bulletsWidth[i] = bullet.getWidth();
				bulletsHeigth[i] = bullet.getHeight();
				bulletsAngle[i] = (float) bullet.getAngle();
				kindOfBullet[i] = bullet.getIndex();
			}
		}

		private void getCheckensInfo() {
			Level level = GamePanel.level;
			if (level.getGroup() instanceof GroupOfBoss) {
				GroupOfBoss bossGroup = (GroupOfBoss) level.getGroup();
				Boss boss = bossGroup.getBoss();
				enemiesX = new int[] { (int) boss.getX() };
				enemiesY = new int[] { (int) boss.getY() };
				kindOfEnemy = new int[] { boss.getIndex() };

			} else {
				ArrayList<Chicken> chickens = level.getGroup().getChickens();
				Vector<Egg> eggs = level.getGroup().getEggs().getEggs();
				ArrayList<Enemy> enemies = new ArrayList<>();
				enemies.addAll(eggs);
				enemies.addAll(chickens);
				int size = enemies.size();
				enemiesX = new int[size];
				enemiesY = new int[size];
				kindOfEnemy = new int[size];
				for (int i = 0; i < size; i++) {
					Enemy enemy = enemies.get(i);
					enemiesX[i] = (int) enemy.getX();
					enemiesY[i] = (int) enemy.getY();
					kindOfEnemy[i] = enemies.get(i).getIndex();
				}
			}
		}

		private void getHeatPanelInfo() {
			score = otherSpaceShip.getScore();
			heat = (int) otherSpaceShip.getTemp();
		}

		private void getSpaceShipsInfo() {
			ArrayList<SpaceShip> others = new ArrayList<>(ServerPlayer.otherPlayers.values());
			others.add(GamePanel.spaceShip);
			int size = others.size();
			spaceShipsX = new int[size];
			spaceShipsY = new int[size];
			kindOfColor = new int[size];
			spaceShipsVisible = new boolean[size];
			for (int i = 0; i < size; i++) {
				SpaceShip spaceShip = others.get(i);
				if (spaceShip.isSeener() == false) {
					spaceShipsX[i] = (int) spaceShip.getX();
					spaceShipsY[i] = (int) spaceShip.getY();
					kindOfColor[i] = spaceShip.getId();
					spaceShipsVisible[i] = spaceShip.isVisible();
				}
			}
		}
	}

	public void draw(Graphics2D g2d) {
		drawSpaceShips(g2d);
		setInformationsOfSpaceShip(g2d);
		drawChickens(g2d);
		drawBullets(g2d);
		drawRockets(g2d);
		drawPowerUps(g2d);
	}

	private void drawPowerUps(Graphics2D g2d) {
		int size = powerUpsX.length;
		for (int i = 0; i < size; i++) {
			g2d.drawImage(PowerUp.images.get(kindOfPowerUp[i]), powerUpsX[i], powerUpsY[i], null);
		}
	}

	private void drawRockets(Graphics2D g2d) {
		int size = rocketsXCenter.length;
		for (int i = 0; i < size; i++) {
			int xc = rocketsXCenter[i];
			int yc = rocketsYCenter[i];
			float angle = rocketsAngle[i];
			AffineTransform at = AffineTransform.getTranslateInstance(xc - Rocket.W / 2, yc - Rocket.H / 2);
			at.rotate(angle + Math.PI / 2, Rocket.W / 2, Rocket.H / 2);
			g2d.drawImage(Rocket.rocket, at, null);
		}
	}

	private void drawBullets(Graphics2D g2d) {
		int size = bulletsXCenter.length;
		for (int i = 0; i < size; i++) {
			int xc = bulletsXCenter[i];
			int yc = bulletsYCenter[i];
			int w = bulletsWidth[i];
			int h = bulletsHeigth[i];
			float angle = bulletsAngle[i];
			AffineTransform at = AffineTransform.getTranslateInstance(xc - w / 2, yc - h / 2);
			at.rotate(angle + Math.PI / 2, w / 2, h / 2);
			g2d.drawImage(Weapon.IMAGES.get(kindOfBullet[i]), at, null);
		}
	}

	private void drawChickens(Graphics2D g2d) {
		int size = enemiesX.length;
		for (int i = 0; i < size; i++) {
			g2d.drawImage(Enemy.IMAGES.get(kindOfEnemy[i]), enemiesX[i], enemiesY[i], null);
		}
	}

	private void setInformationsOfSpaceShip(Graphics2D g2d) {
		SpaceShip spaceShip = GamePanel.spaceShip;
		spaceShip.onlySetVisiblity(visible);
		spaceShip.setScore(score);
		spaceShip.setHeart(heart);
		spaceShip.setScoreOfCoins(coin);
		spaceShip.setPower(power);
		spaceShip.setTemp(heat);
		spaceShip.setRocket(rocket);
	}

	private void drawSpaceShips(Graphics2D g2d) {
		int size = spaceShipsX.length;
		for (int i = 0; i < size; i++) {
			if (spaceShipsX[i] != 0) {
				if (spaceShipsVisible[i])
					g2d.drawImage(SpaceShip.spaceShip, spaceShipsX[i], spaceShipsY[i], null);
				int xc = spaceShipsX[i] + GamePanel.spaceShip.getWidth() / 2;
				int yc = spaceShipsY[i] + GamePanel.spaceShip.getHeight() / 2;
				g2d.setColor(ServerPlayer.colors.get(kindOfColor[i]));
				g2d.fillOval(xc - 12, yc - 12, 24, 24);
			}
		}
	}
}