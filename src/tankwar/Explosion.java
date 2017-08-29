package tankwar;

import java.awt.Color;
import java.awt.Graphics;

public class Explosion {
	public static final Color COLOR = Color.ORANGE;
	private int center_x, center_y;
	private boolean isLive;

	private TankClient tc;
	int [] diameter = {5, 8, 13, 20, 32, 45, 28, 15, 6};
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
	
	public void draw (Graphics g) {
		if (!isLive)
			return ;
		Color c = g.getColor();
		if (explodeStep == diameter.length) {
			isLive = false;
			explodeStep = 0;
		}
		g.setColor(COLOR);
		g.fillOval(center_x - diameter[explodeStep]/2, center_y - diameter[explodeStep]/2, diameter[explodeStep], diameter[explodeStep]);
		explodeStep++;
		g.setColor(c);
	}
}
