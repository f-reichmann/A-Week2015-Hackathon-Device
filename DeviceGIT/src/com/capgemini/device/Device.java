package com.capgemini.device;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import com.capgemini.device.Ambulance;
import com.capgemini.device.Car;
import com.capgemini.device.DisplayTableForRaspberryPi;

public class Device {


	public static String id;
	public static String token;
	public static String org;
	public static String type;
	

	public static void main(String[] args) throws Exception {

		
		InputStream input = null;
		
		Integer numCars = 0;
		Integer numAmbulances = 0;
		boolean printTable = false;
		
		try {
			
			input = new FileInputStream("config.properties");
			Properties prop = new Properties();
			prop.load(input);
			
			//Get the ID of device
			id = prop.getProperty("device.id");
			//Get the token of device
			token = prop.getProperty("device.token");
			//Get the organization of device
			org = prop.getProperty("device.org");
			//Get the type of device
			type = prop.getProperty("device.type");
			//Get the number of cars
			numCars = new Integer(prop.getProperty("count.cars"));
			//Get the number of ambulances
			numAmbulances = new Integer(prop.getProperty("count.ambulances"));
			//Get the value of parameter to show tables or not
			printTable = new Boolean(prop.getProperty("table.print"));
			
			System.out.println(
					prop.getProperty("device.id") + ", " +
					prop.getProperty("device.token") + ", " +
					numCars + ", " +
					numAmbulances + ", " +
					printTable + ", "
				);
			
		
		} catch (FileNotFoundException e) {
			System.out.println("Could not find properties file. " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Could not read properties file. " + e.getMessage());
		} finally {
			if (input != null) input.close();
		}

		/*Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("Terminated");
				Client client = new Client();
				try {
					client.getClient().disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});*/
		
		ArrayList<Car> c = new ArrayList<Car>();
		
		for (int i = 0; i < numCars; i++) {
			Car car = new Car(48.1233 + (Math.random() * 0.0096), 11.6519 + (Math.random() * 0.0306), "car", "thread"+(i+1));
			car.publishLocation(car.currentLatitude, car.currentLongitude);
			car.route();
			c.add(car);
		}
		
		ArrayList<Ambulance> a = new ArrayList<Ambulance>();
		
		for (int i = 0; i < numAmbulances; i++) {
			  Ambulance ambulance = new Ambulance(48.1233 + (Math.random() * 0.0096), 11.6519 + (Math.random() * 0.0306), "ambulance", "thread"+(numCars+i+1));
			  ambulance.publishLocation(ambulance.currentLatitude, ambulance.currentLongitude);
			  ambulance.route();
			  a.add(ambulance);
		}

		if (printTable) {
			Car[] cars = c.toArray(new Car[c.size()]);
			Ambulance[] ambulances = a.toArray(new Ambulance[a.size()]);
			// print all cars and ambulances on the RaspberryPi display
			DisplayTableForRaspberryPi display = new DisplayTableForRaspberryPi(cars, ambulances, "displayThread");
		    display.printDisplayTableForRaspberryPi();
		}
	}
}
