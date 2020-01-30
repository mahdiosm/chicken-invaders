package Users;

import java.io.Serializable;

public class User implements Comparable<User>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name = "------";
	private int id;
	private int score = 0, heart = 5, power = 0, coin = 0, rocket = 3;
	private int level = 11;
	private int weaponType = 1;
	private long timePassed = 0, beginingOfGame;
	private boolean DiedAndExited = false;
	private boolean canContinue = false;

	public User(String name, int score, int power, int heart, int coin, int rocket, int level, long timePassed,
			int weaponType, boolean canContinue) {
		this.power = power;
		this.heart = heart;
		this.coin = coin;
		this.rocket = rocket;
		this.name = name;
		this.score = score;
		this.level = level;
		this.weaponType = weaponType;
		this.canContinue = canContinue;
		this.timePassed = timePassed;
		id = name.hashCode();
	}

	public User(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRocket() {
		return rocket;
	}

	public void setRocket(int rocket) {
		this.rocket = rocket;
	}

	public int getHeart() {
		return heart;
	}

	public void setHeart(int heart) {
		this.heart = heart;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}

	public boolean isDiedAndExited() {
		return DiedAndExited;
	}

	public void setDiedAndExited(boolean diedAndExited) {
		DiedAndExited = diedAndExited;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getLevelsPassed() {
		return level;
	}

	public void setLevelsPassed(int levelsPassed) {
		this.level = levelsPassed;
	}

	@Override
	public int compareTo(User o) {
		if (o.getScore() != getScore()) {
			return o.getScore() - getScore();
		} else if (o.getLevelsPassed() != getLevelsPassed()) {
			return getLevelsPassed() - o.getLevelsPassed();
		} else {
			return getTimePassed() > o.getTimePassed() ? 1 : -1;
		}
	}

	public boolean CanContiue() {
		return canContinue;
	}

	public void setCanContiue(boolean canContiue) {
		this.canContinue = canContiue;
	}

	public long getTimePassed() {
		return timePassed;
	}

	public void setTimePassed(long timePassed) {
		this.timePassed = timePassed;
	}

	public int getWeaponType() {
		return weaponType;
	}

	public void setWeaponType(int weaponType) {
		this.weaponType = weaponType;
	}

	public long getBeginingOfGame() {
		return beginingOfGame;
	}

	public void setBeginingOfGame(long beginingOfGame) {
		this.beginingOfGame = beginingOfGame;
	}

}
