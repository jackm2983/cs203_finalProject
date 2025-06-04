import processing.core.PImage;

import java.util.List;

public abstract class PlantEntity extends ActionObject {
    private int health;
    private final int healthLimit;

    public PlantEntity (String id, Point position, List<PImage> images, int health, int healthLimit, double animationPeriod, double actionPeriod) {
        super(id, position, images, animationPeriod, actionPeriod);
        this.health = health;
        this.healthLimit = healthLimit;
    }

    public int getHealth() {
        return health;
    }

    public int getHealthLimit() { return healthLimit; }

    public void setHealth(int health) {
        this.health = health;
    }

}
