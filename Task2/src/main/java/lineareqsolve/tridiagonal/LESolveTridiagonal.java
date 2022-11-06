package lineareqsolve.tridiagonal;

public interface LESolveTridiagonal {
    public double[] solve(double[] underDiagonal, double[] diagonal, double[] upDiagonal, double[] rightCoef);
}
