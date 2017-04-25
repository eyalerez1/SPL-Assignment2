package bgu.spl.app;

import bgu.spl.mics.Request;

/**
 * A request that is sent when the {@link ManagmentService} wants that a
 * {@link ShoeFactoryService} will manufacture a shoe for the store.
 */
public class ManufacturingOrderRequest implements Request<Receipt> {

	private String shoeType;
	private int amount;
	private int issuedTick;

	/**
	 * Instantiates a new manufacturing order request.
	 *
	 * @param shoeType the shoe type
	 * @param amount the amount of shoes to manufacture
	 * @param issuedTick the tick when the request was issued
	 */
	public ManufacturingOrderRequest(String shoeType, int amount, int issuedTick) {
		this.shoeType = shoeType;
		this.amount = amount;
		this.issuedTick= issuedTick;
	}

	/**
	 * Gets the shoe type.
	 *
	 * @return the shoe type
	 */
	public String getShoeType() {
		return shoeType;
	}

	/**
	 * Gets the amount.
	 *
	 * @return the amount  of shoes to manufacture
	 */
	public int getAmount() {
		return amount;
	}
	
	/**
	 * Gets the issued tick.
	 *
	 * @return the tick when the request was issued
	 */
	public int getIssuedTick() {
		return issuedTick;
	}

	
	
	
	

}
