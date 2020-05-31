package battle.game.players.auto.neuralnet.math;


import java.util.Arrays;

/**
 * Careful: not immutable. Most matrix operations are made on same object.
 */
public class Matrix {

    private final double[][] data;
    private final int rows;
    private final int cols;

    public Matrix(double[][] data) {
        this.data = data;
        this.rows = data.length;
        this.cols = data[0].length;
    }

    public Matrix(int rows, int cols) {
        this(new double[rows][cols]);
    }

    public Vector multiply(Vector v) {
        double[] out = new double[this.rows];
        for (int y = 0; y < this.rows; y++)
            out[y] = new Vector(this.data[y]).dot(v);

        return new Vector(out);
    }

    public Matrix map(Function fn) {
        for (int y = 0; y < this.rows; y++)
            for (int x = 0; x < this.cols; x++)
                this.data[y][x] = fn.apply(this.data[y][x]);

        return this;
    }

    public int rows() {
        return this.rows;
    }

    public int cols() {
        return this.cols;
    }

    public Matrix mul(double scalar) {
        return this.map(value -> scalar * value);
    }

    public Matrix copy() {
        Matrix matrix = new Matrix(this.rows, this.cols);
        for (int y = 0; y < this.rows; y++)
            if (this.cols >= 0) System.arraycopy(this.data[y], 0, matrix.data[y], 0, this.cols);

        return matrix;
    }

    public double[][] getData() {
        return this.data;
    }

    public Matrix add(Matrix other) {
        this.assertCorrectDimension(other);

        for (int y = 0; y < this.rows; y++)
            for (int x = 0; x < this.cols; x++)
                this.data[y][x] += other.data[y][x];

        return this;
    }

    public Matrix sub(Matrix other) {
        this.assertCorrectDimension(other);

        for (int y = 0; y < this.rows; y++)
            for (int x = 0; x < this.cols; x++)
                this.data[y][x] -= other.data[y][x];

        return this;
    }

    public double average() {
        return Arrays.stream(this.data).flatMapToDouble(Arrays::stream).average().getAsDouble();
    }

    public double variance() {
        double avg = this.average();
        return Arrays.stream(this.data).flatMapToDouble(Arrays::stream).map(a -> (a - avg) * (a - avg)).average().getAsDouble();
    }

    // -------------------------------------------------------------------------

    private void assertCorrectDimension(Matrix other) {
        if (this.rows != other.rows || this.cols != other.cols)
            throw new IllegalArgumentException(String.format("Matrix of different dim: Input is %d x %d, Vector is %d x %d", this.rows, this.cols, other.rows, other.cols));
    }
}

