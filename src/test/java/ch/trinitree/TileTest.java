package ch.trinitree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trainride.tiles.StandardTile;
import trainride.tiles.Tile;
import trainride.tiles.TileManager;
import trainride.tiles.TileType;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {
    TileManager manager;

    @BeforeEach
    void setup() {
        manager = new TileManager();
    }

    @Test
    void TileManagerCanLoadfirstMap() {
        assertNotNull(manager.getTileAt(0, 0));
    }

    @Test
    void checkImageGetter() {
        BufferedImage image = TileType.GRAS.getImage();
        assertTrue(compareImages(image, manager.getTileAt(0, 0).getImage()));
    }

    @Test
    void checkImageGetterOutsideBorders(){
        BufferedImage image = TileType.OUTSIDE.getImage();
        assertTrue(compareImages(image, manager.getTileAt(-1, 0).getImage()));
    }

    @Test
    void testEquals() {
        Tile tile= manager.getTileAt(0, 0);
        Tile sameTile = new StandardTile(TileType.GRAS.getImage(), false);
        sameTile.setWorldPosition(tile.getWorldPosition());
        Tile otherTile = new StandardTile(TileType.OUTSIDE.getImage(), true);

        assertTrue(tile.equals(sameTile) && sameTile.equals(tile));
        assertEquals(tile.hashCode(), sameTile.hashCode());
        assertFalse(tile.equals(otherTile) || otherTile.equals(tile));
    }

    /**
     * Compares two images pixel by pixel.
     *
     * @param imgA the first image.
     * @param imgB the second image.
     * @return whether the images are both the same or not.
     */
    public boolean compareImages(BufferedImage imgA, BufferedImage imgB) {
        // The images must be the same size.
        if (imgA.getWidth() != imgB.getWidth() || imgA.getHeight() != imgB.getHeight()) {
            return false;
        }

        int width = imgA.getWidth();
        int height = imgA.getHeight();

        // Loop over every pixel.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Compare the pixels for equality.
                if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }
}