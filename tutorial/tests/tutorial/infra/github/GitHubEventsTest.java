package tutorial.infra.github;

import static org.junit.Assert.assertEquals;

import java.util.List;

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
		EventReader eventReader = new EventReader();
		EventReader.readEvents(eventReader);
		assertEquals(539, eventReader.getRepositories().size());
		assertEquals(426, eventReader.getUsers().size());
	}

	@Test
	public void testRepositoriesHaveCreateDate() {
		EventReader eventReader = new EventReader();
		EventReader.readEvents(eventReader);
		int brokenCount = 0;
		for(Repository repo: eventReader.getRepositories()) {
			if (repo.getCreatedAt() == null) {
				brokenCount++;
			}
		}
		assertEquals(16, brokenCount);
	}
}
