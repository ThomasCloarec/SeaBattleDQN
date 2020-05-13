package battle.game.players.auto;

import battle.game.players.Player;
import battle.game.players.Square;
import battle.game.ships.Direction;
import battle.game.ships.Ship;

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
        int line = 0;
        int column = 0;

        boolean positionFound = false;
        while (!positionFound) {
            line = (int) (Math.random() * this.opponentGrid.length);
            column = (int) (Math.random() * this.opponentGrid[0].length);

            positionFound = !this.opponentGrid[line][column].isHit();
        }
        return new int[]{line, column};
    }

    /**
     * Initialize positions of ships in fleet
     */
    @Override
    public void shipPlacement() {
        // TODO make smarter by not hitting around ship already sunk
        for (Ship ship : this.fleet) {
            boolean shipAdded = false;

            while (!shipAdded) {
                Direction direction;
                int shipLine = (int) (Math.random() * this.myGrid.length);
                int shipColumn = (int) (Math.random() * this.myGrid[0].length);
                Square[] squares = new Square[ship.getSize()];
                if (Math.random() > 0.5) {
                    direction = Direction.HORIZONTAL;
                    for (int i = shipColumn; i < shipColumn + ship.getSize(); i++) {
                        squares[i - shipColumn] = new Square(shipLine, i);
                    }
                } else {
                    direction = Direction.VERTICAL;
                    for (int i = shipLine; i < shipLine + ship.getSize(); i++) {
                        squares[i - shipLine] = new Square(i, shipColumn);
                    }
                }

                if (this.checkPosition(squares)) {
                    // Insert squares of ship if allowed position is true
                    for (Square square : squares) {
                        this.myGrid[square.getLine()][square.getColumn()].setBusy();
                    }
                    ship.setLineOrigin(shipLine);
                    ship.setColumnOrigin(shipColumn);
                    ship.setDirection(direction);

                    shipAdded = true;
                }
            }
        }
    }
}
