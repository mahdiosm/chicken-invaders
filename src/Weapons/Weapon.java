package Weapons;

import java.awt.Image;
import java.awt.Point;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import Bullet.Bullets;
import Weapons.Blaster.Blaster;
import Weapons.Laser.Laser;
import Weapons.Neutron.Neutron;

public abstract class Weapon {
	public static final List<Image> IMAGES = Arrays.asList(Blaster.blasterImage, Neutron.smallNeutronImage,
			Neutron.mediumNeutronImage, Neutron.largeNeutronImage, Laser.images[0], Laser.images[1], Laser.images[2],
			Laser.images[3]);
	HashMap<Integer, Image> iMap = new HashMap<>();
	protected float minPower = 1;
	private int id;
	public int x = 0, y = 0;
	public int timeDelay = 0;
	public int hotNess = 0;
	public int speed = 0;

	public abstract void add(Bullets bullets);

	public abstract int getWidth();

	public abstract int getHeigth();

	public float getPower() {
		return minPower;
	}

	public void setPower(float power) {
		this.minPower = power;
	}

	public void setBeginPoint(Point begin) {
		this.x = (int) begin.getX();
		this.y = (int) begin.getY();
	}

	public Point getBeginPoint() {
		return new Point(x, y);
	}

	public Weapon setTimeDelay(int timeDelay) {
		this.timeDelay = timeDelay;
		return this;
	}

	public Weapon setHotNess(int hotNess) {
		this.hotNess = hotNess;
		return this;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Weapon setSpeed(int speed) {
		this.speed = speed;
		return this;
	}

	public static Weapon create(int weaponType, int power) {
		String p = "Weapons.";
		Weapon weapon = null;
		try {
			@SuppressWarnings("unchecked")
			Class<Weapon> s = (Class<Weapon>) Class.forName(p + WeaponsName.values()[weaponType - 1] + "."
					+ WeaponsName.values()[weaponType - 1] + (power >= 4 ? 4 : power));
			Constructor<Weapon> constructor = s.getConstructor();
			weapon = (Weapon) constructor.newInstance();
			if (power > 4) {
				for (int i = 4; i < power; i++) {
					weapon.setPower(weapon.getPower() * 1.25f);
				}
			}
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return weapon;
	}
}
