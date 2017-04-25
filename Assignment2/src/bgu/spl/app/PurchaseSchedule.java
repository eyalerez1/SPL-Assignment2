package bgu.spl.app;

import java.util.*;

/**
 * The class contains purchase that the client needs to make on specific ticks.
 */
public class PurchaseSchedule extends Schedule {

	private String shoeType;
	private int tick;
	
	/**
	 * Instantiates a new purchase schedule.
	 *
	 * @param shoeType the shoe type to buy
	 * @param tick the tick to buy at
	 */
	public PurchaseSchedule(String shoeType, int tick) {
		this.shoeType = shoeType;
		this.tick = tick;
	}

	/**
	 * Gets the shoe type.
	 *
	 * @return the shoe type to buy
	 */
	public String getShoeType() {
		return shoeType;
	}

	/**
	 * Gets the tick.
	 *
	 * @return the tick to buy at
	 */
	public int getTick() {
		return tick;
	}
	

	

	


}
