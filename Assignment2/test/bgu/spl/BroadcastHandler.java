package bgu.spl;

import bgu.spl.mics.MicroService;
import java.util.concurrent.atomic.*;

public class BroadcastHandler extends MicroService {
	private  AtomicInteger counter;
	
	public BroadcastHandler(String name, AtomicInteger counter) {
		super(name);
		this.counter=counter;
	}

	protected void initialize() {
		this.subscribeBroadcast(Broadcast1.class, (Broadcast1)->{counter.incrementAndGet();});
	}

}
