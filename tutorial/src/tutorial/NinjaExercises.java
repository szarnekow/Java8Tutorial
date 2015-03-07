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
 * More exercises with lambda expressions and streams.
 */
@SuppressWarnings("unused")
public class NinjaExercises {

	/**
	 * The data set contains a lot of {@link EventType#CreateEvent create events}.
	 * Assert their total number.
	 */
	@Ignore
	@Test
	public void testCountCreateEvents() {
		List<? extends Event> events = events();
		assertEquals(44184, $returnInt$());
	}
	
	/**
	 * How many different branch names have been used?
	 */
	@Ignore
	@Test
	public void testBranchNames() {
		List<? extends Event> events = events();
		assertEquals(3922, $returnInt$());
	}
	
	/**
	 * Which repository has the highest number of forks and watchers at a given point in time.
	 * Validate against the length of the project's name + the number of watchers + number of forks.
	 */
	@Ignore
	@Test
	public void testMostPopularRepository() {
		List<? extends Event> events = events();
		assertEquals(3917, $returnInt$());
	}
	
	/**
	 * Which repository has the highest number of forks and watches at a given point in time in 2012.
	 * Validate against the length of the project's name + the number of watches and forks.
	 */
	@Ignore
	@Test
	public void testMostPopularRepositoryIn2012() {
		List<? extends Event> events = events();
		assertEquals(272, $returnInt$());
	}
	
	/**
	 * The data is bogus in the sense that there are a few repositories in the
	 * data set, that do not have a creation date. Assert their count.
	 */
	@Ignore
	@Test
	public void testBrokenRepositoryData() {
		List<? extends Event> events = events();
		assertEquals(16, $returnInt$());
	}
	
	/**
	 * Find the repository with the biggest number of {@link EventType#PushEvent push events}.
	 * How many pushed have been recorded?
	 */
	@Ignore
	@Test
	public void testRepositoryWithMostPushes() {
		List<? extends Event> events = events();
		assertEquals(6056, $returnInt$());
	}
	
	/**
	 * Find the repository with the second-most number of {@link EventType#PushEvent push events}.
	 * Validate your result against the sum of the int values of the project's name.
	 */
	@Ignore
	@Test
	public void testRepositoryWithSecondMostPushes() {
		List<? extends Event> events = events();
		assertEquals(615, $returnInt$());
	}

	/**
	 * What is the average length of the commit message for {@link EventType#PushEvent push events}.
	 */
	@Ignore
	@Test
	public void testAverageCommitMessageLength() {
		List<? extends Event> events = events();
		assertEquals(100.264, $returnDouble$(), 0.01);
	}

	/**
	 * How long is the longest email address of any user.
	 */
	@Ignore
	@Test
	public void testLongestEMailAddress() {
		List<? extends Event> events = events();
		assertEquals(36, $returnInt$());
	}

	/**
	 * Which committer worked on the largest number of projects. Only consider {@link EventType#PushEvent push events}.
	 * Assert the sum of the chars in the committers name.
	 */
	@Ignore
	@Test
	public void testMostDiverseCommitter() {
		List<? extends Event> events = events();
		assertEquals(1803, $returnInt$());
	}
	
}
