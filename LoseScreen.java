import java.awt.*;

public class LoseScreen extends GraphicsObject {

    public LoseScreen(double x, double y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(115, 24, 34));
        g.fillRect(0,0,600,400);
        g.setColor(Color.WHITE);
        g.drawString("You Lose!", 150, 150);
    }
}
