package org.eric.neldermeadmethod;

import java.util.ArrayList;
import java.util.List;

public class Vertex implements Cloneable{
    private final List<Double> coordinates;
    private double functionValue;

    public Vertex(List<Double> coordinates, double functionValue) {
        this.coordinates = coordinates;
        this.functionValue = functionValue;
    }

    // TODO: remove cloning
    @Override
    public Vertex clone() {
        List<Double> coordinates = new ArrayList<>(this.getCoordinates());
        return new Vertex(coordinates, this.getFunctionValue());
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
