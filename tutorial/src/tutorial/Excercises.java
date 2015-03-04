package tutorial;

import static org.junit.Assert.assertEquals;
import static tutorial.infra.GitHubEvents.events;
import static tutorial.infra.MissingImplementation.$returnDouble$;
import static tutorial.infra.MissingImplementation.$returnInt$;

import java.time.DayOfWeek;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;

import org.junit.Test;

import tutorial.infra.github.Event;
import tutorial.infra.github.EventType;

/**
 * Excercises with lamdba expressions and streams.
 */
@SuppressWarnings("unused")
public class Excercises {

	/**
	 * The data set contains a lot of {@link EventType#CreateEvent create events}.
	 * Assert their total number.
	 */
	@Test
	public void testCountCreateEvents() {
		List<? extends Event> events = events();
		assertEquals(44184, $returnInt$());
	}
	
	/**
	 * How many different branch names have been used?
	 */
	@Test
	public void testBranchNames() {
		List<? extends Event> events = events();
		assertEquals(3922, $returnInt$());
	}
	
	/**
	 * Which repository has the highest number of forks and watchers at a given point in time.
	 * Validate against the length of the project's name + the number of watchers + number of forks.
	 */
	@Test
	public void testMostPopularRepository() {
		List<? extends Event> events = events();
		assertEquals(3917, $returnInt$());
	}
	
	/**
	 * Which repository has the highest number of forks and watches at a given point in time in 2013.
	 * Validate against the length of the project's name + the number of watches and forks.
	 */
	@Test
	public void testMostPopularRepositoryIn2013() {
		List<? extends Event> events = events();
		assertEquals(272, $returnInt$());
	}
	
	/**
	 * The data is bogus in the sense that there are a few repositories in the
	 * data set, that do not have a creation date. Assert their count.
	 */
	@Test
	public void testBrokenRepositoryData() {
		List<? extends Event> events = events();
		assertEquals(16, $returnInt$());
	}
	
	/**
	 * Find the repository with the biggest number of {@link EventType#PushEvent push events}.
	 * How many pushed have been recorded?
	 */
	@Test
	public void testRepositoryWithMostPushes() {
		List<? extends Event> events = events();
		assertEquals(6056, $returnInt$());
	}
	
	/**
	 * Find the repository with the second-most number of {@link EventType#PushEvent push events}.
	 * Validate your result against the sum of the int values of the project's name.
	 */
	@Test
	public void testRepositoryWithSecondMostPushes() {
		List<? extends Event> events = events();
		assertEquals(615, $returnInt$());
	}

	/**
	 * What is the average length of the commit message for {@link EventType#PushEvent push events}.
	 */
	@Test
	public void testAverageCommitMessageLength() {
		List<? extends Event> events = events();
		assertEquals(100.264, $returnDouble$(), 0.01);
	}

	/**
	 * How long is the longest email address of any user.
	 */
	@Test
	public void testLongestEMailAddress() {
		List<? extends Event> events = events();
		assertEquals(36, $returnInt$());
	}

	/**
	 * Which committer worked on the largest number of projects. Only consider {@link EventType#PushEvent push events}.
	 * Assert the sum of the chars in the committers name.
	 */
	@Test
	public void testMostDiverseCommitter() {
		List<? extends Event> events = events();
		assertEquals(1803, $returnInt$());
	}
	
	/**
	 * How many commits (push events) have been recorded in the most active week in 2014.
	 * A week is supposed to start on Monday.
	 */
	@Test
	public void testMostActiveWeekIn2014() {
		WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 4);
		TemporalField weekOfYear = weekFields.weekOfYear();
		List<? extends Event> events = events();
		
		assertEquals(-1 /* not solved yet*/, $returnInt$());
	}
	
	/**
	 * Which week had the biggest number of commits in 2013 (again: {@link EventType#PushEvent push events})
	 * for a single project.
	 * Return the first day of the week in that year, e.g. February the first would be the 32nd day.
	 */
	@Test
	public void testMostActiveWeekIn2013() {
		List<? extends Event> events = events();
		assertEquals(-1 /* not solved yet*/, $returnInt$());
	}
	
	/**
	 * How many commits have been recorded in the most busy 7 consecutive days for any single project.
	 */
	@Test
	public void testMostActive7DaysForSingleProject() {
		List<? extends Event> events = events();
		assertEquals(-1 /* not yet solved */, $returnInt$());
	}
	
	/**
	 * How many consecutive days with commits have been recorded for a single user.
	 */
	@Test
	public void testBusiestSingleUser() {
		List<? extends Event> events = events();
		assertEquals(-1 /* not yet solved */, $returnInt$());
	}
	
	/**
	 * Which project had the highest ration of commits on weekends and holidays compared
	 * to work days.
	 * Assert against the hash code of the project name.
	 */
	@Test
	public void testHolidaycoders() {
		List<? extends Event> events = events();
		assertEquals(-1 /* not solved yet */, $returnInt$());
	}
}
