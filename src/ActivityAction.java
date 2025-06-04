public class ActivityAction extends Action {

    public ActivityAction(Entity entity, WorldModel world, ImageStore imageStore) {
        super(entity, world, imageStore, 0);
    }

    /**
     * Ask the EventScheduler to execute an activity action for this action's Entity.
     * This entails telling the Entity to execute its activity.
     *
     * @param scheduler The scheduler that queues up events.
     */
    @Override
    public void executeAction(EventScheduler scheduler) {
        ((Executable)this.entity).executeActivity(this.world, this.imageStore, scheduler);
    }
}
