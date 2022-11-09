package lineareqsolve.tridiagonal.parallel;

import lineareqsolve.tridiagonal.LESolveTridiagonal;
import lineareqsolve.tridiagonal.TridiagonalMatrix;
import lineareqsolve.tridiagonal.sequential.LESolveTridiagonalSequential;

public class LESolveTridiagonalParallel implements LESolveTridiagonal {
    private int size;
    private int numThreads;
    private double[][] sparseMatrix;

    public LESolveTridiagonalParallel() {
        numThreads = 1;
    }

    public LESolveTridiagonalParallel(int numThreads) {
        this.numThreads = numThreads;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    private void joinThreads(Thread[] threads) {
        try {
            for (int i = 0; i < numThreads; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    @Override
    public double[] solve(TridiagonalMatrix tridiagonalMatrix) {
        size = tridiagonalMatrix.diagonal.length;
        assert size % numThreads == 0: "LESolveTridiagonalParallel: size must divide numThreads";

        sparseMatrix = new double[size][4];

        // modifying matrix in blocks within different threads
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                sparseMatrix[i][0] = 0;
            }
            else {
                sparseMatrix[i][0] = tridiagonalMatrix.underDiagonal[i - 1];
            }
            sparseMatrix[i][1] = tridiagonalMatrix.diagonal[i];
            if (i == size - 1) {
                sparseMatrix[i][2] = 0;
            }
            else {
                sparseMatrix[i][2] = tridiagonalMatrix.upDiagonal[i];
            }
            sparseMatrix[i][3] = tridiagonalMatrix.rightCoef[i];
        }

        Thread[] threads = new Thread[numThreads];
        int blockSize = size / numThreads;

        // forward pass
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new ForwardPassRunnable(sparseMatrix, i, blockSize));
            threads[i].start();
        }
        joinThreads(threads);

        // backward pass
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new BackwardPassRunnable(sparseMatrix, i, blockSize));
            threads[i].start();
        }
        joinThreads(threads);


        // solve sequentially tridiagonal system with numThreads equations
        LESolveTridiagonalSequential sequentialSolver = new LESolveTridiagonalSequential();
        TridiagonalMatrix seqTridiagonalMatrix = new TridiagonalMatrix(numThreads);
        for (int i = 0; i < numThreads; i++) {
            int pos = i * blockSize + blockSize - 1;
            seqTridiagonalMatrix.diagonal[i] = sparseMatrix[pos][1];
            if (i != 0) {
                seqTridiagonalMatrix.underDiagonal[i - 1] = sparseMatrix[pos][0];
            }
            if (i != numThreads - 1) {
                seqTridiagonalMatrix.upDiagonal[i] = sparseMatrix[pos][2];
            }
            seqTridiagonalMatrix.rightCoef[i] = sparseMatrix[pos][3];
        }
        double[] seqSolution = sequentialSolver.solve(seqTridiagonalMatrix);

        // collecting full solution from previous sequential solution within different threads
        double[][] blocksSolutions = new double[numThreads][blockSize];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new SecondPartRunnable(sparseMatrix, i, blockSize, seqSolution[i],
                    i > 0 ? seqSolution[i-1] : 0, blocksSolutions[i]));
            threads[i].start();
        }
        joinThreads(threads);

        double[] solution = new double[size];
        for (int i = 0; i < numThreads; i++) {
            System.arraycopy(blocksSolutions[i], 0, solution, i * blockSize, blockSize);
        }

        return solution;
    }
}
