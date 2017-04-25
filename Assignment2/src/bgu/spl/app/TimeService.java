package bgu.spl.app;
import java.util.Timer;
import java.util.TimerTask;



import java.util.logging.Logger;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.impl.MessageBusImpl;

/**
 * This {@ link MicroService} is the global system timer.
 */
public class TimeService extends MicroService {
	
	private int speed;
	private int duration;
	private int currentTick;
	private Logger logger=Logger.getLogger("ShoeStoreLogger");
	private Timer t=new Timer(true);


	
	/**
	 * Instantiates a new time service.
	 *
	 * @param speed 		the number of milliseconds between each tick
	 * @param duration 		the duration of the program
	 */
	public TimeService(int speed, int duration) {
		super("timer");
		this.speed = speed;
		this.duration = duration;
		this.currentTick=0;		
	}
	 /**
		 * subscribes to relevant Requests and Broadcasts
		 * this method is called once when the event loop starts. 
		 */
	@Override
	protected void initialize(){
		subscribeBroadcast(TerminationBroadcast.class, (TerminationBroadcast t)->{
			logger.info(super.getName()+" is about to terminate");
			terminate();
		} );
		
		logger.info("The Timer initialized successfully");
		synchronized(MessageBusImpl.getInstance()) {			
			sendBroadcast(new InisializeCompleted());
			try {
					MessageBusImpl.getInstance().wait();
			}catch(InterruptedException e) {}
		}
		
		
		t.schedule(new TimerTask() {
			public void run() {
				currentTick++;
				if(currentTick>duration) {
					sendBroadcast(new TerminationBroadcast());
					logger.info("The timer is about to terminate");
					cancel();
				} else {					
					sendBroadcast(new TickBroadcast(currentTick));
					logger.info("The Timer sent TickBroadcast: "+currentTick);
				}
			}
		}, 0, speed);	
	}
}
