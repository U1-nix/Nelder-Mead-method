package org.eric.neldermeadmethod;

import java.util.ArrayList;
import java.util.List;

public class Vertex{
    // mutable
    private final List<Double> coordinates;
    // primitive so immutable
    private double functionValue;

    public Vertex(List<Double> coordinates, double functionValue) {
        this.coordinates = coordinates;
        this.functionValue = functionValue;
    }

    // Copy constructor
    public Vertex(Vertex vertex) {
        this.coordinates = new ArrayList<>(vertex.getCoordinates());
        this.functionValue = vertex.getFunctionValue();
    }

    public double getFunctionValue() {
        return functionValue;
    }


    public void setFunctionValue(double functionValue) {
        this.functionValue = functionValue;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }
}
