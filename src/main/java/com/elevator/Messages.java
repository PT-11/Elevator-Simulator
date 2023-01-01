package com.elevator;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Generic messaging to send and receive Event classes
 * Contains two Event queues, one for input events and one for output Events
 * Events can be added to the queues by calling the postMessage methods
 * Events can be retrieved from an queue by calling the getMessage 
 * and the getAllMessages methods
 *
 */
public class Messages extends Component
{
	private ArrayList<Event> inputs;			// list of input events 
	private ArrayList<Event> outputs;			// list of output events
	private Condition inputQueueFull;
	private Lock lock;
	private static final Logger LOG = LogManager.getLogger(Messages.class);	
	/**
	 * Constructor for messages. It needs no parameters
	 * It creates an instance of the input and output ArayLists. 
	 */
	public Messages(Controller controller){
		super("messages");
		inputs = new ArrayList<>();
		outputs = new ArrayList<>();
		lock = new ReentrantLock();
		inputQueueFull = lock.newCondition();
		
	}
	
	/**
	 * Adds an Event to the specified list with an integer as the message type.
	 * @param queueType TypeType the event array type, INPUT or OUTPUT
	 * @param id identifier for the receiver like thread number
	 * @param eventType The event type like C for command, E for exit  
	 * @param message message The message you want to pass
	 */
	public void postMessage(QueueType queueType, int id, char eventType, int message) {
		postMessage(queueType, id, eventType, "" + message);
	}
	
	/**
	 * Adds an Event to the specified list with a String as the message type.
	 * @param queueType TypeType the event array type, INPUT or OUTPUT
	 * @param id identifier for the receiver like thread number
	 * @param eventType The event type like C for command, E for exit  
	 * @param message The message you want to pass
	 */
	public void postMessage(QueueType queueType, int id, char eventType, String message) {
		lock.lock();
		try {
			if (queueType.equals(QueueType.INPUT)) {
				while(getEventSize(QueueType.INPUT) > 5) {
					try {
						LOG.info("Input Queue has more than 5 commands.");
						inputQueueFull.await();
					} catch (InterruptedException e) {
						LOG.error(e.getMessage());
						e.printStackTrace();
					}
				}
				
			}
		} finally {
			lock.unlock();
		}
		
		switch (queueType) {
		case INPUT:
			inputs.add(new Event(id, eventType, message));
			LOG.info("Successfully added new message to input queue: " + message + ". Input Queue size is now: " + getEventSize(QueueType.INPUT));
			break;
		case OUTPUT :
			outputs.add(new Event(id, eventType, message));
			LOG.info("Added new message to output queue: " + message);
			break;
		}
	}
	
	/**
	 * Gets the first Event in the specified Input or Output array that matches the id and head parameters.
	 * @param queueType Type the event array type, INPUT or OUTPUT
	 * @param id identifier for the receiver like thread number
	 * @param eventType The event type like C for command, E for exit  
	 * @param remove
	 * @return the message in the event
	 */
	public String getMessage(QueueType queueType, int id, char eventType, boolean remove) {
		String message = null;
		
		lock.lock();

		try {
			switch (queueType) {
			case INPUT:
				message = getMessage(inputs, id, eventType, remove);
				System.out.println("Queue size is now: " + getEventSize(QueueType.INPUT));
				if (getEventSize(QueueType.INPUT) <= 2) {
					inputQueueFull.signal();
					LOG.info("Input queue is low on commands. Has signaled Command Generator Thread to awaken");
				}
				break;
			case OUTPUT :
				message = getMessage(outputs, id, eventType, remove);
				break;
			}
			
			return message;
		}
		finally {
			lock.unlock();
		}

		
	}
	
	/**
	 * Return all events matching the select queue with matching id and eventType  
	 * @param queueType Type the event array type, INPUT or OUTPUT
	 * @param id identifier for the receiver like thread number
	 * @param eventType The event type like C for command, E for exit  
	 * @param remove remove the event read
	 * @return An ArrayList of the matching events in the queue
	 */
	public ArrayList<String> getAllMessages(QueueType queueType, int id, char eventType, boolean remove) {
		ArrayList<String> message = null;
		lock.lock();
		try {
			switch (queueType) {
			case INPUT:
				message = getAllMessages(inputs, id, eventType, remove);
				break;
			case OUTPUT :
				message = getAllMessages(outputs, id, eventType, remove);
				break;
			}	
		
		return message;
		}
		finally {
			lock.unlock();
		}

		
	}	

	/**
	 * Return an ArrayList with the contents of the selected queue
	 * @param queueType the event array type, INPUT or OUTPUT
	 * @return An ArrayList of the events in the queue
	 */
	public ArrayList<Event> getEventQueue(QueueType queueType){
		ArrayList<Event> outEvent = null;
		
		switch (queueType) {
			case INPUT:
				outEvent = inputs;
				break;
			case OUTPUT :
				outEvent = outputs;
				break;
		}	

		return (outEvent);
	}
	
	/**
	 * Return the number of events in the selected queue
	 * @param queueType the event array type, INPUT or OUTPUT
	 * @return the size of the queue
	 */
	public int getEventSize(QueueType queueType){
		int outsize = 0;
		
		switch (queueType) {
		case INPUT:
			outsize = inputs.size();
			break;
		case OUTPUT :
			outsize = outputs.size();
			break;
		}	

		return outsize;
	}
	

	/**
	 * Get the message from the selected with the matching id and eventType queue. 
	 * If one exists return the message if not return null. 
	 * @param events The selected event queue
	 * @param id identifier for the receiver like thread number
	 * @param eventType The event type like C for command, E for exit  
	 * @param delete delete the event after reading it
	 * @return the message in the matching event or null if not found
	 */
	private String getMessage(ArrayList<Event> events, int id, char eventType, boolean delete) {
		String message = null; 

		for (Event event : events) {
			if (event.getId() == id && event.getType() == eventType) {		// first match
				
				if ((message = event.getMessage())!= null ) {
					if (delete == true) {
						events.remove(event);
					}
					break;
				}
			}
		}
		return message;
	}
	
	/**
	 * Return an ArrayList of all messages in a queue
	 * @param events The selected event queue
	 * @param id identifier for the receiver like thread number
	 * @param eventType The event type like C for command, E for exit  
	 * @param delete delete the event after reading it
	 * @return ArrayList of events
	 */
	private ArrayList<String> getAllMessages(ArrayList<Event> events, int id, char eventType, boolean delete) {
		ArrayList<String> messages = new ArrayList<String>(); 
		String message;

		for (Event event : events) {
			if (event.getId() == id && event.getType() == eventType) {		// first match
				
				if ((message = event.getMessage())!= null ) {
					messages.add(message);
					
					if (delete == true) {
						events.remove(event);
					}
					break;
				}
			}
		}
		return messages;
	}	
	
	/**
	 * Deletes all events in the select queue
	 * @param Queue type, INPUT or OUTPUT 
	 */
	public void clear(QueueType type) {
		switch (type) {
		case INPUT:
			inputs.clear();
			break;
		case OUTPUT :
			outputs.clear();
			break;
		}	
		
	}
	
	/**
	 * Delete all events in all queues
	 */
	public void clearAll() {
		clear(QueueType.INPUT);
		clear(QueueType.OUTPUT);
	}	
	

}
