package lineareqsolve.tridiagonal.sequential;

import lineareqsolve.tridiagonal.LESolveTridiagonal;

public class LESolveTridiagonalSequential implements LESolveTridiagonal {

    @Override
    public double[] solve(double[] underDiagonal, double[] diagonal, double[] upDiagonal, double[] rightCoef) {
        int size = diagonal.length;
        double[] solution = new double[size];
        // forward pass
        double[] alphas = new double[size - 1];
        double[] betas = new double[size - 1];
        alphas[0] = -upDiagonal[0] / diagonal[0];
        betas[0] = rightCoef[0] / diagonal[0];
        for (int i = 1; i < size - 1; i++) {
            double z = underDiagonal[i-1] * alphas[i-1] + diagonal[i];
            alphas[i] = (-1) * upDiagonal[i] / z;
            betas[i] = (rightCoef[i] - underDiagonal[i-1] * betas[i-1]) / z;
        }
        // backward pass
        solution[size - 1] = (rightCoef[size - 1] - underDiagonal[size - 2] * betas[size - 2]) /
                (underDiagonal[size - 2] * alphas[size - 2] + diagonal[size - 1]);
        for (int i = size - 2; i >= 0; i--) {
            solution[i] = alphas[i] * solution[i+1] + betas[i];
        }
        return solution;
    }
}
