package tutorial.infra.github;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Reads the events from disk. 
 */
public class EventReader {
	
	/**
	 * Reads the records from {@code data/EclipseAtGitHub.cvs.zip} as 
	 * Java beans.
	 * @return a list of 
	 */
	public static List<? extends Event> readEvents() {
		EventReader eventReader = new EventReader();
		readEvents(eventReader);
		return eventReader.getEvents();
	}
	
	static void readEvents(EventReader eventReader) {
		try (ZipFile file = new ZipFile(new File("data/EclipseAtGitHub.csv.zip"))){
			ZipEntry entry = file.getEntry("EclipseAtGithub.csv");
			try (
				InputStream inputStream = file.getInputStream(entry);
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")))
			) {
				reader.readLine();
				reader.lines().parallel().forEach(eventReader::readEvent);
			}
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder()
		    .parseCaseInsensitive()
		    .append(DateTimeFormatter.ISO_LOCAL_DATE)
		    .appendLiteral(' ')
		    .append(DateTimeFormatter.ISO_LOCAL_TIME)
		    .toFormatter();
	
	private Map<String, Repository> repositories = new ConcurrentHashMap<>();
	private Map<String, User> users = new ConcurrentHashMap<>();
	private Queue<Event> events = new ConcurrentLinkedQueue<>();
	
	// type,created_at,repository_name,repository_forks,repository_size,repository_created_at,repository_watchers,payload_ref,payload_commit_email,payload_commit_name,payload_commit_msg
	// PushEvent,2014-01-30 08:09:28,vert.x,525,369125,2011-06-17 14:54:55,2685,refs/heads/master,timvolpe@gmail.com,purplefox,inced version
	void readEvent(String line) {
		Iterator<String> parts = Stream.of(line.split(","))
				.map(part -> "null".equals(part) ? null : part)
				.iterator();
		EventType eventType = EventType.valueOf(parts.next());
		String timeStamp = parts.next();
		String projectName = parts.next();
		if (projectName.startsWith("eclipse/")) {
			projectName = projectName.substring("eclipse/".length());
		}
		String forks = parts.next();
		String size = parts.next();
		String repositoryCreation = parts.next();
		String watchers = parts.next();
		String ref = parts.next();
		String email = parts.next();
		String committer = parts.next();
		String message = join(parts);
		Repository repo = repositories.computeIfAbsent(projectName, name->new Repository(name, null));
		repo.setDateIfAbsent(parseDate(repositoryCreation));
		User user = Optional.ofNullable(email).map(mail -> users.computeIfAbsent(mail, id->new User(id, committer))).orElse(null);
		Event event = new Event(
				eventType,
				LocalDateTime.parse(timeStamp, dateFormatter),
				repo,
				user,
				size != null ? Integer.parseInt(size) : -1,
				watchers != null ? Integer.parseInt(watchers) : -1,
				forks != null ? Integer.parseInt(forks) : -1,
				ref,
				message);
		events.add(event);
	}

	private String join(Iterator<String> parts) {
		StringJoiner joiner = new StringJoiner(",");
		parts.forEachRemaining(joiner::add);
		String message = joiner.toString();
		return message;
	}
	
	private LocalDateTime parseDate(String s) {
		return Optional.ofNullable(s).map(date->LocalDateTime.parse(date, dateFormatter)).orElse(null);
	}
	
	public List<Event> getEvents() {
		return new ArrayList<>(events);
	}
	
	public Collection<Repository> getRepositories() {
		return Collections.unmodifiableCollection(repositories.values());
	}
	
	public Collection<User> getUsers() {
		return Collections.unmodifiableCollection(users.values());
	}

}
