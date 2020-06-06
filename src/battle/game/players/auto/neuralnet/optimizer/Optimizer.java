package battle.game.players.auto.neuralnet.optimizer;

import battle.game.players.auto.neuralnet.math.Matrix;
import battle.game.players.auto.neuralnet.math.Vector;

import java.io.Serializable;

/**
 * The interface Optimizer.
 */
public interface Optimizer extends Serializable {
    /**
     * Update weights.
     *
     * @param weights the weights
     * @param dCdW    the d cd w
     */
    void updateWeights(Matrix weights, Matrix dCdW);

    /**
     * Update bias vector.
     *
     * @param bias the bias
     * @param dCdB the d cd b
     * @return the vector
     */
    Vector updateBias(Vector bias, Vector dCdB);

    /**
     * Copy optimizer.
     *
     * @return the optimizer
     */
    Optimizer copy();

}
