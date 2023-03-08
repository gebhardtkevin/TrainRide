package trainride.paths;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class TrackPath implements Serializable {
    @Serial
    private static final long serialVersionUID = 2405172041950251807L;
    private List<Point> pathPointList = new ArrayList<>();

    /*==CONSTRUCTORS===============================================================**/
    public TrackPath(List<Point> pointLlist) {
        this.pathPointList.addAll(pointLlist);
    }

    public TrackPath(Point... pointList) {
        this.pathPointList.addAll(Arrays.asList(pointList));
    }

    /*==PUBLIC=====================================================================**/
    public Iterator<Point> getIterator() {
        return pathPointList.iterator();
    }

    public TrackPath rotate(double rotationDegree, Point center) {
        double rotationRadians = Math.toRadians(rotationDegree);
        this.pathPointList = pathPointList.stream().
                map(point -> rotatePoint(point, rotationRadians, center)).
                collect(Collectors.toList());
        return this;
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

    public TrackPath toWorldCoordinates(Point worldPosition) {
        return new TrackPath(pathPointList.stream().
                map(point -> new Point(
                        point.x + worldPosition.x,
                        point.y + worldPosition.y)).
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

    public Point moveOnPath(Point currentPoint, int speed) {
        Point nextPoint= currentPoint;
        for (int i = 0; i < pathPointList.size() - 1; i++) {
            Point p1 = pathPointList.get(i);
            Point p2 = pathPointList.get(i+1);
            if (isCollinear(p1, p2, currentPoint)&&
                    !isOnEdgeForward(p1,p2,currentPoint,speed)) {
                //get direction
                int dy = p2.y - p1.y;
                int dx = p2.x - p1.x;
                if (dy == 0) {
                    nextPoint = new Point(currentPoint.x+speed, currentPoint.y);
                } else if (dx == 0) {
                    nextPoint = new Point(currentPoint.x, currentPoint.y+speed);
                }
                //check if we are too far
                int dist = distanceToLine(nextPoint, p1, p2);
                if (dist != 0) {
                    nextPoint = moveOnPath(nearestPoint(nextPoint, p1, p2), Integer.signum(speed)*dist);
                }
                return nextPoint;
            }
        }
        //not on Path, we will not move
        return currentPoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackPath trackPath = (TrackPath) o;
        return Objects.equals(pathPointList, trackPath.pathPointList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pathPointList);
    }

    public boolean isTrackEnd(Point p){
        return p.equals(last())||p.equals(first());
    }

    /*==PRIVATE====================================================================**/
    private int distanceToLine(Point nextPoint, Point p1, Point p2) {
        if (isBetween(nextPoint, p1, p2)) return 0;
        else {
            double d1 = nextPoint.distance(p1);
            double d2 = nextPoint.distance(p2);
            return (d1 < d2) ? (int)Math.round(d1) : (int)Math.round(d2);
        }

    }

    private Point nearestPoint(Point nextPoint, Point p1, Point p2) {
        double d1 = nextPoint.distance(p1);
        double d2 = nextPoint.distance(p2);
        return d1<d2?p1:p2;
    }

    private Point rotatePoint(Point point, double rotationRadians, Point center) {
        long x = Math.round(Math.cos(rotationRadians) * (point.x - center.x) -
                Math.sin(rotationRadians) * (point.y - center.y) + center.x);
        long y = Math.round(Math.sin(rotationRadians) * (point.x - center.x) +
                Math.cos(rotationRadians) * (point.y - center.y) + center.y);
        return new Point((int) x, (int) y);
    }

    private boolean isCollinear(Point p1, Point p2, Point p3) {
        //checks collinearity per comparing the slopes between the Points
        return ((p3.y - p2.y) * (p2.x - p1.x) ==
                (p2.y - p1.y) * (p3.x - p2.x));
    }

    private boolean isBetween(Point p1, Point p2, Point p3) {
        //checks collinearity per comparing the slopes between the Points
        return isCollinear(p1, p2, p3) && within(p1.x, p2.x, p3.x) && within(p1.y, p2.y, p3.y);
    }

    private boolean within(int v1, int v2, int v3) {
        return (v2 <= v1 && v1 <= v3) || (v3 <= v1 && v1 <= v2);
    }

    private boolean isOnEdgeForward(Point trackFrom, Point trackTo, Point currentPoint,int speed) {
    return ((currentPoint.equals(trackTo)&&speed>0)||(currentPoint.equals(trackFrom)&&speed<0));
    }
}
