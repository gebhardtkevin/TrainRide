package trainride.entities;

import java.awt.*;

public enum Direction{
    UP,DOWN,LEFT,RIGHT;

    public static Direction getDirection(Point from, Point to) {
        int distX = to.x- from.x;
        int distY = to.y-from.y;
        if (distY == 0) {
            if (distX<0)
                return LEFT;
            if (distX>0)
                return  RIGHT;
        }
        if (distX == 0) {
            if (distY<0)
                return UP;
            if (distY>0)
                return  DOWN;
        }
        return RIGHT;
    }
}
