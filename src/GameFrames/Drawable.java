package GameFrames;

import java.awt.Graphics;
import java.io.Serializable;

import MyPoint.Point;

public abstract class Drawable implements Serializable{
	private static final long serialVersionUID = 1L;
	protected int r, w, h;
	protected int id;
	protected float xCenter, yCenter, x, y;
	protected boolean visible = true;

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean f) {
		this.visible = f;
	}

	public abstract void render(Graphics g);

	public abstract void move();

	public Point getCenterPoint() {
		return new Point(xCenter, yCenter);
	}

	public void setCenterPoint(Point point) {
		this.xCenter = point.getX();
		this.yCenter = point.getY();
		x = xCenter - w / 2;
		y = yCenter - h / 2;
	}

	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return h;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getR() {
		return r;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
		this.xCenter = x + w / 2;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
		this.yCenter = y + h / 2;
	}
}
