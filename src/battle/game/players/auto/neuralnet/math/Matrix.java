package battle.game.players.auto.neuralnet.math;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Careful: not immutable. Most matrix operations are made on same object.
 */
public class Matrix implements Serializable {
    /**
     * The Cols.
     */
    private final int cols;
    /**
     * The Data.
     */
    private final double[][] data;
    /**
     * The Rows.
     */
    private final int rows;

    /**
     * Instantiates a new Matrix.
     *
     * @param data the data
     */
    public Matrix(double[][] data) {
        this.data = data;
        this.rows = data.length;
        this.cols = data[0].length;
    }

    /**
     * Instantiates a new Matrix.
     *
     * @param rows the rows
     * @param cols the cols
     */
    public Matrix(int rows, int cols) {
        this(new double[rows][cols]);
    }

    /**
     * Multiply vector.
     *
     * @param v the v
     * @return the vector
     */
    public Vector multiply(Vector v) {
        double[] out = new double[this.rows];
        for (int y = 0; y < this.rows; y++)
            out[y] = new Vector(this.data[y]).dot(v);

        return new Vector(out);
    }

    /**
     * Map matrix.
     *
     * @param fn the fn
     * @return the matrix
     */
    public Matrix map(Function fn) {
        for (int y = 0; y < this.rows; y++)
            for (int x = 0; x < this.cols; x++)
                this.data[y][x] = fn.apply(this.data[y][x]);

        return this;
    }

    /**
     * Rows int.
     *
     * @return the int
     */
    public int rows() {
        return this.rows;
    }

    /**
     * Cols int.
     *
     * @return the int
     */
    public int cols() {
        return this.cols;
    }

    /**
     * Mul matrix.
     *
     * @param scalar the scalar
     * @return the matrix
     */
    public Matrix mul(double scalar) {
        return this.map(value -> scalar * value);
    }

    /**
     * Copy matrix.
     *
     * @return the matrix
     */
    public Matrix copy() {
        Matrix matrix = new Matrix(this.rows, this.cols);
        for (int y = 0; y < this.rows; y++)
            if (this.cols >= 0) System.arraycopy(this.data[y], 0, matrix.data[y], 0, this.cols);

        return matrix;
    }

    /**
     * Add matrix.
     *
     * @param other the other
     * @return the matrix
     */
    public Matrix add(Matrix other) {
        this.assertCorrectDimension(other);

        for (int y = 0; y < this.rows; y++)
            for (int x = 0; x < this.cols; x++)
                this.data[y][x] += other.data[y][x];

        return this;
    }

    /**
     * Sub matrix.
     *
     * @param other the other
     * @return the matrix
     */
    public Matrix sub(Matrix other) {
        this.assertCorrectDimension(other);

        for (int y = 0; y < this.rows; y++)
            for (int x = 0; x < this.cols; x++)
                this.data[y][x] -= other.data[y][x];

        return this;
    }

    /**
     * Average double.
     *
     * @return the double
     */
    public double average() {
        return Arrays.stream(this.data).flatMapToDouble(Arrays::stream).average().getAsDouble();
    }

    /**
     * Variance double.
     *
     * @return the double
     */
    public double variance() {
        double avg = this.average();
        return Arrays.stream(this.data).flatMapToDouble(Arrays::stream).map(a -> (a - avg) * (a - avg)).average().getAsDouble();
    }

    /**
     * Assert correct dimension.
     *
     * @param other the other
     */
    private void assertCorrectDimension(Matrix other) {
        if (this.rows != other.rows || this.cols != other.cols)
            throw new IllegalArgumentException(String.format("Matrix of different dim: Input is %d x %d, Vector is %d x %d", this.rows, this.cols, other.rows, other.cols));
    }

    // -------------------------------------------------------------------------

    /**
     * Get data double [ ] [ ].
     *
     * @return the double [ ] [ ]
     */
    public double[][] getData() {
        return this.data;
    }
}

