package bgu.spl.app;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.impl.MessageBusImpl;

import java.util.*;
import java.util.logging.Logger;

/**
 * This  {@ link MicroService} describes a shoe factory that manufacture shoes for the store.
 * Handles {@link ManufacturingOrderRequest}
 */
public class ShoeFactoryService extends MicroService {

	private int currentTick;
	private LinkedList<Pair> orders;
	private Logger logger=Logger.getLogger("ShoeStoreLogger");

	/**
	 * Instantiates a new shoe factory service.
	 */
	public ShoeFactoryService(String num) {
		 super("Factory"+num);
		currentTick=1;
		orders=new LinkedList<Pair>();
	}
	 /**
	 * subscribes to relevant Requests and Broadcasts
	 * this method is called once when the event loop starts. 
	 */
	@Override
	protected void initialize() {

		subscribeBroadcast(TickBroadcast.class, (TickBroadcast tick)->{
			currentTick=tick.getCurrentTick();
			logger.info(super.getName()+" updated his tick to "+currentTick);
			Pair p=orders.peekFirst();
			if(p!=null) {
				if(p.getRemainingAmountOfShoes().get()==0) {
					logger.info(super.getName()+" finished manufactoring the "+p.getRequest().getShoeType()+" order and sent a receipt");
					complete(orders.pollFirst().getRequest(),new Receipt(super.getName(), "store", p.getRequest().getShoeType(), false, currentTick, p.getRequest().getIssuedTick(), p.getRequest().getAmount()));
				}
				p=orders.peekFirst();
				if(p!=null) {
					p.setRemainingAmountOfShoes();
					logger.info(super.getName()+" manufactored a "+p.getRequest().getShoeType());
				}
			}
		});
		logger.info(super.getName()+" subscribed to TickBroadcast");
		
		subscribeBroadcast(TerminationBroadcast.class, (TerminationBroadcast t)->{
			logger.info(super.getName()+" is about to terminate");
			terminate();
		} );
		logger.info(super.getName()+" subscribed to TerminationBroadcast");
		
		subscribeRequest(ManufacturingOrderRequest.class, (ManufacturingOrderRequest request)->{
			orders.add(new Pair(request));
		});
		
		logger.info(super.getName()+" initialized successfully");
		synchronized(MessageBusImpl.getInstance()) {			
			sendBroadcast(new InisializeCompleted());
			try {
					MessageBusImpl.getInstance().wait();
			}catch(InterruptedException e) {}
		}
	}
}
