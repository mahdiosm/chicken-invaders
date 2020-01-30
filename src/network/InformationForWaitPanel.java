package network;

public class InformationForWaitPanel {
	private int id, heart, score;
	private boolean seener;

	public InformationForWaitPanel(int id, int heart, int score, boolean seener) {
		this.id = id;
		this.heart = heart;
		this.score = score;
		this.seener = seener;
	}

	public boolean isSeener() {
		return seener;
	}

	public int getID() {
		return id;
	}

	public int getHeart() {
		return heart;
	}

	public int getScore() {
		return score;
	}
}
