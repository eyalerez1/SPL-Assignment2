package bgu.spl.app;

import bgu.spl.mics.Broadcast;

/**
 * A broadcast messages that is sent at every passed clock tick..
 */
public class TickBroadcast implements Broadcast {

	private int currentTick;
	
	/**
	 * Instantiates a new tick broadcast.
	 *
	 * @param tick 		the first tick
	 */
	public TickBroadcast(int tick) {
		currentTick=tick;
	}
	
	/**
	 * Gets the current tick.
	 *
	 * @return 		the current tick
	 */
	public int getCurrentTick() {
		return currentTick;
	}

}
