package lineareqsolve.tridiagonal.parallel;

import lineareqsolve.tridiagonal.LESolveTridiagonal;

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

    private void joinThreads(Thread[] threads) throws InterruptedException {
        for (int i = 0; i < numThreads; i++) {
            threads[i].join();
        }
    }

    @Override
    public double[] solve(double[] underDiagonal, double[] diagonal, double[] upDiagonal, double[] rightCoef) {
        size = diagonal.length;
        sparseMatrix = new double[size][4];
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                sparseMatrix[i][0] = 0;
            }
            else {
                sparseMatrix[i][0] = underDiagonal[i - 1];
            }
            sparseMatrix[i][1] = diagonal[i];
            if (i == size - 1) {
                sparseMatrix[i][2] = 0;
            }
            else {
                sparseMatrix[i][2] = upDiagonal[i];
            }
            sparseMatrix[i][3] = rightCoef[i];
        }
        Thread[] threads = new Thread[numThreads];
        int blocksize = size / numThreads;
        for (int i = 0; i < numThreads - 1; i++) {
            threads[i] = new Thread(new FirstPartRunnable(sparseMatrix, i, blocksize));
        }
        threads[numThreads - 1] = new Thread(new FirstPartRunnable(sparseMatrix, numThreads - 1,
                blocksize - size % numThreads));
        try {
            joinThreads(threads);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        return new double[0];
    }
}
