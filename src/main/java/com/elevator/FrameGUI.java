package com.elevator;

import java.lang.Thread.State;

/**
 * Interface to make the FrameView graphics work with your elevator.
 * You must at least fully implement getCurrentFloor, the rest are optional for
 * full implementation (you need to declare them in elevator , but, if not using 
 * them they don't need to do anything more. 
 *
 */
public interface FrameGUI
{

	public int getCurrentFloor();		// Get the current floor number

	public String getCommand(); 		// Get the current command being used
	
	public int getPeople(); 			// Get the number of people in the elevator
	
	public State getState(); 			// Get the current elevator state for colour change
}
