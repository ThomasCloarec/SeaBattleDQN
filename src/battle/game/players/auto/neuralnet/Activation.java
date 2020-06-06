package battle.game.players.auto.neuralnet;

import battle.game.players.auto.neuralnet.math.Function;
import battle.game.players.auto.neuralnet.math.Vector;

import java.io.Serializable;

/**
 * The type Activation.
 */
public class Activation implements Serializable {
    /**
     * The constant Identity.
     */
    public static final Activation Identity = new Activation(
            "Identity",
            x -> x,                             // fn
            x -> 1                              // dFn
    );
    /**
     * The constant Leaky_ReLU.
     */
    public static final Activation Leaky_ReLU = new Activation(
            "Leaky_ReLU",
            x -> x <= 0 ? 0.01 * x : x,         // fn
            x -> x <= 0 ? 0.01 : 1              // dFn
    );
    /**
     * The constant Softmax.
     */
    public static final Activation Softmax = new Activation("Softmax") {
        @Override
        public Vector fn(Vector in) {
            double[] data = in.getData();
            double sum = 0;
            double max = in.max();
            for (double a : data)
                sum += StrictMath.exp(a - max);

            double finalSum = sum;
            return in.map(a -> StrictMath.exp(a - max) / finalSum);
        }

        @Override
        public Vector dCdI(Vector out, Vector dCdO) {
            double x = out.elementProduct(dCdO).sumElements();
            Vector sub = dCdO.sub(x);
            return out.elementProduct(sub);
        }
    };
    /**
     * The Name.
     */
    private final String name;
    /**
     * The D fn.
     */
    private Function dFn;
    /**
     * The Fn.
     */
    private Function fn;

    /**
     * Instantiates a new Activation.
     *
     * @param name the name
     */
    public Activation(String name) {
        this.name = name;
    }

    /**
     * Instantiates a new Activation.
     *
     * @param name the name
     * @param fn   the fn
     * @param dFn  the d fn
     */
    public Activation(String name, Function fn, Function dFn) {
        this.name = name;
        this.fn = fn;
        this.dFn = dFn;
    }

    /**
     * Fn vector.
     *
     * @param in the in
     * @return the vector
     */
    public Vector fn(Vector in) {
        return in.map(this.fn);
    }

    /**
     * D fn vector.
     *
     * @param out the out
     * @return the vector
     */
    public Vector dFn(Vector out) {
        return out.map(this.dFn);
    }

    /**
     * D cd i vector.
     *
     * @param out  the out
     * @param dCdO the d cd o
     * @return the vector
     */
    public Vector dCdI(Vector out, Vector dCdO) {
        return dCdO.elementProduct(this.dFn(out));
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }
}
