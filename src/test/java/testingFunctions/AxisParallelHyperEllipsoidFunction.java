package testingFunctions;

import org.eric.neldermeadmethod.Minimizable;

import java.util.List;

public class AxisParallelHyperEllipsoidFunction implements Minimizable {
    private static final AxisParallelHyperEllipsoidFunction instance = new AxisParallelHyperEllipsoidFunction();

    private AxisParallelHyperEllipsoidFunction() {}

    public static AxisParallelHyperEllipsoidFunction getInstance() {
        return instance;
    }

    @Override
    public double calculate(List<Double> arguments) {
        double value = 0.0;

        for (int i = 0; i < arguments.size(); i++) {
            value += (i+1) * Math.pow(arguments.get(i), 2);
        }

        return value;
    }
}
