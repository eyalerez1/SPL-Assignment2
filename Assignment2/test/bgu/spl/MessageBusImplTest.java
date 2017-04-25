package bgu.spl;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.*;
import bgu.spl.mics.impl.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bgu.spl.mics.MessageBus;
import bgu.spl.mics.impl.MessageBusImpl;

import bgu.spl.mics.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bgu.spl.mics.MessageBus;
import bgu.spl.mics.impl.MessageBusImpl;

public class MessageBusImplTest {
	
	MessageBusImpl m;
	//public static AtomicInteger counter;
	
	@Before
	public void setUp() throws Exception {
		
		m=MessageBusImpl.getInstance();
		//counter=new AtomicInteger(0);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInstance() {
		assertEquals(true,MessageBusImpl.getInstance()==m);
	}

	@Test
	public void testRequestAndComplete() {
		AtomicInteger counter=new AtomicInteger(0);
		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.execute(new RequestHandler("rh1",counter));
		try {
			Thread.currentThread().sleep(750);
		} catch (InterruptedException e){}
		executor.execute(new RequestSender("rs1",counter));
		try {
			Thread.currentThread().sleep(2000);
		} catch (InterruptedException e){}
		assertEquals(3, counter.get());	
	}

	@Test
	public void testBroadcast() {
		AtomicInteger counter=new AtomicInteger(0);
		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.execute(new BroadcastHandler("bh1",counter));
		try {
			Thread.currentThread().sleep(750);
		} catch (InterruptedException e){}
		executor.execute(new BroadcastSender("bs1"));
		try {
			Thread.currentThread().sleep(2000);
		} catch (InterruptedException e){}
		assertEquals(1, counter.get());	
	}


	@Test
	public void testRegister() {
		//no way to check
	}

	@Test
	public void testUnregister() {
		AtomicInteger counter=new AtomicInteger(0);
		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.execute(new OneRequestHandler("rh1",counter));
		try {
			Thread.currentThread().sleep(750);
		} catch (InterruptedException e){}
		executor.execute(new TwoRequestsSender("rs1",counter));
		try {
			Thread.currentThread().sleep(5000);
		} catch (InterruptedException e){}
		assertEquals(-1, counter.get());	
	}

	@Test
	public void testAwaitMessage() {
		//no way to check
	}

}
