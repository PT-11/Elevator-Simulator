package com.elevator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;


/**
 * Controller ties in all the objects together and runs the elevator simulation. Is the mediator.
 */

public class Controller {
	
	public Queue<Integer> commands = new LinkedList<>();
	private Messages messages;
	private CommandGeneratorThread commandGeneratorThread;
	private Map<String, Component> components = new HashMap<>();

	public Controller() {

	}

	public void addComponent(Component component) {
		
		components.put(component.getId(), component);
	}

	public void userInputAction(String action) {
		
		commandGeneratorThread = (CommandGeneratorThread) components.get("commandGeneratorThread");
		commandGeneratorThread.setAction(action);
	}

	public void clearCommands() {
		
		messages = (Messages) components.get("messages");
		messages.clear(QueueType.INPUT);
	}

	public String getMessage(QueueType input, int i, char c, boolean b) {
		
		messages = (Messages) components.get("messages");
		return messages.getMessage(input, i, c, b);
	}

	public void postMessage(QueueType input, int threadNumber, char c, String string) {
		
		messages = (Messages) components.get("messages");
		messages.postMessage(input, threadNumber, c, string);	
	}

	public void postMessage(QueueType output, int threadNumber, char c, int currentFloor, Elevator elevator) {

		messages = (Messages) components.get("messages");
		messages.postMessage(output, threadNumber, c, elevator.getCurrentFloor());
	}

	public int getEventSize(QueueType input) {
		
		messages = (Messages) components.get("messages");
		return messages.getEventSize(input);
	}

	public int getMaxCapacity() {

		commandGeneratorThread = (CommandGeneratorThread) components.get("commandGeneratorThread");
		return commandGeneratorThread.getMaxCapacity();
	}

	public void postMessage(QueueType output, int threadNumber, char c, int number) {
		
		messages = (Messages) components.get("messages");
		messages.postMessage(output, threadNumber, c, number);
	}

}