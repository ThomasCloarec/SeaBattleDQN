package battle.game.players.auto.neuralnet.math;

import java.util.Arrays;
import java.util.stream.DoubleStream;

public class Vector {
    private final double[] data;

    public Vector(double... data) {
        this.data = data;
    }

    public Vector(int... data) {
        this(Arrays.stream(data).asDoubleStream().toArray());
    }

    public Vector(int size) {
        this.data = new double[size];
    }

    public int dimension() {
        return this.data.length;
    }

    public double dot(Vector u) {
        this.assertCorrectDimension(u.dimension());

        double sum = 0;
        for (int i = 0; i < this.data.length; i++)
            sum += this.data[i] * u.data[i];

        return sum;
    }

    public Vector map(Function fn) {
        double[] result = new double[this.data.length];
        for (int i = 0; i < this.data.length; i++)
            result[i] = fn.apply(this.data[i]);
        return new Vector(result);
    }

    public int indexOfLargestElement() {
        int ixOfLargest = 0;
        for (int i = 0; i < this.data.length; i++)
            if (this.data[i] > this.data[ixOfLargest]) ixOfLargest = i;
        return ixOfLargest;
    }

    public Vector sub(Vector u) {
        this.assertCorrectDimension(u.dimension());

        double[] result = new double[u.dimension()];

        for (int i = 0; i < this.data.length; i++)
            result[i] = this.data[i] - u.data[i];

        return new Vector(result);
    }

    public Vector mul(double scalar) {
        return this.map(value -> scalar * value);
    }

    public Matrix outerProduct(Vector u) {
        double[][] result = new double[u.dimension()][this.dimension()];

        for (int i = 0; i < this.data.length; i++)
            for (int j = 0; j < u.data.length; j++)
                result[j][i] = this.data[i] * u.data[j];

        return new Matrix(result);
    }

    public Vector elementProduct(Vector u) {
        this.assertCorrectDimension(u.dimension());

        double[] result = new double[u.dimension()];

        for (int i = 0; i < this.data.length; i++)
            result[i] = this.data[i] * u.data[i];

        return new Vector(result);
    }

    public Vector add(Vector u) {
        this.assertCorrectDimension(u.dimension());

        double[] result = new double[u.dimension()];

        for (int i = 0; i < this.data.length; i++)
            result[i] = this.data[i] + u.data[i];

        return new Vector(result);
    }

    public Vector mul(Matrix matrix) {
        this.assertCorrectDimension(matrix.rows());

        double[][] mData = matrix.getData();
        double[] result = new double[matrix.cols()];

        for (int col = 0; col < matrix.cols(); col++)
            for (int row = 0; row < matrix.rows(); row++)
                result[col] += mData[row][col] * this.data[row];

        return new Vector(result);
    }

    public double max() {
        return DoubleStream.of(this.data).max().getAsDouble();
    }

    public Vector sub(double a) {
        double[] result = new double[this.dimension()];

        for (int i = 0; i < this.data.length; i++)
            result[i] = this.data[i] - a;

        return new Vector(result);
    }

    public double sumElements() {
        return DoubleStream.of(this.data).sum();
    }

    private void assertCorrectDimension(int inpDim) {
        if (this.dimension() != inpDim)
            throw new IllegalArgumentException(String.format("Different dimensions: Input is %d, Vector is %d", inpDim, this.dimension()));
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        Vector vector = (Vector) o;

        return Arrays.equals(this.data, vector.data);
    }

    @Override
    public String toString() {
        return "Vector{" + "data=" + Arrays.toString(this.data) + '}';
    }

    public double[] getData() {
        return this.data;
    }
}
