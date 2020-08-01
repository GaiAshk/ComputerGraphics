package edu.cg.models.Car;


public class Specification {
	// WheelS specification:
	public static final double TIRE_DEPTH = 0.1;
	public static final double TIRE_RADIUS = .075;
	public static final double PAIR_OF_WHEELS_ROD_DEPTH = 0.2;
	public static final double PAIR_OF_WHEELS_ROD_RADIUS = 0.01;
	
	// Front Body Specification:
	public static final double F_LENGTH = 0.6;
	// Hood
	public static final double F_HOOD_LENGTH = 0.75*F_LENGTH;
	public static final double F_HOOD_LENGTH_2 = 2.5*TIRE_RADIUS;
	public static final double F_HOOD_LENGTH_1 = F_HOOD_LENGTH-F_HOOD_LENGTH_2;
	public static final double F_HOOD_DEPTH_1 = 2.0 * TIRE_DEPTH + PAIR_OF_WHEELS_ROD_DEPTH;
	public static final double F_HOOD_DEPTH_2 = 0.25*F_HOOD_DEPTH_1;
	public static final double F_HOOD_DEPTH_3 = 0.8*F_HOOD_DEPTH_2;
	public static final double F_HOOD_HEIGHT_1 = 1.75*TIRE_RADIUS;
	public static final double F_HOOD_HEIGHT_2 = 1.5*TIRE_RADIUS;
	public static final double F_DEPTH = F_HOOD_DEPTH_1;
	public static final double F_HEIGHT = F_HOOD_HEIGHT_1;


	// Bumper
	public static final double F_BUMPER_LENGTH = 0.25*F_LENGTH;
	public static final double F_BUMPER_DEPTH = 0.5*F_HOOD_DEPTH_1;
	public static final double F_BUMPER_HEIGHT_2 = 5e-4;
	public static final double F_BUMPER_HEIGHT_1 = 0.25*F_HOOD_HEIGHT_2;
	public static final double F_BUMPER_WINGS_DEPTH = 0.8*0.5*(F_HOOD_DEPTH_1-F_BUMPER_DEPTH);
	public static final double F_BUMPER_WINGS_HEIGHT_1 = 0.75*F_HOOD_HEIGHT_1;
	public static final double F_BUMPER_WINGS_HEIGHT_2 = F_BUMPER_HEIGHT_2;
	public static final double bumperBoxDepthfactor = 1.75;
	
	// Center Body Specification:
	public static final double C_LENGTH  = 0.25;
	public static final double C_HIEGHT= 1.75*F_HOOD_HEIGHT_1;
	public static final double C_DEPTH = F_HOOD_DEPTH_1;

	// --> Base
	public static final double C_BASE_LENGTH  = C_LENGTH;
	public static final double C_BASE_HEIGHT= 0.1*F_HOOD_HEIGHT_1;
	// --> Back Seat
	public static final double C_BACK_LENGTH = C_BASE_LENGTH *0.5*0.75;
	public static final double C_BACK_HEIGHT_1= F_HOOD_HEIGHT_1;
	public static final double C_BACK_HEIGHT_2= 1.75*F_HOOD_HEIGHT_1;
	public static final double C_BACK_DEPTH = 0.25*F_HOOD_DEPTH_1;
	// --> Front
	public static final double C_FRONT_LENGTH = C_BASE_LENGTH *0.25*0.75;
	public static final double C_FRONT_HEIGHT_2= F_HOOD_HEIGHT_1-C_BASE_HEIGHT;
	public static final double C_FRONT_HEIGHT_1= F_HOOD_HEIGHT_1;
	public static final double C_FRONT_DEPTH_2 = F_HOOD_DEPTH_1;
	public static final double C_FRONT_DEPTH_1 = 0.25*C_FRONT_DEPTH_2;
	// --> Side
	public static final double C_SIDE_LENGTH = (C_DEPTH-C_FRONT_DEPTH_1)/2.0;
	public static final double C_SIDE_HEIGHT_2= F_HOOD_HEIGHT_1;
	public static final double C_SIDE_HEIGHT_1= F_HOOD_HEIGHT_1-C_BASE_HEIGHT;
	public static final double C_SIDE_DEPTH_1 = C_BASE_LENGTH ;
	public static final double C_SIDE_DEPTH_2 = C_BASE_LENGTH -2*C_FRONT_LENGTH;

	// Back Body Specification:
	// --> Base
	public static final double B_BASE_HEIGHT= C_BASE_HEIGHT;
	public static final double B_BASE_LENGTH = 5.0*TIRE_RADIUS;
	public static final double B_BASE_DEPTH = F_HOOD_DEPTH_1;
	// --> Top Box
	public static final double B_LENGTH = 7.0*TIRE_RADIUS;
	public static final double B_HEIGHT_2= F_HOOD_HEIGHT_1-B_BASE_HEIGHT;
	public static final double B_HEIGHT_1= 1.5*TIRE_RADIUS-B_BASE_HEIGHT;
	public static final double B_DEPTH_2 = B_BASE_DEPTH;
	public static final double B_DEPTH_1 = 0.25*B_DEPTH_2;
	// --> Exhaust Pipes
	public static final double B_EP_RADIUS_1 = 0.02;
	public static final double B_EP_RADIUS_2 = 0.028;
	public static final double B_EP_HEIGHT = 0.13;	//love the long Exhaust Pipes!


	
	// Spoiler Specification:    
	// --> Rods
	public static final double S_RODS_SIZE = 0.5*0.5*(B_DEPTH_1 + B_DEPTH_2);
	public static final double S_RODS_DISTANCE = 0.4*S_RODS_SIZE;
	public static final double S_ROD_RADIUS = 0.25*(S_RODS_SIZE-S_RODS_DISTANCE);
	public static final double S_ROD_HIEGHT = 0.05;
	// --> Wings
	public static final double S_DEPTH = 2.*0.5 * (B_DEPTH_1 + B_DEPTH_2);
	public static final double S_BASE_DEPTH = 0.9*S_DEPTH;
	public static final double S_WINGS_DEPTH = 0.5*(S_DEPTH - S_BASE_DEPTH);
	public static final double S_WINGS_HEIGHT = 0.15;
	public static final double S_BASE_HEIGHT = 0.015;
	public static final double S_LENGTH = 0.15;
	public static final double B_DEPTH = S_DEPTH;
	public static final double B_HEIGHT = S_WINGS_HEIGHT + B_HEIGHT_2+S_ROD_HIEGHT;
	
}
