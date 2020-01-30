package collision;

import java.util.ArrayList;
import java.util.Iterator;
import Bullet.Bullet;
import Bullet.Bullets;
import Bullet.LaserBullet;
import GameFrames.Drawable;
import GameFrames.GamePanel;
import GameFrames.SpaceShip;
import MainFrame.GameMode;
import MyPoint.Point;
import boss.Boss;
import boss.BossWeapon;
import chickens.Chicken;
import chickensGroups.GroupOfBoss;
import chickensGroups.Group;
import egg.Egg;
import egg.Eggs;
import levels.BossWave;
import levels.Level;
import network.ServerPlayer;
import powerUp.Coin;
import powerUp.PowerUp;

public class Collision {
	public static boolean intersectes(Drawable a, Drawable b) {
		if ((distance(a, b) <= a.getR() + b.getR()) && a.isVisible() && b.isVisible()) {
			return true;
		}
		return false;
	}

	private static float distance(Drawable a, Drawable b) {
		Point a1 = a.getCenterPoint();
		Point b1 = b.getCenterPoint();
		return (float) Math.sqrt(Math.pow(a1.getX() - b1.getX(), 2) + Math.pow(a1.getY() - b1.getY(), 2));
	}

	public static void checkCollisions(GamePanel p) {
		if (GameMode.type == GameMode.SINGLE) {
			chicken_Bullet_CollisionCheck(p, GamePanel.spaceShip.getBullets());
			spaceShip_PowerUp_CollisionCheck(GamePanel.spaceShip);
			spaceShip_BulletsOfBoss_CollisionCheck(GamePanel.spaceShip);
			spaceShip_Eggs_CollisionCheck(GamePanel.spaceShip, GamePanel.level.getGroup().getEggs());
			spaceShip_ChickensOrBoss_CollisionCheck(GamePanel.spaceShip, GamePanel.level);
			bullet_coin_collisionCheck(GamePanel.spaceShip);
		} else {
			ArrayList<SpaceShip> spaceShips = new ArrayList<>(ServerPlayer.otherPlayers.values());
			spaceShips.add(GamePanel.spaceShip);
			chicken_Bullet_CollisionCheck_server(p);
			spaceShip_PowerUp_CollisionCheck_server(spaceShips);
			spaceShip_Eggs_CollisionCheck_server(spaceShips);
			spaceShip_ChickensOrBoss_CollisionCheck_server(spaceShips);
			bullet_coin_collisionCheck(GamePanel.spaceShip);
			spaceShip_BulletsOfBoss_CollisionCheck_server(spaceShips);
		}
	}

	private static void spaceShip_BulletsOfBoss_CollisionCheck_server(ArrayList<SpaceShip> spaceShips) {
		Iterator<SpaceShip> iterator1 = spaceShips.iterator();
		while (iterator1.hasNext()) {
			SpaceShip spaceShip = iterator1.next();
			Iterator<Bullet> bullets = GamePanel.spaceShip.getBullets().getBullets().iterator();
			while (bullets.hasNext()) {
				Bullet bullet = bullets.next();
				if (intersectes(bullet, spaceShip) && bullet.getId() != spaceShip.getId() && spaceShip.isHeatAble()) {
					spaceShip.setVisible(false);
					bullet.setVisible(false);
					bullets.remove();
				}
			}
		}
	}

	private static void spaceShip_ChickensOrBoss_CollisionCheck_server(ArrayList<SpaceShip> spaceShips) {
		Iterator<SpaceShip> iterator1 = spaceShips.iterator();
		Level level = GamePanel.level;
		if (level instanceof BossWave) {
			while (iterator1.hasNext()) {
				SpaceShip spaceShip = iterator1.next();
				if (intersectes(spaceShip, ((GroupOfBoss) level.getGroup()).getBoss()) && spaceShip.isHeatAble()) {
					spaceShip.setVisible(false);
					((GroupOfBoss) level.getGroup()).getBoss()
							.setHealth(((GroupOfBoss) level.getGroup()).getBoss().getHealth() - 20);
				}
			}
		} else {
			while (iterator1.hasNext()) {
				SpaceShip spaceShip = iterator1.next();
				Iterator<Chicken> iterator2 = level.getGroup().getChickens().iterator();
				while (iterator2.hasNext()) {
					Chicken chicken = iterator2.next();
					if (intersectes(spaceShip, chicken) && spaceShip.isHeatAble()) {
						chicken.setVisible(false);
						spaceShip.setVisible(false);
					}
				}
			}
		}
	}

	private static void spaceShip_Eggs_CollisionCheck_server(ArrayList<SpaceShip> spaceShips) {
		Iterator<SpaceShip> iterator1 = spaceShips.iterator();
		while (iterator1.hasNext()) {
			SpaceShip spaceShip = iterator1.next();
			Iterator<Egg> iterator2 = GamePanel.level.getGroup().getEggs().getEggs().iterator();
			while (iterator2.hasNext()) {
				Egg egg = iterator2.next();
				if (intersectes(egg, spaceShip) && spaceShip.isVisible()) {
					spaceShip.setVisible(false);
					iterator2.remove();
				}
			}
		}
	}

	private static void spaceShip_PowerUp_CollisionCheck_server(ArrayList<SpaceShip> spaceShips) {
		Iterator<SpaceShip> iterator1 = spaceShips.iterator();
		while (iterator1.hasNext()) {
			SpaceShip spaceShip = iterator1.next();
			Iterator<PowerUp> iterator2 = GamePanel.spaceShip.getPowerUps().iterator();
			while (iterator2.hasNext()) {
				PowerUp powerUp = iterator2.next();
				if (intersectes(powerUp, spaceShip) && powerUp.getId() == spaceShip.getId() && spaceShip.isVisible()) {
					powerUp.action(spaceShip);
					iterator2.remove();
				}
			}
		}
	}

	private static void chicken_Bullet_CollisionCheck_server(GamePanel p) {
		chicken_Bullet_CollisionCheck(p, SpaceShip.bullets);
	}

	private static void bullet_coin_collisionCheck(SpaceShip spaceShip) {
		for (Iterator<Bullet> iterator = spaceShip.getBullets().getBullets().iterator(); iterator.hasNext();) {
			Bullet bullet = iterator.next();
			for (Iterator<PowerUp> iterator2 = spaceShip.getPowerUps().iterator(); iterator2.hasNext();) {
				PowerUp powerUp = iterator2.next();
				if (powerUp instanceof Coin && !(bullet instanceof LaserBullet) && intersectes(powerUp, bullet)) {
					powerUp.setVisible(false);
					bullet.setVisible(false);
				}
			}
		}
	}

	private static void spaceShip_ChickensOrBoss_CollisionCheck(SpaceShip spaceShip, Level level) {
		if (level instanceof BossWave) {
			if (intersectes(spaceShip, ((GroupOfBoss) level.getGroup()).getBoss()) && spaceShip.isHeatAble()) {
				spaceShip.setVisible(false);
				((GroupOfBoss) level.getGroup()).getBoss()
						.setHealth(((GroupOfBoss) level.getGroup()).getBoss().getHealth() - 20);
			}
		} else {
			for (Iterator<Chicken> iterator = level.getGroup().getChickens().iterator(); iterator.hasNext();) {
				Chicken chicken = iterator.next();
				if (intersectes(spaceShip, chicken) && spaceShip.isHeatAble()) {
					chicken.setVisible(false);
					spaceShip.setVisible(false);
				}
			}
		}
	}

	private static void spaceShip_Eggs_CollisionCheck(SpaceShip spaceShip, Eggs eggs) {
		for (Egg egg : eggs.getEggs()) {
			if (intersectes(spaceShip, egg) && spaceShip.isHeatAble()) {
				spaceShip.setVisible(false);
				egg.setVisible(false);
			}
		}
	}

	private static void spaceShip_BulletsOfBoss_CollisionCheck(SpaceShip spaceShip) {
		for (Bullet bullet : spaceShip.getBullets().getBullets()) {
			if (intersectes(bullet, spaceShip) && bullet.getId() != spaceShip.getId() && spaceShip.isHeatAble()) {
				spaceShip.setVisible(false);
				bullet.setVisible(false);
			}
		}
	}

	private static void spaceShip_PowerUp_CollisionCheck(SpaceShip spaceShip) {
		Iterator<PowerUp> powerIterator = spaceShip.getPowerUps().iterator();
		while (powerIterator.hasNext()) {
			PowerUp powerUp = powerIterator.next();
			if (intersectes(powerUp, spaceShip) && powerUp.getId() == spaceShip.getId() && spaceShip.isVisible()) {
				powerUp.action(spaceShip);
				powerIterator.remove();
			}
		}
	}

	private static void chicken_Bullet_CollisionCheck(GamePanel gamePanel, Bullets bullets) {
		Level level = GamePanel.level;
		if (!(level instanceof BossWave)) {
			Group group = level.getGroup();
			Iterator<Chicken> iterator = group.getChickens().iterator();
			while (iterator.hasNext()) {
				Chicken chicken = iterator.next();
				Iterator<Bullet> iterator2 = bullets.getBullets().iterator();
				while (iterator2.hasNext()) {
					Bullet bullet = iterator2.next();
					if (intersectes(chicken, bullet) && !(bullet instanceof LaserBullet)) {
						chicken.setId(bullet.getId());
						chicken.setHealth(chicken.getHealth() - bullet.getPower());
						bullet.setVisible(false);
						iterator2.remove();
					}
				}
			}
		} else {
			Boss boss = ((GroupOfBoss) level.getGroup()).getBoss();
			for (Iterator<Bullet> iterator2 = bullets.getBullets().iterator(); iterator2.hasNext();) {
				Bullet bullet = iterator2.next();
				if (intersectes(boss, bullet) && bullet.getId() != BossWeapon.BOSS_WEAPON_ID
						&& !(bullet instanceof LaserBullet)) {
					boss.setId(bullet.getId());
					boss.setHealth(boss.getHealth() - bullet.getPower());
					bullet.setVisible(false);
				}
			}
		}
	}

	public static boolean laserBullet_firstCollisionCheck(SpaceShip spaceShip, Level level, LaserBullet bullet) {
		return checkChickens(level, bullet) || checkCoins(spaceShip, bullet);

	}

	private static boolean checkCoins(SpaceShip spaceShip, LaserBullet bullet) {
		for (Iterator<PowerUp> iterator = spaceShip.getPowerUps().iterator(); iterator.hasNext();) {
			PowerUp powerUp = iterator.next();
			if (powerUp instanceof Coin && intersectes(powerUp, bullet)) {
				powerUp.setVisible(false);
				return true;
			}
		}
		return false;
	}

	private static boolean checkChickens(Level level, LaserBullet bullet) {
		if (!(level instanceof BossWave)) {
			Group group = level.getGroup();
			for (Iterator<Chicken> iterator = group.getChickens().iterator(); iterator.hasNext();) {
				Chicken chicken = iterator.next();
				if (intersectes(chicken, bullet)) {
					chicken.setId(bullet.getId());
					chicken.setHealth(chicken.getHealth() - bullet.getPower());
					return true;
				}
			}
			return false;
		} else {
			Boss boss = ((GroupOfBoss) level.getGroup()).getBoss();
			if (intersectes(boss, bullet)) {
				boss.setId(bullet.getId());
				boss.setHealth(boss.getHealth() - bullet.getPower());
				return true;
			}
			return false;
		}
	}

}
