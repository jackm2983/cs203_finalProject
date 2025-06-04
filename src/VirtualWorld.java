import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import processing.core.*;

public final class VirtualWorld extends PApplet {
    private static String[] ARGS;

    public static final int VIEW_WIDTH = 640;
    public static final int VIEW_HEIGHT = 480;
    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;

    public static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    public static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;

    public static final String IMAGE_LIST_FILE_NAME = "imagelist";
    public static final String DEFAULT_IMAGE_NAME = "background_default";
    public static final int DEFAULT_IMAGE_COLOR = 0x808080;

    private String loadFile = "world.sav";
    private long startTimeMillis = 0;

    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        parseCommandLine(ARGS);
        loadImages(IMAGE_LIST_FILE_NAME);
        loadWorld(loadFile, this.imageStore);

        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH, TILE_HEIGHT);
        this.scheduler = new EventScheduler();
        this.startTimeMillis = System.currentTimeMillis();
        this.scheduleActions(this.world, this.scheduler, this.imageStore);
    }

    public void draw() {
        double appTime = (System.currentTimeMillis() - this.startTimeMillis) * 0.001;
        double frameTime = (appTime - this.scheduler.getCurrentTime());
        this.update(frameTime);
        this.view.drawViewport();
    }

    public void update(double frameTime){
        this.scheduler.updateOnTime(frameTime);
    }

    public void infectNearbyDudes(Point center) {
        for (int dx = -1; dx <=1; dx++) {
            for (int dy = -1; dy <=1; dy++) {
                Point p = new Point(center.x + dx, center.y + dy);

                if(world.withinBounds(p)) {
                    Optional<Entity> occupant = world.getOccupant(p);
                    if (occupant.isPresent() && occupant.get() instanceof Dude) {
                        Entity dude = occupant.get();
                        world.removeEntity(scheduler, dude);

                        Zombie zombie = new Zombie("zombie_" + p.x + "_" + p.y,
                                p, 1.0, 0.3, imageStore.getImageList(Zombie.ZOMBIE_KEY));
                        world.addEntity(zombie);
                        zombie.scheduleActions(scheduler, world, imageStore);
                    }
                }
            }
        }
    }

    // Just for debugging and for P4
    // Be sure to refactor this method as appropriate
    public void mousePressed() {
        Point pressed = mouseToPoint();

        if (!world.isOccupied(pressed)) {
            String bgId = world.getBackgroundCell(pressed).getId();
            if (!bgId.equals("tree") && !bgId.equals("house")) {
                Graveyard grave = new Graveyard("graveyard",
                        pressed, imageStore.getImageList(Graveyard.GRAVEYARD_KEY));
                world.addEntity(grave);

                infectNearbyDudes(pressed);

                ZombieKing king = new ZombieKing("king", pressed,
                        0.75, 0.3, imageStore.getImageList(ZombieKing.KING_KEY));
                world.addEntity(king);
                king.scheduleActions(scheduler, world, imageStore);
            }
        }

        System.out.println("Zombie images: " + imageStore.getImageList("zombie").size());
        System.out.println("Graveyard images: " + imageStore.getImageList("graveyard").size());
        System.out.println("King images: " + imageStore.getImageList("king").size());





        System.out.println("CLICK! " + pressed.x + ", " + pressed.y);
        Optional<Entity> entityOptional = world.getOccupant(pressed);
        if (entityOptional.isPresent()) {
            Entity entity = entityOptional.get();
            System.out.print(entity.getId() + ": " + entity.getClass().getSimpleName());

            if (entity instanceof PlantEntity plant) {
                System.out.println(" : " + plant.getHealth());
            } else {
                System.out.println(); // Just end the line if not a PlantEntity
            }
        }

    }

    public void scheduleActions(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof ActionObject) {
                ((ActionObject) entity).scheduleActions(scheduler, world, imageStore);
            }
        }
    }

    private Point mouseToPoint() {
        return view.getViewport().viewportToWorld(mouseX / TILE_WIDTH, mouseY / TILE_HEIGHT);
    }

    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP -> dy -= 1;
                case DOWN -> dy += 1;
                case LEFT -> dx -= 1;
                case RIGHT -> dx += 1;
            }
            view.shiftView(dx, dy);
        }
    }

    public static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME, imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    public static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        Arrays.fill(img.pixels, color);
        img.updatePixels();
        return img;
    }

    public void loadImages(String filename) {
        this.imageStore = new ImageStore(createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
        try {
            Scanner in = new Scanner(new File(filename));
            WorldModel.loadImages(in, imageStore,this);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public void loadWorld(String file, ImageStore imageStore) {
        this.world = new WorldModel();
        try {
            Scanner in = new Scanner(new File(file));
            world.load(in, imageStore, createDefaultBackground(imageStore));
        } catch (FileNotFoundException e) {
            Scanner in = new Scanner(file);
            world.load(in, imageStore, createDefaultBackground(imageStore));
        }
    }

    public void parseCommandLine(String[] args) {
        if (args.length != 0) {
            this.loadFile = args[0]; // Instead of the default world.sav
        }
    }

    public static void main(String[] args) {
        VirtualWorld.ARGS = args;
        PApplet.main(VirtualWorld.class);
    }

    public static List<String> headlessMain(String[] args, double lifetime){
        VirtualWorld.ARGS = args;

        VirtualWorld virtualWorld = new VirtualWorld();
        virtualWorld.setup();
        virtualWorld.update(lifetime);

        return virtualWorld.world.log();
    }
}
