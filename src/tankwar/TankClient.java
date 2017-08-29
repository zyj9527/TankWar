package tankwar;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;

public class TankClient extends Frame {
	public static final int START_X = 200,START_Y = 200;
	public static final int WIDTH = 800, HEIGHT = 600;
	public static final int FRAME_INTERVAL = 50;
	public static final Color BACKGROUND_COLOR = Color.LIGHT_GRAY;
	private int myTankStartX = 50, myTankStartY = 50;
	private int robotTankStartX = 200, robotTankStartY = 50;
	
	private Tank myTank = new Tank (myTankStartX, myTankStartY, true, this);
	private Tank robotTank = new Tank (robotTankStartX, robotTankStartY, false, this);
	private List<Missile> missileList = new LinkedList<Missile>();
	private List<Explosion> explosionList = new LinkedList<Explosion> ();
	
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
		myTank.draw(g);
		robotTank.draw(g);
		if (!missileList.isEmpty()) {
			for (int i = 0; i < missileList.size(); ++i) {
				Missile m = missileList.get (i);
				if (m.hitTank(robotTank)) {
					robotTank.setLive(false);
					missileList.remove(m);
				} else
					m.draw(g);
			}
		}
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
		repaint ();
		try {
			Thread.sleep(FRAME_INTERVAL);
		}catch (InterruptedException e) {
			e.printStackTrace();
		} 
	}
	
	private void drawString (Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.drawString("missile count:"+missileList.size(), 20, 10);
		g.drawString("explosion count:"+explosionList.size(), 20, 30);
		g.setColor(c);
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

	public static void main(String[] args) {
		TankClient tc = TankClient.getInstance();
		tc.launch();
	}

}
