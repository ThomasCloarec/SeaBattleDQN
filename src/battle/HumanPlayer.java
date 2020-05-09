package battle;

import javax.swing.JFrame;
import java.util.ArrayList;

/**
 * A human player extending from the abstract class Player
 */
public class HumanPlayer extends Player {
    /**
     * The line delimiter for the configuration file
     */
    private static final String DELIMITER = "\\s*:\\s*";
    /**
     * A JFrame used to display to the human player
     */
    private JFrame jFrame;

    /**
     * The constructor of the HumanPlayer class, it copies the fleet passed as parameter and set others attributes.
     *
     * @param fleet  the fleet to copy
     * @param name   the name of the player
     * @param width  The width of the player's grid
     * @param height The height of the player's grid
     */
    public HumanPlayer(ArrayList<Ship> fleet, String name, int width, int height) {
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
