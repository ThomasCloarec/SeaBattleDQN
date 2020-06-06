package battle.game.players.auto.neuralnet;

import battle.game.players.auto.neuralnet.math.Vector;

/**
 * The interface Cost function.
 */
public interface CostFunction {
    /**
     * Gets name.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets total.
     *
     * @param expected the expected
     * @param actual   the actual
     * @return the total
     */
    double getTotal(Vector expected, Vector actual);

    /**
     * Gets derivative.
     *
     * @param expected the expected
     * @param actual   the actual
     * @return the derivative
     */
    Vector getDerivative(Vector expected, Vector actual);

    /**
     * Mean square error, C = 1/n * ∑(y−exp)^2
     */
    class MSE implements CostFunction {
        /**
         * Gets name.
         *
         * @return the name
         */
        @Override
        public String getName() {
            return "MSE";
        }

        /**
         * Gets total.
         *
         * @param expected the expected
         * @param actual   the actual
         * @return the total
         */
        @Override
        public double getTotal(Vector expected, Vector actual) {
            Vector diff = expected.sub(actual);
            return diff.dot(diff) / actual.dimension();
        }

        /**
         * Gets derivative.
         *
         * @param expected the expected
         * @param actual   the actual
         * @return the derivative
         */
        @Override
        public Vector getDerivative(Vector expected, Vector actual) {
            return actual.sub(expected).mul(2.0 / actual.dimension());
        }
    }
}
