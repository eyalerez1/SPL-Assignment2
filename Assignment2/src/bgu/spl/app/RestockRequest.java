package bgu.spl.app;

import bgu.spl.mics.Request;

/**
 * A request that is sent by the selling service to the store manager so that he
 * will know that he need to order new shoes from a factory.
 */
public class RestockRequest implements Request<Boolean> {

	private String shoeType;

	/**
	 * Instantiates a new RestockRequest.
	 *
	 * @param shoeType the shoe type requested
	 */
	public RestockRequest(String shoeType) {
		this.shoeType = shoeType;
	}

	/**
	 * Gets the shoe type.
	 *
	 * @return the shoe type the shoe type requested
	 */
	public String getShoeType() {
		return shoeType;
	}
	
	
}
