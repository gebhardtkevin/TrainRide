package trainride.tiles;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface Tile {

    void setWorldPosition(Point worldPosition);
    BufferedImage getImage();
    boolean isSolid();
    Point getWorldPosition();
    int getRotation();
    void draw(Graphics2D graphics, Point position);

    boolean isTrack();
    boolean isSwitch();

    Tile copy();

    Point getTilePosition();

    void setRed();
}
