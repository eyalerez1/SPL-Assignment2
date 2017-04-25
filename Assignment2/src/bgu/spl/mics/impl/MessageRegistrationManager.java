 
package bgu.spl.mics.impl;
import bgu.spl.mics.*;
import java.util.*;
import java.util.concurrent.atomic.*;


/**
 * The Class MessageRegistrationManager contains a list of MicroServices who are all subscribed to the same message type.
 * The Class also contains an AtomicInteger uses as iterator.
 */
class MessageRegistrationManager {
	
	private AtomicInteger iterator;
	private ArrayList<MicroService> microServices;
	private AtomicInteger numberOfMicroServices;
	
	/**
	 * Instantiates a new message registration manager.
	 */
	public MessageRegistrationManager() {
		iterator=new AtomicInteger(-1);
		microServices=new ArrayList<MicroService>();
		numberOfMicroServices=new AtomicInteger(0);
	}
	
	/**
	 * Adds the MicroService to the MicroServices list.
	 *
	 * @param microService the micro service
	 */
	public synchronized void add(MicroService microService) {
		if(!(microServices.contains(microService))) {
			microServices.add(microService);
			numberOfMicroServices.incrementAndGet();
		}
	}
	
	/**
	 * Removes the MicroService from the MicroServices list.
	 *
	 * @param microService the micro service
	 */
	public synchronized void remove(MicroService microService) {
		if((microServices.contains(microService))) {
			microServices.remove(microService);
			numberOfMicroServices.decrementAndGet();
		}
	}
	
	/**
	 * Gets the number of micro services.
	 *
	 * @return the number of micro services
	 */
	public int getNumberOfMicroServices(){
		return numberOfMicroServices.get();
	}
	
	/**
	 * Return the next micro service on the list.
	 *
	 * @return the nest micro service
	 */
	public synchronized MicroService next() {
		while(numberOfMicroServices.get()<=0) {
			try{
				this.wait();
			} catch (Exception e) {	}
		}
		iterator.incrementAndGet();
		if(iterator.get()>numberOfMicroServices.get()-1) {
			iterator.set(0);
		}
		return microServices.get(iterator.get());
	}
	
	public synchronized MicroService[] getMicroServices() {
		return microServices.toArray(new MicroService[microServices.size()]);
	}
}
