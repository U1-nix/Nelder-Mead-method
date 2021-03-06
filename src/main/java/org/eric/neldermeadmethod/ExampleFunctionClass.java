package org.eric.neldermeadmethod;

import java.util.List;

public class ExampleFunctionClass implements Minimizable {
    private static final ExampleFunctionClass instance = new ExampleFunctionClass();

    private ExampleFunctionClass() {}

    public static ExampleFunctionClass getInstance() {
        return instance;
    }

    // example function
    @Override
    public double calculate(List<Double> arguments) {
        double value = 0;

        for (double argument : arguments) {
            value += Math.pow(argument, 2);
        }

        return value;
    }
}
