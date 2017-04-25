package bgu.spl.app;

import bgu.spl.mics.MicroService;

import bgu.spl.mics.impl.MessageBusImpl;

import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.logging.Logger;

/**
 * The  {@ link MicroService} is a {@link MicroService} that uses the {@link MessageBus} in
 * order to communicate with other services.
 * @author USER
 *
 */

public class ManagmentService extends MicroService {
	
	private int currentTick;
	private Store myStore;
	private PriorityQueue<DiscountSchedule> discountSchedule;
	private ArrayList<RestockRequestManager> expectedShoesNumber;
	private Logger logger=Logger.getLogger("ShoeStoreLogger");
	
	/**
	 * Instantiates a new managment service
	 * 
	 * @param discountSchedule		a list of {@link discountSchedule}
	 */
	public ManagmentService(List<DiscountSchedule> discountSchedule) {
		super("manager");
		myStore=Store.getInstance();
		this.currentTick = 1;
		this.discountSchedule = new PriorityQueue<DiscountSchedule>(1, DiscountSchedule.getComparator()); 
		this.discountSchedule.addAll(discountSchedule);
		expectedShoesNumber= new ArrayList<RestockRequestManager>();
		
	}
	
	/**
	 * A method which goes over all the {@link RestockRequestManager} in {@code expectedShoesNumber} in a reverse order
	 * and finds the last {@link RestockRequestManager} matching to {@code shoeType} 
	 * @param shoeType 			the type of shoe to match with the {@code shoeType} of the relevant {@link RestockRequestManager} 
	 * @return the last {@link RestockRequestManager} in {@code expectedShoesNumber}  matching to {@code shoeType}, 
	 * 			{@code null} if no such element exists
	 */
	private RestockRequestManager getRestockRequestManagerByShoeType(String shoeType) {
		for(int i=expectedShoesNumber.size()-1 ; i>=0 ; i--) {
			if(expectedShoesNumber.get(i).getShoeType().equals(shoeType)) {
				return expectedShoesNumber.get(i);
			}
		}
		return null;
	}
	
	/**
	 * A method which goes over all the {@link RestockRequestManager} in {@code expectedShoesNumber} 
	 * and finds the {@link RestockRequestManager} matching to {@code shoeType} and {@code receiptTick}
	 * @param shoeType			the type of shoe to match with the {@code shoeType} of the relevant {@link RestockRequestManager} 
	 * @param receiptTick		the tick to match witch {@code receiptTick} of the relevant {@link RestockRequestManager}
	 * @return the {@link RestockRequestManager} in {@code expectedShoesNumber}  matching to {@code shoeType} and to {@code receiptTick}, 
	 * 			{@code null} if no such element exists
	 */
	private RestockRequestManager getRestockRequestManagerByIssuedTick(String shoeType, int receiptTick) {
		for (int i = 0; i < expectedShoesNumber.size(); i++) {
			if(expectedShoesNumber.get(i).getShoeType().equals(shoeType)) {
				if(expectedShoesNumber.get(i).getRequestedTick()==receiptTick) {
					return expectedShoesNumber.get(i);
				}
			}
		}
		return null;
	}
	
    /**
	 * subscribes to relevant Requests and Broadcasts
	 * this method is called once when the event loop starts. 
	 */
	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, (TickBroadcast tick)->{
			currentTick=tick.getCurrentTick();
			logger.info("The manager updated his tick to "+currentTick);
			while(!discountSchedule.isEmpty() && discountSchedule.peek().getTick()==currentTick) {
				DiscountSchedule d=discountSchedule.poll();
				myStore.addDiscount(d.getShoeType(), d.getAmount());
				sendBroadcast(new NewDiscountBroadcast(d.getShoeType(),d.getAmount()));
				logger.info("The manager sent a new discount broadcast");
			}	
		});
		logger.info("The manager subscribed to TickBroadcast");

		subscribeBroadcast(TerminationBroadcast.class, (TerminationBroadcast t)->{
			logger.info("The manager is about to terminate");
			terminate();
		});
		logger.info("The manager subscribed to TerminationBroadcast");
		
		subscribeRequest(RestockRequest.class, (RestockRequest request)->{ //callback of RestockRequest
			RestockRequestManager restockRequestManagerByShoeType=getRestockRequestManagerByShoeType(request.getShoeType());
			boolean wasRecieved=true;
			if(restockRequestManagerByShoeType==null || restockRequestManagerByShoeType.getnumOfExpectedShoes().get()==0) {
				AtomicInteger numberOfOrders=new AtomicInteger((currentTick%5)+1);
				logger.info("Manager sent a ManufacturingOrderRequest of "+numberOfOrders+" of "+request.getShoeType());	
				wasRecieved=sendRequest(new ManufacturingOrderRequest(request.getShoeType(),numberOfOrders.get(),currentTick), (result)->{
					//callback of ManufacturingOrderRequest Completed
					logger.info("Manager received a ManufacturingOrderRequest completed of "+result.getAmountSold()+" of "+request.getShoeType());
					myStore.add(request.getShoeType(), getRestockRequestManagerByIssuedTick(result.getShoeType(),result.getRequestTick()).getnumOfExpectedShoes().get());
					myStore.file(result);
					RestockRequestManager restockRequestManager=getRestockRequestManagerByIssuedTick(result.getShoeType(),result.getRequestTick());
					restockRequestManager.setCompleted(true);
					while(!restockRequestManager.getRequests().isEmpty()) {
						complete(restockRequestManager.getRequests().remove(),true);
						logger.info("Manager sent a RestockRequest completed of "+request.getShoeType());
					}
					expectedShoesNumber.remove(restockRequestManager);
					//end of callback of ManufacturingOrderRequest Completed
				});
				if(!wasRecieved) {
					logger.info("The ManufacturingOrderRequest of "+numberOfOrders+" of "+request.getShoeType()+" was unsuccessfull");
					complete(request,false);
				} else {
					expectedShoesNumber.add(new RestockRequestManager(numberOfOrders,request.getShoeType(),currentTick));
				}
			}
			if(wasRecieved) {
				getRestockRequestManagerByShoeType(request.getShoeType()).addRequest(request);
			}
			//end of callback of RestockRequest
		});
		logger.info("The manager subscribed to RestockRequest");
		
		logger.info("The manager initialized successfully");
		synchronized(MessageBusImpl.getInstance()) {			
			sendBroadcast(new InisializeCompleted());
			try {
					MessageBusImpl.getInstance().wait();
			}catch(InterruptedException e) {}
		}
	}

}
