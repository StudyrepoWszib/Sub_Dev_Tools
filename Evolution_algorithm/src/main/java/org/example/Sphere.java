package org.example;


public class Sphere implements Form{
    private double radius;

    @Override
    public double calculateVolume() {
        return (double) 3 /4*Math.PI*radius*radius;
    }

    public Sphere(double radius) {
        this.radius = radius;
    }
}
