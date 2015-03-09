package tutorial;

import static org.junit.Assert.assertEquals;
import static tutorial.infra.GitHubEvents.events;
import static tutorial.infra.MissingImplementation.$returnDouble$;
import static tutorial.infra.MissingImplementation.$returnInt$;

import java.time.DayOfWeek;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.Test;

import tutorial.infra.github.Event;
import tutorial.infra.github.EventType;
import tutorial.infra.github.Repository;
import tutorial.infra.github.User;

/**
 * More exercises with lambda expressions and streams.
 */
@SuppressWarnings("unused")
public class NinjaExercises {

	/**
	 * The data set contains a lot of {@link EventType#CreateEvent create events}.
	 * Assert their total number.
	 */
	@Test
	public void testCountCreateEvents() {
		List<? extends Event> events = events();
		assertEquals(44184, events.stream()
				.filter(event -> event.getEventType() == EventType.CreateEvent)
				.count()
		);
	}
	
	/**
	 * How many different branch names have been used?
	 */
	@Test
	public void testBranchNames() {
		List<? extends Event> events = events();
		assertEquals(3922, events.stream()
				.map(Event::getBranchName)
				.filter(name->name != null)
				.distinct()
				.count()
		);
	}
	
	/**
	 * Which repository has the highest number of forks and watchers at a given point in time.
	 * Validate against the length of the project's name + the number of watchers + number of forks.
	 */
	@Test
	public void testMostPopularRepository() {
		List<? extends Event> events = events();
		Event max = mostPopular(events.stream());
		assertEquals(3917, max.getRepo().getName().length() + forksAndWatchers(max));
	}
	
	/**
	 * Which repository has the highest number of forks and watches at a given point in time in 2012.
	 * Validate against the length of the project's name + the number of watches and forks.
	 */
	@Test
	public void testMostPopularRepositoryIn2012() {
		List<? extends Event> events = events();
		Event max = mostPopular(events.stream().filter(e->e.getTime().getYear() == 2012));
		assertEquals(272, max.getRepo().getName().length() + forksAndWatchers(max));
	}
	
	private Event mostPopular(Stream<? extends Event> stream) {
		return stream.max(Comparator.comparing(this::forksAndWatchers)).get();
	}
	
	private Integer forksAndWatchers(Event e1) {
		return e1.getForks() + e1.getWatchers();
	}
	
	/**
	 * The data is bogus in the sense that there are a few repositories in the
	 * data set, that do not have a creation date. Assert their count.
	 */
	@Test
	public void testBrokenRepositoryData() {
		List<? extends Event> events = events();
		assertEquals(16, events.stream().map(Event::getRepo).distinct().filter(r->r.getCreatedAt()==null).count());
	}
	
	/**
	 * Find the repository with the biggest number of {@link EventType#PushEvent push events}.
	 * How many pushed have been recorded?
	 */
	@Test
	public void testRepositoryWithMostPushes() {
		List<? extends Event> events = events();
		Map<Repository, Integer> countByRepo = pushCountByRepo(events);
		Integer max = countByRepo.values().stream().max(Integer::compare).get();
		assertEquals(6056, max.intValue());
	}

	private Map<Repository, Integer> pushCountByRepo(List<? extends Event> events) {
		return events.stream().collect(Collectors.groupingBy(Event::getRepo, Collectors.summingInt((Event e) -> e.getEventType() == EventType.PushEvent ? 1 : 0)));
	}
	
	/**
	 * Find the repository with the second-most number of {@link EventType#PushEvent push events}.
	 * Validate your result against the sum of the int values of the project's name.
	 */
	@Test
	public void testRepositoryWithSecondMostPushes() {
		List<? extends Event> events = events();
		Map<Repository, Integer> countByRepo = pushCountByRepo(events);
		Map.Entry<Repository, Integer> butLast = countByRepo.entrySet().stream()
				.sorted(Comparator.comparing(Map.Entry<Repository, Integer>::getValue).reversed())
				.skip(1).findFirst().get();
		assertEquals(615, butLast.getKey().getName().chars().sum());
	}

	/**
	 * What is the average length of the commit message for {@link EventType#PushEvent push events}.
	 */
	@Test
	public void testAverageCommitMessageLength() {
		List<? extends Event> events = events();
		OptionalDouble average = events.stream().filter(e-> e.getEventType() == EventType.PushEvent)
				.mapToDouble(e-> Optional.ofNullable(e.getMessage()).orElse("").length())
				.average();
		assertEquals(100.264, average.getAsDouble(), 0.01);
	}

	/**
	 * How long is the longest email address of any user.
	 */
	@Test
	public void testLongestEMailAddress() {
		List<? extends Event> events = events();
		OptionalInt max = events.stream()
			.mapToInt(e-> Optional.ofNullable(e.getUser()).map(User::getEmail).orElse("").length())
			.max();
		assertEquals(36, max.getAsInt());
	}

	/**
	 * Which committer worked on the largest number of projects. Only consider {@link EventType#PushEvent push events}.
	 * Assert the sum of the chars in the committers name.
	 */
	@Test
	public void testMostDiverseCommitter() {
		List<? extends Event> events = events();
		Map<User, Integer> reposByUser = events.stream()
				.filter(e -> e.getEventType() == EventType.PushEvent)
				.collect(Collectors.groupingBy(Event::getUser,
						Collectors.mapping(Event::getRepo, Collectors.collectingAndThen(Collectors.toSet(), Set::size))));
		Map.Entry<User, Integer> max = reposByUser.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get();
		assertEquals(1803, max.getKey().getName().chars().sum());
	}
	
}
