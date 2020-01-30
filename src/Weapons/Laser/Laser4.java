package Weapons.Laser;

import Bullet.Bullets;
import Bullet.LaserBullet;

public class Laser4 extends Laser {
	@Override
	public void add(Bullets bullets) {
		bullets.add(new LaserBullet(images[0], x - 46, y + 15, speed, Math.toRadians(-90), getPower() * minP, getId()));
		bullets.add(new LaserBullet(images[3], x - 13, y + 5, speed, Math.toRadians(-90), getPower() * maxP, getId()));
		bullets.add(new LaserBullet(images[0], x + 46, y + 15, speed, Math.toRadians(-90), getPower() * minP, getId()));
	}
}
