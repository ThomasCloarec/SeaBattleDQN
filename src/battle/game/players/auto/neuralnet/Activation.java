package battle.game.players.auto.neuralnet;

import battle.game.players.auto.neuralnet.math.Function;
import battle.game.players.auto.neuralnet.math.Vector;

public class Activation {

    private final String name;
    private Function fn;
    private Function dFn;

    public Activation(String name) {
        this.name = name;
    }

    public Activation(String name, Function fn, Function dFn) {
        this.name = name;
        this.fn = fn;
        this.dFn = dFn;
    }

    // For most activation function it suffice to map each separate element. 
    // I.e. they depend only on the single component in the vector.
    public Vector fn(Vector in) {
        return in.map(this.fn);
    }

    public Vector dFn(Vector out) {
        return out.map(this.dFn);
    }

    // Also when calculating the Error change rate in terms of the input (dCdI)
    // it is just a matter of multiplying, i.e. ∂C/∂I = ∂C/∂O * ∂O/∂I.
    public Vector dCdI(Vector out, Vector dCdO) {
        return dCdO.elementProduct(this.dFn(out));
    }

    public String getName() {
        return this.name;
    }


    // --------------------------------------------------------------------------
    // --- A few predefined ones ------------------------------------------------
    // --------------------------------------------------------------------------
    // The simple properties of most activation functions as stated above makes
    // it easy to create the majority of them by just providing lambdas for
    // fn and the diff dfn.

    public static final Activation Leaky_ReLU = new Activation(
            "Leaky_ReLU",
            x -> x <= 0 ? 0.01 * x : x,         // fn
            x -> x <= 0 ? 0.01 : 1              // dFn
    );

    public static final Activation Identity = new Activation(
            "Identity",
            x -> x,                             // fn
            x -> 1                              // dFn
    );


    // --------------------------------------------------------------------------
    // Softmax needs a little extra love since element output depends on more
    // than one component of the vector. Simple element mapping will not suffice.
    // --------------------------------------------------------------------------
    public static final Activation Softmax = new Activation("Softmax") {
        @Override
        public Vector fn(Vector in) {
            double[] data = in.getData();
            double sum = 0;
            double max = in.max();    // Trick: translate the input by largest element to avoid overflow.
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

}
