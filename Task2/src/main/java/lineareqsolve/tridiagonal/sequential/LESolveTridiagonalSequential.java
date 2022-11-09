package lineareqsolve.tridiagonal.sequential;

import lineareqsolve.tridiagonal.LESolveTridiagonal;
import lineareqsolve.tridiagonal.TridiagonalMatrix;

public class LESolveTridiagonalSequential implements LESolveTridiagonal {

    @Override
    public double[] solve(TridiagonalMatrix tridiagonalMatrix) {
        int size = tridiagonalMatrix.diagonal.length;
        double[] solution = new double[size];

        if (size == 1) {
            solution[0] = tridiagonalMatrix.rightCoef[0] / tridiagonalMatrix.diagonal[0];
            return solution;
        }

        // forward pass
        double[] alphas = new double[size - 1];
        double[] betas = new double[size - 1];
        alphas[0] = -tridiagonalMatrix.upDiagonal[0] / tridiagonalMatrix.diagonal[0];
        betas[0] = tridiagonalMatrix.rightCoef[0] / tridiagonalMatrix.diagonal[0];
        for (int i = 1; i < size - 1; i++) {
            double z = tridiagonalMatrix.underDiagonal[i-1] * alphas[i-1] + tridiagonalMatrix.diagonal[i];
            alphas[i] = (-1) * tridiagonalMatrix.upDiagonal[i] / z;
            betas[i] = (tridiagonalMatrix.rightCoef[i] - tridiagonalMatrix.underDiagonal[i-1] * betas[i-1]) / z;
        }
        // backward pass
        solution[size - 1] = (tridiagonalMatrix.rightCoef[size - 1] - tridiagonalMatrix.underDiagonal[size - 2] *
                betas[size - 2]) / (tridiagonalMatrix.underDiagonal[size - 2] * alphas[size - 2] +
                tridiagonalMatrix.diagonal[size - 1]);
        for (int i = size - 2; i >= 0; i--) {
            solution[i] = alphas[i] * solution[i+1] + betas[i];
        }
        return solution;
    }
}
