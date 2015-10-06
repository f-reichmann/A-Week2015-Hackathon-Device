package com.capgemini.device;

import java.util.ArrayList;
import java.util.List;

public class Ambulance extends Car{
	
	//List of all ambulances
	public static List<Ambulance> ambulances = new ArrayList<Ambulance>();

	public Ambulance(double currentLatitude, double currentLongitude, String deviceType, String threadname) {
		super(currentLatitude, currentLongitude, deviceType, threadname);
		ambulances.add(this);
		
	}
	

	

}
