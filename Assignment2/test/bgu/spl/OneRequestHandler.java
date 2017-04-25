package bgu.spl;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.impl.MessageBusImpl;
import java.util.concurrent.atomic.*;


public class OneRequestHandler extends MicroService{
	private AtomicInteger counter;
	
	public OneRequestHandler(String name, AtomicInteger counter) {
		super(name);
		this.counter=counter;
	}

	@Override
	protected void initialize() {
		this.subscribeRequest(Request1.class, (Request1)->{	counter.incrementAndGet();
			complete(Request1, true);
			MessageBusImpl.getInstance().unregister(this);}
				);
		
		terminate();
		

	}
}
