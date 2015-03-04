package tutorial.infra;

import java.util.List;

import tutorial.infra.github.Event;
import tutorial.infra.github.EventReader;

/**
 * Provides access to the persisted list of GitHub repository events.
 */
public class GitHubEvents {

	private static final List<? extends Event> events = EventReader.readEvents();
	
	/**
	 * Obtain the events as a list.
	 */
	public static List<? extends Event> events() {
		return events;
	}
	
}
