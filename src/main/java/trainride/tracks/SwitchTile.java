package trainride.tracks;

import trainride.GamePanel;
import trainride.paths.TrackPath;
import trainride.tiles.Tile;
import trainride.tiles.TileType;

import java.awt.Point;
import java.util.Objects;

public class SwitchTile extends TrackTile implements PathProvider, Tile {
    private final TrackPath path;
    int switchStatus = 2;

    public SwitchTile(int rotation) {
        super(TileType.TRACK_SWITCH_W.getImage(), TrackType.SWITCH, rotation);
        this.path = getPath();
    }

    public SwitchTile(SwitchTile tile) {
        super(tile);
        this.switchStatus = tile.switchStatus;
        this.path = tile.path;
    }

    public void toggleSwitch() {
        switchStatus = ++switchStatus % 3;
    }

    public int getSwitchStatus() {
        return this.switchStatus;
    }

    public void setSwitchStatus(int switchStatus) {
        this.switchStatus = switchStatus % 3;
    }

    private TrackPath getPath() {
        int tileSize = GamePanel.getInstance().getTileSize();
        Point center = new Point(worldPositionX + tileSize / 2, worldPositionY + tileSize / 2);
        if (switchStatus == 0) //Straight Up->Down
            return new TrackPath(
                    new Point(worldPositionX + tileSize / 2, worldPositionY),
                    new Point(worldPositionX + tileSize / 2, worldPositionY + tileSize)).
                    rotate(rotation, center);
        if (switchStatus == 1)//Curve Up->Left
            return new TrackPath(
                    new Point(worldPositionX + tileSize / 2, worldPositionY),
                    new Point(worldPositionX + tileSize / 2, worldPositionY + tileSize / 2),
                    new Point(worldPositionX, worldPositionY + tileSize / 2)).
                    rotate(rotation, center);
        if (switchStatus == 2)//Curve Down->Left
            return new TrackPath(
                    new Point(worldPositionX + tileSize / 2, worldPositionY + tileSize),
                    new Point(worldPositionX + tileSize / 2, worldPositionY + tileSize / 2),
                    new Point(worldPositionX, worldPositionY + tileSize / 2)).
                    rotate(rotation, center);
        return null;
    }

    @Override
    public TrackPath getTrackPath() {
        return path;
    }

    @Override
    public boolean isSwitch() {
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
    public Tile copy() {
        return new SwitchTile(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SwitchTile that = (SwitchTile) o;
        return switchStatus == that.switchStatus && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), path, switchStatus);
    }
}
