package battle.game.players.auto.neuralnet;

import battle.game.players.auto.neuralnet.math.Matrix;

/**
 * The interface Initializer.
 */
public interface Initializer {
    /**
     * Init weights.
     *
     * @param weights the weights        // Check parameters
     * @param layer   the layer
     */
    void initWeights(Matrix weights, int layer);

    /**
     * The type Random.
     */
    class Random implements Initializer {
        /**
         * The Max.
         */
        private final double max;
        /**
         * The Min.
         */
        private final double min;

        /**
         * Instantiates a new Random.
         *
         * @param min the min
         * @param max the max
         */
        public Random(double min, double max) {
            this.min = min;
            this.max = max;
        }

        /**
         * Init weights.
         *
         * @param weights the weights
         * @param layer   the layer
         */
        @Override
        public void initWeights(Matrix weights, int layer) {
            double delta = this.max - this.min;
            weights.map(value -> this.min + Math.random() * delta);
        }
    }
}
