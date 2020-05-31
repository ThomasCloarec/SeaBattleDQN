package battle.game.players.auto.neuralnet;

import battle.game.players.auto.neuralnet.math.Matrix;
import battle.game.players.auto.neuralnet.math.Vector;
import battle.game.players.auto.neuralnet.optimizer.Optimizer;

/**
 * A single layer in the network.
 * Contains the weights and biases coming into this layer.
 */
public class Layer {

    private final int size;
    private final ThreadLocal<Vector> out = new ThreadLocal<>();
    private final Activation activation;
    private Optimizer optimizer;
    private Matrix weights;
    private Vector bias;

    private Layer precedingLayer;

    // Not yet realized changes to the weights and biases ("observed things not yet learned")
    private transient Matrix deltaWeights;
    private transient Vector deltaBias;
    private transient int deltaWeightsAdded;
    private transient int deltaBiasAdded;

    public Layer(int size, Activation activation) {
        this(size, activation, 0);
    }

    public Layer(int size, Activation activation, double initialBias) {
        this.size = size;
        this.bias = new Vector(size).map(x -> initialBias);
        this.deltaBias = new Vector(size);
        this.activation = activation;
    }

    public Layer(int size, Activation activation, Vector bias) {
        this.size = size;
        this.bias = bias;
        this.deltaBias = new Vector(size);
        this.activation = activation;
    }

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
            this.out.set(i);    // No calculation i input layer, just store data
        } else {
            this.out.set(this.activation.fn(i.mul(this.weights).add(this.bias)));
        }
        return this.out.get();
    }

    public Vector getOut() {
        return this.out.get();
    }

    public Activation getActivation() {
        return this.activation;
    }

    public void setWeights(Matrix weights) {
        this.weights = weights;
        this.deltaWeights = new Matrix(weights.rows(), weights.cols());
    }

    public void setOptimizer(Optimizer optimizer) {
        this.optimizer = optimizer;
    }

    public Matrix getWeights() {
        return this.weights;
    }

    public Layer getPrecedingLayer() {
        return this.precedingLayer;
    }

    public void setPrecedingLayer(Layer precedingLayer) {
        this.precedingLayer = precedingLayer;
    }

    public boolean hasPrecedingLayer() {
        return this.precedingLayer != null;
    }

    public Vector getBias() {
        return this.bias;
    }

    /**
     * Add upcoming changes to the Weights and Biases.
     * This does not mean that the network is updated.
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


    // ------------------------------------------------------------------


    public LayerState getState() {
        return new LayerState(this);
    }

    public static class LayerState {

        double[][] weights;
        double[] bias;
        String activation;

        public LayerState(Layer layer) {
            this.weights = layer.getWeights() != null ? layer.getWeights().getData() : null;
            this.bias = layer.getBias().getData();
            this.activation = layer.activation.getName();
        }

        public double[][] getWeights() {
            return this.weights;
        }

        public double[] getBias() {
            return this.bias;
        }
    }
}
