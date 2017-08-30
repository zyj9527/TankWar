package tankwar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import tankwar.Tank.Direction;

public class Missile {
	public static final int SIZE_X = 10;
	public static final int SIZE_Y = 10;
	public static final int STEP = 10;
	public static final Color BAD_COLOR = Color.BLACK, GOOD_COLOR = Color.cyan;
	private Color color;
	private int x, y;
	private boolean isLive;
	private boolean isGood;

	private TankClient tc;

	private Tank.Direction direction;
	
	public Missile(int x, int y, boolean good, Direction direction, TankClient tc) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.tc = tc;
		this.isGood = good;
		isLive = true;
		if (isGood)
			color = GOOD_COLOR;
		else
			color = BAD_COLOR;
			
	}

	public void draw (Graphics g) {
		Color c = g.getColor();
		g.setColor(color);
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
		default:
			break;
		}
		if (x < 0 || y < 0 || x > TankClient.WIDTH || y > TankClient.HEIGHT || collideWall()) {
			tc.removeMissile(this);
		}
	}
	
	public Rectangle getRect () {
		return new Rectangle(x, y, SIZE_X, SIZE_Y);
	}
	
	private boolean hitTank (Tank t) {
		if (t.isLive()) {
			if (getRect().intersects(t.getRect()) && (isGood != t.isGood())) {
				if (t.isGood()) {
					t.life -= 20;
					if (t.life <= 0) {
						t.setLive(false);
						tc.addExplosion(new Explosion(t.getX()+Tank.SIZE_X/2, t.getY()+Tank.SIZE_Y/2, tc));
					}
				} else {
					tc.addExplosion(new Explosion(t.getX()+Tank.SIZE_X/2, t.getY()+Tank.SIZE_Y/2, tc));
				}
				return true;
			}
		}
		return false;
	}
	
	public boolean hitRobotTanks (List<Tank> robottanks) {
		for (int i = 0; i < robottanks.size(); ++i) {
			Tank t = robottanks.get(i);
			if (hitTank (t)) {
				if (!t.isGood()) {
					robottanks.get(i).setLive(false);
					robottanks.remove(i);
				}
				return true;
			}
		}
		return false;
	}
	
	public boolean collideWall () {
		if (getRect().intersects(tc.getWall().getRect())) {
			return true;
		}
		return false;
	}
}
