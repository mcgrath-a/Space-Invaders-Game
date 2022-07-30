import java.awt.*;

public class WinScreen extends GraphicsObject {

    public WinScreen(double x, double y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(85, 187, 76));
        g.fillRect(0,0,600,400);
        g.setColor(Color.BLACK);
        g.drawString("You Win!", 300, 300);
    }
}
