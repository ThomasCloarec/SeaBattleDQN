package battle.game.players.auto;

import battle.game.ShotResult;
import battle.game.players.Player;
import battle.game.players.Square;
import battle.game.players.auto.neuralnet.Activation;
import battle.game.players.auto.neuralnet.CostFunction;
import battle.game.players.auto.neuralnet.Initializer;
import battle.game.players.auto.neuralnet.Layer;
import battle.game.players.auto.neuralnet.NeuralNetwork;
import battle.game.players.auto.neuralnet.math.Vector;
import battle.game.players.auto.neuralnet.optimizer.GradientDescent;
import battle.game.ships.Direction;
import battle.game.ships.Ship;

import java.util.ArrayList;
import java.util.Arrays;

public class SmartAutoPlayer extends Player {
    private final NeuralNetwork neuralNetwork;
    private final int totalGridSize = this.opponentGrid.length * this.opponentGrid[0].length;
    private final int inputLayerSize = this.totalGridSize * 2;

    public SmartAutoPlayer(ArrayList<Ship> fleet, String name, int width, int height) {
        super(fleet, name, width, height);

        int hiddenLayerSize = (int) Math.sqrt(this.inputLayerSize + this.totalGridSize);
        this.neuralNetwork =
                new NeuralNetwork.Builder(this.inputLayerSize)
                        .addLayer(new Layer(this.totalGridSize, Activation.Softmax))
                        .addLayer(new Layer(this.totalGridSize, Activation.Softmax))
                        .setCostFunction(new CostFunction.MSE())
                        .setOptimizer(new GradientDescent(10))
                        .initWeights(new Initializer.Random(0, 0.000001))
                        .create();
    }

    /**
     * Ask two positions to the player for where to shoot
     *
     * @return the two positions
     */
    @Override
    public int[] newShot() {
        // Create an input and feed it to the network
        double[] input = new double[this.inputLayerSize];
        for (int rows = 0; rows < this.opponentGrid.length; rows++) {
            for (int columns = 0; columns < this.opponentGrid[0].length; columns++) {
                int hitIndex = rows * this.opponentGrid[0].length + columns;
                input[hitIndex] = this.opponentGrid[rows][columns].isHit() ? 1 : 0;

                int freeIndex = hitIndex + this.opponentGrid.length * this.opponentGrid[0].length;
                input[freeIndex] = this.opponentGrid[rows][columns].isFree() ? 0 : 1;
            }
        }

        // Get the output of the network
        double[] output = this.neuralNetwork.evaluate(new Vector(input)).getOutput().getData();

        // Get the maximum output of the softmax, this will be the position of the hit
        double max = 0;
        int[] maxIndex = new int[2];
        for (int rows = 0; rows < this.opponentGrid.length; rows++) {
            for (int columns = 0; columns < this.opponentGrid[0].length; columns++) {
                int index = rows * this.opponentGrid[0].length + columns;
                if (output[index] > max) {
                    max = output[index];
                    maxIndex[0] = rows;
                    maxIndex[1] = columns;
                }
            }
        }
        return maxIndex;
    }

    /**
     * Initialize positions of ships in fleet
     */
    @Override
    public void shipPlacement() {
        for (Ship ship : this.fleet) {
            ship.resetHitNumber();
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

    @Override
    public void sendLastShotResult(ShotResult shotResult, int[] shotPosition) {
        super.sendLastShotResult(shotResult, shotPosition);

        double[] expected = new double[this.totalGridSize];
        if (shotResult == ShotResult.HIT || shotResult == ShotResult.SUNK) {
            expected[shotPosition[0] * this.opponentGrid[0].length + shotPosition[1]] = 1.0d;
        } else {
            expected[shotPosition[0] * this.opponentGrid[0].length + shotPosition[1]] = -1.0d;
        }

        this.neuralNetwork.learnFrom(new Vector(expected));
        this.neuralNetwork.updateFromLearning();
    }

    @Override
    public void gameEnded() {
        super.gameEnded();
    }
}
