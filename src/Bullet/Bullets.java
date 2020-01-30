package Bullet;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import GameFrames.Drawable;

public class Bullets extends Drawable implements Serializable {
	private static final long serialVersionUID = 1L;
	private ArrayList<Bullet> bullets = new ArrayList<>();
	private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	public ArrayList<Bullet> getBullets() {
		return bullets;
	}

	public void setBullets(ArrayList<Bullet> bullets) {
		this.bullets = bullets;
	}

	public void removeBulletsWithId(int id) {
		readWriteLock.writeLock().lock();
		Iterator<Bullet> iterator = bullets.iterator();
		while (iterator.hasNext()) {
			Bullet bullet = iterator.next();
			if (bullet.getId() == id)
				bullet.setVisible(false);
		}
		readWriteLock.writeLock().unlock();
	}

	@Override
	public void render(Graphics g) {
		readWriteLock.readLock().lock();
		for (Bullet bullet : bullets) {
			bullet.render(g);
		}
		readWriteLock.readLock().unlock();
	}

	@Override
	public void move() {
		readWriteLock.writeLock().lock();
		Iterator<Bullet> iterator = bullets.iterator();
		while (iterator.hasNext()) {
			Bullet bullet = iterator.next();
			bullet.move();
			if (!bullet.isVisible()) {
				iterator.remove();
			}
		}
		readWriteLock.writeLock().unlock();
	}

	public void add(Bullet bullet) {
		bullets.add(bullet);
	}
}
