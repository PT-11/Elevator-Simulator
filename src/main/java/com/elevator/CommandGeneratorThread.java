package com.elevator;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * InputGenerator is used to generate random commands for the elevator threads to follow
 * and can manipulate the commands created based off user input
 */

public class CommandGeneratorThread extends Component implements Runnable{
	private int maxFloor;
	private int minFloor;
	private int numberOfElevators;
	private int minSource;
	private int maxSource;
	private int minDest;
	private int maxDest;
	private ArrayList<String> config;
	private int maxCapacity;
	private int commandTimeInterval;
	private String action;
	private Controller controller;
	private static final Logger LOG = LogManager.getLogger(Thread.currentThread().getClass() + ": " + Thread.currentThread().getName());	
	
	public CommandGeneratorThread(ArrayList<String> config, Controller controller) {
		super("commandGeneratorThread");
		maxFloor = Integer.parseInt(config.get(0));
		minFloor = Integer.parseInt(config.get(1));
		numberOfElevators = Integer.parseInt(config.get(2));
		maxCapacity = Integer.parseInt(config.get(3));
		commandTimeInterval = Integer.parseInt(config.get(4));
		minSource = minFloor;
		maxSource = maxFloor;
		minDest = minFloor;
		maxDest = maxFloor;
		action = "generateCommand";
		this.controller = controller;
		
		
	}

	@Override
	public void run() {

		while(true) {
			
			switch(action) {
			
			case "generateCommand":
				generateInput();
				break;
			
			case "MORNING":
				System.out.println("Morning was selected");
				overwiteInput(action);
				action = "generateCommand";
				break;
			
			case "ENDOFDAY":
				System.out.println("End of day was selected");
				overwiteInput(action);
				action = "generateCommand";
				break;
				
			case "NORMAL":
				System.out.println("Normal was selected");
				overwiteInput(action);
				action = "generateCommand";
				break;
			}
		}
	}

	// Generates a command for the elevators
	public String generateInput() {

		int source = (int) ((Math.random() * (maxSource - minSource)) + minSource);
		int destination = (int) ((Math.random() * (maxDest - minDest)) + minDest);
		controller.postMessage(QueueType.INPUT, 1, 'C', source+":"+destination);
//		System.out.println(source+":"+destination);
		LOG.debug("New command generatored. Pick up level: " + source + ". Destination level: " + destination);
		
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}

		return source+":"+destination;
	}
	
	
	public boolean overwiteInput(String action) {

		if (action.equalsIgnoreCase("Morning")) { //all trips end on bottom floor
			this.minSource = minFloor;
			this.maxSource = maxFloor;
			this.maxDest = minFloor;
			this.minDest = minFloor;

			System.out.println("Destination is now all set to ground floor");
		} else if (action.equalsIgnoreCase("EndOfDay")) { //all trips originate from bottom floor
			this.minDest = minFloor;
			this.maxDest = maxFloor;
			this.minSource = minFloor;
			this.maxSource = minFloor;

			System.out.println("Sources all begin at ground floor");
		} else if (action.equalsIgnoreCase("Normal")) { //Resets source and destination
			this.minSource = minFloor;
			this.maxSource = maxFloor;
			this.minDest = minFloor;
			this.maxDest = maxFloor;

			System.out.println("Back to normal");
		}
		
		controller.clearCommands();
		
		return true;
	}
	
	public int getMaxFloor() {
		return maxFloor;
	}

	public void setMaxFloor(int maxFloor) {
		this.maxFloor = maxFloor;
	}

	public int getMinFloor() {
		return minFloor;
	}

	public void setMinFloor(int minFloor) {
		this.minFloor = minFloor;
	}

	public int getNumberOfElevators() {
		return numberOfElevators;
	}

	public void setNumberOfElevators(int numberOfElevators) {
		this.numberOfElevators = numberOfElevators;
	}

	public int getMinSource() {
		return minSource;
	}

	public void setMinSource(int minSource) {
		this.minSource = minSource;
	}

	public int getMaxSource() {
		return maxSource;
	}

	public void setMaxSource(int maxSource) {
		this.maxSource = maxSource;
	}

	public int getMinDest() {
		return minDest;
	}

	public void setMinDest(int minDest) {
		this.minDest = minDest;
	}

	public int getMaxDest() {
		return maxDest;
	}

	public void setMaxDest(int maxDest) {
		this.maxDest = maxDest;
	}

	public ArrayList<String> getConfig() {
		return config;
	}

	public void setConfig(ArrayList<String> config) {
		this.config = config;
	}

	public int getMaxCapacity() {
		return maxCapacity;
	}

	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	public int getCommandTimeInterval() {
		return commandTimeInterval;
	}

	public void setCommandTimeInterval(int commandTimeInterval) {
		this.commandTimeInterval = commandTimeInterval;
	}
	
	public void setAction(String action) {
		this.action = action;
		System.out.println("Action is now: " + action);
	}
	
	
}