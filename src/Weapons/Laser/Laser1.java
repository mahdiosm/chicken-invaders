package Weapons.Laser;

import Bullet.Bullets;
import Bullet.LaserBullet;

public class Laser1 extends Laser {
	@Override
	public void add(Bullets bullets) {
		bullets.add(new LaserBullet(images[1], x - 5, y + 5, speed, Math.toRadians(-90), nimMidP, getId()));
	}
}
