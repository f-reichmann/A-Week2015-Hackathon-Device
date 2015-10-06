package com.capgemini.client;

import java.util.Properties;
import java.util.logging.Logger;

import com.capgemini.device.Device;
import com.capgemini.client.EventCallbackCommand;
import com.ibm.iotf.client.AbstractClient;
import com.ibm.iotf.client.device.DeviceClient;

public class Client {

	private static final String CLASS_NAME = AbstractClient.class.getName();
	private static final Logger LOG = Logger.getLogger(CLASS_NAME);

	private static DeviceClient client;
	private String ORG = "ozz0rt"; 
	private String TYPE = "Raspberry";
	private String ID = "aca21322819c";    
	private String AUTH = "token";
	private String AUTH_TOKEN = "XFFW!DLlDIIGL4IS&j"; 
	
	public DeviceClient initiateClient() throws Exception {
		LOG.info("Initiate client");
		DeviceClient localClient = null;

		// Provide the device specific data, as well as Auth-key and token using
		// Properties class
		Properties options = new Properties();

		if (!Device.org.isEmpty()) {
			options.setProperty("org", Device.org);
		} else {
			options.setProperty("org", ORG);
		}
		if (!Device.type.isEmpty()) {
			options.setProperty("type", Device.type);
		} else {
			options.setProperty("type", TYPE);
		}
		
		if (!Device.id.isEmpty()) {
			options.setProperty("id", Device.id);
		} else {
			options.setProperty("id", ID);
		}
		
		options.setProperty("auth-method", AUTH);
		
		if (!Device.token.isEmpty()) {
			options.setProperty("auth-token", Device.token);
		} else {
			options.setProperty("auth-token", AUTH_TOKEN);
		}
		
		// Instantiate the class by passing the properties file
		localClient = new DeviceClient(options);
		localClient.setCommandCallback(new EventCallbackCommand());

		LOG.info("Start connecting");
		localClient.connect();
		LOG.info("Client connected successfully");
		return localClient;
	}

	public DeviceClient getClient() throws Exception {
		if (client == null) {
			client = initiateClient();
			
		} else {
			if (client.isConnected() == false) {
				client.connect();
			}
		}
		return client;
	}

}
