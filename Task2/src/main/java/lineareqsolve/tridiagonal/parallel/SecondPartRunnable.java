package lineareqsolve.tridiagonal.parallel;

public class SecondPartRunnable implements Runnable {
    private double[][] sparseMatrix;
    private int blockNum;

    private int blockSize;

    private double[] blockSolutions;

    public SecondPartRunnable(double[][] sparseMatrix, int blockNum, int blockSize, double lastSolution) {
        this.sparseMatrix = sparseMatrix;
        this.blockNum = blockNum;
        this.blockSize = blockSize;
        blockSolutions = new double[blockSize];
        blockSolutions[blockSize - 1] = lastSolution;
    }

    @Override
    public void run() {
        int offset = blockNum * blockSize;
        for (int it = blockSize - 2; it >= 0; it--) {
            int i = offset + it;
            blockSolutions[i] = (sparseMatrix[i][3] - sparseMatrix[i][2] * blockSolutions[i+1]) / sparseMatrix[i][1];
        }
    }
}
