package GameFrames;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.Vector;
import javax.swing.Timer;
import Bullet.Bullets;
import Commons.Common;
import MainFrame.GameMode;
import MainFrame.HighScorePanel;
import MainFrame.MainFrame;
import MainFrame.MainMenu;
import Users.User;
import Weapons.Weapon;
import Weapons.Blaster.Blaster0;
import network.MessageManager;
import network.ServerPlayer;
import powerUp.PowerUp;

public class SpaceShip extends Drawable implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean seener = false;
	public boolean paused = false;
	private long LasttimeMilliSecond, timeOfDeath, timeOfBearth;
	private int finalTemp = 100;
	public transient GamePanel gamePanel;
	public boolean isOverHeat = false, shooting = true;
	private int restTime = 4000;
	private boolean heatAble = true;
	private double temp = 0, dTemp = 0.04, dtempWhenOverHeat = 0.04;
	private int heart = 5, weaponType = 1, power = 0, rocket = 3;
	private int score = 0;
	public transient static Bullets bullets;
	private transient Vector<PowerUp> powerUps = new Vector<>();
	private transient Weapon weapon;
	public transient static Timer timer, overHeatTimer;
	private transient Rockets rockets;
	private transient int scoreOfCoins;
	public transient static Image spaceShip = Common.createImage(130, 130, "resources\\Images\\space ship.png");
	private transient boolean ready = true;
	private String name;
	// for server

	public SpaceShip() {
		w = 130;
		h = 130;
		r = 50;
		spaceShip = Common.createImage(w, h, "resources\\Images\\space ship.png");
		rockets = new Rockets();
		bullets = new Bullets();
		if (GameMode.type == GameMode.SINGLE) {
			MainMenu.user.setBeginingOfGame(System.currentTimeMillis());
			id = MainMenu.user.getId();
			score = MainMenu.user.getScore();
			heart = MainMenu.user.getHeart();
			scoreOfCoins = MainMenu.user.getCoin();
			power = MainMenu.user.getPower();
			weaponType = MainMenu.user.getWeaponType();
			rocket = MainMenu.user.getRocket();
			name = MainMenu.user.getName();
			weapon = Weapon.create(weaponType, power);
		} else {
			weaponType = 1;
			weapon = new Blaster0();

		}
	}

	@Override
	public void render(Graphics g) {
		if (visible) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.drawImage(getImage(), (int) (xCenter - w / 2), (int) (yCenter - h / 2), null);
		} else {
			if (System.currentTimeMillis() - timeOfDeath >= 3000) {
				timeOfBearth = System.currentTimeMillis();
				visible = true;
				setX(MainMenu.Width / 2 - w / 2);
				setY(MainMenu.Height - h);
				GamePanel.setMousePosition(this);
			}
		}
		if (!heatAble && visible && System.currentTimeMillis() - timeOfBearth >= 2000) {
			heatAble = true;
		}
		for (PowerUp powerUp : powerUps) {
			powerUp.render(g);
		}
	}

	@Override
	public void move() {
		for (int i = powerUps.size() - 1; i >= 0; i--) {
			powerUps.get(i).move();
			if (powerUps.get(i).visible == false) {
				powerUps.remove(i);
			}
		}
	}

	public void fire() {
		if (!isOverHeat && shooting) {
			if (GameMode.type != GameMode.SINGLE && GameMode.type != GameMode.SERVER) {
				GamePanel.player.sendMessage(MessageManager.FIRE);
			} else {
				weapon.setBeginPoint(
						new Point((int) (xCenter - weapon.getWidth() / 2), (int) (yCenter - weapon.getHeigth())));
				shooting = false;
				weapon.setId(id);
				timer = new Timer(weapon.timeDelay, e -> {
					shooting = true;
					timer.stop();
				});
				timer.start();
				weapon.add(bullets);
				this.setTemp(temp + weapon.hotNess);
			}
		}
	}

	public void mouseMoved(MouseEvent e) {
		int x = e.getX() - w / 2;
		int y = e.getY() - h / 2;
		if (x >= 0 && MainMenu.Width - x >= w) {
			setX(x);
		}
		if (y >= 0 && MainMenu.Height - y >= h) {
			setY(y);
		}
		if (GameMode.type != GameMode.SERVER && GameMode.type != GameMode.SINGLE)
			GamePanel.player.sendMessage(MessageManager.POSITION);
	}

	public long getLasttimeMilliSecond() {
		return LasttimeMilliSecond;
	}

	public void setLasttimeMilliSecond(long lasttimeMilliSecond) {
		LasttimeMilliSecond = lasttimeMilliSecond;
	}

	public Rockets getRockets() {
		return rockets;
	}

	public void setRockets(Rockets rockets) {
		this.rockets = rockets;
	}

	public int getHeart() {
		return heart;
	}

	public void setHeart(int heart) {
		this.heart = heart;
		if (heart == 0 && GameMode.type == GameMode.SINGLE)
			saveSpaceShipInfo(HighScorePanel.LOOSER_MODE);
		else if (heart == 0 && GameMode.type == GameMode.SERVER) {
			if (getId() != GamePanel.spaceShip.getId()) {
				ServerPlayer.findWithSpaceShip(this).manageSendingMessage(MessageManager.LOSE);
				ServerPlayer.otherPlayers.remove(getId());
			} else
				goToRankingPageMultiPlayer(HighScorePanel.LOOSER_MODE);
		}
	}

	public static void goToRankingPageMultiPlayer(int type) {
		if (GamePanel.spaceShip.gamePanel != null)
			GamePanel.spaceShip.gamePanel.stop();
		HighScorePanel highScorePanel = new HighScorePanel(type);
		MainFrame.ChangeCurrentPanelWith(highScorePanel);
	}

	public void saveSpaceShipInfo(int type) {
		String name = MainMenu.user.getName();
		long timePassed = MainMenu.user.getTimePassed() + System.currentTimeMillis()
				- MainMenu.user.getBeginingOfGame();
		User user = new User(name, getScore(), getPower(), getHeart(), getScoreOfCoins(), getRocket(),
				GamePanel.level.getCode(), timePassed, getWeaponType(), false);
		gamePanel.stop();
		HighScorePanel highScorePanel = new HighScorePanel(type);
		highScorePanel.addNewUser(user);
		MainMenu.updateUser(new User(name));
		MainFrame.ChangeCurrentPanelWith(highScorePanel);
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
		weapon = Weapon.create(weaponType, power);
		weapon.setPower(weapon.getPower() * 1.25f);
	}

	@Override
	public void setVisible(boolean f) {
		this.visible = f;
		if (GameMode.type != GameMode.SERVER && heatAble && !f) {
			gamePanel.pressed = false;
		}
		if (heatAble && !f) {
			setHeart(heart - 1);
			timeOfDeath = System.currentTimeMillis();
			setTemp(0);
			setScoreOfCoins(0);
			setFinalTemp(100);
			setPower((int) Math.sqrt(getPower() - 1));
		}
		heatAble = f;
	}

	public void onlySetVisiblity(boolean f) {
		this.visible = f;
	}

	public int getRocket() {
		return rocket;
	}

	public void setRocket(int rocket) {
		this.rocket = rocket;
	}

	public double getdTemp() {
		return dTemp;
	}

	public void setdTemp(double dTemp2) {
		dTemp = dTemp2;
	}

	public Image getImage() {
		return spaceShip;
	}

	public boolean isOverHeat() {
		return isOverHeat;
	}

	public void setOverHeat(boolean isOverHeat) {
		this.isOverHeat = isOverHeat;
	}

	public double getTemp() {
		return temp;
	}

	public void setTemp(double temp) {
		this.temp = temp;
		if (this.temp >= this.finalTemp + 5 && isOverHeat == false) {
			isOverHeat = true;
			dtempWhenOverHeat = (temp) * 1f / restTime;
		} else if (temp == 0) {
			isOverHeat = false;
		}
		if (GameMode.type != GameMode.SERVER && GameMode.type != GameMode.SINGLE) {
			GamePanel.player.sendMessage(MessageManager.TEMP_CHANGED);
		}
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
		xCenter = x + w / 2;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
		yCenter = y + h / 2;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Bullets getBullets() {
		return bullets;
	}

	public void setBullets(Bullets bullet) {
		bullets = bullet;
	}

	public int getFinalTemp() {
		return finalTemp;
	}

	public void setFinalTemp(int finalTemp) {
		this.finalTemp = finalTemp;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
		this.weapon.setId(this.id);
	}

	public int getScoreOfCoins() {
		return scoreOfCoins;
	}

	public void setScoreOfCoins(int scoreOfCoins) {
		this.scoreOfCoins = scoreOfCoins;
	}

	public Vector<PowerUp> getPowerUps() {
		return powerUps;
	}

	public void setPowerUps(Vector<PowerUp> powerUps) {
		this.powerUps = powerUps;
	}

	public int getWeaponType() {
		return weaponType;
	}

	public void setWeaponType(int weaponType) {
		if (this.weaponType == weaponType)
			setPower(power + 1);
		else {
			this.weaponType = weaponType;
			weapon = Weapon.create(weaponType, power);
		}
	}

	public double getDtempWhenOverHeat() {
		return dtempWhenOverHeat;
	}

	public void setDtempWhenOverHeat(double dtempWhenOverHeat) {
		this.dtempWhenOverHeat = dtempWhenOverHeat;
	}

	public boolean isHeatAble() {
		return heatAble;
	}

	public void setHeatAble(boolean heatAble) {
		this.heatAble = heatAble;
	}

	public void setPanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GamePanel getGamePanel() {
		return gamePanel;
	}

	public boolean isSeener() {
		return seener;
	}

	public void setSeener(boolean seener) {
		this.seener = seener;
//		if (seener)
//			GameMode.type = GameMode.SEENER;
	}

}
