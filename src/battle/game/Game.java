package battle.game;

import battle.BattleShip;
import battle.game.players.HumanPlayer;
import battle.game.players.Player;
import battle.game.players.auto.AutoPlayer;
import battle.game.ships.Ship;

import javax.swing.JOptionPane;
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
     * The game mode
     */
    private final Mode mode;
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

        this.mode = mode;
        this.current = this.player1;
    }

    /**
     * Change the current player of the game by swapping them
     */
    public void changeCurrent() {
        if (this.current == this.player1) {
            this.current = this.player2;
        } else if (this.current == this.player2) {
            this.current = this.player1;
        } else {
            System.err.println("current variable contains neither player1 or player2");
            System.exit(1);
        }
    }

    /**
     * Analyse the shot sent by asking the opponentif it is a MISS, an HIT or a SUNK
     *
     * @param shot the shot position
     * @return the ShotResult
     */
    public ShotResult analyzeShot(int[] shot) {
        // Check parameters
        if (shot == null) {
            throw new IllegalArgumentException("One or more parameter is null. See the concerned method.");
        }

        ShotResult shotResult = ShotResult.MISS;
        if (this.current == this.player1) {
            shotResult = this.player2.analyzeShot(shot);
        } else if (this.current == this.player2) {
            shotResult = this.player1.analyzeShot(shot);
        } else {
            System.err.println("current variable contains neither player1 or player2");
            System.exit(1);
        }

        return shotResult;
    }

    public boolean allSunk(Player player) {
        // Check parameters
        if (player == null) {
            throw new IllegalArgumentException("One or more parameter is null. See the concerned method.");
        }
        return player.allSunk();
    }

    /**
     * Get the position where a player wants to shoot
     *
     * @param player the player who will choose where to shoot
     * @return the chosen position
     */
    public int[] readShot(Player player) {
        // Check parameters
        if (player == null) {
            throw new IllegalArgumentException("One or more parameter is null. See the concerned method.");
        }
        return player.newShot();
    }

    /**
     * This method gives the description of the game
     *
     * @return the description of the game
     */
    @Override
    public String description() {
        return "\nThe object of Battleship is to try and sink all of the other player's before they sink all of your ships. " +
                "\nAll of the other player's ships are somewhere on his/her board.  " +
                "\nYou try and hit them by calling out the coordinates of one of the squares on the board.  " +
                "\nThe other player also tries to hit your ships by calling out coordinates.  " +
                "\nNeither you nor the other player can see the other's board so you must try to guess where they are.  " +
                "\nEach board in the physical game has two grids:  the lower (horizontal) section for the player's ships and the upper part (vertical during play) for recording the player's guesses." +
                "\nGOOD LUCK !\n";
    }

    /**
     * This method starts the game
     */
    @Override
    public void start() {
        System.out.println("--- BEGINNING OF THE GAME ---");
        System.out.println(this.description());

        this.player1.displayMygrid();
        this.player1.displayOpponentGrid();

        this.player2.displayMygrid();
        this.player2.displayOpponentGrid();

        boolean gameRunning = true;
        while (gameRunning) {
            int[] shot = this.readShot(this.current);
            ShotResult shotResult = this.analyzeShot(shot);
            this.current.sendLastShotResult(shotResult, shot);

            this.changeCurrent();
            gameRunning = !this.allSunk(this.current);

            // Slow done the game if two AIs are fighting
            if (this.mode == Mode.AA) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        this.changeCurrent();
        this.endOfGame();
    }

    /**
     * This method stops the game
     */
    @Override
    public void endOfGame() {
        JOptionPane.showMessageDialog(null, "Well done " + this.current.getName() + ", you won !");
        System.out.println("Well done " + this.current.getName() + ", you won !");
    }
}
