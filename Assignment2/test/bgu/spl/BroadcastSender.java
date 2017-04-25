package bgu.spl;

import bgu.spl.mics.MicroService;

public class BroadcastSender extends MicroService {
	public BroadcastSender(String name) {
		super(name);
	}

	protected void initialize() {
		sendBroadcast(new Broadcast1());
	}
}
