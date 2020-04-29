package battle;

/**
 * Represent a ship and its position
 */
public class Ship {
    /**
     * The name of the ship
     */
    private final String name;
    /**
     * The size of the ship
     */
    private final int size;
    /**
     * The direction of the ship
     */
    private Direction direction;
    /**
     * The number of hits received by the ship
     */
    private int hitNumber;
    /**
     * The xOrigin of the ship (vertical axis)
     */
    private int xOrigin;
    /**
     * The yOrigin of the ship (horizontal axis)
     */
    private int yOrigin;

    /**
     * The constructor of the class (initialize name and size)
     *
     * @param name the name of the ship
     * @param size the size of the ship
     */
    public Ship(String name, int size) {
        this.name = name;
        this.size = size;
        this.direction = Direction.HORIZONTAL;
    }

    /**
     * Return each field of the ship
     *
     * @return the formatted string
     */
    @Override
    public String toString() {
        return "Ship{" +
                "name='" + this.name + '\'' +
                ", size=" + this.size +
                ", hitNumber=" + this.hitNumber +
                ", xOrigin=" + this.xOrigin +
                ", yOrigin=" + this.yOrigin +
                ", direction=" + this.direction +
                "}\n";
    }
}
