package battle;

import java.util.ArrayList;

/**
 * An auto player extending from the abstract class Player
 */
public class AutoPlayer extends Player {
    /**
     * The constructor of the AutoPlayer class, it copies the fleet passed as parameter and set others attributes.
     *
     * @param fleet  the fleet to copy
     * @param name   the name of the player
     * @param width  The width of the player's grid
     * @param height The height of the player's grid
     */
    public AutoPlayer(ArrayList<Ship> fleet, String name, int width, int height) {
        super(fleet, name, width, height);
    }

    /**
     * Ask two positions to the player for where to shoot
     *
     * @return the two positions
     */
    @Override
    public int[] newShot() {
        return new int[0];
    }

    /**
     * Initialize positions of ships in fleet
     */
    @Override
    public void shipPlacement() {

    }
}
