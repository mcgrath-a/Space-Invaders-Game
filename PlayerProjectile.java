import java.awt.*;

public class PlayerProjectile extends GraphicsObject{

    public PlayerProjectile(double x, double y) {
        super(x, y);
        this.speed_y = -4;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect((int)this.x, (int)this.y, 4, 20);
    }

    @Override
    public void update(int pic_width, int pic_height, int frame) {
        this.y += this.speed_y;
    }
}
