package battle.game.players.auto.neuralnet;

import battle.game.players.auto.neuralnet.math.Matrix;

public interface Initializer {
    void initWeights(Matrix weights, int layer);

    // -----------------------------------------------------------------
    // --- A few predefined ones ---------------------------------------
    // -----------------------------------------------------------------
    class Random implements Initializer {
        private final double max;
        private final double min;

        public Random(double min, double max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public void initWeights(Matrix weights, int layer) {
            double delta = this.max - this.min;
            weights.map(value -> this.min + Math.random() * delta);
        }
    }
}
