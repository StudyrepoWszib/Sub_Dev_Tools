package org.example.demo;

public class Sphere implements Form{
    private double radius;

    @Override
    public double calculateVolume() {
        return (double) 3 /4*Math.PI*radius*radius;
    }

    public Sphere(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
