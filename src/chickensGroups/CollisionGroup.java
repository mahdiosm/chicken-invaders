package chickensGroups;

import java.util.Random;
import MainFrame.MainMenu;
import MyPoint.Point;
import chickens.Chicken;
import levels.DangerPlace;

public class CollisionGroup extends Group {
	private static final long serialVersionUID = 1L;
	long lastTime = 0;
	int angle;

	public CollisionGroup() {
		this.angle = new Random().nextInt(140) - 70;
		entered = false;
		createChickens(new Random().nextInt(10) + 30);
		for (Chicken chicken : chickens) {
			chicken.setAngle(angle);
		}
		makeChickensVisible();
		delta = 3;
		pattern = a -> new Point(0, 0);
		takePosition();
		setChickensPattern();
		setDangerPlace();
	}

	@Override
	public void move() {
		if (begins) {
			for (int i = chickens.size() - 1; i >= 0; i--) {
				Chicken chicken = chickens.get(i);
				chicken.move();
				if (chicken.isEntered()) {
					changeChickenPattern(chicken);
				} else {
					if (chicken.getCenterPoint().getX() > chicken.getWidth() / 2) {
						chicken.setEntered(true);
					}
				}
				if (!chicken.isVisible()) {
					chickens.remove(i);
				}
			}
			getEggs().move();
		}
	}

	void changeChickenPattern(Chicken chicken) {
		float xCenter = chicken.getCenterPoint().getX();
		float yCenter = chicken.getCenterPoint().getY();
		int angle = (int) chicken.getAngle();
		int oldAngle = angle, dis = 40;
		if (yCenter < dis || yCenter + dis > MainMenu.Height) {
			angle = -angle;
			yCenter = yCenter < dis ? dis : MainMenu.Height - dis;
		}
		if (xCenter < dis || xCenter + dis > MainMenu.Width) {
			angle = 180 - angle;
			xCenter = xCenter < dis ? dis : MainMenu.Width - dis;
		}
		chicken.setCenterPoint(new Point(xCenter, yCenter));
		chicken.setAngle(angle);
		if (angle != oldAngle)
			chicken.setPattern(a -> new Point(delta * Math.cos(Math.toRadians(chicken.getAngle())),
					delta * Math.sin(Math.toRadians(chicken.getAngle()))));
	}

	@Override
	public void checkEntered() {

	}

	@Override
	protected void takePosition() {
		float x = -100, y = MainMenu.Height / 2, dx = (float) (-100 * Math.cos(Math.toRadians(angle)));
		for (Chicken chicken : chickens) {
			chicken.setCenterPoint(new Point(x, y));
			x += dx;
			y += (float) (dx * Math.tan(Math.toRadians(chicken.getAngle())));
		}
	}

	@Override
	protected void setChickensPattern() {
		for (Chicken chicken : chickens) {
			chicken.setPattern(a -> new Point(delta * Math.cos(Math.toRadians(chicken.getAngle())),
					delta * Math.sin(Math.toRadians(chicken.getAngle()))));
			chicken.setGroupPattern(this.pattern);
		}
	}

	@Override
	protected void setDangerPlace() {
		dangerPlace = new DangerPlace(0, MainMenu.Height / 2, 100);
	}

}
