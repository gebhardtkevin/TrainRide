package trainride.paths;

import trainride.tiles.Tile;
import trainride.tiles.TileManager;
import trainride.tracks.TrackTile;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PathManager {
    private final TileManager tileManager;
    List<TrackPath> paths;
    List<Tile> usedEndPoints = new ArrayList<>();
    List<TrackTile> allTracksInMap;

    public PathManager(TileManager tileManager) {
        this.tileManager = tileManager;
        allTracksInMap = collectTracks();
        paths = loadPaths();
    }

    public void add(TrackPath path) {
        this.paths.add(path);
    }

    public List<TrackPath> getPaths() {
        return this.paths;
    }

    private List<TrackTile> collectTracks() {
        List<TrackTile> tracks = new ArrayList<>();
        tileManager.getTiles().stream().filter(Tile::isTrack).forEach(tile ->
            tracks.add((TrackTile) tile));
        return tracks;
    }

    public List<TrackPath> loadPaths() {

        List<TrackPath> trackPaths = new ArrayList<>();
        List<TrackTile> trackTerminationPoints = allTracksInMap.
                stream().
                filter(this::isEndPoint).
                toList();
        trackTerminationPoints.forEach(startTile -> {
            startTile.setRed();
            if (!usedEndPoints.contains(startTile)) {
                usedEndPoints.add(startTile);

                TrackPath path = new TrackPath();

                TrackTile previousTrack= null;
                TrackTile currentTrack = startTile;
                TrackTile nextTrack;

                while (true) {
                    if (isBackwards(currentTrack,previousTrack)){
                        currentTrack.getTrackPath().reverse();
                    }
                    path.attachPath(currentTrack.getTrackPath().toWorldCoordinates(currentTrack));
                    nextTrack = findNextTrack(previousTrack, currentTrack);
                    if (nextTrack == null) {
                        usedEndPoints.add(currentTrack);
                        break;
                    }
                    previousTrack = currentTrack;
                    currentTrack = nextTrack;
                }
                path.simplify();
                trackPaths.add(path);
            }
        });
        return trackPaths;
    }

    public TrackTile findNextTrack(TrackTile previousTrack, TrackTile currentTrack) {
        TrackTile nextTrack = null;
        for (TrackTile track : allTracksInMap) {
            if (isConnected(currentTrack, track) && !Objects.equals(previousTrack, track)) {
                nextTrack = track;
                break;
            }
        }
        return nextTrack;
    }

    private boolean isEndPoint(TrackTile tile) {
        return getConnected(tile).size() == 1;
    }

    private List<TrackTile> getConnected(TrackTile tile) {
        return this.allTracksInMap.
                stream().
                filter(otherTrack -> isConnected(otherTrack,tile)).toList();
    }

    private boolean isConnected(TrackTile nextTrack,TrackTile thisTrack) {
        if (thisTrack==null||nextTrack==null||thisTrack.equals(nextTrack)) {
            return false;
        }
        Point thisFirst = thisTrack.getTrackPath().toWorldCoordinates(thisTrack).first();
        Point thisLast = thisTrack.getTrackPath().toWorldCoordinates(thisTrack).last();
        Point nextFirst = nextTrack.getTrackPath().toWorldCoordinates(nextTrack).first();
        Point nextLast = nextTrack.getTrackPath().toWorldCoordinates(nextTrack).last();
        return thisFirst.equals(nextFirst) || thisFirst.equals(nextLast) || thisLast.equals(nextFirst) || thisLast.equals(nextLast);
    }

    private boolean isBackwards(TrackTile nextTrack,TrackTile thisTrack){
        //check direction of startPoint
        if (thisTrack==null&&isEndPoint(nextTrack)){
            var nextNextTrack = getConnected(nextTrack).get(0);
            Point nextNextFirst = nextNextTrack.getTrackPath().toWorldCoordinates(nextNextTrack).first();
            Point nextNextLast = nextNextTrack.getTrackPath().toWorldCoordinates(nextNextTrack).last();
            Point nextFirst = nextTrack.getTrackPath().toWorldCoordinates(nextTrack).first();
            return nextFirst.equals(nextNextFirst)||nextFirst.equals(nextNextLast);
        }

        //no consistent track
        if (nextTrack==null||Objects.equals(thisTrack,nextTrack)||!isConnected(nextTrack,thisTrack)) {
            return false;
        }

        Point thisFirst = thisTrack.getTrackPath().toWorldCoordinates(thisTrack).first();
        Point thisLast = thisTrack.getTrackPath().toWorldCoordinates(thisTrack).last();
        Point nextFirst = nextTrack.getTrackPath().toWorldCoordinates(nextTrack).first();
        Point nextLast = nextTrack.getTrackPath().toWorldCoordinates(nextTrack).last();

        return thisLast.equals(nextLast)||thisFirst.equals(nextFirst);
    }
}


