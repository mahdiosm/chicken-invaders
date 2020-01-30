package levels.level3;

import levels.BossWave;

public class Wave5 extends Level3 implements BossWave {
	private static final long serialVersionUID = 1L;

	public Wave5() {
		setCode(35).setNextLevelCode(41).setBossGroup().count = 1;
	}
}