import java.awt.*;
import java.util.Random;

public class Enemy extends GraphicsObject{

    public int width;
    public int height;
    public int timer;
    public int counter;
    public boolean endTimer;

    public Enemy(double x, double y, double xSpeed, double ySpeed) {
        super(x, y);
        this.speed_x = xSpeed;
        this.speed_y = ySpeed;
        this.width = 40;
        this.height = 20;
        this.counter = 1;
        this.timer = createTimer();
        this.endTimer = false;
    }

    public boolean wallHit(int pic_width) {
        if (this.x < 0 || this.x + this.width > pic_width) {
            return true;
        }
        return false;
    }

    //determine timer for when to shoot
    public int createTimer() {
        Random rand = new Random();
        int newTimer = rand.nextInt(40, 401);
        return newTimer;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect((int) this.x, (int) this.y, this.width, this.height);
    }

    @Override
    public void update(int pic_width, int pic_height, int frame) {
        this.x += this.speed_x;
        this.y += this.speed_y;
        this.speed_y = 0;

        if (counter % timer == 0) {
            counter = 0;
            endTimer = true;
        }
        counter++;
    }
}
