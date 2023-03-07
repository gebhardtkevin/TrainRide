package trainride.entities;

import java.awt.*;
import java.io.Serializable;

public class Entity implements Serializable {

    int posX;
    int posY;
    int speed;
    int screenX;
    int screenY;
    protected static final int MAX_SPRITE_NUM = 2;

    Rectangle solidArea;

    private int frameCounter = 0;
    private int spriteNum = 0;

    Direction direction = Direction.RIGHT;

    public Entity(int posX, int posY, int speed) {
        this.posX = posX;
        this.posY = posY;
        this.speed = speed;
    }

    public int getX() {
        return posX;
    }

    public void setX(int posX) {
        this.posX = posX;
    }

    public int getY() {
        return posY;
    }

    public void setY(int posY) {
        this.posY = posY;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void down() {
        setY(getY() + getSpeed());
        this.direction = Direction.DOWN;
    }

    public void up() {
        setY(getY() - getSpeed());
        this.direction = Direction.UP;
    }


    public void left() {
        setX(getX() - getSpeed());
        this.direction = Direction.LEFT;
    }

    public void right() {
        setX(getX() + getSpeed());
        this.direction = Direction.RIGHT;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public int getFrameCounter() {
        return frameCounter;
    }

    public void resetFrameCounter() {
        this.frameCounter = 0;
    }

    public void incrementFrameCounter() {
        this.frameCounter++;
    }

    public int getSpriteNum() {
        return spriteNum;
    }

    public void toggleSpriteNum() {
        spriteNum = (spriteNum + 1) % MAX_SPRITE_NUM;
    }

    public Point getWorldPos() {
        return new Point(posX, posY);
    }

    public Point getScreenPos() {
        return new Point(screenX, screenY);
    }

    public void setSolidArea(int x,int y,int width, int height){
        this.solidArea = new Rectangle(x,y,width, height);
    }

    public Rectangle getSolidArea(){
        return  this.solidArea;
    }

    public Point getSize() {
        return new Point(0,0);
    }
}
