package tankwar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Explosion {
	public static final Color COLOR = Color.ORANGE;
	private int center_x, center_y;
	private boolean isLive;

	private TankClient tc;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] imgs = {
			tk.getImage(Explosion.class.getClassLoader().getResource("images/0.gif")),
			tk.getImage(Explosion.class.getClassLoader().getResource("images/1.gif")),
			tk.getImage(Explosion.class.getClassLoader().getResource("images/2.gif")),
			tk.getImage(Explosion.class.getClassLoader().getResource("images/3.gif")),
			tk.getImage(Explosion.class.getClassLoader().getResource("images/4.gif")),
			tk.getImage(Explosion.class.getClassLoader().getResource("images/5.gif")),
			tk.getImage(Explosion.class.getClassLoader().getResource("images/6.gif")),
			tk.getImage(Explosion.class.getClassLoader().getResource("images/7.gif")),
			tk.getImage(Explosion.class.getClassLoader().getResource("images/8.gif")),
			tk.getImage(Explosion.class.getClassLoader().getResource("images/9.gif")),
			tk.getImage(Explosion.class.getClassLoader().getResource("images/10.gif")),
	};
	private static boolean initImg = false;
	
	//int [] diameter = {5, 8, 13, 20, 32, 45, 28, 15, 6};
	private int explodeStep;
	
	Explosion (int center_x, int center_y, TankClient tc) {
		this.center_x = center_x;
		this.center_y = center_y;
		this.isLive = true;
		this.explodeStep = 0;
	}
	
	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}
	
//	public void draw (Graphics g) {
//		if (!isLive)
//			return ;
//		Color c = g.getColor();
//		if (explodeStep == diameter.length) {
//			isLive = false;
//			explodeStep = 0;
//		}
//		g.setColor(COLOR);
//		g.fillOval(center_x - diameter[explodeStep]/2, center_y - diameter[explodeStep]/2, diameter[explodeStep], diameter[explodeStep]);
//		explodeStep++;
//		g.setColor(c);
//	}
	
	public void draw (Graphics g) {
		if (!initImg) {
			for (int i = 0; i < imgs.length; i++) {
				g.drawImage(imgs[i], -100, -100, null);
			}
			initImg = true;
		}
		if (!isLive)
			return;
		if (explodeStep == imgs.length) {
			isLive = false;
			explodeStep = 0;
		}
		g.drawImage(imgs[explodeStep], center_x, center_y, null);
		explodeStep++;
	}
}
