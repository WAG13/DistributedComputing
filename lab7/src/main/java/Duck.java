public class Duck extends GameObject {
    Thread thread;
    private boolean isMovingRight;
    private boolean isDead;

    Duck(int x, int y, int dx, int dy) {
        super(x, y, dx, dy);

        if (dx > 0) {
            isMovingRight = true;
            loadImage("src/main/resources/duckRight.gif");
        } else {
            isMovingRight = false;
            loadImage("src/main/resources/duckLeft.gif");
        }
        width = 200;
        height = 100;


        thread = new Thread(this);
        thread.start();
    }

    void setIsDead(boolean b) {
        isDead = b;
        loadImage("src/main/resources/duckDead.png");
    }

    boolean isDead() {
        return isDead;
    }

    @Override
    public void run() {
        while (!isDead) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            move();
            if (x > GamePanel.GAME_WIDTH - 200 || x < 0) {
                dx = -dx;
                if (isMovingRight) {
                    isMovingRight = false;
                    loadImage("src/main/resources/duckLeft.gif");
                } else {
                    isMovingRight = true;
                    loadImage("src/main/resources/duckRight.gif");
                }
            }
        }
        try {
            Thread.sleep(sleepTime * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        visible = false;
    }

    @Override
    protected void loadImage(String imageName) {
        super.loadImage(imageName);
        image = image.getScaledInstance(100,100, 0);
        getImageDimensions();
    }

    @Override
    protected void getImageDimensions() {
        width = 100;
        height = 100;
    }
}
