package bgu.spl;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bgu.spl.app.BuyResult;
import bgu.spl.app.ShoeStorageInfo;
import bgu.spl.app.Store;

public class StoreTest {

	private Store theStore;
	
	@Before
	public void setUp() throws Exception {
		theStore=Store.getInstance();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInstance() {
		assertEquals(true, theStore==Store.getInstance());
	}

	@Test
	public void testLoad() {
		ShoeStorageInfo[] shoes=new ShoeStorageInfo[3];
		shoes[0]=new ShoeStorageInfo("lili",1);
		shoes[1]=new ShoeStorageInfo("libi",2);
		shoes[2]=new ShoeStorageInfo("lihi",1);
		theStore.load(shoes);
		assertEquals(true, theStore.take("lili", false)==BuyResult.REGULAR_PRICE);
		assertEquals(true, theStore.take("lili", false)==BuyResult.NOT_IN_STOCK);
		assertEquals(true, theStore.take("libi", false)==BuyResult.REGULAR_PRICE);
		assertEquals(true, theStore.take("libi", false)==BuyResult.REGULAR_PRICE);
		assertEquals(true, theStore.take("lihi", false)==BuyResult.REGULAR_PRICE);
		assertEquals(true, theStore.take("libi", false)==BuyResult.NOT_IN_STOCK);
		assertEquals(true, theStore.take("lihi", false)==BuyResult.NOT_IN_STOCK);
	}

	@Test
	public void testTake() {
		ShoeStorageInfo[] shoes=new ShoeStorageInfo[1];
		shoes[0]=new ShoeStorageInfo("libi",3);
		theStore.load(shoes);
		assertEquals(true, theStore.take("libi", false)==BuyResult.REGULAR_PRICE);
		assertEquals(true, theStore.take("libi", true)==BuyResult.NOT_ON_DISCOUNT);
		theStore.addDiscount("libi",2);
		assertEquals(true, theStore.take("libi", false)==BuyResult.DISCOUNTED_PRICE);
		assertEquals(true, theStore.take("libi", true)==BuyResult.DISCOUNTED_PRICE);
		assertEquals(true, theStore.take("libi", false)==BuyResult.NOT_IN_STOCK);
		assertEquals(true, theStore.take("libi", true)==BuyResult.NOT_ON_DISCOUNT);
	}

	@Test
	public void testAdd() {
		ShoeStorageInfo[] shoes=new ShoeStorageInfo[1];
		shoes[0]=new ShoeStorageInfo("libi",1);
		theStore.load(shoes);
		theStore.add("libi", 1);
		assertEquals(true, theStore.take("lihi", false)==BuyResult.NOT_IN_STOCK);
		theStore.add("lihi", 2);
		assertEquals(true, theStore.take("libi", false)==BuyResult.REGULAR_PRICE);
		assertEquals(true, theStore.take("libi", false)==BuyResult.REGULAR_PRICE);
		assertEquals(true, theStore.take("libi", false)==BuyResult.NOT_IN_STOCK);
		theStore.add("libi", 1);
		assertEquals(true, theStore.take("libi", false)==BuyResult.REGULAR_PRICE);
		assertEquals(true, theStore.take("libi", false)==BuyResult.NOT_IN_STOCK);
		assertEquals(true, theStore.take("lihi", false)==BuyResult.REGULAR_PRICE);
		assertEquals(true, theStore.take("lihi", false)==BuyResult.REGULAR_PRICE);
		assertEquals(true, theStore.take("lihi", false)==BuyResult.NOT_IN_STOCK);
	}

	@Test
	public void testAddDiscount() {
		ShoeStorageInfo[] shoes=new ShoeStorageInfo[2];
		shoes[0]=new ShoeStorageInfo("libi",1);
		shoes[1]=new ShoeStorageInfo("lili",3);
		theStore.load(shoes);
		assertEquals(true, theStore.take("libi", true)==BuyResult.NOT_ON_DISCOUNT);
		theStore.addDiscount("libi", 2);
		assertEquals(true, theStore.take("libi", true)==BuyResult.DISCOUNTED_PRICE);
		assertEquals(true, theStore.take("libi", true)==BuyResult.NOT_ON_DISCOUNT);
		theStore.addDiscount("lili", 2);
		assertEquals(true, theStore.take("lili", false)==BuyResult.DISCOUNTED_PRICE);
		assertEquals(true, theStore.take("lili", false)==BuyResult.DISCOUNTED_PRICE);
		assertEquals(true, theStore.take("lili", false)==BuyResult.REGULAR_PRICE);		
	}

	@Test
	public void testFile() {
		//no way to check
	}

	@Test
	public void testPrint() {
		//no way to check
	}

}
