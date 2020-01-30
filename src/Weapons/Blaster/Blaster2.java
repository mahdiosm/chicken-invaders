package Weapons.Blaster;

import Bullet.Bullet;
import Bullet.Bullets;

public class Blaster2 extends Blaster {

	@Override
	public void add(Bullets bullets) {
		bullets.add(new Bullet(blasterImage, x, y, speed, Math.toRadians(-90), this.getPower(),getId()));
		bullets.add(new Bullet(blasterImage, x + getWidth() / 2, y, speed, Math.toRadians(-75), this.getPower(),getId()));
		bullets.add(new Bullet(blasterImage, x - getWidth() / 2, y, speed, Math.toRadians(-105), this.getPower(),getId()));
	}
}
