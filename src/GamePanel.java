import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;


public class GamePanel extends JPanel implements Runnable {

    final int WIDTH = 800;
    final int HEIGHT = 600;

    int playerX = 375;
    int playerY = 500;
    int playerSpeed = 5;
    int shotCoolDown =0;
    int enemySpawnTimer=0;
    int score =0;
    int health =3;
    boolean gameOver = false;
    Random random = new Random();

    KeyHandler keyH = new KeyHandler();
    BufferedImage playerImage;
    BufferedImage enemyImage;
    BufferedImage backgroundImage;


    Thread gameThread;
    ArrayList<Bullet> bullets = new ArrayList<>();
    ArrayList<Enemy> enemies = new ArrayList<>();

    public GamePanel() {

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        this.setBackground(Color.black);

        this.setDoubleBuffered(true);

        this.addKeyListener(keyH);

        this.setFocusable(true);
        try {

            playerImage = ImageIO.read(getClass().getResourceAsStream("/assets/player.png"));

            enemyImage = ImageIO.read(getClass().getResourceAsStream("/assets/enemy.png"));

            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/assets/background.png"));

        } catch(IOException e) {

            e.printStackTrace();
        }
    }

    public void startGameThread() {

        gameThread = new Thread(this);

        gameThread.start();

    }

    @Override
    public void run() {

        while(gameThread != null) {

            update();

            repaint();

            try {

                Thread.sleep(16);

            } catch(Exception e) {

                e.printStackTrace();
            }
        }
    }

    public void update() {
        if(gameOver){
            return;
        }

        enemySpawnTimer++;
        if(shotCoolDown>0){
            shotCoolDown--;
        }
        if(keyH.upPressed) {
            playerY -= playerSpeed;
        }
        if(keyH.downPressed) {
            playerY += playerSpeed;
        }
        if(keyH.leftPressed) {
            playerX -= playerSpeed;
        }
        if(keyH.rightPressed) {
            playerX += playerSpeed;
        }
        if(keyH.shootPressed && shotCoolDown==0){
            bullets.add(new Bullet(playerX+22,playerY));
            shotCoolDown=15;
        }
        if(enemySpawnTimer>40){
            int enemyX= random.nextInt(WIDTH-40);
            enemies.add(new Enemy(enemyX,0,enemyImage));
            enemySpawnTimer=0;
        }
        for(int i=enemies.size()-1;i>=0;i--){
            enemies.get(i).update();
           if(enemies.get(i).y>HEIGHT){
               enemies.remove(i);
           }
        }
        for(int i= bullets.size()-1;i>=0;i--){
            for(int j=enemies.size()-1;j>=0;j--){
                if(bullets.get(i).getBounds().intersects(enemies.get(j).getBounds())){
                    bullets.remove(i);
                    enemies.remove(j);
                    score+=10;
                    break;
                }
            }
        }
        for(int i=enemies.size()-1;i>=0;i--){
            if(enemies.get(i).getBounds().intersects(getPlayerBounds())){
                enemies.remove(i);
                health--;

                if(health<=0){
                    gameOver = true;
                }
            }
        }
        for(int i = bullets.size() - 1; i >= 0; i--) {
            bullets.get(i).update();
            if(bullets.get(i).y < 0) {
                bullets.remove(i);
            }
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Background
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WIDTH, HEIGHT);
        }

        // Player
        if (playerImage != null) {
            g.drawImage(playerImage, playerX, playerY, 50, 50, null);
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(playerX, playerY, 50, 50);
        }

        // Bullets
        for(int i = 0; i < bullets.size(); i++) {
            bullets.get(i).draw(g);
        }

        // Enemies
        for(int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }

        // UI
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 20, 20);
        g.drawString("Health: " + health, 20, 40);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(g.getFont().deriveFont(40f));
            g.drawString("GAME OVER", WIDTH/2 - 100, HEIGHT/2);
        }
    }
    public Rectangle getPlayerBounds(){
        return new Rectangle(playerX,playerY,50,50);
    }

}