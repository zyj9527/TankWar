package tankwar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Tank {
	private final int SIZE_X = 20;
	private final int SIZE_Y = 20;
	private final int STEP = 10;
	private int x;
	private int y;
	
	private boolean up_pressed = false, down_pressed = false, left_pressed = false, right_pressed = false;
	enum Direction {UP,DOWN,LEFT,RIGHT,LEFT_UP,RIGHT_UP,LEFT_DOWN,RIGHT_DOWN,STOP};
	private Direction direction = Direction.STOP;
	
	public Tank(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void draw (Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.RED);
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
		case STOP:
			break;
		}
	}
	
	public void keyPressed (KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			up_pressed = true;
			break;
		case KeyEvent.VK_DOWN:
			down_pressed = true;
			break;
		case KeyEvent.VK_RIGHT:
			right_pressed = true;
			break;
		case KeyEvent.VK_LEFT:
			left_pressed = true;
			break;
		default:
			break;
		}
		directionUpdate ();
	}

	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			up_pressed = false;
			break;
		case KeyEvent.VK_DOWN:
			down_pressed = false;
			break;
		case KeyEvent.VK_RIGHT:
			right_pressed = false;
			break;
		case KeyEvent.VK_LEFT:
			left_pressed = false;
			break;
		default:
			break;
		}
		directionUpdate ();
	}
	
	private void directionUpdate () {
		if (up_pressed && !down_pressed && !left_pressed && !right_pressed) direction = Direction.UP;
		else if (!up_pressed && down_pressed && !left_pressed && !right_pressed) direction = Direction.DOWN;
		else if (!up_pressed && !down_pressed && left_pressed && !right_pressed) direction = Direction.LEFT;
		else if (!up_pressed && !down_pressed && !left_pressed && right_pressed) direction = Direction.RIGHT;
		else if (up_pressed && !down_pressed && left_pressed && !right_pressed) direction = Direction.LEFT_UP;
		else if (!up_pressed && down_pressed && left_pressed && !right_pressed) direction = Direction.LEFT_DOWN;
		else if (up_pressed && !down_pressed && !left_pressed && right_pressed) direction = Direction.RIGHT_UP;
		else if (!up_pressed && down_pressed && !left_pressed && right_pressed) direction = Direction.RIGHT_DOWN;
		else if (!up_pressed && !down_pressed && !left_pressed && !right_pressed) direction = Direction.STOP;
	}
}
