package com.elevator;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ThreadComm class is used as a thread to adjust the elevator objects
 * 
 */

public class ThreadComm extends Component implements Runnable {
	private Elevator elevator;
	private int threadNumber;
	private String[] commandString; // Contains elevator commands in a String format
	private int[] commandInt; // Contains elevator commands in an int format
	private Controller controller;
	private int passengers;



	public ThreadComm(String id, Controller controller) {
		super(id);
		commandInt = new int[2];
		commandString = new String[2];
		elevator = new Elevator();
		elevator.setCurrentFloor(0);
		this.controller = controller;

	}

	@Override
	public void run() {
		finalSetThreadNumber();

		while (true){

			if (controller.getEventSize(QueueType.INPUT) > 0) {
				
				// Converting command from String to an int
				String msg = controller.getMessage(QueueType.INPUT, 1, 'C', true);

				if (msg != null) {
					commandString = msg.split(":");
					commandInt[0] = Integer.parseInt(commandString[0]);
					commandInt[1] = Integer.parseInt(commandString[1]);
					elevator.setInUse(true);

					// If elevator is not already located at the pick-up level then it will call
					// method to do this
					if (commandInt[0] != elevator.getCurrentFloor()) {
						passengers = randomNumber();
						goToFloor(commandInt[0]);
					}

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
					}

					// If elevator is not already at the destination level then it will call a
					// method to do this
					if (commandInt[1] != elevator.getCurrentFloor()) {
						passengers = 0;
						goToFloor(commandInt[1]);
					}
					elevator.setInUse(false);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
					}
				} // inner if
			} // outer if
		}//while
	}

	private void goToFloor(int desFloor) {
		controller.postMessage(QueueType.OUTPUT, threadNumber, 'C', "Moving");

		while (elevator.getCurrentFloor() != desFloor) {

			// Move up or down one floor at a time
			if (elevator.getCurrentFloor() > desFloor) {
				elevator.setCurrentFloor(elevator.getCurrentFloor() - 1);
			} else {
				elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);
			}
			
			controller.postMessage(QueueType.OUTPUT, threadNumber, 'F', elevator.getCurrentFloor(), getElevator());
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}
		controller.postMessage(QueueType.OUTPUT, threadNumber, 'C', "Idle");
		passengerChange(passengers);

	}
	
	public void passengerChange(int number) {
		
		elevator.setPeople(number);
		controller.postMessage(QueueType.OUTPUT, threadNumber, 'P', elevator.getPeople());
		controller.postMessage(QueueType.OUTPUT, threadNumber, 'C', "Idle");
	}
	
	public int randomNumber() {
		Random rn = new Random();
		int minCapacity = 1;
		int range = controller.getMaxCapacity();
		int randomNum =  rn.nextInt(range) + minCapacity;
		return randomNum;
	}
	
	public int getThreadNumber() {
		return threadNumber;
	}

	public void setThreadNumber(int threadNumber) {
		this.threadNumber = threadNumber;
	}

	public void finalSetThreadNumber() {
		setThreadNumber(Integer.parseInt(Thread.currentThread().getName()));
	}

	public Elevator getElevator() {
		return elevator;
	}

	public void setElevator(Elevator elevator) {
		this.elevator = elevator;
	}
}