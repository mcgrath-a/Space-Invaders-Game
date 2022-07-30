import java.awt.*;

public class EnemyProjectile extends GraphicsObject{

    public EnemyProjectile(double x, double y) {
        super(x, y);
        this.speed_y = 2;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect((int)this.x, (int)this.y, 4, 20);
    }

    @Override
    public void update(int pic_width, int pic_height, int frame) {
        this.y += this.speed_y;
    }
}
