package battle.game.players.auto.neuralnet;

import battle.game.players.auto.neuralnet.math.Matrix;
import battle.game.players.auto.neuralnet.math.Vector;
import battle.game.players.auto.neuralnet.optimizer.GradientDescent;
import battle.game.players.auto.neuralnet.optimizer.Optimizer;

import java.util.ArrayList;
import java.util.List;

public final class NeuralNetwork {

    private final CostFunction costFunction;
    private final int networkInputSize;
    private final Optimizer optimizer;

    private final List<Layer> layers = new ArrayList<>();

    /**
     * Creates a neural network given the configuration set in the builder
     *
     * @param nb The config for the neural network
     */
    private NeuralNetwork(Builder nb) {
        this.costFunction = nb.costFunction;
        this.networkInputSize = nb.networkInputSize;
        this.optimizer = nb.optimizer;

        // Adding inputLayer

        Layer inputLayer = new Layer(this.networkInputSize, Activation.Identity);
        this.layers.add(inputLayer);

        Layer precedingLayer = inputLayer;

        for (int i = 0; i < nb.layers.size(); i++) {
            Layer layer = nb.layers.get(i);
            Matrix w = new Matrix(precedingLayer.size(), layer.size());
            nb.initializer.initWeights(w, i);
            layer.setWeights(w);    // Each layer contains the weights between preceding layer and itself
            layer.setOptimizer(this.optimizer.copy());
            layer.setPrecedingLayer(precedingLayer);
            this.layers.add(layer);

            precedingLayer = layer;
        }
    }


    /**
     * Evaluates an input vector, returning the networks output,
     * without cost or learning anything from it.
     */
    public Result evaluate(Vector input) {
        return this.evaluate(input, null);
    }


    /**
     * Evaluates an input vector, returning the networks output.
     * If <code>expected</code> is specified the result will contain
     * a cost and the network will gather some learning from this
     * operation.
     */
    public Result evaluate(Vector input, Vector expected) {
        Vector signal = input;
        for (Layer layer : this.layers)
            signal = layer.evaluate(signal);

        if (expected != null) {
            this.learnFrom(expected);
            double cost = this.costFunction.getTotal(expected, signal);
            return new Result(signal, cost);
        }

        return new Result(signal);
    }


    /**
     * Will gather some learning based on the <code>expected</code> vector
     * and how that differs to the actual output from the network. This
     * difference (or error) is backpropagated through the net. To make
     * it possible to use mini batches the learning is not immediately
     * realized - i.e. <code>learnFrom</code> does not alter any weights.
     * Use <code>updateFromLearning()</code> to do that.
     */
    public void learnFrom(Vector expected) {
        Layer layer = this.getLastLayer();

        // The error is initially the derivative of the cost-function.
        Vector dCdO = this.costFunction.getDerivative(expected, layer.getOut());

        // iterate backwards through the layers
        do {
            Vector dCdI = layer.getActivation().dCdI(layer.getOut(), dCdO);
            Matrix dCdW = dCdI.outerProduct(layer.getPrecedingLayer().getOut());

            // Store the deltas for weights and biases
            layer.addDeltaWeightsAndBiases(dCdW, dCdI);

            // prepare error propagation and store for next iteration
            dCdO = layer.getWeights().multiply(dCdI);

            layer = layer.getPrecedingLayer();
        }
        while (layer.hasPrecedingLayer());     // Stop when we are at input layer
    }


    /**
     * Let all gathered (but not yet realised) learning "sink in".
     * That is: Update the weights and biases based on the deltas
     * collected during evaluation & training.
     */
    public synchronized void updateFromLearning() {
        for (Layer layer : this.layers)
            if (layer.hasPrecedingLayer())         // Skip input layer
                layer.updateWeightsAndBias();

    }

    // --------------------------------------------------------------------


    public List<Layer> getLayers() {
        return this.layers;
    }

    private Layer getLastLayer() {
        return this.layers.get(this.layers.size() - 1);
    }


    // --------------------------------------------------------------------

    /**
     * Simple builder for a NeuralNetwork
     */
    public static class Builder {

        private final List<Layer> layers = new ArrayList<>();
        private final int networkInputSize;

        // defaults:
        private Initializer initializer = new Initializer.Random(-0.5, 0.5);
        private CostFunction costFunction = new CostFunction.MSE();
        private Optimizer optimizer = new GradientDescent(0.005);

        public Builder(int networkInputSize) {
            this.networkInputSize = networkInputSize;
        }

        public Builder initWeights(Initializer initializer) {
            this.initializer = initializer;
            return this;
        }

        public Builder setCostFunction(CostFunction costFunction) {
            this.costFunction = costFunction;
            return this;
        }

        public Builder setOptimizer(Optimizer optimizer) {
            this.optimizer = optimizer;
            return this;
        }

        public Builder addLayer(Layer layer) {
            this.layers.add(layer);
            return this;
        }

        public NeuralNetwork create() {
            return new NeuralNetwork(this);
        }

    }
}

