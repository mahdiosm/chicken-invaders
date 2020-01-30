package chickensGroups;

import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;
import GameFrames.GamePanel;
import MainFrame.MainMenu;
import MyPoint.Point;
import chickens.Chicken;

public class SuicideGroup extends Group {
	private static final long serialVersionUID = 1L;
	private int suicideChickenIndex = -1;
	Chicken chicken = null;
	private ArrayList<Point> distances;
	private Timer timer;

	public SuicideGroup() {
		entered = false;
		createChickens(new Random().nextInt(21) + 10);
		delta = 2;
		pattern = a -> new Point(0, 0);
		takePosition();
		setChickensPattern();
		createTimer();
	}

	private void createTimer() {
		timer = new Timer(10000, e -> {
			readWriteLock.readLock().lock();
			if (chickens.size() > 0) {
				Random random = new Random();
				suicideChickenIndex = random.nextInt(chickens.size());
				chicken = chickens.get(suicideChickenIndex);
			}
			readWriteLock.readLock().unlock();
		});
		timer.start();
	}

	@Override
	public void checkEntered() {
	}

	@Override
	public void move() {
		checkReached();
		super.move();
	}

	private void checkReached() {
		readWriteLock.readLock().lock();
		for (Chicken chicken : chickens) {
			Point point = distances.get(chickens.indexOf(chicken));
			Point point2 = chicken.getCenterPoint();
			if (Group.distance(point2, point) <= delta) {
				createRandomPattern(chicken, point2.getX(), point2.getY());
			}
		}
		if (chickens.contains(chicken)) {
			Chicken chicken = chickens.get(suicideChickenIndex);
			createSpecifiedPattern(chicken, chicken.getCenterPoint().getX(), chicken.getCenterPoint().getY(),
					GamePanel.spaceShip.getCenterPoint());
			this.chicken = null;
		}
		readWriteLock.readLock().unlock();
	}

	@Override
	public void remove(Chicken chicken) {
		super.remove(chicken);
		readWriteLock.writeLock().lock();
		distances.remove(distances.get(chickens.indexOf(chicken)));
		readWriteLock.writeLock().unlock();
	}

	@Override
	public void remove(int i) {
		super.remove(i);
		readWriteLock.writeLock().lock();
		distances.remove(i);
		readWriteLock.writeLock().unlock();
	}

	@Override
	protected void takePosition() {
		Random random = new Random();
		for (Chicken chicken : chickens) {
			int x = random.nextInt(MainMenu.Width);
			chicken.setBeginPoint(x, -150);
		}
	}

	void createRandomPattern(Chicken chicken, float x, float y) {
		Random random = new Random();
		int x2 = random.nextInt(MainMenu.Width - 300) + 150;
		int y2 = random.nextInt(MainMenu.Height / 3 - 100) + 150;
		int index = chickens.indexOf(chicken);
		distances.remove(index);
		distances.add(index, new Point(x2, y2));
		Point p = chicken.getCenterPoint();
		double angle = Math.atan2(-p.getY() + y2, -p.getX() + x2);
		chicken.setPattern(a -> new Point(delta * Math.cos(angle), (delta * Math.sin(angle))));
	}

	void createRandomPatternFirstTime(Chicken chicken, float x, float y) {
		Random random = new Random();
		int x2 = random.nextInt(MainMenu.Width - 300) + 150;
		int y2 = random.nextInt(MainMenu.Height / 3 - 100) + 150;
		distances.add(new Point(x2, y2));
		Point p = chicken.getCenterPoint();
		double angle = Math.atan2(-p.getY() + y2, -p.getX() + x2);
		chicken.setPattern(a -> new Point(delta * Math.cos(angle), (delta * Math.sin(angle))));
	}

	void createSpecifiedPattern(Chicken chicken, float x, float y, Point spaceShipPoint) {
		int x2 = (int) spaceShipPoint.getX();
		int y2 = (int) spaceShipPoint.getY();
		int index = chickens.indexOf(chicken);
		distances.remove(index);
		distances.add(index, new Point(x2, y2));
		Point p = chicken.getCenterPoint();
		double angle = Math.atan2(-p.getY() + y2, -p.getX() + x2);
		chicken.setPattern(a -> new Point(2 * delta * Math.cos(angle), (2 * delta * Math.sin(angle))));
	}

	@Override
	protected void setChickensPattern() {
		distances = new ArrayList<>();
		for (Chicken chicken : chickens) {
			Point p = chicken.getCenterPoint();
			chicken.setGroupPattern(this.pattern);
			createRandomPatternFirstTime(chicken, p.getX(), p.getY());
		}
	}

}
