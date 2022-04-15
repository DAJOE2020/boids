package com.boids;
import java.lang.Math;

public class Boid {

	private final double TWO_PI = 6.2831;

	public double x, y;
	public double angle;
	public double speed;

	public Boid(double x_in, double y_in, double a, double s) {
		x = x_in;
		y = y_in;
		angle = a;
		speed = s;
	}

	public void move() {
		x += Math.cos(angle)*speed;
		y += Math.sin(angle)*speed;
	}

	public boolean equals(Boid b) {
		return (x == b.x && y == b.y && angle == b.angle && speed == b.speed);
	}

	public void turn(double da) {
		angle += da;
	}
}
