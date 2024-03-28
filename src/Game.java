
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.*;
import java.awt.event.*; 


public class Game extends JPanel implements Runnable, KeyListener, MouseMotionListener, MouseListener{

	private BufferedImage back; 
	private int key; 
	private ArrayList<Enemy> enemies;
	private ArrayList<Bullet> bullets;
	private Player ship;
	private long lastFireTime;
	private String gameState;
	private long time, curtime;
	private Sound play;
	private Boolean soundcheck1, soundcheck2;
	
	
	// Variables for enemy movement control
	private boolean movingRight = true; // Enemies start by moving to the right
	private int moveDownStep = 10; // Vertical step down after reaching a horizontal boundary
	private int enemySpeed = 5; // Speed of enemy movement to the side
	private int boundaryPadding = 10; // Distance from the edge of the screen
	private long lastEnemyMoveTime = 0; // Track the last move time
	private long enemyMoveDelay = 75; // Delay in ms between move
	private int speedMod = 0;
	
	// Variables for enemy shooting
	private long lastEnemyFireTime  = 0; // Track last shot time
	private long randomEnemyFireDelay = 150; // Delay between shots in ms
	private long randomEFDVariation = 350;
	private int randomUpperBound = 1000;
	
	
	public Game() {
		new Thread(this).start();	
		this.addKeyListener(this);
		this.addMouseMotionListener(this);
		key =-1; 
		new Enemy();
		enemies = setEnemies();
		bullets = new ArrayList<Bullet>();
		ship = new Player();
		lastFireTime = 0;
		gameState = "Start";
		time = System.currentTimeMillis();
		curtime = 0;
		play = new Sound();
		soundcheck1 = false;
		soundcheck2 = false;
		}

	
	
	public void run()
	   {
	   	try
	   	{
	   		while(true)
	   		{
	   		   Thread.currentThread().sleep(2);
	           repaint();

	   		}
	    }
	   		catch(Exception e)
	      {
	      }
	  	}
	

	
	
	
	public void paint(Graphics g){
		
		
		Graphics2D twoDgraph = (Graphics2D) g; 
		if(back == null)
			back=(BufferedImage)((createImage(getWidth(), getHeight()))); 
		

		Graphics g2d = back.createGraphics();
	
		g2d.clearRect(0,0,getSize().width, getSize().height);
		
		// CODE BELOW
		
		 switch (gameState) {
         case "Start":
             drawStartScreen(g2d);
             break;
         case "Gameplay":
             drawGameplayScreen(g2d);
             break;
         case "Win":
             drawWinScreen(g2d);
             break;
         case "Lose":
             drawLoseScreen(g2d);
             break;
     }
     	
		// CODE ABOVE
		
		twoDgraph.drawImage(back, null, 0, 0);

	}

	  private void drawStartScreen(Graphics g) {
	        // Draw the start screen
	        g.setColor(Color.BLACK);
	        g.fillRect(0, 0, getWidth(), getHeight());
	        g.setColor(Color.WHITE);
	        g.drawString("Space to Start - Space to Shoot", getWidth() / 2 - 100, getHeight() / 2);
	    }

	    private void drawGameplayScreen(Graphics g2d) 
	    {
			
			randomEnemyShoot();
			
			movePlayer();
			moveBullets();
			moveEnemies();
			drawEnemies(g2d);
			drawPlayer(g2d);
			drawBullets(g2d);
			drawTimer(g2d);
			
			curtime = (System.currentTimeMillis()-time)/1000;

	    }

	    private void drawTimer(Graphics g2d)
	    {
	    	g2d.setFont(new Font("Courier", Font.BOLD, 20));
			g2d.drawString(new DecimalFormat("#0").format(curtime), 378, 50);
	    }
	    
	    private void drawWinScreen(Graphics g) {
	        // Draw the win screen
	        g.setColor(Color.GREEN);
	        g.fillRect(0, 0, getWidth(), getHeight());
	        g.setColor(Color.WHITE);
	        g.drawString("You Win!", getWidth() / 2 - 30, getHeight() / 2);
	        if (!soundcheck1)
	        {
	        	play.playmusic("win.wav");
	        	soundcheck1 = true;
	        }
	        
	    }

	    private void drawLoseScreen(Graphics g) {
	        // Draw the lose screen
	        g.setColor(Color.RED);
	        g.fillRect(0, 0, getWidth(), getHeight());
	        g.setColor(Color.WHITE);
	        g.drawString("Game Over", getWidth() / 2 - 35, getHeight() / 2);
	        
	        if (!soundcheck2)
	        {
	        	play.playmusic("lose.wav");
	        	soundcheck2 = true;
	        }
	    }
	
	public void game(Graphics g2d)
	{

		
		randomEnemyShoot();
		
		movePlayer();
		moveBullets();
		moveEnemies();
		drawEnemies(g2d);
		drawPlayer(g2d);
		drawBullets(g2d);

	}
	
	public void drawEnemy(Enemy enemy, Graphics g2d)
	{
		Image enemyImage = enemy.getPic().getImage();
		
		if (!(enemy.getPic() == null))
		{
			if (!(enemyImage == null))
			{
				g2d.drawImage(enemyImage, enemy.getX(), enemy.getY(), this);
				System.out.println("img drawn");
			}		
		}
	}
	
	public void fireBullet() {
			bullets.add(new Bullet(ship.getX() + ship.getWidth() / 2, ship.getY()));
	}
	
	public void enemyFireBullet(Enemy enemy) 
	{	
	    bullets.add(new Bullet(enemy.getX() + enemy.getWidth() / 2, enemy.getY() + enemy.getHeight(), true, false));
	}

	public void randomEnemyShoot() {
	    long currentTime = System.currentTimeMillis();
	    if (currentTime - lastEnemyFireTime >= randomEnemyFireDelay && !enemies.isEmpty()) {

            Random rand = new Random();
            int randomIndex = rand.nextInt(enemies.size());
            Enemy shootingEnemy = enemies.get(randomIndex);
	            
            enemyFireBullet(shootingEnemy);
	            
            lastEnemyFireTime = currentTime;
            randomEnemyFireDelay = randomEFDVariation + rand.nextInt(randomUpperBound); 
	    }
	}

	public ArrayList<Enemy> setEnemies()
	{
		ArrayList<Enemy> temp = new ArrayList<Enemy>();
		int y = 20;
		for (int i = 0; i < 4; i++)
		{
			int x = 100;
			for (int j = 0; j < 11; j++)
			{
				temp.add(new Enemy(x, y));
				x+=105;
			}
			y+=100;
		}
		return temp;
	}
	
	public void moveBullets() 
	{
	    Iterator<Bullet> bulletIterator = bullets.iterator();
	    while (bulletIterator.hasNext()) {
	        Bullet bullet = bulletIterator.next();
	        bullet.move();
	        
	        if (bullet.getY() < 0) {
	            bulletIterator.remove();
	            continue;
	        }

	        Iterator<Enemy> enemyIterator = enemies.iterator();
	        while (enemyIterator.hasNext()) {
	            Enemy enemy = enemyIterator.next();
	            if (bullet.hit(enemy) && bullet.isFriendly())
	            {
	                bulletIterator.remove(); 
	                enemyIterator.remove(); 
	                play.playmusic("boom.wav");
	                
	                if (enemies.isEmpty())
	                	gameState = "Win";
	                
	                break;
	            }
	            
	            if (bullet.hit(ship) && !(bullet.isFriendly()))
	            	gameState = "Lose";
	        }
	    }
	}
	
	public void drawBullets(Graphics g2d) {
	    for (Bullet bullet : bullets) 
	    {
	    	
	    	g2d.setColor(Color.white);
	        g2d.fillRect(bullet.getX(), bullet.getY(), 5, 10); 
	    }
    }

	public void drawEnemies(Graphics g2d)
	{
		for (Enemy e : enemies)
		{
			g2d.drawImage(e.getPic().getImage(), e.getX(), e.getY(), this);
		}
	}
	
	public void drawPlayer(Graphics g2d)
	{
		g2d.drawImage(ship.getPic().getImage(), ship.getX(), ship.getY(), this);
	}
	
	public void movePlayer()
	{
		if (ship.isMovert() && ship.getX() + ship.getWidth() <= 1650)
		{
			ship.setX(ship.getX() + ship.getDx());
		}
		
		else if (ship.getX() >= 0)
		{
			ship.setX(ship.getX() - ship.getDx());
		}
			
	}
	
	public void moveEnemies() {
	    long currentTime = System.currentTimeMillis();

	    if (currentTime - lastEnemyMoveTime < enemyMoveDelay) {
	        return; 
	    }

	    boolean edgeReached = false;

	    for (Enemy e : enemies) {
	        if (movingRight) {
	            e.setX(e.getX() + enemySpeed + speedMod);
	            if (e.getX() + e.getPic().getIconWidth() >= getWidth() - boundaryPadding) {
	                edgeReached = true;
	            }
	        } else {
	            e.setX(e.getX() - enemySpeed + speedMod);
	            if (e.getX() <= boundaryPadding) {
	                edgeReached = true;
	            }
	        }
	    }

	    if (edgeReached) {
	        movingRight = !movingRight; // Reverse direction
	        for (Enemy e : enemies) {
	            e.setY(e.getY() + moveDownStep); // Move down after reaching edge
	        }
	    }

	    lastEnemyMoveTime = currentTime; // Update the last move time
	}
	
	
	//DO NOT DELETE
	@Override
	public void keyTyped(KeyEvent e) {
		
	}



//DO NOT DELETE
	@Override
	public void keyPressed(KeyEvent e) {

		key= e.getKeyCode();
		System.out.println(key);
		
		if (key == 65)
		{
			ship.setMovert(false);
			ship.setDx(1);
		}
		if (key == 68)
		{
			ship.setMovert(true);
			ship.setDx(1);
		}
		
	
	}


	//DO NOT DELETE
	@Override
	public void keyReleased(KeyEvent e) {
		
		key = e.getKeyCode();
		
		int doublingCount = 1;
		
		long currentTime = System.currentTimeMillis();
		
		if (key == 65)
			ship.setDx(0);
		
		if (key == 68)
			ship.setDx(0);
		
		if (gameState.equals("Start") && key == 32)
			gameState = "Gameplay";
		
		
		if ((gameState.equals("Lose") || gameState.equals("win")) && key == 32)
			gameState = "Start";
		
		if ((gameState.equals("Gameplay") && key == 32 && (currentTime - lastFireTime >= 400)))
			{
            	fireBullet(); 
            	lastFireTime = currentTime;
			}
		
		if (gameState.equals("Gameplay") && key == 77)
		{
			if (doublingCount < 3)
			{
				randomEFDVariation /= 2;
				randomUpperBound /= 2;
				doublingCount++;			
			}
		}
		
		if (gameState.equals("Gameplay") && key == 76)
		{
			if (doublingCount > -1)
			{
				randomEFDVariation *= 2;
				randomUpperBound *= 2;
				doublingCount--;
			}
		}	
		
		if (key == 82)
			gameState = "Win";
		if (key == 84)
			gameState = "Lose";
	}



	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseMoved(MouseEvent e) {
		
		
	}



	@Override
	public void mouseClicked(MouseEvent e) {

	}



	@Override
	public void mousePressed(MouseEvent e) {
		
	}



	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	

	
}
