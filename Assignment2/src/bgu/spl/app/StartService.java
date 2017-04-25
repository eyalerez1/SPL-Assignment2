package bgu.spl.app;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.impl.MessageBusImpl;

import java.util.concurrent.atomic.*;
import java.util.logging.Logger;

/**
 * The  {@ link MicroService} StartService is making sure all the MicroServices will start to run on the same tick, after they are all initialized.
 * Terminates after notifying all the microServices that they can continue after the initialization stage.
 */
public class StartService extends MicroService {
	
	private AtomicInteger countDown;
	private Logger logger=Logger.getLogger("ShoeStoreLogger");

	
	/**
	 * Instantiates a new start service.
	 *
	 * @param countDown the number of MicroServices
	 */
	public StartService(int countDown) {
		super("startService");
		this.countDown=new AtomicInteger(countDown);
	}
	 /**
	 * subscribes to relevant Requests and Broadcasts
	 * this method is called once when the event loop starts. 
	 */
	@Override
	protected void initialize() {
		subscribeBroadcast(InisializeCompleted.class, (InisializeCompleted i)->{
			countDown.decrementAndGet();
			if(countDown.get()==0) {
				logger.info("StratService sent notifyAll");
				synchronized(MessageBusImpl.getInstance()) {
					MessageBusImpl.getInstance().notifyAll();
				}
				terminate();
			}
		});
		synchronized(MessageBusImpl.getInstance()) {		
			MessageBusImpl.getInstance().notifyAll();
		}
	}

}
