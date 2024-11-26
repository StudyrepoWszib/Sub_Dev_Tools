package org.example;

public class Cylinder implements Form {
    private double radius;
    private double height;


    @Override
    public double calculateVolume() {
        return Math.PI * radius * radius * height;
    }

    public Cylinder(double radius, double height) {
        this.radius = radius;
        this.height = height;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
