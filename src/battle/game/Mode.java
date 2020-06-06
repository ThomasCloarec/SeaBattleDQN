package battle.game;

/**
 * Allowed modes for the game :
 * <ul>
 *     <li>HH (Human - Human)</li>
 *     <li>HA (Human - Auto</li>
 *     <li>AA (Auto - Auto)</li>
 * </ul>
 */
public enum Mode {
    /**
     * Hh mode.
     */
    HH,
    /**
     * Ha mode.
     */
    HA,
    /**
     * Aa mode.
     */
    AA;

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
        while (!ret && i < Mode.values().length) {
            ret = Mode.values()[i].name().equals(testMode);
            i++;
        }

        return ret;
    }
}
