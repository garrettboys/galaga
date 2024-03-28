import java.awt.Color;

import javax.swing.ImageIcon;

public class Player extends Entity {

	public Player()
	{
		x = 600;
		y = 625;
		width = 90;
		height = 90;
		c = Color.white;
		dx = 0;
		dy = 0;
		movert = false;
		pic = new ImageIcon("ship.png");
	}
	
	public Player(int x1, int y1, int w, int h, Color c1, int dx1, int dy1, String imglink)
	{
		x = x1;
		y = y1;
		width = w;
		height = h;
		c = c1;
		dx = dx1;
		dy = dy1;
		pic = new ImageIcon(imglink);
	}
	
	public Player(int x1, int y1)
	{
		x = x1;
		y = y1;
		width = 50;
		height = 50;
		c = Color.white;
		dx = 0;
		dy = 0;
		pic = new ImageIcon("ship.png");
	}
	
}
