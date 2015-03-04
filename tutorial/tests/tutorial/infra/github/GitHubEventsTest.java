package tutorial.infra.github;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

/**
 * Small test for the parsing of events.
 */
public class GitHubEventsTest {

	@Test
	public void testReadEvents() {
		List<? extends Event> events = EventReader.readEvents();
		assertEquals(71777, events.size());
	}
	
	@Test
	public void testReadUsersAndRepositories() {
		Map<String, Repository> repositories = new ConcurrentHashMap<>();
		Map<String, User> users = new ConcurrentHashMap<>();
		EventReader.readEvents(repositories, users);
		assertEquals(539, repositories.size());
		assertEquals(426, users.size());
	}

	@Test
	public void testRepositoriesHaveCreateDate() {
		Map<String, Repository> repositories = new ConcurrentHashMap<>();
		EventReader.readEvents(repositories, new ConcurrentHashMap<>());
		int brokenCount = 0;
		for(Repository repo: repositories.values()) {
			if (repo.unsafeGetCreatedAt() == null) {
				brokenCount++;
			}
		}
		assertEquals(16, brokenCount);
	}
}
