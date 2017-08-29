package tankwar;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;

public class TankClient extends Frame {
	public static final int START_X = 200;
	public static final int START_Y = 200;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final int FRAME_INTERVAL = 50;
	public static final Color BACKGROUND_COLOR = Color.LIGHT_GRAY;
	private int x = 50;
	private int y = 50;
	
	private Tank tank = new Tank (x, y, this);
	private List<Missile> missile_List = new LinkedList<Missile>();
	
	public void setMissileList(Missile missile) {
		this.missile_List.add(missile);
	}
	
	public void removeMissileList(Missile missile) {
		if (this.missile_List.contains(missile))
			this.missile_List.remove(missile);
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
		tank.draw(g);
		if (!missile_List.isEmpty()) {
			for (int i = 0; i < missile_List.size(); ++i) {
				missile_List.get(i).draw(g);
			}
		}
		drawString (g);
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
		g.drawString("missile count:"+missile_List.size(), 30, 30);
		g.setColor(c);
	}
	
	private class KeyMonitor extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			tank.keyPressed(e);
		}
		public void keyReleased(KeyEvent e) {
			tank.keyReleased(e);
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
