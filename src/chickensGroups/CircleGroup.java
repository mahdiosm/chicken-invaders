package chickensGroups;

import java.util.Random;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.atan2;
import static java.lang.Math.toRadians;
import javax.swing.Timer;
import MainFrame.MainMenu;
import MyPoint.Point;
import chickens.Chicken;
import levels.DangerPlace;

public class CircleGroup extends Group {
	private static final long serialVersionUID = 1L;
	double angle;
	Timer timer;
	boolean reached = false, whileReach = false, firstTime = true;
	int lx, ly;

	public CircleGroup() {
		entered = false;
		x = 300;
		y = -400;
		createChickens(new Random().nextInt(10) + 25);
		takePosition();
		createNewPoint();
		delta = 5;
		setChickensPattern();
	}

	private void createNewPoint() {
		Random r = new Random();
		lx = r.nextInt(MainMenu.Width - 600) + 200;
		ly = r.nextInt(MainMenu.Height / 4) + 200;
		angle = atan2(ly - y, lx - x);
		pattern = e -> new Point((float) (delta * cos(angle)), (float) (delta * sin(angle)));
		setPattern();
		setDangerPlace();
	}

	@Override
	public void checkEntered() {
		if (!entered) {
			if (x < lx + delta && x > lx - delta && y < ly + delta && y > ly - delta) {
				entered = true;
				x = lx;
				y = ly;
				delta = 3;
				setPattern();
			}
		}
	}

	private void checkReached() {
		if (!whileReach) {
			if (Group.distance(new Point(lx, ly), new Point(x, y)) <= delta) {
				whileReach = true;
				pattern = a -> new Point(0, 0);
				setPattern();
				timer = new Timer(3000, e -> {
					whileReach = false;
					createNewPoint();
					timer.stop();
				});
				timer.start();
				x = lx;
				y = ly;
			}
		}
		if (reached) {
		}
	}

	@Override
	public void move() {
		checkEntered();
		checkReached();
		super.move();
	}

	@Override
	protected void takePosition() {
		float angle = 0;
		int c = 2, cOfBAngle = 0, helpI = 1, remain = chickens.size(), dAngle = 4 * c;
		r = 100;
		for (int i = 0; i < chickens.size(); i++) {
			if (helpI <= 4 * c) {
				chickens.get(i).setBeginPoint((float) (r * cos(toRadians(angle)) + x - chickens.get(i).getWidth() / 2),
						(float) (r * sin(toRadians(angle)) + y - chickens.get(i).getHeight() / 2));
				helpI++;
				angle += 360f / dAngle;
			} else if (helpI == 4 * c + 1) {
				remain -= 4 * c;
				c++;
				i--;
				helpI = 1;
				cOfBAngle++;
				r += 100;
				angle = 20 * cOfBAngle;
				dAngle = remain < 4 * c ? remain : 4 * c;
			}
		}
		firstTime = false;
	}

	@Override
	protected void setChickensPattern() {
		for (Chicken chicken : chickens) {
			chicken.setPattern(a -> {
				double angle = Math.toDegrees(atan2(a.getY() - y, a.getX() - x));
				float r = Group.distance(chicken.getCenterPoint(), new Point(x, y));
				float newX = (float) (x + r * cos(toRadians(angle - 0.7))) - a.getX();
				float newY = (float) (y + r * sin(toRadians(angle - 0.7))) - a.getY();
				return new Point(newX, newY);
			});
			chicken.setGroupPattern(this.pattern);
		}
	}

	@Override
	protected void setDangerPlace() {
		dangerPlace = new DangerPlace((int) lx, (int) ly, r);
	}
}
