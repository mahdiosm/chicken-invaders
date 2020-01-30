package GameFrames;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Rockets extends Drawable implements Serializable{
	private static final long serialVersionUID = 1L;
	private ArrayList<Rocket> rockets;

	public Rockets() {
		rockets = new ArrayList<>();
	}

	public void addToRockets(Rocket rocket) {
		rockets.add(rocket);
	}

	public int getSize() {
		return rockets.size();
	}

	@Override
	public void render(Graphics g) {
		for (Rocket rocket : rockets) {
			rocket.render(g);
		}
	}

	@Override
	public void move() {
		Iterator<Rocket> iterator = rockets.iterator();
		while (iterator.hasNext()) {
			Rocket rocket = iterator.next();
			rocket.move();
			if (!rocket.visible) {
				iterator.remove();
			}
		}
	}

	public ArrayList<Rocket> getRockets() {
		return rockets;
	}
}
