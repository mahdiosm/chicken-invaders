package Bullet;

import java.awt.Graphics;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.util.ArrayList;

import GameFrames.Drawable;
import Weapons.Weapon;
import Weapons.Blaster.Blaster;
import Weapons.Laser.Laser;
import Weapons.Neutron.Neutron;

public class Bullet extends Drawable implements Serializable {
	private static final long serialVersionUID = 1L;
	protected Image image;
	private float power;
	private double angle;
	protected double dx;
	protected double dy;

	public Bullet(Image image, int x, int y, int speed, double angle, float power, int id) {
		this.x = x;
		this.y = y;
		this.angle = angle;
		dx = speed * Math.cos(angle);
		dy = speed * Math.sin(angle);
		this.image = image;
		w = image.getWidth(null);
		h = image.getHeight(null);
		r = w / 2;
		xCenter = x + w / 2;
		yCenter = y + h / 2;
		this.power = power;
		this.id = id;
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform at = AffineTransform.getTranslateInstance(xCenter - w / 2, yCenter - h / 2);
		at.rotate(angle + Math.PI / 2, w / 2, h / 2);
		g2d.drawImage(image, at, null);
	}

	@Override
	public void move() {
		x += dx;
		y += dy;
		xCenter += dx;
		yCenter += dy;
	}

	public float getPower() {
		return power;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public double getAngle() {
		return angle;
	}

	public int getIndex() {
		if (image.equals(Blaster.blasterImage))
			return 0;
		else if (image.equals(Neutron.smallNeutronImage))
			return 1;
		else if (image.equals(Neutron.mediumNeutronImage))
			return 2;
		else if (image.equals(Neutron.largeNeutronImage))
			return 3;
		else if (image.equals(Laser.images[0]))
			return 4;
		else if (image.equals(Laser.images[1]))
			return 5;
		else if (image.equals(Laser.images[2]))
			return 6;
		else if (image.equals(Laser.images[3]))
			return 7;
		return -1;
	}
}
