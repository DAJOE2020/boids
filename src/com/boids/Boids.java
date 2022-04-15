package com.boids;

import java.time.Instant;
import java.time.Duration;
import java.awt.Color;

public class Boids {
	public static void main(String[] args) {

		int w = 1536;
		int h = 846;
		int boidAmount = 1000;
		int boidSize = 10;
		
		double speedLimit = 2;
		double sightRange = 70;
		double sightAngle = 270;

		boolean fast = false;
		boolean debug = false;

		String title = "Boids";

		Color boidColor = new Color(0x0063BAF7);
		Color backgroundColor = new Color(0x0024334A);
		
		String arg;
		for (int i=0;i<args.length;i++) {
			arg = args[i];

			if (arg.equals("--help") || arg.equals("-h")) {
				String[] helpText = {
					"Usage: java -jar <file_name>.jar [options]",
					"",
					"Options:",
					"	--help OR -h:              shows this help text",
					"	--width OR -w:             sets window width",
					"	--height OR -h:            sets window height",
					"	--title OR -t:             sets window title",
					"	--boidAmount OR -ba:       sets amount of boids in simulation",
					"	--boidSize OR -bs:         sets size of boids",
					"	--speedLimit OR -sl:       sets boid speed",
					"	--sightRange OR -sr:       sets boid sight radius",
					"	--sightAngle OR -sa:       sets boid field of view",
					"	--boidColor OR -boc:       sets color of boids",
					"	--backgroundColor OR -bac: sets color of background",
					"	--fast OR -f:              sets if boids are shown as",
					"	                           rectangles (fast) or triangles (slow)",
					"	--debug OR -d:             shows debug around boid at index 0"
				};
				for (String line : helpText)
					System.out.println(line);
				System.exit(0);
			}

			if (i != args.length-1) {
				if (arg.equals("--width") || arg.equals("-w"))
					w = parseInt(args[i+1]);
				else if (arg.equals("--height") || arg.equals("-h"))
					h = parseInt(args[i+1]);
				else if (arg.equals("--title") || arg.equals("-t")) title = args[i+1];
				else if (arg.equals("-ba") || arg.equals("--boidAmount"))
					boidAmount = parseInt(args[i+1]);
				else if (arg.equals("-bs") || arg.equals("--boidSize"))
					boidSize = parseInt(args[i+1]);
				else if (arg.equals("-sl") || arg.equals("--speedLimit"))
					speedLimit = parseDouble(args[i+1]);
				else if (arg.equals("-sr") || arg.equals("--sightRange"))
					sightRange = parseDouble(args[i+1]);
				else if (arg.equals("-sa") || arg.equals("--sightAngle"))
					sightAngle = parseDouble(args[i+1]);
				else if (arg.equals("-boc") || arg.equals("--boidColor"))
					boidColor = parseColor(args[i+1]);
				else if (arg.equals("-bac") || arg.equals("--backgroundColor"))
					backgroundColor = parseColor(args[i+1]);
				else if (arg.equals("-f") || arg.equals("--fast"))
					fast = parseBoolean(args[i+1]);
				else if (arg.equals("-d") || arg.equals("--debug"))
					debug = parseBoolean(args[i+1]);
			}
		}

		World world = new World(w,h,title,boidAmount,boidSize,speedLimit,sightRange,sightAngle,boidColor,backgroundColor,fast,debug);

		Duration time_dif;
		Instant start;

		while (true) {
			start = Instant.now();
			world.update();
			time_dif = Duration.between(start,Instant.now());
			try {Thread.sleep(16-time_dif.toMillis());} catch (InterruptedException e) {} catch (IllegalArgumentException ie) {}
		}
	}

	static int parseInt(String x) {
		int out = -1;
		try {out = Integer.parseInt(x);}
		catch(NumberFormatException e) {System.out.println("You inputted an invalid number"); System.exit(1);}
		if (out < 0) {System.out.println("You inputted a number less than 0"); System.exit(1);}
		return out;
	}
	static double parseDouble(String x) {
		double out = -1;
		try {out = Double.parseDouble(x);}
		catch(NumberFormatException e) {System.out.println("You inputted an invalid number"); System.exit(1);}
		if (out < 0) {System.out.println("You inputted a number less than 0"); System.exit(1);}
		return out;
	}
	static Color parseColor(String x) {
		int RGB = 0;
		if (x.charAt(0) == '#') x = x.substring(1);
		if (x.substring(0,2) != "0x") x = "0x"+x;
		try {RGB = Integer.decode(x);}
		catch(NumberFormatException e) {System.out.println("You inputted an invalid RGB value"); System.exit(1);}
		return new Color(RGB);
	}
	static boolean parseBoolean(String x) {
		if (x.equals("0")) return false;
		return true;
	}
}
