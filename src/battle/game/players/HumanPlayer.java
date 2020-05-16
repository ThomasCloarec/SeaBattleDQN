package battle.game.players;

import battle.game.ships.Direction;
import battle.game.ships.Ship;
import battle.text.AppText;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Predicate;

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
    private final JFrame jFrame = new JFrame();

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
     * Allow us to ask a question to the user (using a {@link javax.swing.JOptionPane}).
     * It returns an integer to us when the condition specified is filled.
     *
     * @param question       the question we want to ask to the user
     * @param errorMessage   the error message we want to show him if he did not match our condition
     * @param validCondition a {@link java.util.function.Predicate} returning true if the number is valid
     * @return the number, after validation
     */
    private int askInteger(String question, String errorMessage, Predicate<? super Integer> validCondition) {
        // Check parameters
        if (question == null || errorMessage == null || validCondition == null) {
            throw new IllegalArgumentException("One or more parameter is null. See the concerned method.");
        }

        int integer = 0;
        boolean validInteger = false;

        while (!validInteger) {
            try {
                String stringAnswer = JOptionPane.showInputDialog(this.jFrame, question);
                if (stringAnswer == null) {
                    System.err.println("The user canceled the input");
                    System.exit(1);
                }
                integer = Integer.parseInt(stringAnswer);
                if (validCondition.test(integer)) {
                    validInteger = true;
                } else {
                    JOptionPane.showMessageDialog(this.jFrame, errorMessage, "INPUT ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this.jFrame, "The input should be a number !", "INPUT ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }

        return integer;
    }

    /**
     * Help use to initialize the game by reading the configuration file
     */
    private void readConfiguration() {
        // Create a new file chooser
        JFileChooser jFileChooser = new JFileChooser();

        // set the default directory to the current one
        File workingDirectory = new File(System.getProperty("user.dir"));
        jFileChooser.setCurrentDirectory(workingDirectory);

        // Set a filter to only accept txt files
        jFileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                boolean ret = false;

                int i = file.getAbsolutePath().lastIndexOf('.');
                String extension = "";
                if (i >= 0) {
                    extension = file.getAbsolutePath().substring(i + 1);
                }

                if (file.isDirectory() || "txt".equals(extension)) {
                    ret = true;
                }
                return ret;
            }

            @Override
            public String getDescription() {
                return "Filtering text files";
            }
        });

        // Keep asking the user the file to use until he gives it or asks to cancel
        String path = null;
        while (path == null) {
            int fileChooserValue = jFileChooser.showDialog(null, AppText.getTextFor("open_the") + " " + this.name + " configuration");
            if (fileChooserValue == JFileChooser.APPROVE_OPTION) {
                path = jFileChooser.getSelectedFile().getAbsolutePath();
            } else if (fileChooserValue == JFileChooser.CANCEL_OPTION) {
                System.err.println("No file chosen");
                System.exit(1);
            }
        }

        // Load the file
        try (Scanner scanner = new Scanner(new File(path)).useDelimiter(HumanPlayer.DELIMITER)) {
            scanner.useDelimiter(HumanPlayer.DELIMITER);

            Square[][] grid = new Square[this.myGrid.length][this.myGrid[0].length];

            // Check and insert ships
            HashMap<String, Integer> shipCompletion = new HashMap<>();
            for (Ship ship : this.fleet) {
                shipCompletion.put(ship.getName(), 0);
            }
            while (scanner.hasNext()) {
                String shipName = scanner.next();
                int shipLine = scanner.nextInt();
                int shipColumn = scanner.nextInt();
                String testOrientation = scanner.next();

                // Check ship direction
                if (Direction.contains(testOrientation)) {
                    Direction direction = Direction.valueOf(testOrientation);

                    // Check ship name
                    if (this.fleetContainsShipName(shipName)) {
                        // Check completion of ships requirements of the battle configuration file
                        if (shipCompletion.get(shipName) < this.getShipSizeByName(shipName)) {

                            int shipSize = this.getShipSizeByName(shipName);

                            if (direction == Direction.HORIZONTAL) {
                                // Check ship line position
                                if (shipLine >= 0 && shipLine <= this.myGrid.length) {
                                    // Check ship column position
                                    if (shipColumn >= 0 && shipColumn + shipSize <= this.myGrid[0].length) {
                                        Square[] squares = new Square[shipSize];
                                        for (int i = shipColumn; i < shipColumn + shipSize; i++) {
                                            squares[i - shipColumn] = new Square(shipLine, i);
                                        }
                                        // Last check on the ship (about free space around), insert it if valid
                                        this.checkPositionAndInsertShipSquares(squares);
                                        shipCompletion.replace(shipName, shipCompletion.get(shipName) + 1);
                                        Ship ship = this.getNextShipToInitializeByName(shipName);
                                        ship.setLineOrigin(shipLine);
                                        ship.setColumnOrigin(shipColumn);
                                        ship.setDirection(Direction.HORIZONTAL);
                                    } else {
                                        System.err.println("Ship column + ship size has to be bigger than 0 and inferior to grid length");
                                        System.exit(1);
                                    }
                                } else {
                                    System.err.println("Ship line has to be bigger than 0 and inferior to grid length");
                                    System.exit(1);
                                }
                            } else if (direction == Direction.VERTICAL) {
                                // Check ship line position
                                if (shipLine >= 0 && shipLine + shipSize <= this.myGrid.length) {
                                    // Check ship column position
                                    if (shipColumn >= 0 && shipColumn <= this.myGrid[0].length) {
                                        Square[] squares = new Square[shipSize];
                                        for (int i = shipLine; i < shipLine + shipSize; i++) {
                                            squares[i - shipLine] = new Square(i, shipColumn);
                                        }
                                        // Last check on the ship (about free space around), insert it if valid
                                        this.checkPositionAndInsertShipSquares(squares);
                                        shipCompletion.replace(shipName, shipCompletion.get(shipName) + 1);
                                        Ship ship = this.getNextShipToInitializeByName(shipName);
                                        ship.setLineOrigin(shipLine);
                                        ship.setColumnOrigin(shipColumn);
                                        ship.setDirection(Direction.VERTICAL);
                                    } else {
                                        System.err.println("[" + shipName + "] Ship column has to be bigger than 0 and inferior to grid length");
                                        System.exit(1);
                                    }
                                } else {
                                    System.err.println("[" + shipName + "] Ship line + ship size has to be bigger than 0 and inferior to grid length");
                                    System.exit(1);
                                }
                            } else {
                                System.err.println("[" + shipName + "] No valid direction specified");
                                System.exit(1);
                            }
                        } else {
                            System.err.println("[" + shipName + "] Too many ships of the same type, not valid regarding to battleship configuration file requirements");
                            System.exit(1);
                        }
                    } else {
                        System.err.println("[" + shipName + "] this ship does not exist in battleship configuration file");
                        System.exit(1);
                    }
                } else {
                    System.err.println("[" + shipName + "] No valid orientation specified");
                    System.exit(1);
                }
            }

            for (Map.Entry<String, Integer> entry : shipCompletion.entrySet()) {
                String shipKeyName = entry.getKey();
                if (entry.getValue() != this.countOccurrenceShipNameInFleet(shipKeyName)) {
                    System.err.println("Wrong number of occurrences for ship name : " + shipKeyName);
                    System.exit(1);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("No file found");
            System.exit(1);
        } catch (InputMismatchException e) {
            System.err.println("The file is not valid. Check this configuration example : \n" +
                    "\tporte-avion : 0:4:HORIZONTAL:\n" +
                    "\tfregate : 1:0:VERTICAL:\n" +
                    "\tfregate : 2:2:HORIZONTAL:\n" +
                    "\tpatrouilleur : 6:3:HORIZONTAL:\n" +
                    "\tsous-marin : 7:7:VERTICAL:\n");
            System.exit(1);
        }
    }

    /**
     * Does the fleet contains the name of the ship
     *
     * @param name the name of the ship
     * @return the answer
     */
    private boolean fleetContainsShipName(String name) {
        // Check parameters
        if (name == null) {
            throw new IllegalArgumentException("One or more parameter is null. See the concerned method.");
        }

        boolean ret = false;
        int i = 0;
        while (!ret && i < this.fleet.size()) {
            ret = this.fleet.get(i).getName().equals(name);
            i++;
        }

        return ret;
    }

    /**
     * Check the positions of all squares and insert them in myGrid if valid
     *
     * @param squares the squares to insert
     */
    private void checkPositionAndInsertShipSquares(Square[] squares) {
        // Check parameters
        if (squares == null) {
            throw new IllegalArgumentException("One or more parameter is null. See the concerned method.");
        }

        // Check if ship can go there
        boolean allowedPosition = this.checkPosition(squares);

        if (allowedPosition) {
            // Insert squares of ship if allowed position is true
            for (Square square : squares) {
                this.myGrid[square.getLine()][square.getColumn()].setBusy();
            }
        } else {
            System.err.println("Your player configuration file is invalid." +
                    " ships should have one empty square between them");
            System.exit(1);
        }
    }

    /**
     * Count the occurrence of a particular ship name in the fleet
     *
     * @param name the name to search
     * @return the count of this name
     */
    private int countOccurrenceShipNameInFleet(String name) {
        // Check parameters
        if (name == null) {
            throw new IllegalArgumentException("One or more parameter is null. See the concerned method.");
        }

        int count = 0;
        for (Ship ship : this.fleet) {
            if (ship.getName().equals(name)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Get a ship size by its name
     *
     * @param name name of the ship
     * @return the ship size
     */
    private int getShipSizeByName(String name) {
        // Check parameters
        if (name == null) {
            throw new IllegalArgumentException("One or more parameter is null. See the concerned method.");
        }

        int size = 0;
        int i = 0;
        while (size == 0 && i < this.fleet.size()) {
            Ship ship = this.fleet.get(i);
            if (ship.getName().equals(name)) {
                size = ship.getSize();
            }
            i++;
        }

        return size;
    }

    /**
     * Get the next ship with a particular name in the fleet to initialize
     *
     * @param name the name
     * @return the next ship to initialize
     */
    private Ship getNextShipToInitializeByName(String name) {
        // Check parameters
        if (name == null) {
            throw new IllegalArgumentException("One or more parameter is null. See the concerned method.");
        }

        Ship ship = null;
        int i = 0;
        while (ship == null && i < this.fleet.size()) {
            Ship testShip = this.fleet.get(i);
            if (testShip.getName().equals(name)) {
                if (!testShip.isDirectionSet()) {
                    ship = testShip;
                }
            }
            i++;
        }

        return ship;
    }

    /**
     * Ask two positions to the player for where to shoot
     *
     * @return the two positions
     */
    @Override
    public int[] newShot() {
        Predicate<Integer> validCondition = integer -> integer >= 0 && integer < this.opponentGrid.length;
        Predicate<Integer> validColumnCondition = integer -> integer >= 0 && integer < this.opponentGrid[0].length;

        int line = this.askInteger("[" + this.name + "] " + AppText.getTextFor("line_position_question"), AppText.getTextFor("error_line_range"), validCondition);
        int column = this.askInteger("[" + this.name + "] " + AppText.getTextFor("column_position_question"), AppText.getTextFor("error_column_range"), validColumnCondition);

        return new int[]{line, column};
    }

    /**
     * Initialize positions of ships in fleet
     */
    @Override
    public void shipPlacement() {
        this.readConfiguration();
    }
}
