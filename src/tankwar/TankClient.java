package tankwar;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TankClient extends Frame {
	
	private TankClient () {};
	private static TankClient tc = null;
	public static TankClient getInstance () {
		if (tc == null) {
			tc = new TankClient ();
		}
		return tc;
	}
	
	private int WIDTH = 800;
	private int HEIGHT = 600;
	private Color color = Color.LIGHT_GRAY;
	private int x = 50;
	private int y = 50;
	
	public void launch () {
		this.setLocation(200, 200);
		this.setSize(WIDTH, HEIGHT);
		this.setResizable(false);
		this.setBackground(color);
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter () {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		//new Thread (new PaintThread ()).start();;
	}
	
	//双缓冲
	Image offScreenImage = null;

	@Override
	public void update(Graphics g) {
		if (offScreenImage == null) {
			offScreenImage = this.createImage(WIDTH, HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		gOffScreen.setColor(color);
		gOffScreen.fillRect(0, 0, WIDTH, HEIGHT);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	@Override
	public void paint(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.black);
		g.fillOval(x, y, 20, 20);
		g.setColor(c);
		y+=20;
		repaint ();
		try {
			Thread.sleep(50);
		}catch (InterruptedException e) {
			e.printStackTrace();
		} 
	}
	
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

	public static void main(String[] args) {
		TankClient tc = TankClient.getInstance();
		tc.launch();
	}

}
