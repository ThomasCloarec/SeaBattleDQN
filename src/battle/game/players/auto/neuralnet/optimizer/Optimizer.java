package battle.game.players.auto.neuralnet.optimizer;

import battle.game.players.auto.neuralnet.math.Matrix;
import battle.game.players.auto.neuralnet.math.Vector;

public interface Optimizer {

    void updateWeights(Matrix weights, Matrix dCdW);
    Vector updateBias(Vector bias, Vector dCdB);
    Optimizer copy();

}
