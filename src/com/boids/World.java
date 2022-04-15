package com.boids;

import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;
import java.awt.Color;

public class World {

	private int w_;
	private int h_;
	private int boidSize_;

	private double sightRange_;
	private double sightAngle_;
	private double speedLimit_;

	private String title_;
	private boolean fast_;
	private boolean debug_;

	private Color boidColor_;
	private Color backgroundColor_;

	private Lookup lookup_;
	private Random rand_;
	private Boid[] boids_;
	private Screen screen_;

	public World(int w, int h, String title, int boidAmount, int boidSize, double speedLimit, double sightRange, double sightAngle, Color boidColor, Color backgroundColor, boolean fast, boolean debug) {

		w_ = w;
		h_ = h;
		boidSize_ = boidSize;

		sightRange_ = sightRange;
		sightAngle_ = sightAngle;
		speedLimit_ = speedLimit;

		fast_ = fast;
		debug_ = debug;

		boidColor_ = boidColor;
		backgroundColor_ = backgroundColor;

		lookup_ = new Lookup();
		screen_ = new Screen(w,h,title);
		rand_ = new Random();

		//  initializing boid array
		boids_ = new Boid[boidAmount];
		for (int i=0;i<boidAmount;i++) {
			double x = rand_.nextDouble()*w_;
			double y = rand_.nextDouble()*h_;
			double angle = rand_.nextDouble()*Math.PI*2;
			boids_[i] = new Boid(x, y, angle, speedLimit_);
		}
	}

	//  shows all boids on screen
	public void show() {
		screen_.clearToColor(backgroundColor_);
		for (Boid boid : boids_) {

			//  draws debug circle around
			//  boid at index 0 and lines
			//  to other seen boids
			if (boid.equals(boids_[0]) && debug_) {

				//  draws circle around boid
				screen_.fillArc(
					(int)(boid.x-sightRange_),
					(int)(boid.y-sightRange_),
					(int)sightRange_*2,
					(int)sightRange_*2,
					(int)(720-sightAngle_/2-boid.angle*57.2958)%360,
					(int)sightAngle_,
					new Color(0xad0057)
				);

				//  draws line to other seen boids
				for (Boid other : boids_) {
					if (canSee_(boid,other) && !other.equals(boid)) {
						screen_.drawArc(
							(int)(other.x-20),
							(int)(other.y-20),
							40,40,
							0,360,Color.RED
						);
						int RGB = (int)((1-distance_(boid,other)/sightRange_)*255);
						Color lineColor = new Color(RGB,RGB,RGB);
						screen_.drawLine((int)boid.x,(int)boid.y,(int)other.x,(int)other.y,lineColor);
					}
				}
			}

			//  draws boid
			if (!fast_) showBoid_(boid);
			else screen_.fillRect((int)boid.x-boidSize_/2,(int)boid.y-boidSize_/2,boidSize_,boidSize_,boidColor_);
		}
		screen_.flip();
	}

	//  updates the position of all
	//  boids and shows the screen
	public void update() {
		
		ArrayList<Boid> nearby = new ArrayList<Boid>();
		for (Boid boid : boids_) {

			//  get nearby boids
			for (Boid other : boids_) {
				if (boid.equals(other)) continue;
				if (canSee_(boid,other)) nearby.add(other);
			}

			double angle = 0;
			double averageX = 0;
			double averageY = 0;
			double tmpAngle;
			for (Boid other : nearby) {
				
				//  rule 1: seperation
				tmpAngle = (Math.PI*3+getAngle_(other.x-boid.x,other.y-boid.y)-boid.angle)%(Math.PI*2);
				if (tmpAngle > Math.PI) tmpAngle -= Math.PI*2;
				angle += tmpAngle*(1-distance_(boid,other)/sightRange_);

				//  rule 2: Alignment
				tmpAngle = (Math.PI*2+other.angle-boid.angle)%(Math.PI*2);
				if (tmpAngle > Math.PI) tmpAngle -= Math.PI*2;
				angle += tmpAngle/2;

				//  rule 3: cohesion
				averageX += other.x;
				averageY += other.y;
			}

			//  rule 3: cohesion
			if (nearby.size() > 0) {
				averageX /= nearby.size();
				averageY /= nearby.size();
				tmpAngle = (Math.PI*2+getAngle_(averageX-boid.x,averageY-boid.y)-boid.angle)%(Math.PI*2);
				if (tmpAngle > Math.PI) tmpAngle -= Math.PI*2;
				angle += tmpAngle*(distance_(averageX,averageY,boid.x,boid.y)/sightRange_);
			}

			if (nearby.size() > 0) angle /= (nearby.size()*2+1);
			else angle = 0;
			boid.turn(angle/2);
			boid.angle = (boid.angle+Math.PI*2)%(Math.PI*2);

			boid.move();
			boid.x = (boid.x+w_)%w_;
			boid.y = (boid.y+h_)%h_;

			nearby.clear();
		}
		show();
	}

	//  calculates the distance squared
	//  between two boids
	private double distanceSquared_(Boid boid1, Boid boid2) {
		return (boid1.x-boid2.x)*(boid1.x-boid2.x)+(boid1.y-boid2.y)*(boid1.y-boid2.y);
	}

	private double distance_(Boid boid1, Boid boid2) {return Math.sqrt(distanceSquared_(boid1,boid2));}

	private double distance_(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
	}

	//  writes image data for a given
	//  boid to the frame buffer
	private void showBoid_(Boid b) {

			/*
			double[] points = lookup_.getLookup(b.angle);
			int[] xPoints = new int[3];
			int[] yPoints = new int[3];

			for (int i=0;i<points.length;i++) {
				int new_val = (int)(points[i]*boidSize_);
				if (i < 3) xPoints[i] = (int)(new_val+b.x);
				else yPoints[i-3] = (int)(new_val+b.y);
			}
			*/
	
			int[] xPoints = new int[3];
			int[] yPoints = new int[3];

			double angle = b.angle;

			xPoints[0] = (int)(Math.cos(angle+3.926)*0.707*boidSize_+b.x);
			yPoints[0] = (int)(Math.sin(angle+3.926)*0.707*boidSize_+b.y);

			xPoints[1] = (int)(Math.cos(angle)*0.854*boidSize_+b.x);
			yPoints[1] = (int)(Math.sin(angle)*0.854*boidSize_+b.y);

			xPoints[2] = (int)(Math.cos(angle+2.356)*0.707*boidSize_+b.x);
			yPoints[2] = (int)(Math.sin(angle+2.356)*0.707*boidSize_+b.y);

			screen_.fillPolygon(xPoints,yPoints,3,boidColor_);
	}

	private boolean canSee_(Boid boid1, Boid boid2) {
		if (distanceSquared_(boid1,boid2) > sightRange_*sightRange_) return false;

		double angle = ((getAngle_(boid2.x-boid1.x,boid2.y-boid1.y)-boid1.angle)*57.2958+360)%360;
		return !(sightAngle_/2 < angle && angle < 360-sightAngle_/2);
	}

	private double getAngle_(double x, double y) {
                if (x == 0) return Math.PI;

                double angle = Math.atan(y/x);
                if (x < 0) angle += Math.PI;
                else if (y < 0) angle += Math.PI*2;

                return angle;
        }
}
