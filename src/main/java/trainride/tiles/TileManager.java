package trainride.tiles;

import trainride.GamePanel;
import trainride.paths.PathManager;
import trainride.paths.TrackPath;
import trainride.tracks.SwitchTile;
import trainride.tracks.TrackTile;
import trainride.tracks.TrackType;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TileManager {

    private final GamePanel panel = GamePanel.getInstance();
    private final Map<TileType, Tile> tileTypes = new EnumMap<>(TileType.class);
    private Tile[][] tiles;
    private List<TrackPath> paths;

    private Point mapSize;

    public TileManager() {
        loadTileImages();
        loadMap("/Maps/map1.map");
    }

    private void loadTileImages() {
        tileTypes.put(TileType.GRAS, new StandardTile(TileType.GRAS.getImage(), false));
        tileTypes.put(TileType.WATER, new StandardTile(TileType.WATER.getImage(), true));
        tileTypes.put(TileType.STONE, new StandardTile(TileType.STONE.getImage(), true));
        tileTypes.put(TileType.TREE, new StandardTile(TileType.TREE.getImage(), true));
        tileTypes.put(TileType.OUTSIDE, new StandardTile(TileType.OUTSIDE.getImage(),true));

        tileTypes.put(TileType.TRACK_HORIZONTAL, new TrackTile(TileType.TRACK_HORIZONTAL.getImage(), TrackType.TRACK, TileType.TRACK_HORIZONTAL.getRotation()));
        tileTypes.put(TileType.TRACK_VERTICAL, new TrackTile(TileType.TRACK_VERTICAL.getImage(), TrackType.TRACK, TileType.TRACK_VERTICAL.getRotation()));
        tileTypes.put(TileType.TRACK_BRIDGE_HORIZONTAL, new TrackTile(TileType.TRACK_BRIDGE_HORIZONTAL.getImage(), TrackType.TRACK, TileType.TRACK_BRIDGE_HORIZONTAL.getRotation()));
        tileTypes.put(TileType.TRACK_BRIDGE_VERTICAL, new TrackTile(TileType.TRACK_BRIDGE_VERTICAL.getImage(), TrackType.TRACK, TileType.TRACK_BRIDGE_VERTICAL.getRotation()));
        tileTypes.put(TileType.TRACK_SWITCH_N, new SwitchTile(TileType.TRACK_SWITCH_N.getRotation()));
        tileTypes.put(TileType.TRACK_SWITCH_E, new SwitchTile(TileType.TRACK_SWITCH_E.getRotation()));
        tileTypes.put(TileType.TRACK_SWITCH_S, new SwitchTile(TileType.TRACK_SWITCH_S.getRotation()));
        tileTypes.put(TileType.TRACK_SWITCH_W, new SwitchTile(TileType.TRACK_SWITCH_W.getRotation()));
        tileTypes.put(TileType.TRACK_CURVE_N/*└*/, new TrackTile(TileType.TRACK_CURVE_N.getImage(), TrackType.CURVE, TileType.TRACK_CURVE_N.getRotation()));
        tileTypes.put(TileType.TRACK_CURVE_E/*┌*/, new TrackTile(TileType.TRACK_CURVE_E.getImage(), TrackType.CURVE, TileType.TRACK_CURVE_E.getRotation()));
        tileTypes.put(TileType.TRACK_CURVE_S/*┐*/, new TrackTile(TileType.TRACK_CURVE_S.getImage(), TrackType.CURVE, TileType.TRACK_CURVE_S.getRotation()));
        tileTypes.put(TileType.TRACK_CURVE_W/*┘*/, new TrackTile(TileType.TRACK_CURVE_W.getImage(), TrackType.CURVE, TileType.TRACK_CURVE_W.getRotation()));
        tileTypes.put(TileType.TRACK_BRIDGE_CURVE_N/*└*/, new TrackTile(TileType.TRACK_BRIDGE_CURVE_N.getImage(), TrackType.CURVE, TileType.TRACK_BRIDGE_CURVE_N.getRotation()));
        tileTypes.put(TileType.TRACK_BRIDGE_CURVE_E/*┌*/, new TrackTile(TileType.TRACK_BRIDGE_CURVE_E.getImage(), TrackType.CURVE, TileType.TRACK_BRIDGE_CURVE_E.getRotation()));
        tileTypes.put(TileType.TRACK_BRIDGE_CURVE_S/*┐*/, new TrackTile(TileType.TRACK_BRIDGE_CURVE_S.getImage(), TrackType.CURVE, TileType.TRACK_BRIDGE_CURVE_S.getRotation()));
        tileTypes.put(TileType.TRACK_BRIDGE_CURVE_W/*┘*/, new TrackTile(TileType.TRACK_BRIDGE_CURVE_W.getImage(), TrackType.CURVE, TileType.TRACK_BRIDGE_CURVE_W.getRotation()));
     }

    public void draw(Graphics2D graphics) {
        Point playerWorldPosition = panel.getPlayer().getWorldPos();
        Point playerScreenPosition = panel.getPlayer().getScreenPos();

        int minXTile = playerWorldPosition.x / panel.getTileSize() - GamePanel.MAX_SCREEN_COLUMN / 2 - 1;
        int maxXTile = playerWorldPosition.x / panel.getTileSize() + GamePanel.MAX_SCREEN_COLUMN / 2 + 1;
        int minYTile = playerWorldPosition.y / panel.getTileSize() - GamePanel.MAX_SCREEN_ROW / 2 - 1;
        int maxYTile = playerWorldPosition.y / panel.getTileSize() + GamePanel.MAX_SCREEN_ROW / 2 + 1;

        IntStream.range(minYTile, maxYTile + 1).forEach(row ->
                IntStream.range(minXTile, maxXTile + 1).forEach(column -> {

                    int worldX = column * panel.getTileSize();
                    int worldY = row * panel.getTileSize();
                    int screenX = worldX - playerWorldPosition.x + playerScreenPosition.x;
                    int screenY = worldY - playerWorldPosition.y + playerScreenPosition.y;

                    Tile tile = getTileAt(column,row);
                    tile.draw(graphics, new Point(screenX, screenY));
                }));
        if (GamePanel.getInstance().isDebugMode()) {
            paths.forEach(path -> path.draw(graphics, new Point(0, 0)));
        }
    }

    public void loadMap(String map) {
        BufferedReader reader = new BufferedReader((new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(map)))));
        this.mapSize = findWorldSize(map);
        this.tiles = new Tile[mapSize.x][mapSize.y];
        IntStream.range(0, mapSize.y).forEach(row -> {
            try {
                String rowString = reader.readLine();
                IntStream.range(0, rowString.length()).forEach(column -> {
                    char c = rowString.charAt(column);
                    Tile tile = (tileTypes.get(TileType.fromChar(c))).copy();
                    tile.setWorldPosition(new Point(column * panel.getTileSize(), row * panel.getTileSize()));
                    this.tiles[column][row] = tile;
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        PathManager pathManager = new PathManager(this);
        this.paths = pathManager.getPaths();
    }

    private Point findWorldSize(String map) {
        BufferedReader mapReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(map))));
        int columns = 0;
        int rows = 0;
        try {
            columns = mapReader.readLine().length();
            rows = (int)mapReader.lines().count();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Point(columns, rows);
    }

    public Point getMapSize() {
        return this.mapSize;
    }

    public Tile getTileAt(int column, int row) {
        if (row < 0 || column < 0 || row >= getMapSize().y || column >= getMapSize().x) {
            return tileTypes.get(TileType.OUTSIDE);
        }
        return tiles[column][row];
    }

    public List<Tile> getTiles() {
        return Stream.of(tiles).flatMap(Stream::of).toList();
    }

    public List<TrackPath> getPaths() {
        return paths;
    }
}

