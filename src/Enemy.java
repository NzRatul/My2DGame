import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Enemy {
    int x;
    int y;
    int width = 40;
    int height = 40;
    int speed = 3;
    BufferedImage enemyImage;

    // New Constructor with image
    public Enemy(int x, int y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.enemyImage = img;
    }

    public void update() {
        y += speed;
        // System.out.println("Enemy y = " + y);   // Comment this out later
    }

    public void draw(Graphics g) {
        if (enemyImage != null) {
            g.drawImage(enemyImage, x, y, width, height, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(x, y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}