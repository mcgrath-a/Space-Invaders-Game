import java.awt.*;

public class Player extends GraphicsObject{

    public boolean dead;

    public Player(double x, double y) {
        super(x, y);
        dead = false;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect((int)this.x, (int)this.y, 40, 40);
    }

    public void incrementX(int move) {
        this.x += move;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }
}
