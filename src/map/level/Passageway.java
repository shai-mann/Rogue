package map.level;

import map.level.table.GameTable;
import rendering.structure.AbstractRenderedModel;
import rendering.level.PassagewayRenderer;
import rendering.structure.Renderer;
import util.Helper;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Passageway extends AbstractRenderedModel implements Serializable, Renderer {

    public static final int DEFAULT_WEIGHT = 1, DIRECTION_MATCHING_WEIGHT = 35000, PREVIOUS_STEP_MATCHING_WEIGHT = 200;
    private static final List<Point> steps = Helper.getAdjacentPoints(new Point(0, 0), false);

    private final PassagewayRenderer renderer;
    private final List<Point> points = new ArrayList<>();

    public Passageway(Room roomTo, Room roomFrom, List<Room> rooms) {
        createPassageway(roomFrom, roomTo, rooms);

        renderer = new PassagewayRenderer(points);
    }

    public boolean contains(Point p) {
        return points.contains(p);
    }

    // PASSAGEWAY GENERATION METHODS

    private void createPassageway(Room origin, Room destination, List<Room> rooms) {
        // create doors
        Point destinationDoor = destination.createDoor(origin.getCenter());
        Point originDoor = origin.createDoor(destinationDoor);

        Point previousStep = new Point(0, 0);
        Point pointer = new Point(originDoor);
        int tries = 0;
        while (!Helper.isNextTo(pointer, destinationDoor)) {
            try {
                previousStep = step(pointer, destinationDoor, previousStep, rooms);
            } catch (Exception e) {
                e.printStackTrace();
                tries++;

                if (tries > 50) {
                    System.out.println("Pointer: (" + pointer.x + ", " + pointer.y + ")");
                    break;
                }
            }

            pointer = Helper.translate(pointer, previousStep);
            if (!points.contains(pointer)) points.add(pointer);
        }
    }

    private Point step(Point pointer, Point destinationDoor, Point previousStep, List<Room> rooms) throws Exception {
        /*
        Steps:
        1) Get adjacent points
        2) Filter out points already in rooms (passageways can be crossed)
        3) Calculate weights
            3a) 1 to all points
            3b) add 1 to any point that would continue the direction that was previously headed
            3c) add 1 to any point that advances towards the destination door
        4) Get random points using weights
         */

        // collects adjacent points, filtering any points inside rooms
        List<Point> options = steps.stream().filter(
                (p) -> {
                    Point pTranslated = Helper.translate(pointer, p);
                    if (!GameTable.isOnTable(pTranslated)) return false;
                    return rooms.stream().noneMatch((r) -> r.bounds().contains(pTranslated));
                }
        ).toList();
        Point direction = constructDirection(pointer, destinationDoor);

        Map<Point, Integer> weightMap = new HashMap<>();
        options.forEach(p -> weightMap.put(p, calculateDirectionalWeight(p, previousStep, direction)));

        int totalWeights = weightMap.values().stream().mapToInt(Integer::intValue).sum();
        int chosenStep;
        try {
            // exclusive upper bound so that a point must be chosen
            chosenStep = Helper.getRandom(0, totalWeights - 1);
        } catch (IllegalArgumentException e) {
            throw new Exception("Total weight (" + totalWeights + ") resulted in no step being chosen.");
        }

        for (Point p : options) {
            if (chosenStep <= weightMap.get(p)) {
                return p;
            }

            chosenStep -= weightMap.get(p);
        }

        throw new Exception("Total weight (" + totalWeights + ") resulted in no step being chosen.");
    }

    /**
     * References the {@code getDirectionalValue(...)} function to create a point that
     * represents the direction that should be headed (both in x and y directions) to go from
     * the current pointer to the destination.
     * @param pointer the current pointer {@link Point}.
     * @param destination the destination {@link Point}.
     * @return a {@link Point} representing the directions (as 1, 0, -1) values to head in.
     */
    private Point constructDirection(Point pointer, Point destination) {
        return new Point(
                getDirectionalValue(pointer.x, destination.x),
                getDirectionalValue(pointer.y, destination.y)
        );
    }

    /**
     * Calculates the directional value given a pointer and a destination.
     * <p>1) If pointer > destination -> -1</p>
     * <p>2) If pointer = destination -> 0</p>
     * <p>3) If pointer < destination -> 1</p>
     * @param pointer the current pointer value.
     * @param destination the destination value.
     * @return the directional value based on the given inputs.
     */
    private int getDirectionalValue(int pointer, int destination) {
        return Integer.compare(destination, pointer);
    }

    private int calculateDirectionalWeight(Point p, Point previousStep, Point direction) {
        int weight = DEFAULT_WEIGHT;

        if (p.equals(previousStep)) weight+= PREVIOUS_STEP_MATCHING_WEIGHT;
        if (p.x != 0 && p.x == direction.x) weight+= DIRECTION_MATCHING_WEIGHT;
        if (p.y != 0 && p.y == direction.y) weight+= DIRECTION_MATCHING_WEIGHT;

        return weight;
    }

    /* OVERRIDES */

    @Override
    protected Renderer renderer() {
        return renderer;
    }
}
