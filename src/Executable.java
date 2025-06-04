import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface Executable {

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

}
