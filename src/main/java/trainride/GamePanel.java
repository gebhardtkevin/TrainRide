package trainride;

import trainride.entities.Entity;
import trainride.entities.Player;
import trainride.tiles.TileManager;

import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel {
    private static final int FPS = 60;
    private static final double FRAME_TIME = 1000000000d / FPS;
    private static final int ORIGINAL_TILE_SIZE = 16;
    private transient CollisionDetection collisionDetection;
    private int scale = 3;
    private int tileSize = ORIGINAL_TILE_SIZE * scale;

    public static final int MAX_SCREEN_COLUMN = 16;
    public static final int MAX_SCREEN_ROW = 12;

    private int screenWidth = tileSize * MAX_SCREEN_COLUMN;

    private int screenHeight = tileSize * MAX_SCREEN_ROW;

    private Player player;
    private transient TileManager tileManager;

    private Point worldTiles;
    private Point worldSize;

    private final transient KeyHandler keys;
    private static GamePanel instance = null;

    public static GamePanel getInstance(){
        if (null==instance){
            instance = new GamePanel();
        }
        return instance;
    }

    private GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        keys = new KeyHandler();
        this.addKeyListener(keys);
        this.setFocusable(true);
    }

    public void setMap(String map) {
        this.tileManager.loadMap(map);
        setWorldSize(this.tileManager.getMapSize());
    }

    private void setWorldSize(Point mapSize) {
        this.worldTiles = mapSize;
        this.worldSize = new Point(mapSize.x * tileSize, mapSize.y * tileSize);
    }

    public void start() {
        this.collisionDetection = new CollisionDetectionWalking();
        this.player = new Player(12 * tileSize, 9 * tileSize, 2*scale, keys);
        this.tileManager = new TileManager();
        setMap("/Maps/map1.map");
        new Thread(this::run).start();
    }

    public void update() {
        player.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;
        tileManager.draw(graphics);
        player.draw(graphics);
        graphics.dispose();
    }

    private void run() {
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (true) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / FRAME_TIME;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if (delta >= 1) {
                this.update();
                this.repaint();
                delta--;
                drawCount++;
            }
            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void setScale(int scale) {
        this.scale = scale;
        tileSize = ORIGINAL_TILE_SIZE * scale;
        this.screenWidth = tileSize * MAX_SCREEN_COLUMN;
        this.screenHeight = tileSize * MAX_SCREEN_ROW;
    }

    public int getTileSize() {
        return this.tileSize;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public Entity getPlayer() {
        return this.player;
    }

    public CollisionDetection getCollisionDetection() {
        return this.collisionDetection;
    }

    public TileManager getTileManager() {
        return tileManager;
    }

    public int getScale() {
        return this.scale;
    }

    public Point getWorldSize() {
        return worldSize;
    }

    public Point getWorldTiles() {
        return worldTiles;
    }
}