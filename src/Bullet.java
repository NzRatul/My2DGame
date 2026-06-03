import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Bullet {

    int x;
    int y;
    int width = 5;
    int height = 15;
    int speed = 10;

    BufferedImage bulletImage;   // Add this for future image

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Optional: Constructor with image
    public Bullet(int x, int y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.bulletImage = img;
    }

    public void update() {
        y -= speed;
    }

    public void draw(Graphics g) {
        if (bulletImage != null) {
            g.drawImage(bulletImage, x, y, width, height, null);
        } else {
            g.setColor(Color.YELLOW);
            g.fillRect(x, y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}