import java.awt.*;
import javax.swing.*;

public class Enemy extends Entity {
	
	public Enemy()
	{
		x = 100;
		y = 100;
		width = 90;
		height = 90;
		c = Color.white;
		dx = 0;
		dy = 0;
		movert = true;
		movedn = true;
		pic = new ImageIcon("enemy.png");
	}
	
	public Enemy(int x1, int y1, int w, int h, Color c1, int dx1, int dy1, String imglink)
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
	
	public Enemy(int x1, int y1)
	{
		x = x1;
		y = y1;
		width = 90;
		height = 90;
		c = Color.white;
		dx = 0;
		dy = 0;
		pic = new ImageIcon("enemy.png");
	}
	
}
