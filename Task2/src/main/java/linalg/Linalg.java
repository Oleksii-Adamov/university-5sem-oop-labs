package linalg;

import lineareqsolve.tridiagonal.TridiagonalMatrix;

import java.util.concurrent.ThreadLocalRandom;

public class Linalg {

    private Linalg() {

    }
    public static double[] randomDoubleArr(int size, double origin, double bound) {
        double[] arr = new double[size];
        for (int i = 0; i < size; i++) {
            arr[i] = ThreadLocalRandom.current().nextDouble(origin, bound);
        }
        return arr;
    }

    public static double[][] matrixMult(double[][] left, double[][] right) {
        double[][] result = new double[left.length][right[0].length];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] = 0;
                for (int k = 0; k < right.length; k++) {
                    result[i][j] += left[i][k] * right[k][j];
                }
            }
        }
        return result;
    }

    public static double[] ColumnVectorToArr(double[][] vector) {
        double[] result = new double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            result[i] = vector[i][0];
        }
        return result;
    }

    public static double vectorEqNormSq(double[] vector1, double[] vector2) {
        double norm2 = 0;
        for (int i = 0; i < vector1.length; i++) {
            double diff = vector1[i] - vector2[i];
            norm2 += diff * diff;
        }
        return norm2;
    }

    // without RightCoef
    public static TridiagonalMatrix randomDiagonalDominantTridiagonalMatrix(int size, double origin, double bound) {
        TridiagonalMatrix tridiagonalMatrix = new TridiagonalMatrix(randomDoubleArr(size - 1, origin, bound),
                randomDoubleArr(size, origin, bound), randomDoubleArr(size - 1, origin, bound),
                randomDoubleArr(size, origin, bound));

        if (size > 1) {
            // satisfying diagonal dominance
            for (int i = 0; i < size; i++) {
                double otherSum = 0;
                if (i == 0) {
                    otherSum = Math.abs(tridiagonalMatrix.upDiagonal[0]);
                } else if (i == size - 1) {
                    otherSum = Math.abs(tridiagonalMatrix.underDiagonal[i - 1]);
                } else {
                    otherSum = Math.abs(tridiagonalMatrix.upDiagonal[i]) + Math.abs(tridiagonalMatrix.underDiagonal[i - 1]);
                }
                if (otherSum > Math.abs(tridiagonalMatrix.diagonal[i])) {
                    tridiagonalMatrix.diagonal[i] += Math.signum(tridiagonalMatrix.diagonal[i]) *
                            (otherSum - Math.abs(tridiagonalMatrix.diagonal[i])) +
                            ThreadLocalRandom.current().nextDouble(origin, bound);
                }
            }
        }
        return tridiagonalMatrix;
    }

    public static double[][] arrToColumnVector(double[] arr) {
        double[][] columnVector = new double[arr.length][1];
        for (int i = 0; i < arr.length; i++) {
            columnVector[i][0] = arr[i];
        }
        return columnVector;
    }

    public static TridiagonalMatrix randomDiagonalDominantTridiagonalProblem(int size, double origin, double bound, double[] solution) {
        TridiagonalMatrix tridiagonalMatrix = Linalg.randomDiagonalDominantTridiagonalMatrix(size, origin, bound);

        tridiagonalMatrix.rightCoef = Linalg.ColumnVectorToArr(Linalg.matrixMult(tridiagonalMatrix.toMatrixArr(),
                arrToColumnVector(solution)));

        return tridiagonalMatrix;
    }
}
