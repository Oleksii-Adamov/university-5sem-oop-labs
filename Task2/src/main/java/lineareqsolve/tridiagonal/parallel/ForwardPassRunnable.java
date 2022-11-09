package lineareqsolve.tridiagonal.parallel;

public class ForwardPassRunnable implements Runnable {

    private double[][] sparseMatrix;
    private int blockNum;

    private int blockSize;

    public ForwardPassRunnable(double[][] sparseMatrix, int blockNum, int blockSize) {
        this.sparseMatrix = sparseMatrix;
        this.blockNum = blockNum;
        this.blockSize = blockSize;
    }

    @Override
    public void run() {
        int offset = blockNum * blockSize;
        // forward pass
        for (int i = offset + 1; i < offset + blockSize; i++) {
            double multiplier = sparseMatrix[i][0] / sparseMatrix[i-1][1];
            sparseMatrix[i][0] = (-1) * sparseMatrix[i-1][0] * multiplier;
            sparseMatrix[i][1] -= sparseMatrix[i-1][2] * multiplier;
            sparseMatrix[i][3] -= sparseMatrix[i-1][3] * multiplier;
        }
    }
}
