package bgu.spl.app;

import java.util.logging.Logger;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.impl.MessageBusImpl;

/**
 * This  {@ link MicroService} describes a seller that sells shoes and asks restock requests.
 * Handles {@link PurchaseOrderRequest.}
 */
public class SellingService extends MicroService {
	
	private int currentTick;
	private Store myStore;
	private Logger logger=Logger.getLogger("ShoeStoreLogger");
	 
	/**
	 * Instantiates a new selling service.
	 */
	public SellingService(String num) {
		 super("Seller"+num);
		 myStore=Store.getInstance();
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
		});
		logger.info(super.getName()+" subscribed to TickBroadcast");
		
		subscribeBroadcast(TerminationBroadcast.class, (TerminationBroadcast t)->{
			logger.info(super.getName()+" is about to terminate");
			terminate();
		} );
		logger.info(super.getName()+" subscribed to TickBroadcast");
		
		subscribeRequest(PurchaseOrderRequest.class, (PurchaseOrderRequest request)-> {
			//callback of PurchaseOrderRequest
			logger.info(super.getName()+" received a PurchaseOrderRequest by "+request.getRequester());
			BuyResult buyResult=myStore.take(request.getShoeType(), request.isOnlyDiscount());
			switch (buyResult){
			case NOT_IN_STOCK: 
				sendRequest(new RestockRequest(request.getShoeType()),(result)->{
					//callback of RestockRequest completed
					if(!result) {
						logger.info(super.getName()+" sent purchaseRequest completed unsuccessfully");
						complete(request,null);
					} else {
						Receipt receipt=new Receipt(super.getName(), request.getRequester(), request.getShoeType(), false, currentTick, request.getIssuedTick(), 1);
						myStore.file(receipt);
						logger.info(super.getName()+" sent purchaseRequest completed successfully");
						complete(request,receipt);
					}
					//end of callback of RestockRequest completed
				});
				logger.info(super.getName()+" sent a RestockRequest of "+request.getShoeType());
				break;
				
			case NOT_ON_DISCOUNT:
				complete(request,null);
				break;
				
			case REGULAR_PRICE:
				Receipt receipt= new Receipt(super.getName(), request.getRequester(), request.getShoeType(), false, currentTick, request.getIssuedTick(), 1);
				myStore.file(receipt);
				complete(request, receipt);
				break;
				
			case DISCOUNTED_PRICE:
				Receipt discountReceipt= new Receipt(super.getName(), request.getRequester(), request.getShoeType(), true, currentTick, request.getIssuedTick(), 1);
				myStore.file(discountReceipt);
				complete(request, discountReceipt);
				break;
			}//end of callback of PurchaseOrderRequest				
		});
		logger.info(super.getName()+" subscribed to PurchaseOrderRequest");
	
		logger.info(super.getName()+" initialized successfully");
		synchronized(MessageBusImpl.getInstance()) {			
			sendBroadcast(new InisializeCompleted());
			try {
					MessageBusImpl.getInstance().wait();
			}catch(InterruptedException e) {}
		}
		
	}
}
