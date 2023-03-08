package trainride.tiles;

import trainride.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class StandardTile implements Tile{
    protected int rotation;
    private BufferedImage image;
    private boolean isSolid = false;
    protected int worldPositionX;
    protected int worldPositionY;

    public StandardTile(BufferedImage image, boolean isSolid) {
        this.isSolid = isSolid;
        this.image = image;
        this.rotation = 0;
    }

    protected StandardTile(BufferedImage image, int rotation) {
        this.image = rotate(image, rotation);
        this.rotation = rotation;
    }

    public StandardTile(Tile tile) {
        this.image = tile.getImage();
        this.isSolid = tile.isSolid();
        this.rotation = tile.getRotation();
    }

    public void setWorldPosition(Point worldPosition) {
        this.worldPositionX = worldPosition.x;
        this.worldPositionY = worldPosition.y;
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public boolean isSolid() {
        return this.isSolid;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return isSolid == tile.isSolid() &&
                rotation == tile.getRotation() &&
                isTrack() ==tile.isTrack()&&
                isSwitch()==tile.isSwitch()&&
                getWorldPosition().equals(tile.getWorldPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(isSolid, rotation,getWorldPosition());
    }

    public Point getWorldPosition() {
        return new Point(this.worldPositionX, this.worldPositionY);
    }

    public int getRotation() {
        return this.rotation;
    }

    private BufferedImage rotate(BufferedImage img, double angle) {

        int w = img.getWidth();
        int h = img.getHeight();

        BufferedImage rotated = new BufferedImage(w, h, img.getType());
        Graphics2D graphic = rotated.createGraphics();
        graphic.rotate(Math.toRadians(angle), w / 2d, h / 2d);
        graphic.drawImage(img, null, 0, 0);
        graphic.dispose();
        return rotated;
    }

    public  void draw(Graphics2D graphics, Point position){
        graphics.drawImage(getImage(),
                position.x,
                position.y,
                GamePanel.getInstance().getTileSize(),
                GamePanel.getInstance().getTileSize(),
                null);

    }

    public boolean isTrack(){
        return false;
    }

    @Override
    public boolean isSwitch() {
        return false;
    }

    /**
     * This is important and likely can't be done otherwise even if it's not pretty
     * We need to clone the tiles as they are only stored once in the TileManager before placement
     * If we call super.clone() we won't get the trackPath
     * If we use the constructor directly we lose the subtype of tile in the TileManager
     * If we use a factory the Tiles can't be preloaded and loading will take ages
     *
     * @return a cloned SwitchTile for positioning by the TileManager
     */
    @Override
    public Tile copy(){
     return new StandardTile(this);
    }

    public Point getTilePosition(){
        return new Point(
                worldPositionX/GamePanel.getInstance().getTileSize(),
                worldPositionY/GamePanel.getInstance().getTileSize());
    }

    public void setRed(){
        try {
            this.image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Tiles/red.png")));
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
