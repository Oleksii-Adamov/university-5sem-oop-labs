package lineareqsolve.tridiagonal.parallel;

import lineareqsolve.tridiagonal.LESolveTridiagonal;
import lineareqsolve.tridiagonal.sequential.LESolveTridiagonalSequential;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    public double[] solve(double[] underDiagonal, double[] diagonal, double[] upDiagonal, double[] rightCoef) {
        size = diagonal.length;
        assert size % numThreads == 0: "LESolveTridiagonalParallel: size must divide numThreads";

        if (size == numThreads) {
            return (new LESolveTridiagonalSequential()).solve(underDiagonal, diagonal, upDiagonal, rightCoef);
        }

        sparseMatrix = new double[size][4];

        // modifying matrix in blocks within different threads
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

        System.out.println("sparse matrix");
        System.out.println(Arrays.deepToString(sparseMatrix));

        Thread[] threads = new Thread[numThreads];
        int blockSize = size / numThreads;
//        int lastBlockSize = blockSize - size % numThreads;
        // forward pass
        for (int i = 0; i < numThreads; i++) {
            //threads[i] = new Thread(new FirstPartRunnable(sparseMatrix, i, blockSize));
            threads[i] = new Thread(new ForwardPassRunnable(sparseMatrix, i, blockSize));
            threads[i].start();
        }

//        threads[numThreads - 1] = new Thread(new FirstPartRunnable(sparseMatrix, numThreads - 1,
//                lastBlockSize));
        joinThreads(threads);

        System.out.println("sparse matrix after forward");
        System.out.println(Arrays.deepToString(sparseMatrix));

        // backward pass
        // initializing locks
        Lock[] locks = new Lock[numThreads];
        for (int i = 0; i < numThreads; i++) {
            locks[i] = new ReentrantLock();
        }
        // staring threads
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new BackwardPassRunnable(sparseMatrix, i, blockSize, locks));
            threads[i].start();
        }

        joinThreads(threads);

        System.out.println("sparse matrix after backward");
        System.out.println(Arrays.deepToString(sparseMatrix));

        // solve sequentially tridiagonal system with numThreads equations
        LESolveTridiagonalSequential sequentialSolver = new LESolveTridiagonalSequential();
        double[] seqDiagonal = new double[numThreads];
        double[] seqUnderDiagonal = new double[numThreads - 1];
        double[] seqUpDiagonal = new double[numThreads - 1];
        double[] seqRightCoef = new double[numThreads];
        for (int i = 0; i < numThreads; i++) {
            int pos = i * blockSize + blockSize - 1;
            seqDiagonal[i] = sparseMatrix[pos][1];
            if (i != 0) {
                seqUnderDiagonal[i - 1] = sparseMatrix[pos][0];
            }
            if (i != numThreads - 1) {
                seqUpDiagonal[i] = sparseMatrix[pos][2];
            }
            seqRightCoef[i] = sparseMatrix[pos][3];
        }
        System.out.println("Seq matrix");
        System.out.println(Arrays.toString(seqUnderDiagonal));
        System.out.println(Arrays.toString(seqDiagonal));
        System.out.println(Arrays.toString(seqUpDiagonal));
        System.out.println(Arrays.toString(seqRightCoef));
        // collecting full solution from previous sequential solution within different threads
        double[] seqSolution = sequentialSolver.solve(seqUnderDiagonal, seqDiagonal, seqUpDiagonal, seqRightCoef);
        double[][] blocksSolutions = new double[numThreads][blockSize];

        System.out.println("Seq solution");
        System.out.println(Arrays.toString(seqSolution));

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new SecondPartRunnable(sparseMatrix, i, blockSize, seqSolution[i],
                    i > 0 ? seqSolution[i-1] : 0, blocksSolutions[i]));
            threads[i].start();
        }
//        threads[numThreads - 1] = new Thread(new SecondPartRunnable(sparseMatrix, numThreads - 1, lastBlockSize,
//                seqSolution[numThreads - 1], blocksSolutions[numThreads - 1]));
        joinThreads(threads);

        double[] solution = new double[size];
        for (int i = 0; i < numThreads; i++) {
            System.arraycopy(blocksSolutions[i], 0, solution, i * blockSize, blockSize);
        }
        //System.arraycopy(blocksSolutions[numThreads - 1], 0, solution, (numThreads - 1) * blockSize, blockSize);

        return solution;
    }
}
