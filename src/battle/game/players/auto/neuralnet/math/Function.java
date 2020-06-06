package battle.game.players.auto.neuralnet.math;

import java.io.Serializable;

/**
 * The interface Function.
 */
@FunctionalInterface
public interface Function extends Serializable {
    /**
     * Apply double.
     *
     * @param value the value
     * @return the double
     */
    double apply(double value);
}
