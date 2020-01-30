package levels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;
import GameFrames.Drawable;
import MainFrame.MainMenu;
import chickensGroups.BossGroup;
import chickensGroups.CircleGroup;
import chickensGroups.CollisionGroup;
import chickensGroups.Group;
import chickensGroups.RectangleGroup;
import chickensGroups.RotatingGroup;
import chickensGroups.SuicideGroup;

public abstract class Level extends Drawable {
	private static final long serialVersionUID = 1L;
	public static ArrayList<Class<? extends Group>> chickenGroups = new ArrayList<>();
	public static ArrayList<Class<? extends Group>> bossGroups = new ArrayList<>();
	static {
		chickenGroups.add(RectangleGroup.class);
		chickenGroups.add(CollisionGroup.class);
		chickenGroups.add(SuicideGroup.class);
		chickenGroups.add(RotatingGroup.class);
		chickenGroups.add(CircleGroup.class);

		bossGroups.add(BossGroup.class);
	}

	public int count = 1;
	private boolean begins = false;

	protected int nextLevelCode;
	public static int levelCode;

	private static Group group;

	public Level update() {
		count--;
		if (count == 0)
			return create(nextLevelCode);
		else
			return setGroup();
	}

	public void stop() {
		if (!(this instanceof BossWave) && group.timer != null) {
			group.timer.stop();
		}
	}

	public void resume() {
		if (!(this instanceof BossWave)) {
			group.startEggProbebility();
		}
	}

	public Level setGroup() {
		try {
			group = chickenGroups.get(new Random().nextInt(chickenGroups.size())).getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return this;
	}

	public Level setBossGroup() {
		try {
			group = bossGroups.get(new Random().nextInt(bossGroups.size())).getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return this;
	}

	public Level setCode(int code) {
		levelCode = code;
		return this;
	}

	public int getCode() {
		return levelCode;
	}

	public Group getGroup() {
		return group;
	}

	public boolean finished() {
		return group.finished();
	}

	@Override
	public void move() {
		if (begins) {
			group.move();
		}
	}

	public int getWave() {
		return getCode() % 10;
	}

	public int getLevel() {
		return getCode() / 10;
	}

	public int getNextLevelCode() {
		return nextLevelCode;
	}

	public Level setNextLevelCode(int nextLevelCode) {
		this.nextLevelCode = nextLevelCode;
		return this;
	}

	@Override
	public void render(Graphics g) {
		if (begins) {
			group.render(g);
		} else {
			showWaveInfo(g);
			timerOfFirst();
		}
	}

	boolean first = true;

	private void timerOfFirst() {
		if (first) {
			first = false;
			new Timer(2000, a -> {
				begins = true;
				((Timer) a.getSource()).stop();
			}).start();
		}
	}

	private void showWaveInfo(Graphics g2) {
		g2.setColor(Color.red);
		g2.setFont(new Font("serif", Font.ITALIC | Font.BOLD, 50));
		g2.drawString("Level " + getLevel() + ", Wave " + getWave(), MainMenu.Width / 2 - 150,
				MainMenu.Height / 2 - 10);
	}

	@SuppressWarnings("unchecked")
	public static Level create(int levelNumber) {
		String p = "levels.level";
		Class<Level> s = null;
		Level level = null;
		try {
			s = (Class<Level>) Class.forName(p + ((levelNumber - 1) / 10) + ".Wave" + ((levelNumber) % 10));
			Constructor<Level> cons = s.getConstructor();
			level = cons.newInstance();
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return level;
	}
}
