package battle.game.ships;

/**
 * Allowed directions for ships
 */
public enum Direction {
    /**
     * Horizontal direction.
     */
    HORIZONTAL,
    /**
     * Vertical direction.
     */
    VERTICAL;

    /**
     * Test if a string correspond to one of the modes
     *
     * @param testMode the string to test
     * @return true if testMode is a Mode
     */
    public static boolean contains(String testMode) {
        // Check parameters
        if (testMode == null) {
            throw new IllegalArgumentException("One or more parameter is null. See the concerned method.");
        }

        boolean ret = false;

        int i = 0;
        while (!ret && i < Direction.values().length) {
            ret = Direction.values()[i].name().equals(testMode);
            i++;
        }

        return ret;
    }
}
