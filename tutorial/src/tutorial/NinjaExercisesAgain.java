package tutorial;

import static org.junit.Assert.assertEquals;
import static tutorial.infra.GitHubEvents.events;
import static tutorial.infra.MissingImplementation.$returnDouble$;
import static tutorial.infra.MissingImplementation.$returnInt$;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.Test;

import tutorial.infra.github.Event;
import tutorial.infra.github.EventType;
import tutorial.infra.github.Repository;

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
	@Test
	public void testMostActiveWeekIn2014() {
		WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 4);
		TemporalField weekOfYear = weekFields.weekOfYear();
		List<? extends Event> events = events();
		Map<Integer, Long> commitsByWeek = events
				.stream()
				.filter(e -> e.getTime().getYear() == 2014
						&& e.getEventType() == EventType.PushEvent)
				.collect(
						Collectors.groupingBy(e -> e.getTime().get(weekOfYear),
								Collectors.counting()));
		Map.Entry<Integer, Long> entry = commitsByWeek.entrySet().stream()
				.max(Comparator.comparing(Map.Entry::getValue)).get();
		int pushEventsPerWeek = entry.getValue().intValue();
		assertEquals(531, pushEventsPerWeek);
	}
	
	/**
	 * Which week had the biggest number of commits in 2013 (again: {@link EventType#PushEvent push events})
	 * for a single project.
	 * Return the first day of the week in that year, e.g. February the first would be the 32nd day.
	 * 
	 * We expect 203 events.
	 */
	@Test
	public void testMostActiveWeekIn2013() {
		WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 4);
		TemporalField weekOfYear = weekFields.weekOfYear();
		List<? extends Event> events = events();
		class RepoAndWeek {
			Repository repo;
			Integer week;

			public RepoAndWeek(Repository repo, int week) {
				this.repo = repo;
				this.week = week;
			}
		}
		Stream<RepoAndWeek> repoAndWeek = events
				.stream()
				.filter(e -> e.getTime().getYear() == 2013 && e.getEventType() == EventType.PushEvent)
				.map(e -> new RepoAndWeek(e.getRepo(), e.getTime().get(weekOfYear)));
		Collector<RepoAndWeek, ?, Map<Integer, Long>> counting = Collectors.groupingBy(r -> r.week, Collectors.counting());
		Map<Repository, Map.Entry<Integer, Long>> projectsToBestWeek = repoAndWeek
				.collect(Collectors.groupingBy(
						r -> r.repo,
						Collectors.collectingAndThen(
								counting,
								map -> map
										.entrySet()
										.stream()
										.max(Comparator.comparingLong(Map.Entry::getValue))
										.get())));
		int bestWeek = projectsToBestWeek.entrySet().stream()
				.max(Comparator.comparing(e -> e.getValue().getValue()))
				.map(e -> e.getValue().getKey()).get();
		LocalDate day = LocalDate.of(2013, 1, 1).with(weekOfYear, bestWeek)
				.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		assertEquals(203, day.getDayOfYear());
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
