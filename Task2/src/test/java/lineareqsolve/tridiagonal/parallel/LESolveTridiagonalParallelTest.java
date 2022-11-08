package lineareqsolve.tridiagonal.parallel;

import lineareqsolve.tridiagonal.LESolveTridiagonal;
import lineareqsolve.tridiagonal.sequential.LESolveTridiagonalSequential;
import org.junit.jupiter.api.Test;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.cpu.nativecpu.NDArray;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

class LESolveTridiagonalParallelTest {
    private double randomFloatOrigin = -100;
    private double randomFloatBound = 100;

    private int numThreads = 3;

    private int maxSize = 9;

    private int iterationsForSize = 5;

    private double eps = 1e-3;

    @Test
    void solveExample() {
        double[] diagonal = new double[] {1, 3, 2};
        double[] underDiagonal = new double[] {1, 1};
        double[] upDiagonal = new double[] {1, 2};
        double[] rightCoef = new double[] {1, 1, 1};
//        //LESolveTridiagonalParallel solver = new LESolveTridiagonalParallel(numThreads);
//        LESolveTridiagonal solver = new LESolveTridiagonalSequential();
//        double[] algoSolutionArr =  solver.solve(underDiagonal, diagonal, upDiagonal, rightCoef);
//        System.out.println("example solution: " + Arrays.toString(algoSolutionArr));
//        double[] diagonal = new double[] {1, 1, 1, 1, 1, 1};
//        double[] underDiagonal = new double[] {6, 5, 4, 3, 2};
//        double[] upDiagonal = new double[] {2, 3, 4, 5, 6};
//        double[] rightCoef = new double[] {5, 17, 29, 41, 53, 16};
//        double[] diagonal = new double[] {6, 7, 8, 9, 10, 11};
//        double[] underDiagonal = new double[] {2, 2, 2, 2, 2};
//        double[] upDiagonal = new double[] {3, 3, 3, 3, 3};
//        double[] rightCoef = new double[] {12, 25, 40, 57, 76, 76};
        //LESolveTridiagonalParallel solver = new LESolveTridiagonalParallel(numThreads);
        LESolveTridiagonal solver = new LESolveTridiagonalParallel(3);
        double[] algoSolutionArr =  solver.solve(underDiagonal, diagonal, upDiagonal, rightCoef);
        System.out.println("example solution: " + Arrays.toString(algoSolutionArr));
    }

    @Test
    void solveGeneratedDataTest2() {
        for (int size = numThreads * 2; size <= maxSize; size += numThreads) {
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
                //INDArray matrix = new NDArray(matrixArr);
                //INDArray matrix = Nd4j.create(matrixArr);
                double[][] solutionVectorArr = new double[size][1];
                double[] solutionArr = new double[size];
                for (int i = 0; i < size; i++) {
                    solutionVectorArr[i][0] = i + 1;
                    solutionArr[i] = solutionVectorArr[i][0];
                }
                //INDArray solution = Nd4j.create(solutionArr);
                //INDArray soltionVector = new NDArray(solutionVectorArr);
                //INDArray soltionVector = Nd4j.create(solutionVectorArr);
                //INDArray rightCoefVector = matrix.mul(matrix, soltionVector);
//                double[] rightCoef = new double[size];
//                for (int i = 0; i < size; i++) {
//                    rightCoef[i] = rightCoefVector.getDouble(i, 0);
//                }
                double[] rightCoef = ColumnVectorToArr(matrixMult(matrixArr, solutionVectorArr));

                LESolveTridiagonalParallel solver = new LESolveTridiagonalParallel(numThreads);
                double[] algoSolutionArr =  solver.solve(underDiagonal, diagonal, upDiagonal, rightCoef);
//                INDArray algoSolution = Nd4j.create(algoSolutionArr);
//
//                double error = algoSolution.distance2(solution);
                double error = vectorEqNormSq(algoSolutionArr, ColumnVectorToArr(solutionVectorArr));
                System.out.println(Arrays.toString(algoSolutionArr));
                System.out.println(Arrays.toString(ColumnVectorToArr(solutionVectorArr)));
                System.out.println(error);
                assertTrue(error < eps);
            }
        }
    }

    private double[] randomFloatArr(int size) {
        double[] arr = new double[size];
        for (int i = 0; i < size; i++) {
            arr[i] = ThreadLocalRandom.current().nextDouble(randomFloatOrigin, randomFloatBound);
        }
        return arr;
    }

    private double[][] matrixMult(double[][] left, double[][] right) {
        double[][] result = new double[left.length][right[0].length];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[j].length; j++) {
                result[i][j] = 0;
                for (int k = 0; k < right.length; k++) {
                    result[i][j] += left[i][k] * right[k][j];
                }
            }
        }
        return result;
    }

    private double[] ColumnVectorToArr(double[][] vector) {
        double[] result = new double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            result[i] = vector[i][0];
        }
        return result;
    }

    private double vectorEqNormSq(double[] vector1, double[] vector2) {
        double norm = 0;
        for (int i = 0; i < vector1.length; i++) {
            double diff = vector1[i] - vector2[i];
            norm += diff * diff;
        }
        return norm;
    }


//    @Test
//    void solveGeneratedDataTest() {
//        for (int size = numThreads; size <= maxSize; size += numThreads) {
//            for (int it = 0; it < iterationsForSize; it++) {
//                double[] diagonal = randomFloatArr(size);
//                double[] underDiagonal = randomFloatArr(size - 1);
//                double[] upDiagonal = randomFloatArr(size - 1);
//
//                double[][] matrixArr = new double[size][size];
//                for (int i = 0; i < size; i++) {
//                    matrixArr[i][i] = diagonal[i];
//                    if (i < size - 1) {
//                        matrixArr[i + 1][i] = underDiagonal[i];
//                        matrixArr[i][i + 1] = upDiagonal[i];
//                    }
//                }
//                //INDArray matrix = new NDArray(matrixArr);
//                //INDArray matrix = Nd4j.create(matrixArr);
//                double[][] solutionVectorArr = new double[size][1];
//                double[] solutionArr = new double[size];
//                for (int i = 0; i < size; i++) {
//                    solutionVectorArr[i][0] = i + 1;
//                    solutionArr[i] = solutionVectorArr[i][0];
//                }
//                //INDArray solution = Nd4j.create(solutionArr);
//                //INDArray soltionVector = new NDArray(solutionVectorArr);
//                //INDArray soltionVector = Nd4j.create(solutionVectorArr);
//                //INDArray rightCoefVector = matrix.mul(matrix, soltionVector);
////                double[] rightCoef = new double[size];
////                for (int i = 0; i < size; i++) {
////                    rightCoef[i] = rightCoefVector.getDouble(i, 0);
////                }
//                double[] rightCoef = ColumnVectorToArr(matrixMult(matrixArr, solutionVectorArr));
//
//                LESolveTridiagonalParallel solver = new LESolveTridiagonalParallel(numThreads);
//                double[] algoSolutionArr =  solver.solve(underDiagonal, diagonal, upDiagonal, rightCoef);
////                INDArray algoSolution = Nd4j.create(algoSolutionArr);
////
////                double error = algoSolution.distance2(solution);
//                double error = vectorEqNormSq(algoSolutionArr, ColumnVectorToArr(solutionVectorArr));
//                System.out.println(Arrays.toString(algoSolutionArr));
//                System.out.println(Arrays.toString(ColumnVectorToArr(solutionVectorArr)));
//                System.out.println(error);
//                assertTrue(error < eps);
//            }
//        }
//    }
}