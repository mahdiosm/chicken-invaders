package levels.level1;

import levels.BossWave;

public class Wave5 extends Level1 implements BossWave{
	private static final long serialVersionUID = 1L;

	public Wave5() {
		setCode(15).setNextLevelCode(21).setBossGroup().count = 1;
	}
}