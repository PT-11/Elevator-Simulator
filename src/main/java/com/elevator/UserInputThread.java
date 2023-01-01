package com.elevator;

import java.util.Scanner;

public class UserInputThread extends Component implements Runnable{
	
	private Controller controller;
	
	public UserInputThread(Controller controller) {
		super("userInputThread");
		this.controller = controller;
	}
	
	@Override
	public void run() {
		
		while(true) {
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			checkForUserInput();
		}
	}
	
	public String checkForUserInput() {
		
		// Adjusts InputGenerator values with console
		@SuppressWarnings("resource")
		Scanner register = new Scanner(System.in);
		
		// input either "morning" or "EndOfDay" or "Normal" 
		System.out.println("To change simulation time enter a command: Morning/EndOfDay/Normal"); 

		String action = register.nextLine().toUpperCase();
		
		switch (action) {
		
		case "MORNING", "ENDOFDAY", "NORMAL":
			controller.userInputAction(action);
			return action;
		
		default:
			System.out.println("Invalid command: Please try again");
			return "";
		
		}
		
		
	
	}
}
