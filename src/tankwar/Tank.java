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
	
	public Tank(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void draw (Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.RED);
		g.fillOval(x, y, SIZE_X, SIZE_Y);
		g.setColor(c);
	} 
	
	public void keyEvent (KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			y -= STEP;
			break;
		case KeyEvent.VK_DOWN:
			y += STEP;
			break;
		case KeyEvent.VK_RIGHT:
			x += STEP;
			break;
		case KeyEvent.VK_LEFT:
			x -= STEP;
			break;
		default:
			break;
		}
	}
}
