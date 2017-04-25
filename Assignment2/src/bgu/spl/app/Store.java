package bgu.spl.app;
import java.util.*;
import java.util.logging.Logger;

/**
 * The Class Store holds the shoe storage and a list of receipts.
 */
public class Store {
	
	private HashMap<String, ShoeStorageInfo> storage;
	private LinkedList<Receipt> receipts;
	private Logger logger=Logger.getLogger("ShoeStoreLogger");

	
	/**
	 * The Class StoreHolder is a tool to make store a Singelton.
	 */
	private static class StoreHolder {
		
		/** The instance. */
		private static Store instance=new Store();
	}
	
	/**
	 * Instantiates a new store.
	 */
	private Store() {
		storage=new HashMap<String, ShoeStorageInfo>();
		receipts=new LinkedList<Receipt>();
	}
	
	/**
	 * Gets the single instance of Store.
	 *
	 * @return single instance of Store
	 */
	public static Store getInstance() {
		return StoreHolder.instance;
	}
	
	/**
	 * Load the shoe storage with new shoes.
	 * this method is invoked only once immediately after the Store is initialized
	 *
	 * @param storage the shoe's to add
	 */
	public void load(ShoeStorageInfo[] storage) {
		for(int i=0 ; i<storage.length ; i++) {
			String shoeType= storage[i].getShoeType();
			this.storage.put(shoeType, storage[i]);
		}
		logger.info("The store's storage was loaded");
	}
	
	/**
	 * Take one shoe out of the storage.
	 *
	 * @param shoeType 			the shoe type to take
	 * @param onlyDiscount 		whether to take a shoe only on discount or not
	 * @return a {@link BuyResult}
	 */
	public BuyResult take(String shoeType, boolean onlyDiscount) {
		logger.info("The store's take method was invoked");

		ShoeStorageInfo shoe=storage.get(shoeType);
		if(shoe==null) {
			if(onlyDiscount) {
				return BuyResult.NOT_ON_DISCOUNT;
			}else {
				return BuyResult.NOT_IN_STOCK;
			}
		} else {
			synchronized (shoe) {
				if(shoe.getAmountOnStorage()==0) {
					if(onlyDiscount) {
						return BuyResult.NOT_ON_DISCOUNT;
					}else {
						return BuyResult.NOT_IN_STOCK;
					}
				} else {
					if(onlyDiscount && shoe.getDiscountedAmount()==0) {
						return BuyResult.NOT_ON_DISCOUNT;
					} else {
						shoe.takeOne();
						if(shoe.getDiscountedAmount()>0) {
							shoe.takeOneOnDiscount();
							return BuyResult.DISCOUNTED_PRICE;
						} else {
							return BuyResult.REGULAR_PRICE;
						}
					}
				}	
			}
			
		}
	}
	
	/**
	 * Adds an amount of shoeType to storage.
	 *
	 * @param shoeType 		the shoe type to add
	 * @param amount 		the amount to add
	 */
	public void add(String shoeType, int amount) {
		ShoeStorageInfo shoe=storage.get(shoeType);
		if(shoe==null) {
			storage.put(shoeType, new ShoeStorageInfo(shoeType,amount));
		} else {
			shoe.addShoe(amount);
		}
		logger.info(amount+" "+shoeType+" was added to the store");
	}
	
	/**
	 * Adds an amount of discounted shoeType to storage.
	 *
	 * @param shoeType 		the shoe type
	 * @param amount 		the amount of shoes on discount to add
	 */
	public void addDiscount(String shoeType, int amount) {
		ShoeStorageInfo shoe=storage.get(shoeType);
		if(shoe!=null) {
			shoe.addDiscountShoe(amount);
		}
	}
	
	/**
	 * Add a receipt to the receipts list.
	 *
	 * @param receipt 		the receipt to file
	 */
	public void file(Receipt receipt) {
		synchronized(receipts) {
			receipts.add(receipt);
			logger.info("new receipt filed");
		}
	}
	
	/**
	 * Prints the shoe's storage info and receipts.
	 */
	public void print() {
		Collection<ShoeStorageInfo> shoes=storage.values();
		System.out.println("Shoes in storage:");
		for(ShoeStorageInfo shoe : shoes) {
			System.out.println(shoe.toString());
		}
		
		System.out.println("Receipts:");
		for(Receipt r : receipts) {
			System.out.println(r.toString());
		}
		
	}

}
