package com.capgemini.device;

import java.util.Locale;
import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.PointList;

public class RouteCalculator {

	private PointList pointList;
	private String folder;
	private String defaultFolder = "defaultRouteFolder";
	private Double distance;

	public PointList getPointList() {
		return pointList;
	}

	public void setPointList(PointList pointList) {
		this.pointList = pointList;
	}

	public RouteCalculator(String folder) {
		this.folder = folder;

	}

	public RouteCalculator(){
		this.folder = defaultFolder;
	}
	
	public void setDistance(Double distance){
		this.distance = distance;
	}
	
	public Double getDistance(){
		return distance;
	}
	public void calcuateRoute(double latFrom, double lonFrom, double latTo, double lonTo) {
		// create singleton
		GraphHopper hopper = new GraphHopper().forServer();
		//hopper.setOSMFile("Muenchen.osm");
		// Store graphhopper files into the folder
		hopper.setGraphHopperLocation(folder);
		hopper.setEncodingManager(new EncodingManager("car"));

		// now this can take minutes if it imports or a few seconds for loading
		// of course this is dependent on the area you import
		hopper.importOrLoad();

		// simple configuration of the request object, see the
		// GraphHopperServlet class for more possibilities.

		GHRequest req = new GHRequest(latFrom, lonFrom, latTo, lonTo).setWeighting("fastest").setVehicle("car")
				.setLocale(Locale.GERMANY);
		GHResponse rsp = hopper.route(req);

		// first check for errors
		if (rsp.hasErrors()) {
			// return null;
		}

		this.setPointList(rsp.getPoints());
		this.setDistance(rsp.getDistance());

	}

}
