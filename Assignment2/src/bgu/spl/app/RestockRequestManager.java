package bgu.spl.app;

import java.util.*;
import java.util.concurrent.atomic.*;;

/**
 * The Class holds the information about a specific {@link RestockRequest} and its status.
 * uses the {@link ManagmentService}
 */
public class RestockRequestManager {
	

		/** The number of shoes that will be added to the store after the request will be completed
		 *  i.e the number of shoes the manger asked from the factory - the number of restock requests for this shoe */
		private AtomicInteger numOfExpectedShoes;
		
		/** true, if the manager received the needed shoes from the factory */
		private boolean completed;
		
		/** all the requests that will be fulfilled when {@code completed==true} */
		private LinkedList<RestockRequest> requests; 
		
		private String shoeType;
		
		/**
		 * the tick in which the manager sent the relevant {@link ManufacturingOrderRequest} */
		private int requestedTick;
		
		/**
		 * Instantiates a new restock request manager.
		 *
		 * @param numOfExpectedShoes the number of shoes sent to the {@link ShoeFactoryService} to manufacture
		 * @param shoeType 			the type of the requested shoe
		 * @param requestedTick		the tick in which the manager sent the relevant {@link ManufacturingOrderRequest}
		 */
		public RestockRequestManager(AtomicInteger numOfExpectedShoes, String shoeType, int requestedTick) {
			this.numOfExpectedShoes = numOfExpectedShoes;
			this.completed = false;
			requests=new LinkedList<RestockRequest>();
			this.shoeType=shoeType;
			this.requestedTick=requestedTick;
		}
		
		/**
		 * Gets the shoeType.
		 *
		 * @return the shoeType of the request
		 */
		public String getShoeType() {
			return shoeType;
		}

		/**
		 * Gets the issuedTick.
		 *
		 * @return the issuedTick of the manufacturing request
		 */
		public int getRequestedTick() {
			return requestedTick;
		}

		/**
		 * Gets the numOfExpectedShoes.
		 *
		 * @return the number of shoes that will be added to the store after the request will be completed
		 * 			the number of shoes the manger asked from the factory - the number of restock requests for this shoe
		 */
		public AtomicInteger getnumOfExpectedShoes() {
			return numOfExpectedShoes;
		}
		
		/**
		 * Checks if is completed.
		 *
		 * @return true, if the manufacturing order is completed
		 */
		public boolean isCompleted() {
			return completed;
		}
		
		/**
		 * Sets the completed.
		 *
		 * @param completed 		is the manufacturing order is completed
		 */
		public void setCompleted(boolean completed) {
			this.completed = completed;
		}
		
		/**
		 * Adds a request that will be fulfilled when {@code completed==true}.
		 *
		 * @param request the request
		 */
		public void addRequest(RestockRequest request) {
			requests.add(request);
			numOfExpectedShoes.decrementAndGet();
		}
		
		/**
		 * Gets the list of all the requests that will be fulfilled when {@code completed==true} .
		 *
		 * @return the requests
		 */
		public LinkedList<RestockRequest> getRequests() {
			return requests;
		}

}
