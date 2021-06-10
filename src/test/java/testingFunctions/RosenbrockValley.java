package testingFunctions;

import org.eric.neldermeadmethod.Minimizable;

import java.util.List;

public class RosenbrockValley implements Minimizable {
    private static final RosenbrockValley instance = new RosenbrockValley();

    private RosenbrockValley() {}

    public static RosenbrockValley getInstance() {
        return instance;
    }

    @Override
    public double calculate(List<Double> arguments) {
        double value = 0;

        for (int i = 1; i < arguments.size(); i++) {
            value += 100 * Math.pow( (arguments.get(i) - Math.pow(arguments.get(i-1), 2) ), 2) + Math.pow((1 - arguments.get(i-1)), 2);
        }

        return value;
    }
}
