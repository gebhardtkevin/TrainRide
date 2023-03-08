package trainride.tracks;

import trainride.GamePanel;
import trainride.paths.TrackPath;
import trainride.tiles.StandardTile;
import trainride.tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class TrackTile extends StandardTile implements PathProvider,Tile {

    private final TrackType trackType;
    private final TrackPath path;


    private final int tileSize;
    private final Point center;

    public TrackTile(BufferedImage image, TrackType trackType, int rotation) {
        super(image, rotation);
        this.trackType = trackType;
        tileSize = GamePanel.getInstance().getTileSize();
        center = new Point(worldPositionX + tileSize / 2, worldPositionY + tileSize / 2);
        this.path = getPath(trackType);
    }

    public TrackTile(TrackTile tile) {
        super(tile);
        this.trackType = tile.trackType;
        this.path = tile.path;
        this.tileSize = tile.tileSize;
        this.center = tile.center;
    }

    private TrackPath getPath(TrackType trackType) {
        return switch (trackType) {
            case TRACK ->
                    new TrackPath(
                        new Point(worldPositionX, tileSize / 2),
                        new Point(worldPositionX + tileSize, tileSize / 2))
                        .rotate(rotation, center);
            case CURVE ->
                    new TrackPath(
                        new Point(worldPositionX, worldPositionY + tileSize / 2),
                        new Point(worldPositionX + tileSize / 2, worldPositionY + tileSize / 2),
                        new Point(worldPositionX + tileSize / 2, worldPositionY + tileSize))
                        .rotate(rotation, center);
            default -> null;
        };
    }

    public TrackPath getTrackPath() {
        return this.path;
    }

    @Override
    public boolean isTrack() {
        return true;
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
        return new TrackTile(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrackTile trackTile)) return false;
        if (!super.equals(o)) return false;
        return tileSize == trackTile.tileSize && trackType == trackTile.trackType && Objects.equals(path, trackTile.path)&&worldPositionX==trackTile.worldPositionX&&worldPositionY==trackTile.worldPositionY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), trackType, path, tileSize,worldPositionX,worldPositionY);
    }
}
