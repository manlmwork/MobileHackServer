package com.fu.common.util;

/**
 * Created by manlm on 10/8/2016.
 */
public class Trilateration {

    private Trilateration() {
        // Default Constructor
    }

    public static Point translateCoordinateSystem(Point origin, Point point) {
        return new Point(point.getX() - origin.getX(), point.getY() - origin.getY(), point.getZ() - origin.getZ());
    }

    public static Point reverseTranslateCoordinateSystem(Point origin, Point point) {
        return new Point(point.getX() + origin.getX(), point.getY() + origin.getY(), point.getZ() + origin.getZ());
    }

    public static double calcRotationAngle(Point point) {
        return Math.acos(Math.abs(point.getX()) / Math.sqrt(Math.pow(point.getX(), 2) + Math.pow(point.getY(), 2)));
    }

    public static Point rotate(Point point, double angle) {
        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);
        return new Point(point.getX() * cosAngle + point.getY() * sinAngle, point.getY() * cosAngle - point.getX() * sinAngle, point.getZ());
    }

    public static Point calcPoint(Point pointA, Point pointB, Point pointC, double distanceSA, double distanceSB, double distanceSC) {

        Point pointA1 = translateCoordinateSystem(pointA, pointA);
        Point pointB1 = translateCoordinateSystem(pointA, pointB);
        Point pointC1 = translateCoordinateSystem(pointA, pointC);

        double rotationAngle = calcRotationAngle(pointB1);
        Point pointB2 = rotate(pointB1, rotationAngle);
        Point pointC2 = rotate(pointC1, rotationAngle);

        double x = (Math.pow(distanceSA, 2) - Math.pow(distanceSB, 2) + Math.pow(pointB2.getX(), 2)) / (2 * pointB2.getX());
        double y = (Math.pow(distanceSA, 2) - Math.pow(distanceSC, 2) + Math.pow(pointC2.getX(), 2) + Math.pow(pointC2.getY(), 2)) / (2 * pointC2.getY())
                - ((pointC2.getX() / pointC2.getY()) * x);

        double z1 = Math.sqrt(Math.pow(distanceSA, 2) - Math.pow(x, 2) - Math.pow(y, 2));
        double z2 = -z1;

        Point pointZ1 = rotate(new Point(x, y, z1), Math.toRadians(-Math.toDegrees(rotationAngle)));
        Point pointZ2 = rotate(new Point(x, y, z2), Math.toRadians(-Math.toDegrees(rotationAngle)));

        Point pointZ1Final = reverseTranslateCoordinateSystem(pointA, pointZ1);
        Point pointZ2Final = reverseTranslateCoordinateSystem(pointA, pointZ2);

        System.out.println(pointZ1Final.getX() + " " + pointZ1Final.getY() + " " + pointZ1Final.getZ());
        System.out.println(pointZ2Final.getX() + " " + pointZ2Final.getY() + " " + pointZ2Final.getZ());

        return new Point(0, 0, 0);
    }

    public static void main(String[] args) {
        Point pointA = new Point(3, 3, 3);
        Point pointB = new Point(6, 7, 3);
        Point pointC = new Point(4, 8, 3);
        double distanceSA = 5.0;
        double distanceSB = 6.0;
        double distanceSC = 7.0;
        calcPoint(pointA, pointB, pointC, distanceSA, distanceSB, distanceSC);

    }
}
