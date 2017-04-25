package bgu.spl;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.impl.MessageBusImpl;

import java.util.concurrent.atomic.*;


public class TwoRequestsSender extends MicroService {
	private AtomicInteger counter;
	
	public TwoRequestsSender(String name, AtomicInteger counter) {
		super(name);
		this.counter=counter;
	}

	@Override
	protected void initialize() {
		boolean b=sendRequest(new Request1(),(result)->{counter.incrementAndGet();});
		if(b) {
		counter.incrementAndGet();
		}
		
		try {			
				Thread.currentThread().sleep(3000);
		} catch (InterruptedException e){}
		
		b=sendRequest(new Request1(),(result)->{counter.incrementAndGet();});
		if(!b) {
			counter.set(-1);
		}
	}
}
