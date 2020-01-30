package chickensGroups;

import boss.Boss;

public abstract class GroupOfBoss extends Group {
	private static final long serialVersionUID = 1L;
	protected Boss boss;

	public Boss getBoss() {
		return boss;
	}

	public void setBoss(Boss boss) {
		this.boss = boss;
	}
}
