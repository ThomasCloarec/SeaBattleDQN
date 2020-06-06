package battle.game.players.auto.neuralnet;

import battle.game.players.auto.neuralnet.math.Matrix;
import battle.game.players.auto.neuralnet.math.Vector;
import battle.game.players.auto.neuralnet.optimizer.Optimizer;

import java.io.Serializable;

/**
 * A single layer in the network.
 * Contains the weights and biases coming into this layer.
 */
public class Layer implements Serializable {
    /**
     * The Activation.
     */
    private final Activation activation;
    /**
     * The Size.
     */
    private final int size;
    /**
     * The Bias.
     */
    private Vector bias;
    /**
     * The Delta bias.
     */
    private transient Vector deltaBias;
    /**
     * The Delta bias added.
     */
    private transient int deltaBiasAdded;
    /**
     * The Delta weights.
     */
    // Not yet realized changes to the weights and biases ("observed things not yet learned")
    private transient Matrix deltaWeights;
    /**
     * The Delta weights added.
     */
    private transient int deltaWeightsAdded;
    /**
     * The Optimizer.
     */
    private Optimizer optimizer;
    /**
     * The Out.
     */
    private Vector out;
    /**
     * The Preceding layer.
     */
    private Layer precedingLayer;
    /**
     * The Weights.
     */
    private Matrix weights;

    /**
     * Instantiates a new Layer.
     *
     * @param size       the size
     * @param activation the activation
     */
    public Layer(int size, Activation activation) {
        this(size, activation, 0);
    }

    /**
     * Instantiates a new Layer.
     *
     * @param size        the size
     * @param activation  the activation
     * @param initialBias the initial bias
     */
    public Layer(int size, Activation activation, double initialBias) {
        this.size = size;
        this.bias = new Vector(size).map(x -> initialBias);
        this.deltaBias = new Vector(size);
        this.activation = activation;
    }

    /**
     * Instantiates a new Layer.
     *
     * @param size       the size
     * @param activation the activation
     * @param bias       the bias
     */
    public Layer(int size, Activation activation, Vector bias) {
        this.size = size;
        this.bias = bias;
        this.deltaBias = new Vector(size);
        this.activation = activation;
    }

    /**
     * Size int.
     *
     * @return the int
     */
    public int size() {
        return this.size;
    }

    /**
     * Feed the in-vector, i, through this layer.
     * Stores a copy of the out vector.
     *
     * @param i The input vector
     * @return The out vector o (i.e. the result of o = iW + b)
     */
    public Vector evaluate(Vector i) {
        if (!this.hasPrecedingLayer()) {
            this.out = i;    // No calculation i input layer, just store data
        } else {
            this.out = this.activation.fn(i.mul(this.weights).add(this.bias));
        }
        return this.out;
    }

    /**
     * Has preceding layer boolean.
     *
     * @return the boolean
     */
    public boolean hasPrecedingLayer() {
        return this.precedingLayer != null;
    }

    /**
     * Add upcoming changes to the Weights and Biases.
     * This does not mean that the network is updated.
     *
     * @param dW the d w
     * @param dB the d b
     */
    public synchronized void addDeltaWeightsAndBiases(Matrix dW, Vector dB) {
        this.deltaWeights.add(dW);
        this.deltaWeightsAdded++;
        this.deltaBias = this.deltaBias.add(dB);
        this.deltaBiasAdded++;
    }

    /**
     * Takes an average of all added Weights and Biases and tell the
     * optimizer to apply them to the current weights and biases.
     *
     * Also applies L2 regularization on the weights if used.
     */
    public synchronized void updateWeightsAndBias() {
        if (this.deltaWeightsAdded > 0) {
            Matrix average_dW = this.deltaWeights.mul(1.0 / this.deltaWeightsAdded);
            this.optimizer.updateWeights(this.weights, average_dW);
            this.deltaWeights.map(a -> 0);   // Clear
            this.deltaWeightsAdded = 0;
        }

        if (this.deltaBiasAdded > 0) {
            Vector average_bias = this.deltaBias.mul(1.0 / this.deltaBiasAdded);
            this.bias = this.optimizer.updateBias(this.bias, average_bias);
            this.deltaBias = this.deltaBias.map(a -> 0);  // Clear
            this.deltaBiasAdded = 0;
        }
    }

    /**
     * Sets optimizer.
     *
     * @param optimizer the optimizer
     */
    public void setOptimizer(Optimizer optimizer) {
        this.optimizer = optimizer;
    }

    /**
     * Gets activation.
     *
     * @return the activation
     */
    public Activation getActivation() {
        return this.activation;
    }

    /**
     * Gets bias.
     *
     * @return the bias
     */
    public Vector getBias() {
        return this.bias;
    }

    /**
     * Gets out.
     *
     * @return the out
     */
    public Vector getOut() {
        return this.out;
    }

    /**
     * Gets preceding layer.
     *
     * @return the preceding layer
     */
    public Layer getPrecedingLayer() {
        return this.precedingLayer;
    }

    /**
     * Sets preceding layer.
     *
     * @param precedingLayer the preceding layer
     */
    public void setPrecedingLayer(Layer precedingLayer) {
        this.precedingLayer = precedingLayer;
    }

    /**
     * Gets state.
     *
     * @return the state
     */
    public LayerState getState() {
        return new LayerState(this);
    }

    /**
     * Gets weights.
     *
     * @return the weights
     */
    public Matrix getWeights() {
        return this.weights;
    }

    // ------------------------------------------------------------------

    /**
     * Sets weights.
     *
     * @param weights the weights
     */
    public void setWeights(Matrix weights) {
        this.weights = weights;
        this.deltaWeights = new Matrix(weights.rows(), weights.cols());
    }

    /**
     * The type Layer state.
     */
    public static class LayerState {
        /**
         * The Activation.
         */
        String activation;
        /**
         * The Bias.
         */
        double[] bias;
        /**
         * The Weights.
         */
        double[][] weights;

        /**
         * Instantiates a new Layer state.
         *
         * @param layer the layer
         */
        public LayerState(Layer layer) {
            this.weights = layer.getWeights() != null ? layer.getWeights().getData() : null;
            this.bias = layer.getBias().getData();
            this.activation = layer.activation.getName();
        }

        /**
         * Get bias double [ ].
         *
         * @return the double [ ]
         */
        public double[] getBias() {
            return this.bias;
        }

        /**
         * Get weights double [ ] [ ].
         *
         * @return the double [ ] [ ]
         */
        public double[][] getWeights() {
            return this.weights;
        }
    }
}
