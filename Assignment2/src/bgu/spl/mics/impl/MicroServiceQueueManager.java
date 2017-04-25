package bgu.spl.mics.impl;
import bgu.spl.mics.*;
import java.util.*;
import java.util.concurrent.*;


/**
 * The Class MicroServiceQueueManager contains a MicroService and all his messages.
 */
class MicroServiceQueueManager {
	
	private MicroService microService;
	private PriorityBlockingQueue<Message> messageQueue;
	
	/**
	 * Instantiates a new micro service queue manager.
	 *
	 * @param microService the micro service
	 */
	public MicroServiceQueueManager(MicroService microService) {
		this.microService=microService;
		messageQueue=new PriorityBlockingQueue<Message>();
	}
	
	/**
	 * Adds the message to the MicroService's message list.
	 *
	 * @param message the message
	 */
	public void addMessage(Message message) {
		messageQueue.put(message);
	}
	
	/**
	 * Return the next message.
	 *
	 * @return the next message
	 */
	public Message nextMessage() {
		Message m=null;
		try {
			m=messageQueue.take();
		} catch(InterruptedException e){}
		return m;
	}
	
	/**
	 * Gets the micro service type.
	 *
	 * @return the micro service type
	 */
	public String getMicroServiceType() {
		return microService.getClass().getName();
	}
	
}
