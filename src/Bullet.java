import java.awt.Rectangle;

public class Bullet extends Entity 
{
    private final int dy = 1; // Adjust the speed as needed
    private boolean movedn = false;
    private boolean friendly = true;
    
    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public Bullet(int x, int y, boolean movedn, boolean friendly) {
        this.x = x;
        this.y = y;
        this.movedn = movedn;
        this.friendly = friendly;
    }


    public void move() {
    	if (!movedn)
    		y -= dy;
    	else
    		y += dy;
    		
    }
    
    public boolean hit(Entity e)
    {
    		if (getX()+getWidth()>=e.getX()&&getX()<=e.getX()+e.getWidth()&&
    			   getY()+getHeight()>=e.getY()&&getY()<=e.getY()+e.getHeight())
    		{
    			   setMovedn(false);
    			   return true;
    		}
    		return false;
    }
    public boolean collidesWith(Enemy enemy) {
        Rectangle bulletRect = new Rectangle(this.x, this.y, this.width, this.height);
        Rectangle enemyRect = new Rectangle(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());
        return bulletRect.intersects(enemyRect);
    }
    
    public boolean isFriendly()
    {
    	return friendly;
    }
}

