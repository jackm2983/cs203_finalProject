public class AnimationAction extends Action {

    public AnimationAction(Entity entity, int repeatCount) {
        super(entity, null, null, repeatCount);
    }
    /**
     * Ask the EventScheduler to execute an animation action for this action's Entity. This entails
     * telling the Entity to cycle through its images (each animation is one step through its images).
     *
     * @param scheduler The scheduler that queues up events.
     */
    @Override
    public void executeAction(EventScheduler scheduler) {
        this.entity.nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(this.entity,
                    new AnimationAction(this.entity, Math.max(this.repeatCount - 1, 0)),
                    ((ActionObject)this.entity).getAnimationPeriod()
            );
        }
    }

}
