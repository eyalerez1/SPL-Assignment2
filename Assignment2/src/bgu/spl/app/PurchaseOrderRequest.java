package bgu.spl.app;
import bgu.spl.mics.Request;


/**
 * A request that is sent when the a store client wishes to buy a shoe.
 */
public class PurchaseOrderRequest implements Request<Receipt> {
	
	private String shoeType;
	private boolean onlyDiscount;
	private String requester;
	private int issuedTick;
	
	/**
	 * Instantiates a new purchase order request.
	 *
	 * @param shoeType the shoe type
	 * @param onlyDiscount whether or not the customer wants to buy only on discount
	 * @param requester the requester
	 * @param issuedTick the issued tick of the request
	 */
	public PurchaseOrderRequest(String shoeType, boolean onlyDiscount, String requester,  int issuedTick) {
		this.shoeType = shoeType;
		this.onlyDiscount = onlyDiscount;
		this.requester=requester;
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
	 * Checks whether or not the customer wants to buy only on discount.
	 *
	 * @return true, if the customer wants to buy only on discount
	 * 			false, otherwise
	 */
	public boolean isOnlyDiscount() {
		return onlyDiscount;
	} 
	
	/**
	 * Gets the requester.
	 *
	 * @return the requester
	 */
	public String getRequester() {
		return requester;
	}
	
	/**
	 * Gets the issued tick.
	 *
	 * @return the issued tick of the request
	 */
	public int getIssuedTick() {
		return issuedTick;
	}

}
