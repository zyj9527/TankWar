package tankwar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Tank {
	public static final int SIZE_X = 26;
	public static final int SIZE_Y = 26;
	public static final int STEP = 8;
	public static final Color COLOR = Color.RED;
	private int x;
	private int y;
	private TankClient tc;
	
	private boolean up_pressed = false, down_pressed = false, left_pressed = false, right_pressed = false;
	enum Direction {UP,DOWN,LEFT,RIGHT,LEFT_UP,RIGHT_UP,LEFT_DOWN,RIGHT_DOWN,STOP};
	private Direction direction = Direction.STOP;
	private Direction ptDirection = Direction.DOWN;
	
	public Tank(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Tank(int x, int y, TankClient tc) {
		this (x, y);
		this.tc = tc;
	}
	
	public void draw (Graphics g) {
		Color c = g.getColor();
		g.setColor(COLOR);
		g.fillOval(x, y, SIZE_X, SIZE_Y);
		g.setColor(c);
		drawPt (g);
		move ();
	} 
	
	private void drawPt(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		switch (ptDirection) {
		case UP:
			g.drawLine(x + Tank.SIZE_X/2, y + Tank.SIZE_Y/2, x + Tank.SIZE_X/2, y);
			break;
		case DOWN:
			g.drawLine(x + Tank.SIZE_X/2, y + Tank.SIZE_Y/2, x + Tank.SIZE_X/2, y+Tank.SIZE_Y);
			break;
		case LEFT:
			g.drawLine(x + Tank.SIZE_X/2, y + Tank.SIZE_Y/2, x, y + Tank.SIZE_Y/2);
			break;
		case RIGHT:
			g.drawLine(x + Tank.SIZE_X/2, y + Tank.SIZE_Y/2, x + Tank.SIZE_X, y + Tank.SIZE_Y/2);
			break;
		case LEFT_UP:
			g.drawLine(x + Tank.SIZE_X/2, y + Tank.SIZE_Y/2, x, y);
			break;
		case LEFT_DOWN:
			g.drawLine(x + Tank.SIZE_X/2, y + Tank.SIZE_Y/2, x, y + Tank.SIZE_Y);
			break;
		case RIGHT_UP:
			g.drawLine(x + Tank.SIZE_X/2, y + Tank.SIZE_Y/2, x + Tank.SIZE_X, y);
			break;
		case RIGHT_DOWN:
			g.drawLine(x + Tank.SIZE_X/2, y + Tank.SIZE_Y/2, x + Tank.SIZE_X, y + Tank.SIZE_Y);
			break;
		default:
			break;
		}
		g.setColor(c);
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
		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;
		if (x + Tank.SIZE_X > TankClient.WIDTH)
			x = TankClient.WIDTH - Tank.SIZE_X;
		if (y + Tank.SIZE_Y > TankClient.HEIGHT)
			y = TankClient.HEIGHT - Tank.SIZE_Y;
	}
	
	public void keyPressed (KeyEvent e) {
		switch (e.getKeyCode()) {
		/*case KeyEvent.VK_CONTROL:
			fire();
			break;*/
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
		case KeyEvent.VK_CONTROL:
			fire();
			break;
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
		
		if (direction != Direction.STOP)
			ptDirection = direction;
	}

	private void fire () {
		int m_x = this.x + Tank.SIZE_X/2 - Missile.SIZE_X/2;
		int m_y = this.y + Tank.SIZE_Y/2 - Missile.SIZE_Y/2;
		tc.setMissileList(new Missile(m_x, m_y, ptDirection,tc));
	}
}
