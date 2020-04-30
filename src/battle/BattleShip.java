package battle;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
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
    //private Game gamePlay;
    /**
     * The folder containing config files
     */
    private static final String DATA_PRE_PATH = "/data/";
    /**
     * The line delimiter for the configuration file
     */
    private final String DELIMITER = "\\s*:\\s*";
    /**
     * The ships of the game
     */
    private final ArrayList<Ship> fleets;
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
        this.fleets = new ArrayList<>();
        this.configure(filename);
        this.printConfiguration();
    }

    /**
     * Print the configuration of the game (width, height, mode and fleet)
     */
    public void printConfiguration() {
        System.out.println("Width : " + this.width);
        System.out.println("Height : " + this.height);
        System.out.println("Mode : " + this.mode);
        System.out.println("\nFleet : " + this.fleets);
    }

    /**
     * Help use to initialize the game by reading the configuration file
     *
     * @param fileName the name of the file
     */
    private void configure(String fileName) {
        URL url = BattleShip.class.getResource(BattleShip.DATA_PRE_PATH + fileName);

        try (Scanner scanner = new Scanner(url.openStream()).useDelimiter(this.DELIMITER)) {
            scanner.useDelimiter(this.DELIMITER);

            try {
                // Check width
                int width = scanner.nextInt();
                if (width <= 0) {
                    throw new Exception("Width has to be bigger than 0");
                } else {
                    this.width = width;
                }

                // Check height
                int height = scanner.nextInt();
                if (height <= 0) {
                    throw new Exception("Height has to be bigger than 0");
                } else {
                    this.height = height;
                }

                // Check if the mode is valid
                if ("mode".equals(scanner.next())) {
                    String modeName = scanner.next();

                    for (Mode testMode : Mode.values()) {
                        if (testMode.name().equals(modeName)) {
                            // If the mode is valid, then use it (we could have just "try and catch" a possible error but we should not rely on exceptions to test something like this)
                            this.mode = Mode.valueOf(modeName);
                        }
                    }

                    // If the mode is not valid, then throw an exception
                    if (this.mode == null) {
                        throw new Exception("No valid mode specified");
                    }
                } else {
                    // If we did not find the word "mode", then throw an exception
                    throw new Exception("Did not find \"mode\" word on the second line");
                }

                // Check and insert ships
                while (scanner.hasNext()) {
                    String shipName = scanner.next();
                    int shipSize = scanner.nextInt();
                    if (shipSize <= 0) {
                        throw new Exception("Ship size has to be bigger than 0");
                    }

                    this.fleets.add(new Ship(shipName, shipSize));
                }
            } catch (Exception readingException) {
                readingException.printStackTrace();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
