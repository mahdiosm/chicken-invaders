package Weapons.Blaster;

import Bullet.Bullet;
import Bullet.Bullets;

public class Blaster3 extends Blaster {

	@Override
	public void add(Bullets bullets) {
		bullets.add(new Bullet(blasterImage, x - getWidth() / 2, y, speed, Math.toRadians(-90), this.getPower(),getId()));
		bullets.add(new Bullet(blasterImage, x + getWidth() / 2, y, speed, Math.toRadians(-90), this.getPower(),getId()));
		bullets.add(new Bullet(blasterImage, x + getWidth(), y, speed, Math.toRadians(-80), this.getPower(),getId()));
		bullets.add(new Bullet(blasterImage, x - getWidth(), y, speed, Math.toRadians(-100), this.getPower(),getId()));
	}
}
