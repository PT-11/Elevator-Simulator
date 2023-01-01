package com.elevator;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
	
	private static final Logger LOG = LogManager.getLogger(Main.class);	
	
	public static void main(String[] args) {
		
		//setting up components
		OurFileReader ourFileReader = new OurFileReader();
		ArrayList<String> configInfo = ourFileReader.populateAttributes(args[0]);

		Controller controller = new Controller();
		Messages messages = new Messages(controller);
		
		FrameView frameView = new FrameView("Elevator", Integer.parseInt(configInfo.get(1)),
				Integer.parseInt(configInfo.get(0)), Integer.parseInt(configInfo.get(2)), messages);
		
		CommandGeneratorThread commandGeneratorThread = new CommandGeneratorThread(configInfo, controller);
		UserInputThread userInputThread = new UserInputThread(controller);
		
		controller.addComponent(messages);
		controller.addComponent(userInputThread);
		controller.addComponent(commandGeneratorThread);

		Thread[] elevatorThreads = new Thread[commandGeneratorThread.getNumberOfElevators() + 1];
		Thread frameViewThread = new Thread(frameView);
		frameViewThread.setName("Frame View Thread");

		Thread commandThread = new Thread(commandGeneratorThread);
		commandThread.setName("Command Generator Thread");
		
		Thread inputThread = new Thread(userInputThread);
		inputThread.setName("User Input Thread");
		
		//starting threads
		frameViewThread.start();
		LOG.info(frameViewThread.getName() + " has successfully started");
		
		commandThread.start();
		LOG.info(commandThread.getName() + " has successfully started");
		
		inputThread.start();
		LOG.info(inputThread.getName() + " has successfully started");
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ThreadComm threadComm;
		System.out.println("Thread will begin");
		for (int i = 0; i < commandGeneratorThread.getNumberOfElevators() + 1; i++) {
			String threadName = Integer.toString(i);

			threadComm = new ThreadComm(threadName, controller);
			elevatorThreads[i] = new Thread(threadComm, threadName);

			elevatorThreads[i].start();
			controller.addComponent(threadComm);
			LOG.info(threadName + "has successfully started");

		}


	}
}