package bgu.spl.app;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.impl.MessageBusImpl;

import java.util.*;
import java.util.logging.Logger;

/**
 * This  {@ link MicroService} describes one client connected to the web-site.
 */
public class WebsiteClientService extends MicroService {
	
	private int currentTick;
	PriorityQueue<PurchaseSchedule> purchaseSchedule;
	LinkedList<String> wishList;
	private int counter;
	private Logger logger=Logger.getLogger("ShoeStoreLogger");
	
	/**
	 * Instantiates a new web client service.
	 *
	 * @param name 					the name of the client
	 * @param purchaseSchedule 		the purchase schedule
	 * @param wishList 				the wish list
	 */
	public WebsiteClientService(String name, List<PurchaseSchedule> purchaseSchedule, LinkedList<String> wishList) {
		super(name);
		this.purchaseSchedule = new PriorityQueue<PurchaseSchedule>(1, PurchaseSchedule.getComparator());
		this.purchaseSchedule.addAll(purchaseSchedule);
		this.wishList = wishList;
		counter=this.wishList.size()+this.purchaseSchedule.size();
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
					while(!purchaseSchedule.isEmpty() && purchaseSchedule.peek().getTick()==currentTick) {
						PurchaseSchedule p=purchaseSchedule.poll();
						logger.info(super.getName()+" sent a PurchaseOrderRequest of "+p.getShoeType()+" from his purchaseSchedule");
						sendRequest(new PurchaseOrderRequest(p.getShoeType(),false, super.getName(),currentTick), (result)-> {
							if(result!=null) {	
								logger.info(super.getName()+" got the "+p.getShoeType()+" from his purchaseSchedule");
								counter--;
								if(counter==0) {
									logger.info(super.getName()+" is about to terminate");
									terminate();
								}
							} else {
								logger.info(super.getName()+" did not get the "+p.getShoeType()+" from his purchaseSchedule");
							}
						});
					}
				});
		logger.info(super.getName()+" subscribed to TickBroadcast");
		
		subscribeBroadcast(TerminationBroadcast.class, (TerminationBroadcast t)->{
			logger.info(super.getName()+" recieved termination broadcast and is about to terminate");
			terminate();} );
		logger.info(super.getName()+" subscribed to TerminationBroadcast");
		
		subscribeBroadcast(NewDiscountBroadcast.class, (NewDiscountBroadcast broadcast)->{ 
			if(wishList.contains(broadcast.getShoeType())) {
				logger.info(super.getName()+" sent a PurchaseOrderRequest of "+broadcast.getShoeType()+" from his wishlist");
				sendRequest(new PurchaseOrderRequest(broadcast.getShoeType(),true, super.getName(),currentTick), (result)-> {
					if(result!=null){
						logger.info(super.getName()+" got the "+broadcast.getShoeType()+" from his wishlist");
						wishList.remove(broadcast.getShoeType());
						counter--;
						if(counter==0) {
							logger.info(super.getName()+" is about to terminate");
							terminate();
						}
					} else {
						logger.info(super.getName()+" did not get the "+broadcast.getShoeType()+" from his wishlist");
					}
				});
			}
		});
		logger.info(super.getName()+" subscribed to NewDiscountBroadcast");
		logger.info(super.getName()+" initialized successfully");
		synchronized(MessageBusImpl.getInstance()) {			
			sendBroadcast(new InisializeCompleted());
			try {
					MessageBusImpl.getInstance().wait();
			}catch(InterruptedException e) {}
		}
		
	}
	
}
