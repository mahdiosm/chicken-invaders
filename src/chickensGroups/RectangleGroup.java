package chickensGroups;

import java.util.Random;
import MainFrame.MainMenu;
import MyPoint.Point;
import chickens.Chicken;

public class RectangleGroup extends Group {
	private static final long serialVersionUID = 1L;
	// chicken per row
	int cpr;
	Boolean firstTime = true;

	public RectangleGroup() {
		delta = 5;
		x = 400;
		y = -400;
		entered = false;
		createChickens(new Random().nextInt(18) + 28);
		pattern = a -> new Point(0, delta);
		takePosition();
		setChickensPattern();
	}

	private void changeSign() {
		delta *= -1;
	}

	@Override
	protected void takePosition() {
		determineCPR();
		float x2 = x;
		float y2 = y;
		for (int i = 0; i < chickens.size(); i++) {
			chickens.get(i).setCenterPoint(new Point(x2, y2));
			x2 += 100;
			if ((i + 1) % cpr == 0) {
				y2 += 85;
				x2 = x;
			}
		}
		firstTime = false;
	}

	@Override
	public void move() {
		checkEntered();
		super.move();
		if ((x <= -3 || (x + cpr * 100) >= MainMenu.Width + 3) && entered) {
			changeSign();
		}
	}

	@Override
	public void checkEntered() {
		if (!entered) {
			if (y >= 100) {
				pattern = a -> new Point(delta, 0);
				delta = -3;
				setPattern();
				entered = true;
			}
		}
	}

	private void determineCPR() {
		if (chickens.size() > 40)
			cpr = 9;
		else if (chickens.size() > 30)
			cpr = 8;
		else
			cpr = 7;
	}

	@Override
	protected void setChickensPattern() {
		for (Chicken chicken : chickens) {
			chicken.setPattern(a -> new Point(0, 0));
			chicken.setGroupPattern(this.pattern);
		}
	}

}
