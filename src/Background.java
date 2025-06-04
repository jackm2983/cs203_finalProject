import java.util.List;

import processing.core.PImage;

/**
 * Represents a background for the 2D world.
 */
public final class Background {
    private final String id;
    private final List<PImage> images;

    public Background(String id, List<PImage> images) {
        this.id = id;
        this.images = images;
    }

    public PImage getCurrentImage() {
        return this.images.get(0);
    }

    /**
     * @return The Background's id. Useful for debugging in project 4.
     */
    public String getId() {
        return this.id;
    }
}
