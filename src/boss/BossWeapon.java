package boss;

import java.awt.Image;

import java.util.Random;
import java.util.function.Predicate;
import Bullet.Bullet;
import Bullet.Bullets;
import Weapons.Weapon;
import Weapons.Blaster.Blaster;
import chickens.Chicken;

public class BossWeapon extends Weapon {
	int speed = 4;
	public static Image image = Blaster.blasterImage;
	int w, h;
	public int angle;
	public Predicate<Integer> check;
	public static final int BOSS_WEAPON_ID = -1;

	public BossWeapon() {
		w = Blaster.blasterImage.getWidth(null);
		h = Blaster.blasterImage.getHeight(null);
		check = a -> Chicken.createRandom(25);
	}

	@Override
	public void add(Bullets bullets) {
		angle = new Random().nextInt(45);
		for (int i = 0; i < 8; i++) {
			if (check.test(angle))
				bullets.add(new Bullet(image, x, y, 1, Math.toRadians(angle), 1, -1));
			angle += 45;
		}
	}

	@Override
	public int getWidth() {
		return w;
	}

	@Override
	public int getHeigth() {
		return h;
	}

	@Override
	public float getPower() {
		return minPower;
	}
}
