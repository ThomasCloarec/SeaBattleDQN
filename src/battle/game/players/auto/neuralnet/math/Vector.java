package battle.game.players.auto.neuralnet.math;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.DoubleStream;

/**
 * The type Vector.
 */
public class Vector implements Serializable {
    /**
     * The Data.
     */
    private final double[] data;

    /**
     * Instantiates a new Vector.
     *
     * @param data the data
     */
    public Vector(double... data) {
        this.data = data;
    }

    /**
     * Instantiates a new Vector.
     *
     * @param data the data
     */
    public Vector(int... data) {
        this(Arrays.stream(data).asDoubleStream().toArray());
    }

    /**
     * Instantiates a new Vector.
     *
     * @param size the size
     */
    public Vector(int size) {
        this.data = new double[size];
    }

    /**
     * Dimension int.
     *
     * @return the int
     */
    public int dimension() {
        return this.data.length;
    }

    /**
     * Dot double.
     *
     * @param u the u
     * @return the double
     */
    public double dot(Vector u) {
        this.assertCorrectDimension(u.dimension());

        double sum = 0;
        for (int i = 0; i < this.data.length; i++)
            sum += this.data[i] * u.data[i];

        return sum;
    }

    /**
     * Map vector.
     *
     * @param fn the fn
     * @return the vector
     */
    public Vector map(Function fn) {
        double[] result = new double[this.data.length];
        for (int i = 0; i < this.data.length; i++)
            result[i] = fn.apply(this.data[i]);
        return new Vector(result);
    }

    /**
     * Index of largest element int.
     *
     * @return the int
     */
    public int indexOfLargestElement() {
        int ixOfLargest = 0;
        for (int i = 0; i < this.data.length; i++)
            if (this.data[i] > this.data[ixOfLargest]) ixOfLargest = i;
        return ixOfLargest;
    }

    /**
     * Sub vector.
     *
     * @param u the u
     * @return the vector
     */
    public Vector sub(Vector u) {
        this.assertCorrectDimension(u.dimension());

        double[] result = new double[u.dimension()];

        for (int i = 0; i < this.data.length; i++)
            result[i] = this.data[i] - u.data[i];

        return new Vector(result);
    }

    /**
     * Mul vector.
     *
     * @param scalar the scalar
     * @return the vector
     */
    public Vector mul(double scalar) {
        return this.map(value -> scalar * value);
    }

    /**
     * Outer product matrix.
     *
     * @param u the u
     * @return the matrix
     */
    public Matrix outerProduct(Vector u) {
        double[][] result = new double[u.dimension()][this.dimension()];

        for (int i = 0; i < this.data.length; i++)
            for (int j = 0; j < u.data.length; j++)
                result[j][i] = this.data[i] * u.data[j];

        return new Matrix(result);
    }

    /**
     * Element product vector.
     *
     * @param u the u
     * @return the vector
     */
    public Vector elementProduct(Vector u) {
        this.assertCorrectDimension(u.dimension());

        double[] result = new double[u.dimension()];

        for (int i = 0; i < this.data.length; i++)
            result[i] = this.data[i] * u.data[i];

        return new Vector(result);
    }

    /**
     * Add vector.
     *
     * @param u the u
     * @return the vector
     */
    public Vector add(Vector u) {
        this.assertCorrectDimension(u.dimension());

        double[] result = new double[u.dimension()];

        for (int i = 0; i < this.data.length; i++)
            result[i] = this.data[i] + u.data[i];

        return new Vector(result);
    }

    /**
     * Mul vector.
     *
     * @param matrix the matrix
     * @return the vector
     */
    public Vector mul(Matrix matrix) {
        this.assertCorrectDimension(matrix.rows());

        double[][] mData = matrix.getData();
        double[] result = new double[matrix.cols()];

        for (int col = 0; col < matrix.cols(); col++)
            for (int row = 0; row < matrix.rows(); row++)
                result[col] += mData[row][col] * this.data[row];

        return new Vector(result);
    }

    /**
     * Max double.
     *
     * @return the double
     */
    public double max() {
        return DoubleStream.of(this.data).max().getAsDouble();
    }

    /**
     * Sub vector.
     *
     * @param a the a
     * @return the vector
     */
    public Vector sub(double a) {
        double[] result = new double[this.dimension()];

        for (int i = 0; i < this.data.length; i++)
            result[i] = this.data[i] - a;

        return new Vector(result);
    }

    /**
     * Sum elements double.
     *
     * @return the double
     */
    public double sumElements() {
        return DoubleStream.of(this.data).sum();
    }

    /**
     * Assert correct dimension.
     *
     * @param inpDim the inp dim
     */
    private void assertCorrectDimension(int inpDim) {
        if (this.dimension() != inpDim)
            throw new IllegalArgumentException(String.format("Different dimensions: Input is %d, Vector is %d", inpDim, this.dimension()));
    }

    /**
     * Hash code int.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.data);
    }

    /**
     * Equals boolean.
     *
     * @param o the o
     * @return the boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        Vector vector = (Vector) o;

        return Arrays.equals(this.data, vector.data);
    }

    /**
     * To string string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "Vector{" + "data=" + Arrays.toString(this.data) + '}';
    }

    /**
     * Get data double [ ].
     *
     * @return the double [ ]
     */
    public double[] getData() {
        return this.data;
    }
}
