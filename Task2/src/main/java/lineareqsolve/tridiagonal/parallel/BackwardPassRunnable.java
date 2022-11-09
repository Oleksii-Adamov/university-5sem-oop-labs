package lineareqsolve.tridiagonal.parallel;

import java.util.concurrent.locks.Lock;

public class BackwardPassRunnable implements Runnable {
    private double[][] sparseMatrix;
    private int blockNum;

    private int blockSize;

    private Lock[] locks;

    public BackwardPassRunnable(double[][] sparseMatrix, int blockNum, int blockSize, Lock[] locks) {
        this.sparseMatrix = sparseMatrix;
        this.blockNum = blockNum;
        this.blockSize = blockSize;
        this.locks = locks;
    }

    @Override
    public void run() {
        int offset = blockNum * blockSize;
        // backward pass
        for (int i = offset + blockSize - 3; i >= offset - 1; i--) {
            if (i >= 0) {
                System.out.println("BlockNum = " + blockNum + " i = " + i);
                if (i == offset + blockSize - 3) {
                    locks[blockNum].lock();
                    System.out.println(blockNum + " locked self");
                }
                if (i == offset - 1) {
                    locks[blockNum - 1].lock();
                    System.out.println(blockNum + " locked previous");
                }

                double multiplier = sparseMatrix[i][2] / sparseMatrix[i + 1][1];

                if (i + 1 == offset) {
                    System.out.println("special");
                    sparseMatrix[i][1] -= sparseMatrix[i + 1][0] * multiplier;
                    //sparseMatrix[i][2] = (-1) * sparseMatrix[i + 1][1] * multiplier;
                }
                else {
                    sparseMatrix[i][0] -= sparseMatrix[i + 1][0] * multiplier;
                    //sparseMatrix[i][2] = (-1) * sparseMatrix[i + 1][2] * multiplier;
                }
                sparseMatrix[i][2] = (-1) * sparseMatrix[i + 1][2] * multiplier;
                sparseMatrix[i][3] -= sparseMatrix[i + 1][3] * multiplier;

                if (i == offset + blockSize - 3) {
                    locks[blockNum].unlock();
                }
                if (i == offset - 1) {
                    locks[blockNum - 1].unlock();
                }
            }
        }
//        for (int i = offset + blockSize - 1; i >= offset; i--) {
//            if (i - 1 >= 0) {
//                double multiplier = sparseMatrix[i-1][2] / sparseMatrix[i][1];
//                sparseMatrix[i-1][0] -= sparseMatrix[i][0] * multiplier;
//                sparseMatrix[i-1][2] = (-1) * sparseMatrix[i][2] * multiplier;
//                sparseMatrix[i-1][3] -= sparseMatrix[i][3] * multiplier;
//            }
//        }
    }
}
