package powerUp.gift;

import GameFrames.SpaceShip;
import powerUp.PowerUp;

public abstract class Gift extends PowerUp {
	int weaponType = 1;
	public int percent;

	public int getPercent() {
		return percent;
	}

	@Override
	public void action(SpaceShip p) {
		p.setWeaponType(weaponType);
	}
}
