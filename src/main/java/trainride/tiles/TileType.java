package trainride.tiles;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public enum TileType {

    GRAS('0', TilePaths.GRAS, 0),
    TREE('t', TilePaths.TREE, 0),
    WATER('w', TilePaths.WATER, 0),
    STONE('q', TilePaths.STONE, 0),
    TRACK_HORIZONTAL('h', TilePaths.TRACK, 0),
    TRACK_VERTICAL('v', TilePaths.TRACK, 90),
    TRACK_SWITCH_N('a', TilePaths.SWITCH, 90),
    TRACK_SWITCH_E('s', TilePaths.SWITCH, 180),
    TRACK_SWITCH_S('d', TilePaths.SWITCH, 270),
    TRACK_SWITCH_W('f', TilePaths.SWITCH, 0),
    TRACK_CURVE_N('<', TilePaths.CURVE, 180),
    TRACK_CURVE_E('y', TilePaths.CURVE, 270),
    TRACK_CURVE_S('x', TilePaths.CURVE, 0),
    TRACK_CURVE_W('c', TilePaths.CURVE, 90),
    TRACK_BRIDGE_VERTICAL('b', TilePaths.BRIDGE, 90),
    TRACK_BRIDGE_HORIZONTAL('n', TilePaths.BRIDGE, 0),
    TRACK_BRIDGE_CURVE_S('m', TilePaths.CURVE_BRIDGE, 0),
    TRACK_BRIDGE_CURVE_W(',', TilePaths.CURVE_BRIDGE, 90),
    TRACK_BRIDGE_CURVE_N('.', TilePaths.CURVE_BRIDGE, 180),
    TRACK_BRIDGE_CURVE_E('-', TilePaths.CURVE_BRIDGE, 270),
    OUTSIDE('?',TilePaths.WATER , 0);


    private final char code;
    final String imagePath;
    final int rotation;

    TileType(char code, String sourcePath, int rotation) {
        this.code = code;
        this.imagePath = sourcePath;
        this.rotation = rotation;
    }

    public char getCode() {
        return this.code;
    }

    public static TileType fromChar(char code) {
        for (TileType t : TileType.values()) {
            if (t.getCode()==code) {
                return t;
            }
        }
        return OUTSIDE;
    }

    public BufferedImage getImage() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(this.imagePath)));
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public int getRotation(){
        return  this.rotation;
    }
}