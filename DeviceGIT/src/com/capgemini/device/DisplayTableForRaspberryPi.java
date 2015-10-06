package com.capgemini.device;

import dnl.utils.text.table.TextTable;

public class DisplayTableForRaspberryPi implements Runnable {
	
	String[] tabelheader = {"Dev", "cLong", "clat", "dLong", "dlat" };
	int numberBeforeCommaPlusOne = 3;
	int numberAfterComma =5;
	public TextTable table;
	private Thread t;
	private String threadname;
	Car[] carArray;
	Ambulance[] ambulanceArray;
	
	public DisplayTableForRaspberryPi(Car[] carArray, Ambulance[] ambulanceArray,String threadname){
		 this.carArray = carArray;
		 this.ambulanceArray = ambulanceArray;
		 this.threadname = threadname;
	 }
	
	public Object[][] createTableData(Car[] arg1, Ambulance[] arg2, String[] tableheader) {
		
		Object[][] tableData = new Object[arg1.length+arg2.length][tableheader.length];
		
		for(int i =0; i < arg1.length; i++)
		{
			tableData[i][0] = arg1[i].carID.substring(0, 5);
			tableData[i][1] = Double.toString(arg1[i].getCurrentLongitude()).substring(0,numberBeforeCommaPlusOne+numberAfterComma);
			tableData[i][2] = Double.toString(arg1[i].getCurrentLatitude()).substring(0,numberBeforeCommaPlusOne+numberAfterComma);
			tableData[i][3] = Double.toString(arg1[i].getDestinationLongitude()).substring(0,numberBeforeCommaPlusOne+numberAfterComma);
			tableData[i][4] = Double.toString(arg1[i].getDestinationLatitude()).substring(0,numberBeforeCommaPlusOne+numberAfterComma);
				
		}
		
		for(int i =0; i <  arg2.length; i++)
		{
			tableData[arg1.length+i][0] = arg2[i].ambulanceID.substring(0, 5);
			tableData[arg1.length+i][1] = Double.toString(arg2[i].getCurrentLongitude()).substring(0,numberBeforeCommaPlusOne+numberAfterComma);
			tableData[arg1.length+i][2] = Double.toString(arg2[i].getCurrentLatitude()).substring(0,numberBeforeCommaPlusOne+numberAfterComma);
			tableData[arg1.length+i][3] = Double.toString(arg2[i].getDestinationLongitude()).substring(0,numberBeforeCommaPlusOne+numberAfterComma);
			tableData[arg1.length+i][4] = Double.toString(arg2[i].getDestinationLatitude()).substring(0,numberBeforeCommaPlusOne+numberAfterComma);
			
		}
		
		return tableData;
	}

	public void printDisplayTableForRaspberryPi(){
		if (t == null) {
			t = new Thread(this, threadname);
			t.start();
		}
		

	}

	@Override
	public void run() {
		while (true) {
			this.table = new TextTable(tabelheader, createTableData(this.carArray, this.ambulanceArray, tabelheader));
			System.out.print("\u001b[2J" + "\u001b[H");
			System.out.println("----------------------------------------------------------");
			System.out.println("------XXX-------XX----------XX--XXXXXX--XXXXXX--XX--XX----");
			System.out.println("-----XX-XX-------XX--------XX---XX------XX------XX-XX----");
			System.out.println("----XXXXXXX--xxx--XX------XX----XXXX----XXXX----XXXX------");
			System.out.println("---XX-----XX-------XX-XX-XX-----XX------XX------XX-XX-----");
			System.out.println("--XX-------XX-------XX--XX------XXXXXX--XXXXXX--XX--XX----");		
			table.printTable();;
		    try {
		        Thread.sleep(500);                 //1000 milliseconds = second.
		    } catch(InterruptedException ex) {
		        Thread.currentThread().interrupt();
		    }
		    System.out.flush();
		}
	}
	  

	
	
	




}
