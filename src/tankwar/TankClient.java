package tankwar;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;

public class TankClient extends Frame {
	public static final int START_X = 200,START_Y = 200;
	public static final int WIDTH = 800, HEIGHT = 600;
	public static final int FRAME_INTERVAL = 50;
	public static final Color BACKGROUND_COLOR = Color.LIGHT_GRAY;
	public static final int WIDTH_WALL = 50, HEIGHT_WALL = 300;
	private int myTankStartX = WIDTH/2, myTankStartY = HEIGHT - Tank.SIZE_Y;
	public static final int ROBOTTANK_NUM = 10;
	
	private QuitDialog messageDialog = new QuitDialog(this, "Game Over", true);
	
	private Wall wall = new Wall (WIDTH/2 - WIDTH_WALL/2,HEIGHT/2 - HEIGHT_WALL/2, WIDTH_WALL, HEIGHT_WALL, this);
	private Tank myTank = new Tank (myTankStartX, myTankStartY, true, this);
	private List<Tank> robotTanks = new LinkedList<Tank> ();
	private List<Missile> missileList = new LinkedList<Missile>();
	private List<Explosion> explosionList = new LinkedList<Explosion> ();
	
	public Wall getWall() {
		return wall;
	}
	public Tank getMyTank() {
		return myTank;
	}
	public List<Tank> getRobotTanks() {
		return robotTanks;
	}
	public void addMissile(Missile missile) {
		this.missileList.add(missile);
	}
	public void removeMissile(Missile missile) {
		if (this.missileList.contains(missile))
			this.missileList.remove(missile);
	}
	
	public void addExplosion(Explosion e) {
		this.explosionList.add(e);
	}
	public void removeExplosion(Explosion e) {
		if (this.explosionList.contains(e))
			this.explosionList.remove(e);
	}

	//单例模式
	private TankClient () {};
	private static TankClient tc = null;
	public static TankClient getInstance () {
		if (tc == null) {
			tc = new TankClient ();
		}
		return tc;
	}
	
	
	public void launch () {
		robotTanks.add(myTank);
		for (int i = 0; i < ROBOTTANK_NUM; ++i) {
			robotTanks.add(new Tank(100+40*i , 50, false, this));
		}
		this.setTitle("Tank War");
		this.setLocation(START_X, START_Y);
		this.setSize(WIDTH, HEIGHT);
		this.setResizable(false);
		this.setBackground(BACKGROUND_COLOR);
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter () {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		//new Thread (new PaintThread ()).start();;
		this.addKeyListener(new KeyMonitor());
	}
	
	//双缓冲
	Image offScreenImage = null;

	@Override
	public void update(Graphics g) {
		if (offScreenImage == null) {
			offScreenImage = this.createImage(WIDTH, HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		gOffScreen.setColor(BACKGROUND_COLOR);
		gOffScreen.fillRect(0, 0, WIDTH, HEIGHT);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	@Override
	public void paint(Graphics g) {
		drawString (g);
		drawTank (g);
		drawMissile (g);
		drawExplosion (g);
		wall.draw(g);
		repaint ();
		try {
			Thread.sleep(FRAME_INTERVAL);
		}catch (InterruptedException e) {
			e.printStackTrace();
		} 
		if (myTank.isLive() == false)
			messageDialog.setVisible(true);
	}
	
	private void drawString (Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.drawString("missile count:"+missileList.size(), 20, 10);
		g.drawString("explosion count:"+explosionList.size(), 20, 20);
		g.drawString("robotTanks count:"+robotTanks.size(), 20, 30);
		g.drawString("My Tank life:"+myTank.life, 20, 40);
		g.setColor(c);
	}
	
	private void drawTank (Graphics g) {
		//myTank.draw(g);
		for (int i = 0; i < robotTanks.size(); ++i) {
			robotTanks.get(i).draw(g);
		}
	}
	
	private void drawMissile (Graphics g) {
		if (!missileList.isEmpty()) {
			for (int i = 0; i < missileList.size(); ++i) {
				Missile m = missileList.get (i);
				if (m.hitRobotTanks(robotTanks)) {
					missileList.remove(m);
				} else
					m.draw(g);
			}
		}
	}
	
	private void drawExplosion (Graphics g) {
		if (!explosionList.isEmpty()) {
			for (int i = 0; i < explosionList.size(); ++i) {
				Explosion e = explosionList.get(i);
				if (e.isLive())
					e.draw(g);
				else {
					explosionList.remove(e);
				}
			}
		}
	}
	
	private class KeyMonitor extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}
	}

/*	
	private class PaintThread implements Runnable {
		@Override
		public void run() {
			while (true) {
				tc.repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
*/
	private class QuitDialog extends Dialog {
		private static final int WIDTH_DIALOG = 300, HEIGHT_DIALOG = 100;
		Label l_over = new Label ("Game Over");
		Button b_restart = new Button ("Restart");
		Button b_quit = new Button ("Quit");
		Panel p = new Panel (new FlowLayout(2));
		QuitDialog (TankClient tc, String s, boolean b) {
			super (tc, s, b);
			setBounds(TankClient.START_X + TankClient.WIDTH/2 - WIDTH_DIALOG/2 , TankClient.START_Y + TankClient.HEIGHT/2 - HEIGHT_DIALOG/2, WIDTH_DIALOG, HEIGHT_DIALOG);
			this.addWindowListener(new WindowAdapter () {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
			p.add(b_restart);
			p.add(b_quit);
			setLayout(new GridLayout(2, 1));
			add(l_over);
			add(p);
			b_restart.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
				}
			});
			b_quit.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
		}
	}
	
	public static void main(String[] args) {
		TankClient tc = TankClient.getInstance();
		tc.launch();
	}

}
