package eric;

import java.util.ArrayList;
import java.util.List;

public class Main {
	// simple example
    public static void main(String[] args) {
    	// arguments for function to minimise
	    List<Double> arguments = new ArrayList<>();

	    arguments.add(2.0);
	    arguments.add(4.0);
	    arguments.add(-3.0);
	    arguments.add(-2.5);
	    arguments.add(3.9);
	    arguments.add(-1.5);
	    arguments.add(-1.5);
	    arguments.add(-2.1);
	    arguments.add(4.9);
	    arguments.add(-5.1);

	    // getting instance of Nelder-Mead class object
	    Minimizable exampleFunctionClass = ExampleFunctionClass.getInstance();

		// Nelder-Mead method output
        List<Double> minimisedArguments = NelderMead.minimise(arguments, exampleFunctionClass);
        System.out.println("Minimised arguments: " + minimisedArguments);
		double minimalFunctionValue = exampleFunctionClass.calculate(minimisedArguments);
		System.out.println("Minimal function value: " + minimalFunctionValue);
    }
}
