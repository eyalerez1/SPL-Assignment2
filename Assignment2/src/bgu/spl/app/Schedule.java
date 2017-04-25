package bgu.spl.app;

import java.util.Comparator;

// TODO: Auto-generated Javadoc
/**
 * An abstract class that any schedule in the system extends.
 * has a {@link Comparator<Schedule>} comparing between schedules according to their {@code tick}
 */
public abstract class Schedule {
	
	/**
	 * compares between schedules according to their {@code tick}
	 * 
	 */
	private static class ScheduleComparator implements Comparator<Schedule> {
		
		/**
		 * compares between schedules according to their {@code tick}
		 * 
		 * @param p1		a {@link Schedule} to compare with {@code p2}
		 * @param p2		a {@link Schedule} to compare with {@code p1}
		 * @return	a positive integer, if {@code p1} is larger than {@code p2} 
		 * 			a negative integer, if {@code p2} is larger than {@code p1}
		 * 			0, if {@code p1} is equal to {@code p2}
		 */
		public int compare(Schedule p1, Schedule p2) {
			return p1.getTick()-p2.getTick();
		}
	}
	
	/**
	 * Gets the comparator.
	 *
	 * @return the comparator
	 */
	public static Comparator<Schedule> getComparator() {
		return new ScheduleComparator();
	}
	
	/**
	 * abstract method. must be implemented by subclasses.
	 * Gets the tick.
	 *
	 * @return the tick
	 */
	public abstract int getTick();

}
