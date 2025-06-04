import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ZombieKing extends ActionObject implements Moveable, Executable{
    public static final String KING_KEY = "king";
    public static final int KING_ANIMATION_PERIOD_IDX = 0;
    public static final int KING_ACTION_PERIOD_IDX = 1;
    public static final int KING_NUM_PROPERTIES = 2;
    private List<Point> currentPath = new ArrayList<>();

    public ZombieKing (String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        super(id, position, images, animationPeriod, actionPeriod);
    }

    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            world.removeEntity(scheduler, target);
            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }


    @Override
    public Point nextPosition(WorldModel world, Point destPos) {

        if (currentPath.isEmpty() || !currentPath.get(currentPath.size()-1).equals(destPos)) {
            PathingStrategy strategy = new AStarPathingStrategy();

            currentPath = strategy.computePath(
                    this.getPosition(),
                    destPos,
                    p -> world.withinBounds(p) &&
                            (!world.isOccupied(p)),
                    (p1, p2) -> p1.adjacent(p2),
                    PathingStrategy.CARDINAL_NEIGHBORS
            );
        }

        if (!currentPath.isEmpty()) {
            return currentPath.removeFirst();
        }

        return this.getPosition();
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> target = world.findNearest(this.getPosition(), List.of(Dude.class));
        if (target.isPresent()) {
            Point tgtPos = target.get().getPosition();
            if (moveTo(world, target.get(), scheduler)) {
                world.removeEntity(scheduler, target.get());
                Zombie newZombie = new Zombie("zombie_" + tgtPos.x + "_" + tgtPos.y,
                        tgtPos, 1.2, 0.3, imageStore.getImageList(Zombie.ZOMBIE_KEY));
                world.addEntity(newZombie);
                newZombie.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this, new ActivityAction(this, world, imageStore), this.getActionPeriod());
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, new ActivityAction(this, world, imageStore), this.getActionPeriod());
        scheduler.scheduleEvent(this, new AnimationAction(this, 0), getAnimationPeriod());
    }

}


