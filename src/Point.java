public final class Point {
    public final int x;
    public final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean adjacent(Point p) {
        return (x == p.x && Math.abs(y - p.y) == 1) ||
                (y == p.y && Math.abs(x - p.x) == 1);
    }

    public int distanceSquared(Point p) {
        int dx = this.x - p.x;
        int dy = this.y - p.y;
        return dx * dx + dy * dy;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Point &&
                ((Point) other).x == this.x &&
                ((Point) other).y == this.y;
    }

    @Override
    public int hashCode() {
        return 31 * (31 + x) + y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}