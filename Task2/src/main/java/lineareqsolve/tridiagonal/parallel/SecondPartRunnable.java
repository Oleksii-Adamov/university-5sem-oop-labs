package lineareqsolve.tridiagonal.parallel;

public class SecondPartRunnable implements Runnable {
    private double[][] sparseMatrix;
    private int blockNum;

    private int blockSize;

    private double[] blockSolutions;

    private double prevBlockSolution;

    public SecondPartRunnable(double[][] sparseMatrix, int blockNum, int blockSize, double lastSolution, double prevBlockSolution, double[] blockSolutions) {
        this.sparseMatrix = sparseMatrix;
        this.blockNum = blockNum;
        this.blockSize = blockSize;
        this.blockSolutions = blockSolutions;
        this.prevBlockSolution = prevBlockSolution;
        blockSolutions[blockSize - 1] = lastSolution;
    }

    @Override
    public void run() {
        int offset = blockNum * blockSize;
        for (int it = blockSize - 2; it >= 0; it--) {
            int i = offset + it;
            blockSolutions[it] = (sparseMatrix[i][3] - sparseMatrix[i][2] * blockSolutions[blockSize - 1] - sparseMatrix[i][0] * prevBlockSolution) / sparseMatrix[i][1];
        }
    }

}
