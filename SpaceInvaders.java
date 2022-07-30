// utility
import java.util.ArrayList;
import java.util.Random;

// graphics
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;

// events
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// swing
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SpaceInvaders extends JPanel implements ActionListener, KeyListener, Runnable {

    private final int canvasWidth;
    private final int canvasHeight;
    private final Color backgroundColor;

    private final int framesPerSecond = 25;
    private final int msPerFrame = 1000 / framesPerSecond;
    private Timer timer;
    private int frame = 0;

    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<PlayerProjectile> playerShots;
    private ArrayList<EnemyProjectile> enemyShots;
    private WinScreen winScreen;
    private LoseScreen loseScreen;

    /* Constructor for a Space Invaders game
     */
    public SpaceInvaders() {
        // fix the window size and background color
        this.canvasWidth = 600;
        this.canvasHeight = 400;
        this.backgroundColor = Color.WHITE;
        setPreferredSize(new Dimension(this.canvasWidth, this.canvasHeight));

        // set the drawing timer
        this.timer = new Timer(msPerFrame, this);

        this.player = new Player(275, 300);
        this.enemies = new ArrayList<Enemy>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 5; j++) {
                enemies.add(new Enemy((50 + (i*60)), (10 + (j*30)), 1, 0));
            }
        }
        this.playerShots = new ArrayList<PlayerProjectile>();
        this.enemyShots = new ArrayList<EnemyProjectile>();
        this.winScreen = new WinScreen(0,0);
        this.loseScreen = new LoseScreen(0,0);
    }

    /* Start the game
     */
    @Override
    public void run() {
        // show this window
        display();

        // set a timer to redraw the screen regularly
        this.timer.start();
    }

    /* Create the window and display it
     */
    private void display() {
        JFrame jframe = new JFrame();
        jframe.addKeyListener(this);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setContentPane(this);
        jframe.pack();
        jframe.setVisible(true);
    }

    /* Run all timer-based events
     *
     * @param e  An object describing the timer
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // update the game objects
        update();
        // draw every object (this calls paintComponent)
        repaint(0, 0, this.canvasWidth, this.canvasHeight);
        // increment the frame counter
        this.frame++;
    }

    /* Paint/Draw the canvas.
     *
     * This function overrides the paint function in JPanel. This function is
     * automatically called when the panel is made visible.
     *
     * @param g The Graphics for the JPanel
     */
    @Override
    protected void paintComponent(Graphics g) {
        // clear the canvas before painting
        clearCanvas(g);
        if (hasWonGame()) {
            paintWinScreen(g);
        } else if (hasLostGame()) {
            paintLoseScreen(g);
        } else {
            paintGameScreen(g);
        }
    }

    /* Clear the canvas
     *
     * @param g The Graphics representing the canvas
     */
    private void clearCanvas(Graphics g) {
        Color oldColor = g.getColor();
        g.setColor(this.backgroundColor);
        g.fillRect(0, 0, this.canvasWidth, this.canvasHeight);
        g.setColor(oldColor);
    }

    /* Respond to key release events
     *
     * A key release is when you let go of a key
     * 
     * @param e  An object describing what key was released
     */
    public void keyReleased(KeyEvent e) {
        // you can leave this function empty
    }

    /* Respond to key type events
     *
     * A key type is when you press then let go of a key
     * 
     * @param e  An object describing what key was typed
     */
    public void keyTyped(KeyEvent e) {
        // you can leave this function empty
    }

    /* Respond to key press events
     *
     * A key type is when you press then let go of a key
     * 
     * @param e  An object describing what key was typed
     */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            this.player.incrementX(-5);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.player.incrementX(5);
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (playerShots.size() < 5) {
                playerShots.add(new PlayerProjectile(this.player.x + 18, this.player.y));
            }
        }
    }

    /* Update the game objects
     */
    private void update() {
        //check if enemies need to bounce
        for (Enemy e: enemies) {
            if (e.wallHit(this.canvasWidth)) {
                for (Enemy en: enemies) {
                    en.speed_x = - en.speed_x;
                    en.speed_y = 20;
                }
                break;
            }
        }
        //update enemy position and shoot
        for (Enemy e : enemies) {
            e.update(this.canvasWidth, this.canvasHeight, 1);
            if (e.endTimer) {
                if (enemyShots.size() < 7) {
                    enemyShots.add(new EnemyProjectile(e.x + 18, e.y + 18));
                }
                e.endTimer = false;
            }
        }
        //increment enemy shot position
        for (EnemyProjectile enemyShot : enemyShots) {
            enemyShot.update(this.canvasWidth, this.canvasHeight, 1);
        }

        //increment player shot position
        for (PlayerProjectile playerShot : playerShots) {
            playerShot.update(this.canvasWidth, this.canvasHeight, 1);
        }

        //check if player shot hits enemy or goes offscreen
        for (int i = 0; i < enemies.size(); i++) {
            for (int j = 0; j < playerShots.size(); j++) {
                boolean destroyEnemy = false;
                boolean destroyProjectile = false;
                if (playerShots.get(j).y <= 0) {
                    destroyProjectile = true;
                }
                if (((playerShots.get(j).y <= enemies.get(i).y + enemies.get(i).height) &&
                    (playerShots.get(j).y >= enemies.get(i).y)) &&
                    ((playerShots.get(j).x <= enemies.get(i).x + enemies.get(i).width) &&
                    (playerShots.get(j).x >= enemies.get(i).x))) {
                        destroyEnemy = true;
                        destroyProjectile = true;
                }

                if (destroyProjectile) {
                    playerShots.remove(j);
                    if (j != 0) {
                        j--;
                    }
                    if (destroyEnemy) {
                        enemies.remove(i);
                        if (i != 0) {
                            i--;
                        }
                    }
                }

            }
        }

        //check if enemy shot hits player
        for (int k = 0 ; k < enemyShots.size(); k++) {
            boolean destroyPlayer = false;
            boolean destroyEnemyProjectile = false;
            if (enemyShots.get(k).y >= canvasHeight) {
                destroyEnemyProjectile = true;
            }
            if (((enemyShots.get(k).y+20 <= player.y + 40) &&
                    (enemyShots.get(k).y+20 >= player.y)) &&
                    ((enemyShots.get(k).x <= player.x + 40) &&
                            (enemyShots.get(k).x >= player.x))) {
                destroyPlayer = true;
                destroyEnemyProjectile = true;
            }

            if (destroyEnemyProjectile) {
                enemyShots.remove(k);
                k--;
                if (destroyPlayer) {
                    player.dead = true;
                }
            }

        }

        //check if enemy hits player
        for (Enemy e : enemies) {
            if (((e.y + 20 <= player.y + 40) && (e.y + 20 >= player.y)) &&
                    (((e.x <= player.x + 40) && (e.x >= player.x)) || ((e.x + 40 <= player.x + 40) && (e.x + 40 >= player.x)))) {
                player.dead = true;
            }
        }

        //check if enemy goes offscreen
        for (Enemy e : enemies) {
            if (e.y + 20 >= canvasHeight) {
                player.dead = true;
            }
        }

    }

    /* Check if the player has lost the game
     * 
     * @returns  true if the player has lost, false otherwise
     */
    private boolean hasLostGame() {
        if (player.dead) {
            return true;
        }
        return false;
    }

    /* Check if the player has won the game
     * 
     * @returns  true if the player has won, false otherwise
     */
    private boolean hasWonGame() {
        if (enemies.size() == 0) {
            return true;
        }
        return false;
    }

    /* Paint the screen during normal gameplay
     *
     * @param g The Graphics for the JPanel
     */
    private void paintGameScreen(Graphics g) {
        player.draw(g);

        for (Enemy e : enemies) {
            e.draw(g);
        }
        for (PlayerProjectile p : playerShots) {
            p.draw(g);
        }
        for (EnemyProjectile ep : enemyShots) {
            ep.draw(g);
        }
    }

    /* Paint the screen when the player has won
     *
     * @param g The Graphics for the JPanel
     */
    private void paintWinScreen(Graphics g) {
        winScreen.draw(g);
    }

    /* Paint the screen when the player has lost
     *
     * @param g The Graphics for the JPanel
     */
    private void paintLoseScreen(Graphics g) {
        loseScreen.draw(g);
    }

    public static void main(String[] args) {
        SpaceInvaders invaders = new SpaceInvaders();
        EventQueue.invokeLater(invaders);
    }
}
