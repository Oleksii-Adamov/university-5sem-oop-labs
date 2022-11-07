package lineareqsolve.tridiagonal.parallel;

import org.junit.jupiter.api.Test;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.cpu.nativecpu.NDArray;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

class LESolveTridiagonalParallelTest {
    private double randomFloatOrigin = -100;
    private double randomFloatBound = 100;

    private int numThreads = 3;

    private int maxSize = 9;

    private int iterationsForSize = 5;

    @Test
    void solveExample() {
        double[] diagonal = new double[] {1, 3, 2};
        double[] underDiagonal = new double[] {1, 1};
        double[] upDiagonal = new double[] {};
        double[] rightCoef = new double[] {};
    }

    double[] randomFloatArr(int size) {
        double[] arr = new double[size];
        for (int i = 0; i < size; i++) {
            arr[i] = ThreadLocalRandom.current().nextDouble(randomFloatOrigin, randomFloatBound);
        }
        return arr;
    }

    @Test
    void solveGeneratedDataTest() {
        for (int size = numThreads; size <= maxSize; size += numThreads) {
            for (int it = 0; it < iterationsForSize; it++) {
                double[] diagonal = randomFloatArr(size);
                double[] underDiagonal = randomFloatArr(size - 1);
                double[] upDiagonal = randomFloatArr(size - 1);

                double[][] matrixArr = new double[size][size];
                for (int i = 0; i < size; i++) {
                    matrixArr[i][i] = diagonal[i];
                    if (i < size - 1) {
                        matrixArr[i + 1][i] = underDiagonal[i];
                        matrixArr[i][i + 1] = upDiagonal[i];
                    }
                }
                NDArray matrix = new NDArray(matrixArr);

                double[] rightCoef = randomFloatArr(size);

            }
        }
    }
}