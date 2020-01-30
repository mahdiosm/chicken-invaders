package Weapons.Blaster;

import Bullet.Bullet;
import Bullet.Bullets;

public class Blaster0 extends Blaster {

	@Override
	public void add(Bullets bullets) {
		bullets.add(new Bullet(Blaster.blasterImage, x, y, speed, Math.toRadians(-90), minPower, getId()));
	}
}
