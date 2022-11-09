package lineareqsolve.tridiagonal;

public class TridiagonalMatrix {
    public double[] underDiagonal;
    public double[] diagonal;
    public double[] upDiagonal;
    public double[] rightCoef;

    public TridiagonalMatrix(int size) {
        diagonal = new double[size];
        rightCoef = new double[size];
        underDiagonal = new double[size - 1];
        upDiagonal = new double[size - 1];
    }

    public TridiagonalMatrix(double[] underDiagonal, double[] diagonal, double[] upDiagonal, double[] rightCoef) {
        this.underDiagonal = underDiagonal;
        this.diagonal = diagonal;
        this.upDiagonal = upDiagonal;
        this.rightCoef = rightCoef;
    }

    // without rightCoef
    public double[][] toMatrixArr() {
        int size = diagonal.length;
        double[][] matrixArr = new double[size][size];
        for (int i = 0; i < size; i++) {
            matrixArr[i][i] = diagonal[i];
            if (i < size - 1) {
                matrixArr[i + 1][i] = underDiagonal[i];
                matrixArr[i][i + 1] = upDiagonal[i];
            }
        }
        return matrixArr;
    }
}
