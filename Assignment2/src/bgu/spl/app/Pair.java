package bgu.spl.app;

import java.util.concurrent.atomic.*;


/**
 * The Class Pair holds a ManufacturingOrderRequest and an AtomicInteger with the remaining amount of shoes to manufacture.
 * This class is used by {@link ShoeFactoryService}
 */
public class Pair {
	
	private ManufacturingOrderRequest request;
	private AtomicInteger remainingAmountOfShoes;
	
	/**
	 * Instantiates a new pair of {@link ManufacturingOrderRequest} and an {@link AtomicInteger}
	 *
	 * @param request 		the manufacturing order request
	 * @param remainingAmountOfShoes	 the remaining amount of shoes to manufacture
	 */
	public Pair(ManufacturingOrderRequest request) {
		this.request = request;
		this.remainingAmountOfShoes = new AtomicInteger(request.getAmount());
	}
	
	/**
	 * Gets the remaining amount of shoes.
	 *
	 * @return the remaining amount of shoes to manufacture
	 */
	public AtomicInteger getRemainingAmountOfShoes() {
		return remainingAmountOfShoes;
	}

	/**
	 * Sets the remaining amount of shoes to manufacture.
	 */
	public void setRemainingAmountOfShoes() {
		remainingAmountOfShoes.decrementAndGet();
	}

	/**
	 * Gets the requests.
	 *
	 * @return the manufacturing order request
	 */
	public ManufacturingOrderRequest getRequest() {
		return request;
	}

	
	
	


}
