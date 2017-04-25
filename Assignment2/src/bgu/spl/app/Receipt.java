package bgu.spl.app;


/**
 * An object representing a receipt that should be sent to a client after buying a shoe.
 */
public class Receipt {
	
	private String seller;
	private String customer;
	private String shoeType;
	private boolean discount;
	private int issuedTick;
	private int requestTick;
	private int amountSold;
	
	/**
	 * Instantiates a new receipt.
	 *
	 * @param seller the seller
	 * @param customer the customer
	 * @param shoeType the shoe type bought
	 * @param discount the discount status (true if the shoe was bought on discount)
	 * @param issuedTick the issued tick of the receipt
	 * @param requestTick the request tick
	 * @param amountSold the amount sold
	 */
	public Receipt(String seller, String customer, String shoeType, boolean discount, int issuedTick, int requestTick, int amountSold) {
		this.seller = seller;
		this.customer = customer;
		this.shoeType = shoeType;
		this.discount = discount;
		this.issuedTick = issuedTick;
		this.requestTick = requestTick;
		this.amountSold = amountSold;
	}

	/**
	 * Gets the seller.
	 *
	 * @return the seller
	 */
	public String getSeller() {
		return seller;
	}

	/**
	 * Gets the customer.
	 *
	 * @return the customer
	 */
	public String getCustomer() {
		return customer;
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
	 * Checks if the purchase was on discount.
	 *
	 * @return true, if the purchase was on discount
	 */
	public boolean isDiscount() {
		return discount;
	}

	/**
	 * Gets the issued tick.
	 *
	 * @return the issued tick of the receipt
	 */
	public int getIssuedTick() {
		return issuedTick;
	}

	/**
	 * Gets the request tick.
	 *
	 * @return the request tick
	 */
	public int getRequestTick() {
		return requestTick;
	}

	/**
	 * Gets the amount sold.
	 *
	 * @return the amount sold
	 */
	public int getAmountSold() {
		return amountSold;
	}

	/**
	 * Gets the String type of a receipt.
	 *
	 * @return the toString receipt
	 */
	@Override
	public String toString() {
		return "Receipt \n seller: " + seller + ", customer: " + customer + ", shoeType: " + shoeType + ", discount: "
				+ discount + ", issuedTick: " + issuedTick + ", requestTick: " + requestTick + ", amountSold: "
				+ amountSold + ".";
	}
	
	
}
