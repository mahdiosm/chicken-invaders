package egg;

import java.awt.Graphics;
import java.util.Iterator;
import java.util.Vector;
import GameFrames.Drawable;

public class Eggs extends Drawable {
	Vector<Egg> eggs = new Vector<>();

	public Vector<Egg> getEggs() {
		return eggs;
	}

	public void addToEggs(Egg egg) {
		eggs.add(egg);
	}

	public int getSize() {
		return eggs.size();
	}

	@Override
	public void render(Graphics g) {
		for (Egg egg : eggs) {
			egg.render(g);
		}
	}

	@Override
	public void move() {
		Iterator<Egg> iterator = eggs.iterator();
		while (iterator.hasNext()) {
			Egg egg = iterator.next();
			egg.move();
			if (!egg.isVisible()) {
				iterator.remove();
			}
		}
	}
}
