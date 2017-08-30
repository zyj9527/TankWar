package tankwar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

public class Tank {
	public static final int SIZE_X = 26, SIZE_Y = 26;
	public static final int STEP = 6, FULL_BLOOD = 100;
	public static final Color GOODCOLOR = Color.BLUE, BADCOLOR = Color.RED;
	private int x, y, old_x, old_y;
	public int life = FULL_BLOOD;

	private boolean isGood;
	private boolean isLive;
	private Random random = new Random ();
	private int robotStep = random.nextInt(12) + 3;
	private TankClient tc;
	
	private boolean up_pressed = false, down_pressed = false, left_pressed = false, right_pressed = false;
	enum Direction {UP,DOWN,LEFT,RIGHT,LEFT_UP,RIGHT_UP,LEFT_DOWN,RIGHT_DOWN,STOP};
	private Direction direction = Direction.STOP;
	private Direction ptDirection = Direction.DOWN;
	
	public Tank(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Tank(int x, int y, boolean good, TankClient tc) {
		this (x, y);
		this.tc = tc;
		this.isGood = good;
		this.isLive = true;
		if (!isGood) {
			Direction[] dir = Direction.values(); 
			direction = dir[random.nextInt(dir.length)];
		}
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public void setLife (int life){
		this.life = life;
	}
	public boolean isGood() {
		return isGood;
	}
	public boolean isLive() {
		return isLive;
	}
	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}
	
	public void draw (Graphics g) {
		if (!this.isLive)
			return;
		Color c = g.getColor();
		if (isGood)
			g.setColor(GOODCOLOR);
		else
			g.setColor(BADCOLOR);
		g.fillOval(x, y, SIZE_X, SIZE_Y);
		g.setColor(c);
		drawPt (g);
		if (isGood)
			drawLife (g);
		move ();
	} 
	
	private void drawLife (Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.GREEN);
		g.drawRect(x, y - 10, SIZE_X , 10);
		if (life > FULL_BLOOD*2/3)
			g.setColor(Color.GREEN);
		else if (life > FULL_BLOOD/3)
			g.setColor(Color.YELLOW);
		else
			g.setColor(Color.RED);
		g.fillRect(x, y - 10, life * SIZE_X/ FULL_BLOOD , 10);
		g.setColor(c);
	}
	
	private void drawPt(Graphics g) {
		if (direction != Direction.STOP)
			ptDirection = direction;
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
		old_x = x; old_y = y;
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
		
		if (!isGood) {
			if (random.nextInt(40) > 38)
				fire ();
			robotStep--;
			Direction [] dirs = Direction.values();
			if (robotStep == 0) {
				direction = dirs[random.nextInt(dirs.length)];
				robotStep = random.nextInt(10) + 3;
			}
		}
		
		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;
		if (x + Tank.SIZE_X > TankClient.WIDTH)
			x = TankClient.WIDTH - Tank.SIZE_X;
		if (y + Tank.SIZE_Y > TankClient.HEIGHT)
			y = TankClient.HEIGHT - Tank.SIZE_Y;
		
		if ((collideWall() && !isGood) || coolideTank()) {
			x = old_x;
			y = old_y;
		}
		
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
		case KeyEvent.VK_A:
			fire_super();
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
		
//		if (direction != Direction.STOP)
//			ptDirection = direction;
	}

	private void fire () {
		if (!isLive)
			return ;
		int m_x = this.x + Tank.SIZE_X/2 - Missile.SIZE_X/2;
		int m_y = this.y + Tank.SIZE_Y/2 - Missile.SIZE_Y/2;
		tc.addMissile(new Missile(m_x, m_y, isGood, ptDirection,tc));
	}
	
	private void fire_super () {
		if (!isLive)
			return ;
		int m_x = this.x + Tank.SIZE_X/2 - Missile.SIZE_X/2;
		int m_y = this.y + Tank.SIZE_Y/2 - Missile.SIZE_Y/2;
		Direction[] dirs = Direction.values();
		for (int i = 0; i < dirs.length - 1; i++) {
			tc.addMissile(new Missile(m_x, m_y,isGood, dirs[i],tc));
		}
	}
	
	public Rectangle getRect () {
		return new Rectangle(x, y, SIZE_X, SIZE_Y);
	}
	
	public boolean collideWall () {
		if (getRect().intersects(tc.getWall().getRect())) {
			return true;
		}
		return false;
	}
	
	public boolean coolideTank () {
		List <Tank> listTanks = tc.getRobotTanks();
		for (int i = 0; i < listTanks.size();i++) {
			if (getRect().intersects(listTanks.get(i).getRect()) && !listTanks.get(i).equals(this))
				return true;
		}
		return false;
	}
}
