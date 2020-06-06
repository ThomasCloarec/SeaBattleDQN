package battle.game.players.auto.neuralnet.optimizer;

import battle.game.players.auto.neuralnet.math.Matrix;
import battle.game.players.auto.neuralnet.math.Vector;

/**
 * Updates Weights and biases based on a constant learning rate - i.e. W -= η * dC/dW
 */
public class GradientDescent implements Optimizer {
    /**
     * The Learning rate.
     */
    private final double learningRate;

    /**
     * Instantiates a new Gradient descent.
     *
     * @param learningRate the learning rate
     */
    public GradientDescent(double learningRate) {
        this.learningRate = learningRate;
    }

    /**
     * Update weights.
     *
     * @param weights the weights
     * @param dCdW    the d cd w
     */
    @Override
    public void updateWeights(Matrix weights, Matrix dCdW) {
        weights.sub(dCdW.mul(this.learningRate));
    }

    /**
     * Update bias vector.
     *
     * @param bias the bias
     * @param dCdB the d cd b
     * @return the vector
     */
    @Override
    public Vector updateBias(Vector bias, Vector dCdB) {
        return bias.sub(dCdB.mul(this.learningRate));
    }

    /**
     * Copy optimizer.
     *
     * @return the optimizer
     */
    @Override
    public Optimizer copy() {
        return this;
    }
}
