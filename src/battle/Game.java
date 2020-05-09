package battle;

import java.util.ArrayList;

/**
 * This class contains the main logic of the game of battleship.
 */
public class Game implements IGame {
    /**
     * The fleet of the game
     */
    private final ArrayList<Ship> fleet = new ArrayList<>();
    /**
     * The player actually playing
     */
    private Player current;
    /**
     * The first player
     */
    private Player player1;
    /**
     * The second player
     */
    private Player player2;
    /**
     * The result of the last shot
     */
    private ShotResult shotResult;

    /**
     * The constructor of the class Game, initialize fleets by copying the parameter.
     * And initialize names of players, width and height of the board and also the mode which is then used to initialize players.
     *
     * @param fleet       the fleet of the game (then copied into attribute)
     * @param playerName1 the name of the first player
     * @param playerName2 the name of the second player
     * @param width       the width of the game board
     * @param height      the height of the game board
     * @param mode        the mode of the game (HH, HA or AA)
     */
    public Game(ArrayList<Ship> fleet, String playerName1, String playerName2, int width, int height, Mode mode) {
        // Check parameters
        if (fleet == null || playerName1 == null || playerName2 == null || mode == null) {
            throw new IllegalArgumentException("One or more parameter is null. See the concerned method.");
        } else if (width < BattleShip.BOARD_MIN_WIDTH || width > BattleShip.BOARD_MAX_WIDTH) {
            throw new IllegalArgumentException("The width parameter should be a number between " + BattleShip.BOARD_MIN_WIDTH + " and " + BattleShip.BOARD_MAX_WIDTH + ".");
        } else if (height < BattleShip.BOARD_MIN_HEIGHT || height > BattleShip.BOARD_MAX_HEIGHT) {
            throw new IllegalArgumentException("The height parameter should be a number between " + BattleShip.BOARD_MIN_HEIGHT + " and " + BattleShip.BOARD_MAX_HEIGHT + ".");
        }

        // Copy fleet parameter to fleet attribute
        try {
            for (Ship ship : fleet) {
                this.fleet.add(ship.clone());
            }
        } catch (CloneNotSupportedException e) {
            System.out.println("Can't copy ship");
        }

        if (mode == Mode.HH) {
            this.player1 = new HumanPlayer(this.fleet, playerName1, width, height);
            this.player2 = new HumanPlayer(this.fleet, playerName2, width, height);
        } else if (mode == Mode.HA) {
            this.player1 = new HumanPlayer(this.fleet, playerName1, width, height);
            this.player2 = new AutoPlayer(this.fleet, playerName2, width, height);
        } else if (mode == Mode.AA) {
            this.player1 = new AutoPlayer(this.fleet, playerName1, width, height);
            this.player2 = new AutoPlayer(this.fleet, playerName2, width, height);
        }

        this.current = this.player1;
    }

    /**
     * This method gives the description of the game
     *
     * @return the description of the game
     */
    @Override
    public String description() {
        return null;
    }

    /**
     * This method starts the game
     */
    @Override
    public void start() {

    }

    /**
     * This method stops the game
     */
    @Override
    public void endOfGame() {

    }
}
