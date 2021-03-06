import org.eric.neldermeadmethod.Minimizable;
import org.eric.neldermeadmethod.NelderMead;
import org.junit.jupiter.api.Test;
import testingFunctions.AxisParallelHyperEllipsoidFunction;
import testingFunctions.DeJong;
import testingFunctions.RosenbrockValley;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MinimisationTest {
    private final List<Double> arguments = new ArrayList<>();

    @Test
    void minimiseTest() {
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

        // De Jong function
        Minimizable deJong = DeJong.getInstance();
        List<Double> minimisedArguments = NelderMead.minimise(arguments, deJong);

        boolean condition = true;
        for (double argument : minimisedArguments) {
            condition = (argument < 0.5) && (argument > -0.5);
        }
        // De Jong minimised argument test (precision +/- 0.5)
        assertTrue(condition);

        double minimisedFunctionValue = deJong.calculate(minimisedArguments);
        // De Jong minimised function value test (precision +/- 0.5)
        assertEquals(0.0, minimisedFunctionValue, 0.5);


        // Axis parallel hyper-ellipsoid function
        Minimizable axisParallelHyperEllipsoidFunction = AxisParallelHyperEllipsoidFunction.getInstance();
        minimisedArguments = NelderMead.minimise(arguments, axisParallelHyperEllipsoidFunction);

        for (double argument : minimisedArguments) {
            condition = (argument < 0.5) && (argument > -0.5);
        }
        // Axis parallel hyper-ellipsoid function minimised argument test (precision +/- 0.5)
        assertTrue(condition);

        minimisedFunctionValue = axisParallelHyperEllipsoidFunction.calculate(minimisedArguments);
        // Axis parallel hyper-ellipsoid function minimised argument test (precision +/- 0.5)
        assertEquals(0.0, minimisedFunctionValue, 0.5);


        arguments.clear();
        arguments.add(0.7);
        arguments.add(-0.9);
        arguments.add(-0.5);
        arguments.add(1.3);

        // Rosenbrock's valley function
        Minimizable rosenbrockValley = RosenbrockValley.getInstance();

        minimisedArguments = NelderMead.minimise(arguments, rosenbrockValley);

        minimisedFunctionValue = rosenbrockValley.calculate(minimisedArguments);
        // Rosenbrock's valley minimised function value test (precision +/- 3.0)
        assertEquals(0.0, minimisedFunctionValue, 3.0);
    }
}
