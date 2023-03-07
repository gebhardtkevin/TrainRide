package trainride;

import trainride.entities.Direction;
import trainride.entities.Entity;
import trainride.tiles.Tile;

public class CollisionDetectionWalking implements CollisionDetection {

    public boolean isNotColliding(Entity entity, Direction direction) {
        int currentTileX;
        int currentTileY;
        int nextTileX;
        int nextTileY;

        switch (direction) {
            case DOWN -> {
                currentTileX = entity.getWorldPos().x + entity.getSize().x / 2;
                currentTileY = entity.getWorldPos().y + entity.getSize().y;
                nextTileX = currentTileX;
                nextTileY = currentTileY + entity.getSpeed();
            }
            case UP -> {
                currentTileX = entity.getWorldPos().x + entity.getSize().x / 2;
                currentTileY = entity.getWorldPos().y;
                nextTileX = currentTileX;
                nextTileY = currentTileY - entity.getSpeed();
            }
            case RIGHT -> {
                currentTileX = entity.getWorldPos().x + entity.getSize().x;
                currentTileY = entity.getWorldPos().y + entity.getSize().y / 2;
                nextTileX = currentTileX+ entity.getSpeed();
                nextTileY = currentTileY;
            }
            default -> {
                currentTileX = entity.getWorldPos().x;
                currentTileY = entity.getWorldPos().y + entity.getSize().x / 2;
                nextTileX = currentTileX-  entity.getSpeed();
                nextTileY = currentTileY;
            }
        }
        int tileSize = GamePanel.getInstance().getTileSize();
        int nextColumn = nextTileX<0?-1:nextTileX / tileSize;
        int nextRow = nextTileY<0?-1:nextTileY / tileSize;

        Tile nextTile = GamePanel.getInstance().getTileManager().getTileAt(nextColumn, nextRow);

        return !nextTile.isSolid();
    }
}
