package battle;

import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class contains everything common to all players.
 */
public abstract class Player {
    /**
     * The fleet of the player
     */
    protected List<Ship> fleet = new ArrayList<>();
    /**
     * The height of the player's grid
     */
    protected int height;
    /**
     * The grid of the player
     */
    protected Square[][] myGrid;
    /**
     * The name of the player
     */
    protected String name;
    /**
     * The grid of the opponent of the player
     */
    protected Square[][] opponentGrid;
    /**
     * The width of the player's grid
     */
    protected int width;

    /**
     * The constructor of the Player class, it copies the fleet passed as parameter and set others attributes.
     *
     * @param fleet  the fleet to copy
     * @param name   the name of the player
     * @param width  The width of the player's grid
     * @param height The height of the player's grid
     */
    public Player(ArrayList<Ship> fleet, String name, int width, int height) {
        // Check parameters
        if (fleet == null || name == null) {
            throw new IllegalArgumentException("One or more parameter is null. See the concerned method.");
        } else if (width < BattleShip.BOARD_MIN_WIDTH || width > BattleShip.BOARD_MAX_WIDTH) {
            throw new IllegalArgumentException("The width parameter should be a number between " + BattleShip.BOARD_MIN_WIDTH + " and " + BattleShip.BOARD_MAX_WIDTH + ".");
        } else if (height < BattleShip.BOARD_MIN_HEIGHT || height > BattleShip.BOARD_MAX_HEIGHT) {
            throw new IllegalArgumentException("The height parameter should be a number between " + BattleShip.BOARD_MIN_HEIGHT + " and " + BattleShip.BOARD_MAX_HEIGHT + ".");
        }

        this.createCopy(fleet);
        this.name = name;
        this.width = width;
        this.height = height;

        this.initializeMyGrid();
        this.initializeOpponentGrid();
    }

    /**
     * Ask two positions to the player for where to shoot
     *
     * @return the two positions
     */
    public abstract int[] newShot();

    /**
     * Initialize positions of ships in fleet
     */
    public abstract void shipPlacement();

    /**
     * Create a copy of a fleet and put it into the fleet attribute of the player object
     *
     * @param fleet the fleet to copy
     */
    protected void createCopy(ArrayList<Ship> fleet) {
        // Check parameters
        if (fleet == null) {
            throw new IllegalArgumentException("One or more parameter is null. See the concerned method.");
        }

        this.fleet.clear();

        try {
            for (Ship ship : fleet) {
                this.fleet.add(ship.clone());
            }
        } catch (CloneNotSupportedException e) {
            System.out.println("Can't copy ship");
        }
    }

    /**
     * Initialize the grid of the player
     */
    protected void initializeMyGrid() {
        this.myGrid = new Square[this.width][this.height];
        this.initializeGrid(this.myGrid);
    }

    /**
     * Initialize the grid of the opponent player
     */
    protected void initializeOpponentGrid() {
        this.opponentGrid = new Square[this.width][this.height];
        this.initializeGrid(this.opponentGrid);
    }

    /**
     * Initialize a grid with squares
     *
     * @param grid the grid to initialize
     */
    private void initializeGrid(Square[][] grid) {
        // Check parameters
        if (grid == null) {
            throw new IllegalArgumentException("One or more parameter is null. See the concerned method.");
        }

        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                grid[x][y] = new Square(x, y);
            }
        }
    }
}
