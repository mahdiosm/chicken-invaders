package chickensGroups;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import javax.swing.Timer;
import GameFrames.Drawable;
import MyPoint.Point;
import chickens.Chicken;
import egg.Egg;
import egg.Eggs;
import levels.DangerPlace;
import levels.Level;
import levels.SafePlace;

public abstract class Group extends Drawable {
	private static final long serialVersionUID = 1L;
	protected float x, y, delta;
	protected Level ownLevel;
	private Eggs eggs = new Eggs();
	protected int firstXOnScreen, firstYOnScreen;
	protected boolean entered;
	protected SafePlace safePlace;
	protected DangerPlace dangerPlace;
	protected int numofChickens;
	public Timer timer;
	protected boolean begins = false;
	boolean firstOfTimer = true;
	protected volatile ArrayList<Chicken> chickens;
	protected ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	/*
	 * if first point is (x0, y0) and the second one is (x1, y1) then pattern
	 * returns (x1 - x0, y1 - y0).
	 */

	protected Function<Point, Point> pattern;

	public abstract void checkEntered();

	protected void setDangerPlace() {
		dangerPlace = null;
	}

	protected void setSafePlace() {
		safePlace = null;
	}

	protected abstract void takePosition();

	protected abstract void setChickensPattern();

	public void setBeginPoint(Point p) {
		this.x = p.getX();
		this.y = p.getY();
	}

	public void remove(int i) {
		readWriteLock.writeLock().lock();
		chickens.remove(i);
		readWriteLock.writeLock().unlock();
	}

	public void remove(Chicken chicken) {
		readWriteLock.writeLock().lock();
		chickens.remove(chicken);
		readWriteLock.writeLock().unlock();
	}

	public ArrayList<Chicken> getChickens() {
		return chickens;
	}

	protected void createChickens(int numofChickens) {
		int level = Level.levelCode / 10;
		chickens = new ArrayList<>();
		Random random = new Random();
		this.numofChickens = numofChickens;
		for (int i = 0; i < numofChickens; i++) {
			chickens.add(Chicken.create(random.nextInt(level) + 1));
			chickens.get(i).setOwner(this);
		}
	}

	public static float distance(Point p1, Point p2) {
		return (float) Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
	}

	public synchronized void setPattern() {
		readWriteLock.readLock().lock();
		for (Chicken chicken : chickens) {
			chicken.setGroupPattern(pattern);
		}
		readWriteLock.readLock().unlock();
	}

	@Override
	public void render(Graphics g) {
		synchronized (chickens) {
			if (!begins) {
				renderBeforeReached(g);
				return;
			} else {
				readWriteLock.readLock().lock();
				for (Chicken chicken : chickens) {
					chicken.render(g);
				}
				readWriteLock.readLock().unlock();
				eggs.render(g);
			}
		}
	}

	public Eggs getEggs() {
		return eggs;
	}

	public void setEggs(Eggs eggs) {
		this.eggs = eggs;
	}

	public Level getOwnLevel() {
		return ownLevel;
	}

	public void setOwnLevel(Level ownLevel) {
		this.ownLevel = ownLevel;
	}

	protected void renderBeforeReached(Graphics g) {
		if (safePlace == null && dangerPlace == null) {
			begins = true;
			startEggProbebility();
			makeChickensVisible();
			return;
		}
		if (safePlace != null) {
			safePlace.render(g);
			createTimerForFirstTime();
		}
		if (dangerPlace != null) {
			dangerPlace.render(g);
			createTimerForFirstTime();
		}
	}

	protected void createTimerForFirstTime() {
		if (firstOfTimer) {
			firstOfTimer = false;
			new Timer(3000, e -> {
				makeChickensVisible();
				begins = true;
				startEggProbebility();
				((Timer) e.getSource()).stop();
			}).start();
		}
	}

	protected void makeChickensVisible() {
		readWriteLock.readLock().lock();
		for (Chicken chicken : chickens) {
			chicken.setVisible(true);
		}
		readWriteLock.readLock().unlock();
	}

	@Override
	public void move() {
		if (begins) {
			Point p = pattern.apply(new Point(x, y));
			for (int i = chickens.size() - 1; i >= 0; i--) {
				readWriteLock.writeLock().lock();
				chickens.get(i).move();
				readWriteLock.writeLock().unlock();
				if (!chickens.get(i).isVisible()) {
					remove(i);
				}
			}
			this.x += p.getX();
			this.y += p.getY();
			eggs.move();
		}
	}

	protected void move0() {
		if (begins) {
			Point p = pattern.apply(new Point(x, y));
			for (int i = chickens.size() - 1; i >= 0; i--) {
				readWriteLock.writeLock().lock();
				chickens.get(i).move();
				readWriteLock.writeLock().unlock();
				if (!chickens.get(i).isVisible()) {
					remove(i);
					takePosition();
				}
			}
			this.x += p.getX();
			this.y += p.getY();
			eggs.move();
		}
	}

	public void startEggProbebility() {
		if (begins) {
			timer = new Timer(1500, e -> {
				readWriteLock.readLock().lock();
				for (Chicken chicken : chickens) {
					if (Chicken.createRandom(chicken.getEggPercent())) {
						Point point = chicken.getCenterPoint();
						eggs.addToEggs(new Egg((int) point.getX(), (int) point.getY(), chicken.getEggDelta()));
					}
				}
				readWriteLock.readLock().unlock();
			});
			timer.start();
		}
	}

	public boolean finished() {
		return chickens.size() == 0;
	}
}
