import processing.core.PImage;

import java.util.List;

public class Graveyard extends Entity {

    public static final String GRAVEYARD_KEY = "graveyard";
    public static final int GRAVEYARD_NUM_PROPERTIES = 0;

    public Graveyard (String id, Point position, List<PImage> images) {
        super(id, position, images);
    }

}
