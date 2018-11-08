package Comunication.generator;

import uniandes.gload.core.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import Comunication.disp.*;
import Comunication.test.StressTest;
import jdk.jfr.events.FileWriteEvent;

/**
 * GLoad Core Class - Task
 * @author Victor Guana at University of Los Andes (vm.guana26@uniandes.edu.co)
 * Systems and Computing Engineering Department - Engineering Faculty
 * Licensed with Academic Free License version 2.1
 * 
 * ------------------------------------------------------------
 * Example Class Client Server:
 * This Class Represents the task that we want to generate in a concurrent way
 * ------------------------------------------------------------
 * 
 */
public class ClientServerTask extends Task
{
	public StressTest test;
	public ClientServerTask(StressTest test) {
		super();
		this.test=test;
	}
	
	@Override
	public void execute() 
	{
		// TODO Auto-generated method stub
		try {
			Client client = new Client();
			client.sendState("41 24.2028, 2 10.4418");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail();
		}
		
	}

	@Override
	public void fail() 
	{
		System.err.println("fallo");
		File file = new File("Fails.txt");
		synchronized (file) {
			try {
				FileWriter fw = new FileWriter(file);
				PrintWriter writer = new PrintWriter(fw,true);
				writer.print(","+1);
				writer.close();
			} catch (Exception e) {
				
			} 
		}
	}

	@Override
	public void success() 
	{
		// TODO Auto-generated method stub
		System.out.println(Task.OK_MESSAGE);
		
	}

}
