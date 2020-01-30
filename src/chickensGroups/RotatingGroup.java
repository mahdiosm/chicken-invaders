package chickensGroups;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
import java.util.Random;
import MainFrame.MainMenu;
import MyPoint.Point;
import chickens.Chicken;
import levels.SafePlace;

public class RotatingGroup extends Group {
	private static final long serialVersionUID = 1L;
	int firstR, minR = 200;

	public RotatingGroup() {
		entered = false;
		x = MainMenu.Width / 2;
		y = MainMenu.Height / 2;
		firstR = (int) Math.sqrt(x * x + y * y) + 150;
		this.minR = 250;
		createChickens(new Random().nextInt(10) + 30);
		this.setSafePlace();
		takePosition();
		pattern = a -> new Point(0, 0);
		delta = 2;
		setChickensPattern();
	}

	@Override
	public void checkEntered() {
		if (!entered && begins) {
			takePosition();
			if (firstR <= minR) {
				entered = true;
				setNewPatterns();
			}
		}
	}

	private void setNewPatterns() {
		entered = true;
		for (int i = chickens.size() - 1; i >= 0; i--) {
			Chicken chicken = chickens.get(i);
			chicken.setPattern(a -> {
				double angle = Math.toDegrees(atan2(a.getY() - y, a.getX() - x));
				float r = Group.distance(chicken.getCenterPoint(), new Point(x, y));
				float newX = (float) (x + r * cos(toRadians(angle - 0.7))) - a.getX();
				float newY = (float) (y + r * sin(toRadians(angle - 0.7))) - a.getY();
				return new Point(newX, newY);
			});
		}
	}

	@Override
	public void move() {
		checkEntered();
		super.move();
	}

	@Override
	protected void takePosition() {
		int r = firstR;
		float angle = 0;
		int c = 2, cOfBAngle = 0, helpI = 1, remain = chickens.size(), dAngle = 5 * c;
		for (int i = chickens.size() - 1; i >= 0; i--) {
			if (helpI <= 5 * c) {
				chickens.get(i).setBeginPoint((float) (r * cos(toRadians(angle)) + x - chickens.get(i).getWidth() / 2),
						(float) (r * sin(toRadians(angle)) + y - chickens.get(i).getHeight() / 2));
				helpI++;
				angle += 360f / dAngle;
			} else if (helpI == 5 * c + 1) {
				remain -= 5 * c;
				c++;
				i++;
				helpI = 1;
				cOfBAngle++;
				r += 100;
				angle = 20 * cOfBAngle;
				dAngle = remain < 5 * c ? remain : 5 * c;
			}
		}
		firstR -= 4;
	}

	@Override
	protected void setChickensPattern() {
		for (int i = chickens.size() - 1; i >= 0; i--) {
			Chicken chicken = chickens.get(i);
			chicken.setPattern(a -> new Point(0, 0));
			chicken.setGroupPattern(this.pattern);
		}
	}

	@Override
	protected void setSafePlace() {
		safePlace = new SafePlace(MainMenu.Width / 2, MainMenu.Height / 2, 120);
	}
}
