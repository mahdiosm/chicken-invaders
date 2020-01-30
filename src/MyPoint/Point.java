package MyPoint;

public class Point {
	private float x, y;

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Point(double x, double y) {
		this.x = (float) x;
		this.y = (float) y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + x + " ," + y + ")";
	}
}
