package testingFunctions;

import org.eric.neldermeadmethod.Minimizable;

import java.util.List;

public class DeJong implements Minimizable {
    private static final DeJong instance = new DeJong();

    private DeJong() {}

    public static DeJong getInstance() {
        return instance;
    }

    @Override
    public double calculate(List<Double> arguments) {
        double value = 0.0;

        for (double argument : arguments) {
            value += Math.pow(argument, 2);
        }

        return value;
    }
}
