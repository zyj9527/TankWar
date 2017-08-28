package tankwar;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;

public class TankClient extends Frame {
	private final int START_X = 200;
	private final int START_Y = 200;
	private final int WIDTH = 800;
	private final int HEIGHT = 600;
	private final int FRAME_INTERVAL = 50;
	private final Color BACKGROUND_COLOR = Color.LIGHT_GRAY;
	private int x = 50;
	private int y = 50;
	
	private Tank tank = new Tank (x, y, this);
	private Missile missile = null;
	private List<Missile> missile_List = new LinkedList<Missile>();
	
	public void setMissileList(Missile missile) {
		this.missile_List.add(missile);
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
		repaint ();
		try {
			Thread.sleep(FRAME_INTERVAL);
		}catch (InterruptedException e) {
			e.printStackTrace();
		} 
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
