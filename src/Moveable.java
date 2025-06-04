public interface Moveable {

    boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);
    Point nextPosition(WorldModel world, Point destPos);
    // add A* algorithm to the next position functions
    // compute path is a next class that we make. call compute path and pass the next position

}
