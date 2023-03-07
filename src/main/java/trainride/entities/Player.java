package trainride.entities;

import trainride.GamePanel;
import trainride.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Player extends Entity{

    private static final  int PLAYER_MOVING_DELAY_IN_FRAMES = 20;
    private final transient KeyHandler keys;

    private final transient BufferedImage[] spriteUp  = new BufferedImage[MAX_SPRITE_NUM];
    private final transient BufferedImage[] spriteDown= new BufferedImage[MAX_SPRITE_NUM];
    private final transient BufferedImage[] spriteLeft= new BufferedImage[MAX_SPRITE_NUM];
    private final transient BufferedImage[] spriteRight= new BufferedImage[MAX_SPRITE_NUM];

    GamePanel panel = GamePanel.getInstance();

    public Player(int worldPosX, int worldPosY, int speed, KeyHandler handler) {
        super(worldPosX, worldPosY, speed);
        this.screenX =panel.getScreenWidth()/2-panel.getTileSize()/2;
        this.screenY = panel.getScreenHeight()/2-panel.getTileSize()/2;
        this.keys = handler;
        setSolidArea(5*panel.getScale(),5*panel.getScale(),6*panel.getScale(),9*panel.getScale());
        loadImages();
    }

    public void loadImages() {
        try {
            spriteLeft[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/train1Left.png")));
            spriteLeft[1] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/train2Left.png")));
            spriteRight[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/train1Right.png")));
            spriteRight[1] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/train2Right.png")));
            spriteUp[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/train1Up.png")));
            spriteUp[1] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/train2Up.png")));
            spriteDown[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/train1Down.png")));
            spriteDown[1] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/train2Down.png")));
        }catch (IOException e){
            Logger.getGlobal().log(Level.SEVERE,"Kann Player-Sprites nicht laden!");
        }
    }

    @Override
    public String toString() {
        return "Player{" +
                "posX=" + posX +
                ", posY=" + posY +
                ", speed=" + speed +
                '}';
    }

    public void update() {
        if (keys.isDownPressed()&&
                panel.getCollisionDetection().isNotColliding(this, Direction.DOWN)){
            this.down();
        }
        if (keys.isUpPressed()&&
                panel.getCollisionDetection().isNotColliding(this, Direction.UP)){
            this.up();
        }
        if (keys.isLeftPressed()&&
                panel.getCollisionDetection().isNotColliding(this, Direction.LEFT)){
            this.left();
        }

        if (keys.isRightPressed()&&
                panel.getCollisionDetection().isNotColliding(this, Direction.RIGHT)){
            this.right();
        }
        incrementFrameCounter();
        if (getFrameCounter()>PLAYER_MOVING_DELAY_IN_FRAMES){
            resetFrameCounter();
            toggleSpriteNum();
        }
    }
    public void draw(Graphics2D graphics) {
        BufferedImage image;
        switch (direction){
            case UP -> image = spriteUp[getSpriteNum()];
            case DOWN -> image = spriteDown[getSpriteNum()];
            case LEFT -> image = spriteLeft[getSpriteNum()];
            default -> image = spriteRight[getSpriteNum()];
        }
        graphics.drawImage(image, screenX,screenY, panel.getTileSize(), panel.getTileSize(),null);
    }
    @Override
    public  Point getSize(){
        return new Point(panel.getTileSize(),panel.getTileSize());
    }
}
