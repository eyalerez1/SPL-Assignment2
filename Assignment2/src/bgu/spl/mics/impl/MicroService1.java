package bgu.spl.mics.impl;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.impl.MessageBusImpl;

public class MicroService1 extends MicroService {
	
	public MicroService1(String name) {
		super(name);
	}

    public void myFunc(){
		System.out.println("initializing "+this.getName());
		this.subscribeRequest(Request1.class, (Request1)->{System.out.println("I am "+this.getName()+" working on: "+Request1);
																	complete(Request1, true);
		});
		
		this.subscribeRequest(Request2.class, (Request2)->{System.out.println("I am "+this.getName()+" working on: "+Request2);
		complete(Request2, true);
			});
    }
	
	@Override
	protected void initialize() {
		System.out.println("initializing "+this.getName());
		this.subscribeRequest(Request1.class, (Request1)->{System.out.println("I am "+this.getName()+" working on: "+Request1);
																	complete(Request1, true);
		});
		
		this.subscribeRequest(Request2.class, (Request2)->{System.out.println("I am "+this.getName()+" working on: "+Request2);
		complete(Request2, true);
			});
		
	}
}
