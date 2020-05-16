package battle;

import battle.game.Game;
import battle.game.Mode;
import battle.game.ships.Ship;
import battle.text.AppText;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Contains all the elements of the game.
 * It does several things :
 * <ul>
 *     <li>reads the configuration file et initialize the game parameters.</li>
 *      <li>creates a game instance</li>
 *      <li>launches the game</li>
 *  </ul>
 */
public class BattleShip {
    /**
     * The maximum height of the board
     */
    public static final int BOARD_MAX_HEIGHT = 15;
    /**
     * The maximum width of the board
     */
    public static final int BOARD_MAX_WIDTH = 15;
    /**
     * The minimum height of the board
     */
    public static final int BOARD_MIN_HEIGHT = 0;
    /**
     * The minimum width of the board
     */
    public static final int BOARD_MIN_WIDTH = 0;
    /**
     * The line delimiter for the configuration file
     */
    private static final String DELIMITER = "\\s*:\\s*";
    /**
     * The ships of the game
     */
    private final ArrayList<Ship> fleet = new ArrayList<>();
    /**
     * The main logic of the game of battleship
     */
    private final Game gamePlay;
    /**
     * The height of the board
     */
    private int height;
    /**
     * The game mode of the game
     */
    private Mode mode;
    /**
     * The width of the board
     */
    private int width;

    /**
     * The constructor of the class, initializing the list of ships
     *
     * @param filename    the name of the file
     * @param playerName1 the name of the first player
     * @param playerName2 the name of the second player
     */
    public BattleShip(String filename, String playerName1, String playerName2) {
        // Check parameters
        if (filename == null || playerName1 == null || playerName2 == null) {
            throw new IllegalArgumentException("One or more parameter is null. See the concerned method.");
        }

        this.configure(filename);
        this.printConfiguration();

        this.gamePlay = new Game(this.fleet, playerName1, playerName2, this.width, this.height, this.mode);
        this.gamePlay.start();
    }

    /**
     * Print the configuration of the game (width, height, mode and fleet)
     */
    public void printConfiguration() {
        System.out.println(AppText.getTextFor("game_configuration"));
        System.out.println(AppText.getTextFor("width") + " : " + this.width);
        System.out.println(AppText.getTextFor("height") + " : " + this.height);
        System.out.println("Mode : " + this.mode);
        System.out.println(AppText.getTextFor("fleet") + " : " + this.fleet);
    }

    /**
     * Help use to initialize the game by reading the configuration file
     *
     * @param fileName the name of the file
     */
    private void configure(String fileName) {
        // Check parameters
        if (fileName == null) {
            throw new IllegalArgumentException("One or more parameter is null. See the concerned method.");
        }

        // Load configuration file
        File file = new File(fileName);

        // Use the file
        try (Scanner scanner = new Scanner(file).useDelimiter(BattleShip.DELIMITER)) {
            scanner.useDelimiter(BattleShip.DELIMITER);

            // Check width
            int width = scanner.nextInt();
            if (width >= BattleShip.BOARD_MIN_WIDTH && width <= BattleShip.BOARD_MAX_WIDTH) {
                this.width = width;

                // Check height
                int height = scanner.nextInt();
                if (height >= BattleShip.BOARD_MIN_HEIGHT && height <= BattleShip.BOARD_MAX_HEIGHT) {
                    this.height = height;

                    // Check if there is a mode
                    if ("mode".equals(scanner.next().toLowerCase())) {
                        String modeName = scanner.next();

                        // Check if the mode is valid
                        if (Mode.contains(modeName)) {
                            this.mode = Mode.valueOf(modeName);

                            // Check and insert ships
                            while (scanner.hasNext()) {
                                String shipName = scanner.next();
                                int shipSize = scanner.nextInt();

                                if (shipSize >= Ship.MIN_SIZE && shipSize <= Ship.MAX_SIZE) {
                                    this.fleet.add(new Ship(shipName, shipSize));
                                } else {
                                    System.err.println("Ship size has to be bigger than 0");
                                    System.exit(1);
                                }
                            }
                        } else {
                            System.err.println("No valid mode specified");
                            System.exit(1);
                        }
                    } else {
                        System.err.println("Did not find \"mode\" word on the second line");
                        System.exit(1);
                    }
                } else {
                    System.err.println("Height has to be bigger than 0");
                    System.exit(1);
                }
            } else {
                System.err.println("Width has to be bigger than 0");
                System.exit(1);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Fichier non trouvé");
            System.exit(1);
        } catch (InputMismatchException e) {
            System.err.println("The file is not valid. Check this configuration example : \n" +
                    "\t10 : 15:\n" +
                    "\tmode : HH:\n" +
                    "\tporte-avion : 5:\n" +
                    "\tfregate : 4:\n" +
                    "\tfregate : 4:\n" +
                    "\tpatrouilleur : 3:\n" +
                    "\tsous-marin : 2:\n" +
                    "\tremorqueur : 1:");
            System.exit(1);
        }
    }
}
