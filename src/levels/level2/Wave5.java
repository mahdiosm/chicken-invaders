package levels.level2;

import levels.BossWave;

public class Wave5 extends Level2 implements BossWave {
	private static final long serialVersionUID = 1L;

	public Wave5() {
		setCode(25).setNextLevelCode(31).setBossGroup().count = 1;
	}
}
