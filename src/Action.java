/**
 * An action that can be taken by a particular Entity.
 * There are two types of actions in this World:
 * - Activity actions: things like the Sapling growing up, or the DudeNotFull finding a
 *      Tree or Sapling to cut down, or the Fairy finding a Stump to turn into a Sapling.
 * - Animation actions: things like the Dude swinging his axe, or the Tree swaying, or
 *      the Fairy twinkling.
 */
public abstract class Action {
     protected final Entity entity;
     protected final WorldModel world;
     protected final ImageStore imageStore;
     protected final int repeatCount;

    public Action(Entity entity, WorldModel world, ImageStore imageStore, int repeatCount) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public abstract void executeAction(EventScheduler scheduler);
}
