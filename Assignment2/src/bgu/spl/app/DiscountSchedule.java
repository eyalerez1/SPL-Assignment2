package bgu.spl.app;

/**
 * An object which describes a schedule of a single discount that the manager will add to a specific
 * shoe at a specific tick.
 * Inherits a comparator from {@link Schedule} 
 * @author USER
 *
 */

public class DiscountSchedule extends Schedule {
	
	private String shoeType;
	private int tick;
	private int amount;
	
	/**
	 * Instantiates a new discount schedule 
	 * 
	 * @param shoeType		the shoe type
	 * @param tick			the tick to release the discount
	 * @param amount		the amount of shoes to put on discount
	 */
	
	public DiscountSchedule(String shoeType, int tick, int amount) {
		this.shoeType = shoeType;
		this.tick = tick;
		this.amount = amount;
	}

	/**
	 * @return a {@link String} representing the {@code shoeType}
	 */
	public String getShoeType() {
		return shoeType;
	}

	/**
	 * @return an {@code int} representing the {@code tick}
	 */
	public int getTick() {
		return tick;
	}

	/**
	 * @return an {@code int} representing the {@code amount}
	 */
	public int getAmount() {
		return amount;
	}
	
	
}
