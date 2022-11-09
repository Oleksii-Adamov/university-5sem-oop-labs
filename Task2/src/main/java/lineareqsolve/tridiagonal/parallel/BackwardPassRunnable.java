package lineareqsolve.tridiagonal.parallel;

public class BackwardPassRunnable implements Runnable {
    private double[][] sparseMatrix;
    private int blockNum;

    private int blockSize;

    public BackwardPassRunnable(double[][] sparseMatrix, int blockNum, int blockSize) {
        this.sparseMatrix = sparseMatrix;
        this.blockNum = blockNum;
        this.blockSize = blockSize;
    }

    @Override
    public void run() {
        int offset = blockNum * blockSize;
        // backward pass
        for (int i = offset + blockSize - 3; i >= offset - 1; i--) {
            if (i >= 0) {
                double multiplier = sparseMatrix[i][2] / sparseMatrix[i + 1][1];

                if (i + 1 == offset) {
                    sparseMatrix[i][1] -= sparseMatrix[i + 1][0] * multiplier;
                }
                else {
                    sparseMatrix[i][0] -= sparseMatrix[i + 1][0] * multiplier;
                }
                sparseMatrix[i][2] = (-1) * sparseMatrix[i + 1][2] * multiplier;
                sparseMatrix[i][3] -= sparseMatrix[i + 1][3] * multiplier;
            }
        }
    }
}
