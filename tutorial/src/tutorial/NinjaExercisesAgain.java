package tutorial;

import static org.junit.Assert.assertEquals;
import static tutorial.infra.GitHubEvents.events;
import static tutorial.infra.MissingImplementation.$returnDouble$;
import static tutorial.infra.MissingImplementation.$returnInt$;

import java.time.DayOfWeek;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import tutorial.infra.github.Event;
import tutorial.infra.github.EventType;

/**
 * Even more exercises with lambda expressions and streams.
 */
@SuppressWarnings("unused")
public class NinjaExercisesAgain {

	/**
	 * How many commits (push events) have been recorded in the most active week in 2014.
	 * A week is supposed to start on Monday.
	 * 
	 * We expect 531 events in that week.
	 */
	@Ignore
	@Test
	public void testMostActiveWeekIn2014() {
		WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 4);
		TemporalField weekOfYear = weekFields.weekOfYear();
		List<? extends Event> events = events();
		
	}
	
	/**
	 * Which week had the biggest number of commits in 2013 (again: {@link EventType#PushEvent push events})
	 * for a single project.
	 * Return the first day of the week in that year, e.g. February the first would be the 32nd day.
	 * 
	 * We expect 203 events.
	 */
	@Ignore
	@Test
	public void testMostActiveWeekIn2013() {
		List<? extends Event> events = events();
	}

	// The following exercises are suprise bundles.
	
	/**
	 * How many commits have been recorded in the most busy 7 consecutive days for any single project.
	 */
	@Ignore
	@Test
	public void testMostActive7DaysForSingleProject() {
		List<? extends Event> events = events();
	}
	
	/**
	 * How many consecutive days with commits have been recorded for a single user.
	 */
	@Ignore
	@Test
	public void testBusiestSingleUser() {
		List<? extends Event> events = events();
	}
	
	/**
	 * Which project had the highest ratio of commits on weekends and holidays compared
	 * to work days.
	 */
	@Ignore
	@Test
	public void testHolidaycoders() {
		List<? extends Event> events = events();
	}
}
