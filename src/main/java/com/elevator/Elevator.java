package com.elevator;

import java.lang.Thread.State;

/**
 * Elevator model class
 */

public class Elevator implements FrameGUI {
	
	private int currentFloor;
	private int people;
	private State state;
	private String command;
	private Boolean inUse;
	
	public Elevator() {
		currentFloor = 0;
	}
	
	public Boolean getInUse() {
		return inUse;
	}

	public void setInUse(Boolean inUse) {
		this.inUse = inUse;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void setCurrentFloor(int currentFloor) {
		this.currentFloor = currentFloor;
	}
	
	public void setPeople(int people) {
		this.people = people;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}
	
	@Override
	public int getCurrentFloor() {
		return this.currentFloor;
	}
	
	@Override
	public String getCommand() {
		return this.command;
	}

	@Override
	public int getPeople() {
		return this.people;
	}

	@Override
	public State getState() {
		return state;
	}
}