package battle.game.players.auto.neuralnet;

import battle.game.players.auto.neuralnet.math.Vector;

/**
 * The outcome of an evaluation.
 * Will always contain the output data.
 * Might contain the cost.
 */
public class Result {
    /**
     * The Cost.
     */
    private final Double cost;
    /**
     * The Output.
     */
    private final Vector output;

    /**
     * Instantiates a new Result.
     *
     * @param output the output
     */
    public Result(Vector output) {
        // Check parameters
        if (output == null) {
            throw new IllegalArgumentException("One or more parameter is null. See the concerned method.");
        }

        this.output = output;
        this.cost = null;
    }

    /**
     * Instantiates a new Result.
     *
     * @param output the output
     * @param cost   the cost
     */
    public Result(Vector output, double cost) {
        // Check parameters
        if (output == null) {
            throw new IllegalArgumentException("One or more parameter is null. See the concerned method.");
        }

        this.output = output;
        this.cost = cost;
    }

    /**
     * To string string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "Result{" + "output=" + this.output +
                ", cost=" + this.cost +
                '}';
    }

    /**
     * Gets cost.
     *
     * @return the cost
     */
    public Double getCost() {
        return this.cost;
    }

    /**
     * Gets output.
     *
     * @return the output
     */
    public Vector getOutput() {
        return this.output;
    }
}
