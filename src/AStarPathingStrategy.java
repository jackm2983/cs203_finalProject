
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

class AStarPathingStrategy implements PathingStrategy {

    private static class Node {
        Point point;
        Node prior;
        int g;
        int h;

        Node(Point point, Node prior, int g, int h) {
            this.point = point;
            this.prior = prior;
            this.g = g;
            this.h = h;
        }

        int getF() {
            return g + h;
        }
    }


    private int heuristic(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);  // Manhattan distance
    }

    @Override
    public List<Point> computePath(
            Point start,
            Point end,
            Predicate<Point> canPassThrough,
            BiPredicate<Point, Point> withinReach,
            Function<Point, Stream<Point>> potentialNeighbors) {

        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(Node::getF));
        Map<Point, Node> openMap = new HashMap<>();
        Set<Point> closed = new HashSet<>();

        Node startNode = new Node(start, null, 0, heuristic(start, end));
        openList.add(startNode);
        openMap.put(start, startNode);

        while (!openList.isEmpty()) {
            Node current = openList.poll();
            openMap.remove(current.point);

            if (withinReach.test(current.point, end)) {
                return reconstructPath(current);
            }

            closed.add(current.point);

            potentialNeighbors.apply(current.point)
                    .filter(canPassThrough)
                    .filter(p -> !closed.contains(p))
                    .forEach(neighbor -> {
                        int tentativeG = current.g + 1;

                        if (!openMap.containsKey(neighbor) || tentativeG < openMap.get(neighbor).g) {
                            Node next = new Node(neighbor, current, tentativeG, heuristic(neighbor, end));
                            openList.add(next);
                            openMap.put(neighbor, next);
                        }
                    });
        }

        return new LinkedList<>();  // No path found
    }

    private List<Point> reconstructPath(Node endNode) {
        LinkedList<Point> path = new LinkedList<>();
        Node current = endNode;

        while (current.prior != null) {
            path.addFirst(current.point);
            current = current.prior;
        }

        return path;
    }

}