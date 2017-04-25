package bgu.spl.app;

import java.util.logging.Logger;

/**
 * An object which represents information about a single type of shoe in the store.
 */
public class ShoeStorageInfo {
	
	private String shoeType;
	private int amountOnStorage;
	private int discountedAmount;
	private Logger logger=Logger.getLogger("ShoeStoreLogger");
	
	/**
	 * Instantiates a new shoe storage info.
	 *
	 * @param shoeType the shoe type
	 * @param amountOnStorage the amount on storage
	 */
	public ShoeStorageInfo(String shoeType, int amountOnStorage) {
		this.shoeType=shoeType;
		this.amountOnStorage=amountOnStorage;
		this.discountedAmount=0;
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
	 * Gets the amount on storage.
	 *
	 * @return the amount on storage
	 */
	public int getAmountOnStorage() {
		return amountOnStorage;
	}

	/**
	 * Sets the amount on storage.
	 *
	 * @param amountOnStorage 		the amount to add to the amount on storage
	 */
	private void setAmountOnStorage(int amountOnStorage) {
		this.amountOnStorage += amountOnStorage;
	}

	/**
	 * Gets the discounted amount.
	 *
	 * @return the discounted amount
	 */
	public int getDiscountedAmount() {
		return discountedAmount;
	}

	/**
	 * Sets the discounted amount.
	 *
	 * @param discountedAmount the amount to add to the discounted amount
	 */
	private void setDiscountedAmount(int discountedAmount) {
		this.discountedAmount += discountedAmount;
	}
	
	/**
	 * Take one shoe of the store.
	 */
	public synchronized void takeOne() {
		setAmountOnStorage(-1);
	}

	/**
	 * Take one shoe on discount from the store.
	 * takes only from discount, does not effect the amount on storage
	 */
	public synchronized void takeOneOnDiscount() {
		setDiscountedAmount(-1);
	}
	
	/**
	 * Add shoes.
	 *
	 * @param amount 		the amount of shoes to add to storage
	 */
	public synchronized void addShoe(int amount) {
		setAmountOnStorage(amount);
	}
	
	/**
	 * Adds discounted shoes according to amount
	 * will add no more then the number of shoes in stock 
	 *
	 * @param amount 		the amount of discounted shoes to add
	 */
	public synchronized void addDiscountShoe(int amount) {
		int min=Math.min(amount, amountOnStorage-discountedAmount);
		setDiscountedAmount(amount);
		logger.info(min+" more "+shoeType+" on discount in the store");
	}
	
	/**
	 * Make a String from the shoe's details.
	 *
	 * @return a String of the shoe's details
	 */
	@Override
	public String toString() {
		return "shoeType: " + shoeType + ", amountOnStorage: " + amountOnStorage + ", discountedAmount: "
				+ discountedAmount + ".";
	}
}
