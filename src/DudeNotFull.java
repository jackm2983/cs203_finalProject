import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeNotFull extends Dude {
    private List<Point> currentPath = new ArrayList<>();

    public DudeNotFull (String id, Point position, double actionPeriod, double animationPeriod,
                        int resourceLimit, List<PImage> images, int resourceCount) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }

    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            this.setResourceCount(this.getResourceCount() + 1);

            if (target instanceof PlantEntity) {
                PlantEntity plant = (PlantEntity) target;
                plant.setHealth(plant.getHealth() - 1);
            }

            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

//    @Override
//    public Point nextPosition(WorldModel world, Point destPos) {
//        int horiz = Integer.signum(destPos.x - this.getPosition().x);
//        Point newPos = new Point(this.getPosition().x + horiz, this.getPosition().y);
//
//        if (horiz == 0 || world.isOccupied(newPos) && !(world.getOccupancyCell(newPos) instanceof Stump)) {
//            int vert = Integer.signum(destPos.y - this.getPosition().y);
//            newPos = new Point(this.getPosition().x, this.getPosition().y + vert);
//
//            if (vert == 0 || world.isOccupied(newPos) && !(world.getOccupancyCell(newPos) instanceof Stump)) {
//                newPos = this.getPosition();
//            }
//        }
//
//        return newPos;
//    }

    @Override
    public Point nextPosition(WorldModel world, Point destPos) {

        if (currentPath.isEmpty() || !currentPath.get(currentPath.size()-1).equals(destPos)) {
            PathingStrategy strategy = new AStarPathingStrategy();

            currentPath = strategy.computePath(
                    this.getPosition(),
                    destPos,
                    p -> world.withinBounds(p) &&
                            (!world.isOccupied(p) || world.getOccupancyCell(p) instanceof Stump),
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
        Optional<Entity> target = world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));

        if (target.isEmpty() || !this.moveTo(world, target.get(), scheduler) || !this.transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, new ActivityAction(this, world, imageStore), this.getActionPeriod());
        }
    }

    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.getResourceCount() >= this.getResourceLimit()) {
            Entity dude = new DudeFull(this.getId(), this.getPosition(), this.getActionPeriod(),
                    this.getAnimationPeriod(), this.getResourceLimit(), this.getImages(), this.getResourceCount());

            world.removeEntity(scheduler, this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(dude);
            ((Dude)dude).scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, new ActivityAction(this, world, imageStore), this.getActionPeriod());
        scheduler.scheduleEvent(this, new AnimationAction(this, 0), getAnimationPeriod());
    }

}
