package battle.game.players.auto.neuralnet;

import battle.game.players.auto.neuralnet.math.Vector;

public interface CostFunction {

    String getName();
    double getTotal(Vector expected, Vector actual);
    Vector getDerivative(Vector expected, Vector actual);


    // --------------------------------------------------------------

    /**
     * Mean square error, C = 1/n * ∑(y−exp)^2
     */
    class MSE implements CostFunction {
        @Override
        public String getName() {
            return "MSE";
        }

        @Override
        public double getTotal(Vector expected, Vector actual) {
            Vector diff = expected.sub(actual);
            return diff.dot(diff) / actual.dimension();
        }

        @Override
        public Vector getDerivative(Vector expected, Vector actual) {
            return actual.sub(expected).mul(2.0 / actual.dimension());
        }
    }
}
