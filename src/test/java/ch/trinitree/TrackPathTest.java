package ch.trinitree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trainride.paths.TrackPath;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class TrackPathTest {
    int tileSize = 10;
    TrackPath hor;
    TrackPath vert;
    TrackPath curve;

    Point worldPoint1;
    Point worldPoint2;
    Point worldPoint3;

    @BeforeEach
    void setup() {
        hor = new TrackPath(
                new Point(0, tileSize / 2),
                new Point(tileSize, tileSize / 2));
        curve = new TrackPath(
                new Point(0, tileSize / 2),
                new Point(tileSize / 2, tileSize / 2),
                new Point(tileSize / 2, tileSize));
        vert = new TrackPath(
                new Point(tileSize / 2, 0),
                new Point(tileSize / 2, tileSize));
        worldPoint1 = new Point(0, 0);
        worldPoint2 = new Point(tileSize, 0);
        worldPoint3 = new Point(tileSize, tileSize);
    }

    @Test
    void combinePathsShouldGiveSimplifiedLongPath() {
        //given
        TrackPath combined = new TrackPath();
        combined.attachPath(hor.toWorldCoordinates(worldPoint1));
        combined.attachPath(curve.toWorldCoordinates(worldPoint2));
        combined.attachPath(vert.toWorldCoordinates(worldPoint3));
        combined.simplify();
        //then
        TrackPath result = new TrackPath(new Point(0, tileSize / 2),
                new Point((int) (1.5 * tileSize), (int) (0.5 * tileSize)),
                new Point((int) (1.5 * tileSize), 2 * tileSize));
        assertEquals(result, combined);
    }

    @Test
    void rotate180shouldMirrorPath() {
        //given
        TrackPath combined = new TrackPath();
        combined.attachPath(hor.toWorldCoordinates(worldPoint1));
        combined.attachPath(curve.toWorldCoordinates(worldPoint2));
        combined.attachPath(vert.toWorldCoordinates(worldPoint3));
        combined.simplify();
        //when
        combined.rotate(180, new Point(10, 10));
        curve.rotate(180, new Point(5, 5));
        //then
        TrackPath resultCombined = new TrackPath(new Point(20, 15),
                new Point(5, 15),
                new Point(5, 0));
        TrackPath resultCurve = new TrackPath(new Point(10, 5),
                new Point(5, 5),
                new Point(5, 0));

        assertEquals(resultCombined, combined);
        assertEquals(resultCurve, curve);
    }

    @Test
    void testPointGetters() {
        assertEquals(new Point(0, 5), curve.first());
        assertEquals((new Point(5, 10)), curve.last());
    }

    @Test
    void testMoveStraightOnPath() {
        //given
        TrackPath combined = new TrackPath();
        combined.attachPath(hor.toWorldCoordinates(worldPoint1));
        combined.attachPath(curve.toWorldCoordinates(worldPoint2));
        combined.attachPath(vert.toWorldCoordinates(worldPoint3));
        combined.simplify();
        Point player;
        //when
        player = combined.first();
        player = combined.moveOnPath(player, 4);
        //then
        assertEquals(new Point(4, 5), player);
    }

    @Test
    void testMoveAroundCornerOnPath() {
        //given
        TrackPath combined = new TrackPath();
        combined.attachPath(hor.toWorldCoordinates(worldPoint1));
        combined.attachPath(curve.toWorldCoordinates(worldPoint2));
        combined.attachPath(vert.toWorldCoordinates(worldPoint3));
        combined.simplify();
        Point player = new Point(13, 5);
        //when
        player = combined.moveOnPath(player, 4);
        //then
        assertEquals(new Point(15, 7), player);
    }

    @Test
    void testMoveToEndPointOnPath() {
        //given
        TrackPath combined = new TrackPath();
        combined.attachPath(hor.toWorldCoordinates(worldPoint1));
        combined.attachPath(curve.toWorldCoordinates(worldPoint2));
        combined.attachPath(vert.toWorldCoordinates(worldPoint3));
        combined.simplify();
        Point player = new Point(15, 18);
        //when
        player = combined.moveOnPath(player, 4);
        //then
        assertEquals(new Point(15, 20), player);
    }

    @Test
    void testMoveBackwardsToStartPointOnPath() {
        //given
        TrackPath combined = new TrackPath();
        combined.attachPath(hor.toWorldCoordinates(worldPoint1));
        combined.attachPath(curve.toWorldCoordinates(worldPoint2));
        combined.attachPath(vert.toWorldCoordinates(worldPoint3));
        combined.simplify();
        Point player = new Point(3, 5);
        //when
        player = combined.moveOnPath(player, -4);
        //then
        assertEquals(new Point(0, 5), player);
    }

    @Test
    void testMoveBackwardsAroundCornerOnPath() {
        //given
        TrackPath combined = new TrackPath();
        combined.attachPath(hor.toWorldCoordinates(worldPoint1));
        combined.attachPath(curve.toWorldCoordinates(worldPoint2));
        combined.attachPath(vert.toWorldCoordinates(worldPoint3));
        combined.simplify();
        Point player = new Point(15, 7);
        //when
        player = combined.moveOnPath(player, -5);
        //then
        assertEquals(new Point(12, 5), player);
    }

    @Test
    void testDontMoveIfNextToPath() {
        //given
        TrackPath combined = new TrackPath();
        combined.attachPath(hor.toWorldCoordinates(worldPoint1));
        combined.attachPath(curve.toWorldCoordinates(worldPoint2));
        combined.attachPath(vert.toWorldCoordinates(worldPoint3));
        combined.simplify();
        Point player = new Point(14, 4);
        //when
        player = combined.moveOnPath(player, 5);
        //then
        assertEquals(new Point(14, 4), player);
    }

    @Test
    void testTrackEndRecognition() {
        //given
        assertTrue(curve.isTrackEnd(new Point(5, 10)));
        assertFalse(curve.isTrackEnd(new Point(1, 1)));
    }

    @Test
    void testEquals() {
        TrackPath t1 = new TrackPath(new Point(0, 0), new Point(10, 0));
        TrackPath t2 = new TrackPath(new Point(0, 0), new Point(10, 0));
        TrackPath t3 = new TrackPath(new Point(0, 5), new Point(10, 5));
        assertTrue(t1.equals(t2) && t2.equals(t1));
        assertEquals(t1.hashCode(), t2.hashCode());
        assertFalse(t1.equals(t3) && t3.equals(t1));
        assertEquals(t1.hashCode(), t3.hashCode());
    }

    @Test
    void testReverseStream() {
        curve.reverse();
        TrackPath result = new TrackPath(
                new Point(tileSize / 2, tileSize),
                new Point(tileSize / 2, tileSize / 2),
                new Point(0, tileSize / 2));
        assertEquals(result, curve);
    }
}
