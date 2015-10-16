package com.capgemini.device;

import com.capgemini.client.Client;
import com.google.gson.JsonObject;

public class Car implements Runnable {
	
	//Latitude of current location
	public double currentLatitude;
	//Longitude of current location
	public double currentLongitude;
	//Latitude of destination location
	public double destinationLatitude;
	//Longitude of destination location
	public double destinationLongitude;
	RouteCalculator routeCalculator;
	private Thread t;
	//Name of thread, each car and ambulance has its own thread
	private String threadname;
	public static Client client = new Client();
	//ID of ambulance
	public String ambulanceID = "ambulance" + (Math.random() * 10000);
	//ID of car
	public String carID = "car" + (Math.random() * 1000);
	//Typy of device, car or ambulance
	public String deviceType;
	public boolean emergency = false;
	//State of ambulance, free or busy
	public boolean isFree = true;

	public double getCurrentLatitude() {
		return currentLatitude;
	}

	public void setCurrentLatitude(double currentLatitude) {
		this.currentLatitude = currentLatitude;
	}

	public double getCurrentLongitude() {
		return currentLongitude;
	}

	public void setCurrentLongitude(double currentLongitude) {
		this.currentLongitude = currentLongitude;
	}

	public double getDestinationLatitude() {
		return destinationLatitude;
	}

	public void setDestinationLatitude(double destinationLatitude) {
		this.destinationLatitude = destinationLatitude;
	}

	public double getDestinationLongitude() {
		return destinationLongitude;
	}

	public void setDestinationLongitude(double destinationLongitude) {
		this.destinationLongitude = destinationLongitude;
	}

	public Car(double currentLatitude, double currentLongitude, String deviceType, String threadname) {
		this.currentLatitude = currentLatitude;
		this.currentLongitude = currentLongitude;
		
		this.threadname = threadname;
		this.deviceType = deviceType;
		routeCalculator = new RouteCalculator(threadname);
	}

	
	/*
	 * This method publishes the current location to the IoT Foundation. 
	 * @param currentLatitude latitude of current location
	 * @param currentLongitude longitude of current location
	 */
	public void publishLocation(double currentLatitude, double currentLongitude) throws Exception {
		JsonObject location = new JsonObject();

		//Check if it is car or ambulance
		if (this.deviceType == "car")
			location.addProperty("vin", this.carID);
		else{
			location.addProperty("vin", this.ambulanceID);
			//Set the state of ambulance (busy or free)
			location.addProperty("isFree", Boolean.toString(isFree));	
		}
		location.addProperty("latitude", Double.toString(currentLatitude));
		location.addProperty("longitude", Double.toString(currentLongitude));
	    
		//Publish event to IoT
		//TODO I added the parameter copyInRoot in the iotf client library
		// now all data is also available in the msg root, which is necessary
		// at least for the longitude, latitude and id
		client.getClient().publishEvent("location", location, 1, true);

	}

	//Set randomly a new destination
	public void setNewDestination() {
		this.destinationLatitude = 48.1233 + (Math.random() * 0.0096);
		this.destinationLongitude = 11.6519 + (Math.random() * 0.0306);

	}

	//Set the destination to the position of emergency
	public void setNewDestination(Double latitude, Double longitude) {
		this.destinationLatitude = latitude;
		this.destinationLongitude = longitude;
		this.emergency = true;

	}

	public void route() {
		if (t == null) {
			t = new Thread(this, threadname);
			t.start();
		}
	}

	@Override
	public void run() {
		while (true) {
			if (!emergency)
				this.setNewDestination();
			else {
				isFree = false;
				emergency = false;
			}
			
			//Calculate the route between the current and destination location
			routeCalculator.calcuateRoute(currentLatitude, currentLongitude, destinationLatitude, destinationLongitude);
			int i = 0;

			while (routeCalculator.getPointList().getSize() > i) {

				double nextPointLatitude = this.routeCalculator.getPointList().getLatitude(i);
				double nextpointLongitude = this.routeCalculator.getPointList().getLongitude(i);

				// Take the difference between current location and the
				// destination
				double distLat = currentLatitude - nextPointLatitude;
				double distLong = currentLongitude - nextpointLongitude;

				// While the difference is bigger than the threshold x
				while (Math.abs(distLat) > 0.0001 || Math.abs(distLong) > 0.0001) {
					// check if we have to move in lat direction
					if (Math.abs(distLat) > 0.0001) {
						// go xxx steps in direction
						if (distLat < 0) {
							currentLatitude = currentLatitude + 0.00005;
						} else {
							currentLatitude = currentLatitude - 0.00005;
						}
					}
					// check if we have to move in long direction
					if (Math.abs(distLong) > 0.0001) {
						if (distLong < 0) {
							currentLongitude = currentLongitude + 0.00005;
						} else {
							currentLongitude = currentLongitude - 0.00005;
						}
					}

					try {
						this.publishLocation(currentLatitude, currentLongitude);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					distLat = currentLatitude - nextPointLatitude;
					distLong = currentLongitude - nextpointLongitude;
					if (emergency) {
						break;
					}
				}
				currentLatitude = nextPointLatitude;
				currentLongitude = nextpointLongitude;

				i = i + 1;

				if (emergency) {
					break;

				}
			}
			
			isFree = true;

		}
	}

}
