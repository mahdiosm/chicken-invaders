package levels.level4;

import GameFrames.GamePanel;
import levels.BossWave;
import levels.Level;

public class Wave5 extends Level4 implements BossWave {
	private static final long serialVersionUID = 1L;

	public Wave5() {
		setCode(51).setNextLevelCode(52).setBossGroup().count = 1;
	}

	@Override
	public Level update() {
		GamePanel.gameFinished = true;
		return this;
	}
}