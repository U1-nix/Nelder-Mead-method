package org.eric.neldermeadmethod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class NelderMead {
    private static final double alpha = 1.0; // default = 1
    private static final double beta = 0.5; // default = 0.5
    private static final double gamma = 2.0; // default = 2
    private static final double epsilon = 0.1; // default = 0.1

    private static final Logger loggerNelderMead = LogManager.getLogger("Nelder-Mead method");
    private static final Logger loggerCenterOfGravity = LogManager.getLogger("Center of gravity");
    private static final Logger loggerReflectedVertex = LogManager.getLogger("Reflected vertex");
    private static final Logger loggerStrainedVertex = LogManager.getLogger("Strained vertex");
    private static final Logger loggerCompressedVertex = LogManager.getLogger("Compressed vertex");

    public static List<Double> minimise(List<Double> arguments, Minimizable f) {
        loggerNelderMead.info("Beginning Nelder-Mead method");

        Vertex p0 = new Vertex(arguments, 0.0);

        // create Vertexes according to p0
        List<Vertex> startingValues = new ArrayList<>(initialize(p0));

        // А - find function values
        for (Vertex e : startingValues) {
            e.setFunctionValue(f.calculate(e.getCoordinates()));
        }

        // iterator counter
        int it = 0;
        while (true) {
            it++;

            // Б - sort by function value; fh > fg > fl
            sort(startingValues);
            int maxVertex = 0;
            int secondMaxVertex = 1;
            int minVertex = startingValues.size() - 1;

            // B - calculate center of gravity; centerOfGravity; f0
            Vertex centerOfGravity = initializeCenterOfGravity(startingValues, startingValues.get(maxVertex).getCoordinates(), f);
            // Г - calculate reflected vertex; reflectedVertex; fr
            Vertex reflectedApex = initializeReflectedVertex(centerOfGravity.getCoordinates(), startingValues.get(maxVertex).getCoordinates(), f);

            // Д - compare fr and fl
            if (reflectedApex.getFunctionValue() < startingValues.get(minVertex).getFunctionValue()) {
                // fr < fl

                // Д.1) - calculate strained vertex; strainedVertex; fe
                Vertex strainedApex = initializeStrainedVertex(reflectedApex.getCoordinates(), centerOfGravity.getCoordinates(), f);

                // compare fe and fl
                if (strainedApex.getFunctionValue() < startingValues.get(minVertex).getFunctionValue()) {
                    // fe < fl

                    // a) - replace xh with xe
                    startingValues.set(maxVertex, strainedApex.clone());

                    if (checkPrecision(startingValues)) {
                        // stop
                        break;
                    }
                    // else --> В
                } else {
                    // fe >= fl

                    // б) - replace xh with xr
                    startingValues.set(maxVertex, reflectedApex.clone());

                    if (checkPrecision(startingValues)) {
                        // stop
                        break;
                    }
                    // else --> B
                }
            } else {
                // fr > fl

                // compare fr and fg
                if (reflectedApex.getFunctionValue() <= startingValues.get(secondMaxVertex).getFunctionValue()) {
                    // fr <= fg

                    // Д.2) - replace xh with xr
                    startingValues.set(maxVertex, reflectedApex.clone());

                    if (checkPrecision(startingValues)) {
                        // stop
                        break;
                    }
                    // else --> B
                } else {
                    // E - compare fr and fh
                    if (reflectedApex.getFunctionValue() < startingValues.get(maxVertex).getFunctionValue()) {
                        // fr < fh

                        // E.1) - replace xh with xr
                        startingValues.set(maxVertex, reflectedApex.clone());
                    }

                    // fr >= fh
                    // E.2) - calculate compressed vertex; compressedVertex; fc
                    Vertex compressedApex = initializeCompressedVertex(startingValues.get(maxVertex).getCoordinates(), centerOfGravity.getCoordinates(), f);

                    // Ж - compare fc and fh
                    if (compressedApex.getFunctionValue() <= startingValues.get(maxVertex).getFunctionValue()) {
                        // fc <= fh

                        // Ж.1) - replace xh with xc;
                        startingValues.set(maxVertex, compressedApex.clone());

                        if (checkPrecision(startingValues)) {
                            // stop
                            break;
                        }
                        // else --> Б
                    } else {
                        // fc > fh
                        // З - double downsizing
                        for (int i = 0; i < startingValues.size(); i++) {
                            if (i != minVertex) {
                                startingValues.set(i, downsize(startingValues.get(i), startingValues.get(minVertex)).clone());
                                startingValues.get(i).setFunctionValue(f.calculate(startingValues.get(i).getCoordinates()));
                            }
                        }

                        startingValues.get(minVertex).setFunctionValue(f.calculate(startingValues.get(minVertex).getCoordinates()));

                        if (checkPrecision(startingValues)) {
                            // stop
                            break;
                        }
                        // else --> В
                    }
                }
            }
        }

        loggerNelderMead.info("End of Nelder-Mead method");
        loggerNelderMead.info("Iterations: " + it);

        // returning minimal of all minimised vertexes
        return startingValues.get(startingValues.size() - 1).getCoordinates();
    }

    public static void sort(List<Vertex> vertexes) {
        // simple descending bubble sort
        boolean sorted;
        do {
            sorted = true;
            for (int i = 0; i < vertexes.size() - 1; i++) {
                if (vertexes.get(i).getFunctionValue() < vertexes.get(i + 1).getFunctionValue()) {
                    Vertex tmp = vertexes.get(i);
                    vertexes.set(i, vertexes.get(i + 1).clone());
                    vertexes.set(i + 1, tmp.clone());
                    sorted = false;
                }
            }
        } while (!sorted);
    }

    private static List<Vertex> initialize(Vertex p0) {
        List<Vertex> startingValues = new LinkedList<>();
        startingValues.add(p0);
        for (int i = 1; i <= p0.getCoordinates().size(); i++) {
            Vertex p = p0.clone();
            double x = p.getCoordinates().get(i - 1);
            p.getCoordinates().remove(i - 1);
            x++;
            p.getCoordinates().add(i - 1, x);
            startingValues.add(p);
        }
        return startingValues;
    }

    private static Vertex initializeCenterOfGravity(List<Vertex> startingValues, List<Double> maxVertexCoordinates, Minimizable f) {
        Vertex centerOfGravity = new Vertex(findCenterOfGravity(startingValues, maxVertexCoordinates),
                0.0);
        centerOfGravity.setFunctionValue(f.calculate(centerOfGravity.getCoordinates()));

        loggerCenterOfGravity.info("Coordinates: " + centerOfGravity.getCoordinates());
        loggerCenterOfGravity.debug("Max vertex coordinates: " + maxVertexCoordinates);
        loggerCenterOfGravity.debug("Value: " + centerOfGravity.getFunctionValue());

        return centerOfGravity;
    }

    private static List<Double> findCenterOfGravity(List<Vertex> startingValues, List<Double> maxVertexCoordinates) {
        List<Double> centerOfGravityCoordinates = new ArrayList<>();
        // i - coordinate number
        for (int i = 0; i < startingValues.get(0).getCoordinates().size(); i++) {
            // sum of all i coordinates
            double sum = 0;
            for (Vertex e : startingValues) {
                sum += e.getCoordinates().get(i);
            }
            // subtraction of i coordinate of highest simplex
            sum -= maxVertexCoordinates.get(i);
            centerOfGravityCoordinates.add(sum / (startingValues.size() - 1));
        }
        return centerOfGravityCoordinates;
    }

    private static Vertex initializeReflectedVertex(List<Double> centerOfGravityCoordinates, List<Double> maxVertexCoordinates, Minimizable f) {
        Vertex reflectedVertex = new Vertex(reflect(centerOfGravityCoordinates, maxVertexCoordinates),
                0.0);
        reflectedVertex.setFunctionValue(f.calculate(reflectedVertex.getCoordinates()));

        loggerReflectedVertex.info("Coordinates: " + reflectedVertex.getCoordinates());
        loggerReflectedVertex.debug("Center of gravity coordinates: " + centerOfGravityCoordinates);
        loggerReflectedVertex.debug("Max vertex coordinates: " + maxVertexCoordinates);
        loggerReflectedVertex.debug("Value: " + reflectedVertex.getFunctionValue());

        return reflectedVertex;
    }

    private static List<Double> reflect(List<Double> centerOfGravityCoordinates, List<Double> maxVertexCoordinates) {
        List<Double> reflectedCoordinates = new ArrayList<>();
        for (int i = 0; i < centerOfGravityCoordinates.size(); i++) {
            double result = (1 + alpha) * centerOfGravityCoordinates.get(i) - alpha * maxVertexCoordinates.get(i);
            reflectedCoordinates.add(result);
        }
        return reflectedCoordinates;
    }

    private static Vertex initializeStrainedVertex(List<Double> reflectedVertexCoordinates, List<Double> centerOfGravityCoordinates, Minimizable f) {
        Vertex strainedVertex = new Vertex(strain(reflectedVertexCoordinates, centerOfGravityCoordinates), 0.00);
        strainedVertex.setFunctionValue(f.calculate(strainedVertex.getCoordinates()));

        loggerStrainedVertex.info("Coordinates: " + strainedVertex.getCoordinates());
        loggerStrainedVertex.debug("Reflected vertex coordinates: " + reflectedVertexCoordinates);
        loggerStrainedVertex.debug("Center of gravity coordinates: " + centerOfGravityCoordinates);
        loggerStrainedVertex.debug("Value: " + strainedVertex.getFunctionValue());

        return strainedVertex;
    }

    private static List<Double> strain(List<Double> reflectedVertexCoordinates, List<Double> centerOfGravityCoordinates) {
        List<Double> strainedCoordinates = new ArrayList<>();
        for (int i = 0; i < centerOfGravityCoordinates.size(); i++) {
            double result = gamma * reflectedVertexCoordinates.get(i) + (1 - gamma) * centerOfGravityCoordinates.get(i);
            strainedCoordinates.add(result);
        }
        return strainedCoordinates;
    }

    private static Vertex initializeCompressedVertex(List<Double> maxVertexCoordinates, List<Double> centerOfGravityCoordinates, Minimizable f) {
        Vertex compressedVertex = new Vertex(compress(maxVertexCoordinates, centerOfGravityCoordinates), 0.0);
        compressedVertex.setFunctionValue(f.calculate(compressedVertex.getCoordinates()));

        loggerCompressedVertex.info("Coordinates" + compressedVertex.getCoordinates());
        loggerCompressedVertex.debug("Max vertex coordinates: " + maxVertexCoordinates);
        loggerCompressedVertex.debug("Center of gravity coordinates: " + centerOfGravityCoordinates);
        loggerCompressedVertex.debug("Value: " + compressedVertex.getFunctionValue());

        return compressedVertex;
    }

    private static List<Double> compress(List<Double> maxVertexCoordinates, List<Double> centerOfGravityCoordinates) {
        List<Double> compressedCoordinates = new ArrayList<>();
        for (int i = 0; i < maxVertexCoordinates.size(); i++) {
            double result = beta * maxVertexCoordinates.get(i) + (1 - beta) * centerOfGravityCoordinates.get(i);
            compressedCoordinates.add(result);
        }
        return compressedCoordinates;
    }

    private static Vertex downsize(Vertex otherVertex, Vertex minVertex) {
        Vertex result = new Vertex(new ArrayList<>(), 0.0);
        for (int i = 0; i < otherVertex.getCoordinates().size(); i++) {
            double xi = (otherVertex.getCoordinates().get(i) + minVertex.getCoordinates().get(i)) / 2;
            result.getCoordinates().add(xi);
        }
        return result;
    }

    private static boolean checkPrecision(List<Vertex> values) {
        for (int j = 0; j < values.size() - 1; j++) {
            for (int k = j + 1; k < values.size(); k++) {
                double distance = checkTwoVertexes(values.get(j), values.get(k));
                if (distance > epsilon) {
                    return false;
                }
            }
        }
        return true;
    }

    private static double checkTwoVertexes(Vertex firstVertex, Vertex secondVertex) {
        double distance = 0.0;
        for (int i = 0; i < firstVertex.getCoordinates().size(); i++) {
            distance += Math.pow(firstVertex.getCoordinates().get(i) - secondVertex.getCoordinates().get(i), 2);
        }
        return distance;
    }
}
