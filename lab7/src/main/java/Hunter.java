import java.util.ArrayList;
import java.util.List;

public class Hunter extends GameObject {
    List<Bullet> bullets;
    Thread thread;
    private final int SPEED = 5;

    Hunter(int x, int y, int dx, int dy) {
        super(x, y, dx, dy);

        loadImage("src/main/resources/hunter.png");

        bullets = new ArrayList<>();

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while(visible) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            move();
        }
    }

    public void fire() {
        Bullet newBullet = new Bullet(x + width + 80, y + 10);
        bullets.add(newBullet);
    }

    @Override
    protected void loadImage(String imageName) {
        super.loadImage(imageName);
        image = image.getScaledInstance(100,200, 0);
        getImageDimensions();
    }

    public int getSpeed() {
        return SPEED;
    }
}
