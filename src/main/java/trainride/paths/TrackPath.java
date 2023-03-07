package trainride.paths;

import trainride.tiles.Tile;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class TrackPath {
    private List<Point> pathPointList = new ArrayList<>();

    public TrackPath(List<Point> pointLlist) {
        this.pathPointList.addAll(pointLlist);
    }

    public TrackPath(Point... pointList) {
        this.pathPointList.addAll(Arrays.asList(pointList));
    }

    public Iterator<Point> getIterator() {
        return pathPointList.iterator();
    }

    public TrackPath rotate(double rotationRadians, Point center) {
        this.pathPointList = pathPointList.stream().map(point -> rotatePoint(point, rotationRadians, center)).collect(Collectors.toList());
        return this;
    }

    private Point rotatePoint(Point point, double rotationRadians, Point center) {
        long x = Math.round(Math.cos(rotationRadians) * (point.x - center.x) - Math.sin(rotationRadians) * (point.y - center.y) + center.x);
        long y = Math.round(Math.sin(rotationRadians) * (point.x - center.x) + Math.cos(rotationRadians) * (point.y - center.y) + center.y);
        return new Point((int)x, (int)y);
    }

    public void draw(Graphics2D graphics, Point startTilePosition) {
        if (!pathPointList.isEmpty()) {
            graphics.setColor(Color.GREEN);
            GeneralPath path = new GeneralPath();
            Iterator<Point> it = getIterator();
            Point start = it.next();
            path.moveTo(startTilePosition.x + start.x, startTilePosition.y + start.y);
            while (it.hasNext()) {
                Point nextPoint = it.next();
                path.lineTo(startTilePosition.x + nextPoint.x, startTilePosition.y + nextPoint.y);
            }
            graphics.draw(path);
        }
    }

    public TrackPath toWorldCoordinates(Tile tile) {
        return new TrackPath(pathPointList.stream().
                map(point -> new Point(
                        point.x + tile.getWorldPosition().x,
                        point.y + tile.getWorldPosition().y)).
                toList());
    }

    public Point first() {
        return pathPointList.get(0);
    }

    public Point last() {
        return pathPointList.get(pathPointList.size() - 1);
    }

    public void attachPath(TrackPath otherPath) {

        pathPointList.addAll(otherPath.pathPointList);
    }

    public void reverse() {
        Collections.reverse(pathPointList);
    }

    public void simplify() {
        if (pathPointList.size() > 2) {
            List<Point> simplified = new ArrayList<>();
            simplified.add(pathPointList.get(0));
            simplified.add(pathPointList.get(1));
            for (int i = 2; i < pathPointList.size(); i++) {
                int lastIndex = simplified.size() - 1;
                if (isCollinear(simplified.get(lastIndex - 1), simplified.get(lastIndex), pathPointList.get(i))) {
                    simplified.set(lastIndex, pathPointList.get(i));
                } else {
                    simplified.add(pathPointList.get(i));
                }
            }
            pathPointList = simplified;
        }
    }

    private boolean isCollinear(Point p1, Point p2, Point p3) {
        //checks collinearity per comparing the slopes between the Points
        return ((p3.y - p2.y) * (p2.x - p1.x) ==
                (p2.y - p1.y) * (p3.x - p2.x));
    }
}
