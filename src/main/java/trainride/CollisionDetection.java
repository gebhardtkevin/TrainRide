package trainride;

import trainride.entities.Direction;
import trainride.entities.Entity;

public interface CollisionDetection {

    boolean isNotColliding(Entity entity, Direction direction);
}
