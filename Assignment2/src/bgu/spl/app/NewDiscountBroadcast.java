package bgu.spl.app;

import bgu.spl.mics.Broadcast;


/**
 * A broadcast message that is sent when the manager of the store
decides to have a sale on a specific shoe.
 */
public class NewDiscountBroadcast implements Broadcast {

	private String shoeType;
	private int amount;
	
	/**
	 * Instantiates a new new discount broadcast.
	 *
	 * @param shoeType the shoe type on discount
	 * @param amount the amount of shoes on discount
	 */
	public NewDiscountBroadcast(String shoeType, int amount) {
		this.shoeType=shoeType;
		this.amount=amount;
	}

	/**
	 * Gets the shoe type.
	 *
	 * @return the shoe type on discount
	 */
	public String getShoeType() {
		return shoeType;
	}

	/**
	 * Gets the amount.
	 *
	 * @return the amount of shoes on discount
	 */
	public int getAmount() {
		return amount;
	}
	
	

}
