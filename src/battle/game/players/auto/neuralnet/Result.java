package battle.game.players.auto.neuralnet;

import battle.game.players.auto.neuralnet.math.Vector;

/**
 * The outcome of an evaluation.
 * Will always contain the output data.
 * Might contain the cost.
 */
public class Result {

    private final Vector output;
    private final Double cost;

    public Result(Vector output) {
        this.output = output;
        this.cost = null;
    }

    public Result(Vector output, double cost) {
        this.output = output;
        this.cost = cost;
    }

    public Vector getOutput() {
        return this.output;
    }

    public Double getCost() {
        return this.cost;
    }

    @Override
    public String toString() {
        return "Result{" + "output=" + this.output +
                ", cost=" + this.cost +
                '}';
    }
}
