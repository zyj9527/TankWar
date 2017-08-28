package tankwar;

import java.awt.Color;
import java.awt.Graphics;

import tankwar.Tank.Direction;

public class Missile {
	private static final int SIZE_X = 10;
	private static final int SIZE_Y = 10;
	private static final int STEP = 15;
	private static final Color COLOR = Color.BLACK;
	private int x;
	private int y;
	
	public static int getSizeX() {
		return SIZE_X;
	}

	public static int getSizeY() {
		return SIZE_Y;
	}

	private Tank.Direction direction;
	
	public Missile(int x, int y, Direction direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
	}

	public void draw (Graphics g) {
		Color c = g.getColor();
		g.setColor(COLOR);
		g.fillOval(x, y, SIZE_X, SIZE_Y);
		g.setColor(c);
		move ();
	}
	
	private void move () {
		switch (direction) {
		case UP:
			y -= STEP;
			break;
		case DOWN:
			y += STEP;
			break;
		case LEFT:
			x -= STEP; 
			break;
		case RIGHT:
			x += STEP;
			break;
		case LEFT_UP:
			x -= STEP;
			y -= STEP;
			break;
		case LEFT_DOWN:
			x -= STEP;
			y += STEP;
			break;
		case RIGHT_UP:
			x += STEP;
			y -= STEP;
			break;
		case RIGHT_DOWN:
			x += STEP;
			y += STEP;
			break;
		}
	}
}
