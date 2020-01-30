package enemy;

import java.awt.Image;
import java.util.Arrays;
import java.util.List;
import GameFrames.Drawable;
import boss.Boss;
import chickens.ChickenLevel1;
import chickens.ChickenLevel2;
import chickens.ChickenLevel3;
import chickens.ChickenLevel4;
import egg.Egg;

public abstract class Enemy extends Drawable {
	private static final long serialVersionUID = 1L;
	public static final List<Image> IMAGES = Arrays.asList(ChickenLevel1.cl1, ChickenLevel2.cl2, ChickenLevel3.cl3,
			ChickenLevel4.cl4, Egg.egg, Boss.boss);
	protected Image image;

	public int getIndex() {
		return IMAGES.indexOf(image);
	}
}
