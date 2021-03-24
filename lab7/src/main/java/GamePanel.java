import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    final static int NUMBER_OF_DUCKS = 15;
    private final Timer timer;
    private int score = 0;
    private int bulletsCount = NUMBER_OF_DUCKS*3;
    boolean inGame;
    Image backgroundImage;

    static int GAME_WIDTH;
    static int GAME_HEIGHT;


    private final Hunter hunter;
    private java.util.List<Duck> ducks;

    public GamePanel() {
        addKeyListener(new MyKeyAdapter());
        setFocusable(true);
        setDoubleBuffered(true);
        inGame = true;
        backgroundImage = new ImageIcon("src/main/resources/background.png").getImage();

        GAME_WIDTH = backgroundImage.getWidth(this);
        GAME_HEIGHT = backgroundImage.getHeight(this);
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));


        hunter = new Hunter(0, GAME_HEIGHT - 300, 0, 0);

        initDucks();

        timer = new Timer(8, this);
        timer.start();
    }

    public void initDucks() {
        ducks = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_DUCKS; i++) {
            Random r = new Random();
            int x = r.nextInt(GAME_WIDTH - 300);
            int y = r.nextInt(GAME_HEIGHT - 600);
            int dx = 0;
            while (dx == 0) dx = r.nextInt(10) - 5;

            Duck newDuck = new Duck(x, y, dx, 0);
            ducks.add(newDuck);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, null);

        if (inGame) {
            drawObjects(g);
        } else {
            drawGameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    void drawObjects(Graphics g) {
        if (hunter.isVisible()) {
            g.drawImage(hunter.getImage(), hunter.getX(), hunter.getY(), this);
        }

        ArrayList<Bullet> bullets = (ArrayList) hunter.bullets;

        for (Bullet s : bullets) {
            if (s.isVisible()) {
                g.drawImage(s.getImage(), s.getX(), s.getY(), this);
            }
        }

        for (Duck duck : ducks) {
            if (duck.isVisible()) {
                g.drawImage(duck.getImage(), duck.getX(), duck.getY(), this);
            }
        }

        g.setColor(Color.WHITE);
        g.drawString("Ducks left: " + ducks.size(), 5, 15);
        g.drawString("Bullets left: " + bulletsCount, 5, 25);
    }

    private void drawGameOver(Graphics g) {
        String msg = "Game Over";
        String scoreMsg = "Score: "+score;
        Font font = new Font("Helvetica", Font.BOLD, 48);
        FontMetrics fm = getFontMetrics(font);

        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(msg, (GAME_WIDTH - fm.stringWidth(msg)) / 2,
                GAME_HEIGHT / 2);

        g.drawString(scoreMsg, (GAME_WIDTH - fm.stringWidth(scoreMsg)) / 2,
                GAME_HEIGHT / 2 + 48);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        inGame();

        updateShots();
        updateDucks();

        checkCollisions();

        repaint();
    }

    private void inGame() {

        if (!inGame) {
            timer.stop();
        }
    }

    private void updateShots() {
        java.util.List<Bullet> ms = hunter.bullets;

        for (int i = 0; i < ms.size(); i++) {

            Bullet m = ms.get(i);

            if (!m.isVisible()) {
                ms.remove(i);
            }
        }
    }

    private void updateDucks() {

        if (ducks.isEmpty()) {
            inGame = false;
            return;
        }

        for (int i = 0; i < ducks.size(); i++) {

            Duck a = ducks.get(i);

            if (!a.isVisible()) {
                ducks.remove(i);
            }
        }
    }

    public void checkCollisions() {
        for (Bullet bullet : hunter.bullets) {
            Rectangle r1 = bullet.getBounds();
            for (Duck duck : ducks) {
                if (!duck.isDead()) {
                    Rectangle r2 = duck.getBounds();

                    if (r1.intersects(r2)) {
                    bullet.setVisible(false);
                        score++;
                        duck.setIsDead(true);
                        if (ducks.size() == 0) {
                            inGame = false;
                        }
                    }
                }
            }
        }
    }

    private class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
                hunter.dx = 0;
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {
                if (bulletsCount > 0){
                    bulletsCount--;
                    hunter.fire();
                } else inGame = false;
            }

            if (key == KeyEvent.VK_LEFT) {
                hunter.dx = -hunter.getSpeed();
            }

            if (key == KeyEvent.VK_RIGHT) {
                hunter.dx = hunter.getSpeed();
            }
        }
    }
}