package battle.game;

import battle.BattleShip;
import battle.game.players.HumanPlayer;
import battle.game.players.Player;
import battle.game.players.auto.AutoPlayer;
import battle.game.players.auto.SmartAutoPlayer;
import battle.game.ships.Ship;
import battle.text.AppText;

import javax.swing.JOptionPane;
import java.util.ArrayList;

/**
 * This class contains the main logic of the game of battleship.
 */
public class Game implements IGame {
    /**
     * The number of games of training between each progress demonstration of the ai
     */
    private static final double TRAINING_GAME_NUMBER = 5000.0d;
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
            System.err.println("Can't copy ship");
        }

        if (mode == Mode.HH) {
            this.player1 = new HumanPlayer(this.fleet, playerName1, width, height);
            this.player2 = new HumanPlayer(this.fleet, playerName2, width, height);
        } else if (mode == Mode.HA) {
            this.player1 = new HumanPlayer(this.fleet, playerName1, width, height);
            this.player2 = new SmartAutoPlayer(this.fleet, "Neural Network AI player", width, height, false);
        } else if (mode == Mode.AA) {
            this.player1 = new SmartAutoPlayer(this.fleet, "Neural Network AI player", width, height);
            this.player2 = new AutoPlayer(this.fleet, "Programmed AI player", width, height);
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

    /**
     * All sunk boolean.
     *
     * @param player the player
     * @return the boolean
     */
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
     * Display grids.
     */
    private void displayGrids() {
        this.player1.displayMygrid();
        this.player1.displayOpponentGrid();
        this.player2.displayMygrid();
        this.player2.displayOpponentGrid();
    }

    /**
     * This method gives the description of the game
     *
     * @return the description of the game
     */
    @Override
    public String description() {
        return AppText.getTextFor("description");
    }

    /**
     * This method starts the game
     */
    @Override
    public void start() {
        double value = Game.TRAINING_GAME_NUMBER;
        double gameWonCount = 0;
        System.out.println(AppText.getTextFor("beginning"));
        System.out.println(this.description());
        int iterationCount = this.mode == Mode.AA ? Integer.MAX_VALUE : 1;

        if (this.mode != Mode.AA) {
            this.displayGrids();
        }

        for (int i = 0; i < iterationCount; i++) {
            if (this.mode == Mode.AA) {
                this.player1.initializeGrids();
                this.player2.initializeGrids();

                if (i % value == 0) {
                    this.displayGrids();
                    double winPercentage = (gameWonCount / value) * 100;
                    System.out.println(winPercentage + "% of games won by the neural network over the programmed IA");
                }
            }

            boolean gameRunning = true;
            while (gameRunning) {
                int[] shot = this.readShot(this.current);
                ShotResult shotResult = this.analyzeShot(shot);
                this.current.sendLastShotResult(shotResult, shot);
                this.changeCurrent();
                gameRunning = !this.allSunk(this.current);

                if (i % value == 0) {
                    try {
                        // Pausing between each moves of AIs
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (this.mode == Mode.AA) {
                if (this.current == this.player2) {
                    gameWonCount++;

                    if (i % value == 0) {
                        System.out.println("The neural network WON the last game !");
                    }
                } else {
                    if (i % value == 0) {
                        System.out.println("The neural network LOOSED the last game !");
                    }
                }
            }

            if (this.mode == Mode.AA && i % value == 0) {
                gameWonCount = 0;
                this.player1.closeGrids();
                this.player2.closeGrids();
            }

            this.changeCurrent();
            if (this.mode != Mode.AA) {
                this.endOfGame();
            }
        }
    }

    /**
     * This method stops the game
     */
    @Override
    public void endOfGame() {
        String message = AppText.getTextFor("well_done") + " " + this.current.getName() + ", " + AppText.getTextFor("you_won") + " !";
        JOptionPane.showMessageDialog(null, message);
        System.out.println(message);
    }
}
