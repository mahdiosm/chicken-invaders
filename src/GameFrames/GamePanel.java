package GameFrames;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import javax.swing.Timer;
import Commons.Common;
import MainFrame.GameMode;
import MainFrame.HighScorePanel;
import MainFrame.MainFrame;
import MainFrame.MainMenu;
import MainFrame.Panel;
import MyPoint.Point;
import Users.User;
import collision.Collision;
import levels.Level;
import levels.level1.Wave1;
import listeners.EscKey;
import network.ClientPlayer;
import network.InfoForDraw;
import network.MessageManager;
import network.Player;
import network.ServerPlayer;

public class GamePanel extends Panel implements MouseMotionListener, MouseListener {
	public transient static Player player;
	public static EscKey escKey;
	private static final long serialVersionUID = 1L;
	public transient static boolean gameFinished = false;
	transient Image background;
	public static SpaceShip spaceShip;
	public transient OverHeatPanel heatAndScore;
	public int levelNum = 12;
	public static Level level;
	public InfoFrames info;
	public transient static EscPanel esc;
	private transient Timer timer;
	transient boolean held = false;
	public transient static int w, h;
	transient int x = 0, y, y1, dy = 2;
	transient long newTime, oldTime;
	public int type;

	public void run() {
		timerWork();
	}

	public void stop() {
		timer.stop();
		if (GameMode.type == GameMode.SERVER || GameMode.type == GameMode.SINGLE)
			level.stop();
	}

	public void resume() {
		timer.start();
		if (GameMode.type == GameMode.SERVER || GameMode.type == GameMode.SINGLE)
			level.resume();
	}

	public GamePanel() {
		type = GameMode.type;
		EscKey.f = true;
		createSpaceShip();
		createPanels();
		createLevel();
		setMousePosition(spaceShip);
		init();
	}

	private void createPanels() {
		heatAndScore = new OverHeatPanel();
		add(heatAndScore);

		info = new InfoFrames();
		add(info);
	}

	private void createSpaceShip() {
		if (GameMode.type == GameMode.SINGLE) {
			spaceShip = new SpaceShip();
			spaceShip.setX(MainMenu.Width / 2 - spaceShip.w / 2);
		}
		spaceShip.setY(MainMenu.Height - spaceShip.h);
		spaceShip.setPanel(this);

	}

	private void createLevel() {
		if (GameMode.type == GameMode.SINGLE) {
			levelNum = MainMenu.user.getLevelsPassed();
			level = Level.create(levelNum);
		} else if (GameMode.type == GameMode.SERVER) {
			levelNum = 11;
			level = new Wave1();
		}
	}

	public static void setMousePosition(SpaceShip spaceShip) {
		Robot r;
		try {
			r = new Robot();
			r.mouseMove((int) spaceShip.getCenterPoint().getX(), (int) spaceShip.getCenterPoint().getY());
		} catch (AWTException e) {
		}
	}

	void init() {
		createBackGround();
		Dimension dim = new Dimension(background.getWidth(null), background.getHeight(null) / 2);
		w = (int) dim.getWidth();
		h = (int) dim.getHeight();
		setFocusable(true);
		setSize(dim);
		setPreferredSize(dim);
		setLayout(null);
		addMouseMotionListener(this);
		addMouseListener(this);
		EscKey escKey = new EscKey();
		escKey.setGame(this);
		addKeyListener(escKey);
	}

	private void createBackGround() {
		background = Common.createImage(MainMenu.Width, MainMenu.Height * 2, "resources\\Images\\back.png");
		y = -background.getHeight(null) / 2;
		y1 = -3 * background.getHeight(null) / 2;
	}

	void timerWork() {
		spaceShip.setLasttimeMilliSecond(System.currentTimeMillis());
		timer = new Timer(5, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (type == GameMode.SERVER) {
					player.sendMessage(MessageManager.POSITIONS);
				}
				if (type == GameMode.SERVER || type == GameMode.SINGLE) {
					checkLevelFinished();
					changeTemp();
					setHeatAndScorePanel();
					setInfo();
				}
				doMoves();
				repaint();
				if (type == GameMode.SERVER || type == GameMode.SINGLE)
					Collision.checkCollisions(GamePanel.this);
			}
		});
		timer.start();
	}

	void checkLevelFinished() {
		if (GameMode.type == GameMode.SINGLE && level.finished()) {
			if (gameFinished) {
				User user = MainMenu.user;
				user.setTimePassed(user.getTimePassed() + System.currentTimeMillis() - user.getBeginingOfGame());
				MainMenu.updateUser(user);
				spaceShip.saveSpaceShipInfo(HighScorePanel.WINNER_MODE);
				timer.stop();
			}
			levelNum = (levelNum + 1) % 10 == 6 ? levelNum + 5 : levelNum + 1;
			level = level.update();
		} else if (GameMode.type != GameMode.SINGLE && level.finished()) {
			if (gameFinished)
				MainFrame.ChangeCurrentPanelWith(new MainMenu());
			levelNum = (levelNum + 1) % 10 == 6 ? levelNum + 5 : levelNum + 1;
			level = level.update();
		}
	}

	void doMoves() {
		if (GameMode.type != GameMode.CLIENT) {
			moveBullets();
			moveRockets();
			spaceShip.move();
			level.move();
		}
		move();
	}

	void changeTemp() {
		double temp = spaceShip.getTemp();
		long time = System.currentTimeMillis() - spaceShip.getLasttimeMilliSecond();
		if (pressed) {
			if (!spaceShip.isOverHeat) {
				spaceShip.fire();
			} else
				spaceShip.setTemp(temp >= spaceShip.getdTemp() * time ? temp - spaceShip.getdTemp() * time : 0);
		} else {
			spaceShip.setTemp(
					temp >= spaceShip.getDtempWhenOverHeat() * time ? temp - spaceShip.getDtempWhenOverHeat() * time
							: 0);
		}
		spaceShip.setLasttimeMilliSecond(System.currentTimeMillis());
	}

	private void moveRockets() {
		spaceShip.getRockets().move();
	}

	private void setInfo() {
		info.coinC.setText(String.valueOf(spaceShip.getScoreOfCoins()));
		info.rocketC.setText(String.valueOf(spaceShip.getRocket()));
		info.powerC.setText(String.valueOf(spaceShip.getPower()));
		info.heartC.setText(String.valueOf(spaceShip.getHeart()));
	}

	private void setHeatAndScorePanel() {
		heatAndScore.temp = spaceShip.getTemp();
		heatAndScore.score.setText(String.valueOf(spaceShip.getScore()));
	}

	void move() {
		y += 4;
		y1 += 4;
	}

	private void moveBullets() {
		spaceShip.getBullets().move();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		renderBackGround(g2d);
		if (GameMode.type != GameMode.SINGLE && GameMode.type != GameMode.SERVER) {
			InfoForDraw infoForDraw = ((ClientPlayer) player).infoForDraw;
			if (infoForDraw != null)
				infoForDraw.draw(g2d);
		} else {
			drawBullets(g2d);
			level.render(g2d);
			renderRockets(g2d);
			spaceShip.render(g2d);
			if (type == GameMode.SERVER) {
				drawSpaceShips(g2d);
			}
			if (esc == null) {
				paintChildren(g2d);
			}
		}
		heatAndScore.paintComponent(g2d);
		info.paintComponent(g2d);
	}

	private void drawSpaceShips(Graphics2D g2d) {
		for (Iterator<SpaceShip> iterator = ServerPlayer.otherPlayers.values().iterator(); iterator.hasNext();) {
			SpaceShip spaceShip = iterator.next();
			spaceShip.render(g2d);
			g2d.setColor(ServerPlayer.colors.get(spaceShip.getId()));
			Point point = spaceShip.getCenterPoint();
			g2d.fillOval((int) point.getX() - 12, (int) point.getY() - 12, 24, 24);
		}
		g2d.setColor(ServerPlayer.colors.get(spaceShip.getId()));
		Point point = spaceShip.getCenterPoint();
		g2d.fillOval((int) point.getX() - 12, (int) point.getY() - 12, 24, 24);
	}

	void renderBackGround(Graphics2D g2d) {
		if (y >= background.getHeight(null) / 2) {
			y = -3 * background.getHeight(null) / 2;
		}
		if (y1 >= background.getHeight(null) / 2) {
			y1 = -3 * background.getHeight(null) / 2;
		}
		g2d.drawImage(background, 0, y, null);
		g2d.drawImage(background, 0, y1, null);
	}

	private void renderRockets(Graphics2D g2d) {
		spaceShip.getRockets().render(g2d);
	}

	private void drawBullets(Graphics2D g2d) {
		spaceShip.getBullets().render(g2d);
	}

	long firstTimePressed = 0, sleep, lastTimePressed = 0;

	boolean LEFT = false, RIGHT = false, CENTER = false;

	@Override
	public void mouseDragged(MouseEvent e) {
		spaceShip.mouseMoved(e);
		setMousePosition(spaceShip);
		if (spaceShip.isVisible() && e.getButton() == 1) {
			pressed = true;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		spaceShip.mouseMoved(e);
		setMousePosition(spaceShip);
		pressed = false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	public boolean pressed = false;

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == 1) {
			LEFT = true;
		}
		if (e.getButton() == 2) {
			CENTER = true;
		}
		if (e.getButton() == 3) {
			RIGHT = true;
		}

		if (spaceShip.isVisible() && LEFT) {
			pressed = true;
			spaceShip.fire();
		}

		if (spaceShip.isVisible() && CENTER && spaceShip.getRocket() > 0) {
			spaceShip.setRocket(spaceShip.getRocket() - 1);
			MyPoint.Point point = spaceShip.getCenterPoint();
			int startX = (int) (point.getX() - Rocket.W / 2);
			int startY = (int) (point.getY() - Rocket.H / 2);
			Rocket rocket = new Rocket(startX, startY,
					Math.atan2(MainMenu.Height / 2 - point.getY(), MainMenu.Width / 2 - point.getX()));
			rocket.setId(spaceShip.getId());
			spaceShip.getRockets().addToRockets(rocket);
		}

	}

	@Override
	public void update(Graphics g) {
		super.update(g);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == 1) {
			LEFT = false;
		}
		if (e.getButton() == 2) {
			CENTER = false;
		}
		if (e.getButton() == 3) {
			RIGHT = false;
		}

		if (!LEFT)
			pressed = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	public Level getLevel() {
		return level;
	}

}
