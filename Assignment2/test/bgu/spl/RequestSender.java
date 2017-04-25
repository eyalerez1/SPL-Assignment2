package bgu.spl;

import bgu.spl.mics.MicroService;
import java.util.concurrent.atomic.*;


public class RequestSender extends MicroService {
	private AtomicInteger counter;
	
	public RequestSender(String name, AtomicInteger counter) {
		super(name);
		this.counter=counter;
	}

	@Override
	protected void initialize() {
		boolean b=sendRequest(new Request1(),(result)->{counter.incrementAndGet();});
		if(b) {
			counter.incrementAndGet();
		}
	}

}
