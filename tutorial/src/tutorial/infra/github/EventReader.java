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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
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
		return readEvents(new ConcurrentHashMap<>(), new ConcurrentHashMap<>());
	}
	
	static List<? extends Event> readEvents(Map<String, Repository> repositories, Map<String, User> users) {
		try {
			try (ZipFile file = new ZipFile(new File("data/EclipseAtGitHub.csv.zip"))){
				ZipEntry entry = file.getEntry("EclipseAtGithub.csv");
				try (
						InputStream inputStream = file.getInputStream(entry);
						BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")))
						) {
					return readEvents(reader.lines(), repositories, users);
				}
			}
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

	static List<? extends Event> readEvents(Stream<String> lines, Map<String, Repository> repositories, Map<String, User> users) {
		return lines.skip(1).map(line->readEvent(line, repositories, users)).collect(Collectors.toList());
	}

	private static DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder()
		    .parseCaseInsensitive()
		    .append(DateTimeFormatter.ISO_LOCAL_DATE)
		    .appendLiteral(' ')
		    .append(DateTimeFormatter.ISO_LOCAL_TIME)
		    .toFormatter();
	
	// type,created_at,repository_name,repository_forks,repository_size,repository_created_at,repository_watchers,payload_ref,payload_commit_email,payload_commit_name,payload_commit_msg
	// PushEvent,2014-01-30 08:09:28,vert.x,525,369125,2011-06-17 14:54:55,2685,refs/heads/master,timvolpe@gmail.com,purplefox,inced version
	static Event readEvent(String line, Map<String, Repository> repositories, Map<String, User> users) {
		AtomicInteger idx = new AtomicInteger(-1);
		EventType eventType = EventType.valueOf(readNext(line, idx));
		String timeStamp = readNext(line, idx);
		String projectName = readNext(line, idx);
		if (projectName.startsWith("eclipse/")) {
			projectName = projectName.substring("eclipse/".length());
		}
		String forks = readNext(line, idx);
		String size = readNext(line, idx);
		String repositoryCreation = readNext(line, idx);
		String watchers = readNext(line, idx);
		String ref = readNext(line, idx);
		String email = readNext(line, idx);
		String committer = readNext(line, idx);
		String message = line.substring(idx.incrementAndGet());
		try {
			Repository repo = repositories.computeIfAbsent(projectName, name->new Repository(name, null));
			repo.setDateIfAbsent(nullableDate(repositoryCreation));
			Optional<User> user = Optional.ofNullable(email).map(mail -> users.computeIfAbsent(mail, id->new User(id, committer)));
			return new Event(
					eventType,
					LocalDateTime.parse(timeStamp, dateFormatter),
					repo,
					user,
					size != null ? Integer.parseInt(size) : -1,
					watchers != null ? Integer.parseInt(watchers) : -1,
					forks != null ? Integer.parseInt(forks) : -1,
					Optional.ofNullable(ref),
					Optional.ofNullable(message));
		} catch(RuntimeException e) {
			throw e;
		}
	}

	private static Optional<LocalDateTime> nullableDate(String s) {
		return Optional.ofNullable(s).map(date->LocalDateTime.parse(date, dateFormatter));
	}
	
	private static String readNext(String line, AtomicInteger from) {
		int idx = line.indexOf(',', from.incrementAndGet());
		try {
			String result = line.substring(from.intValue(), idx);
			if ("null".equals(result)) {
				return null;
			}
			return result;
		} finally {
			from.set(idx);
		}
	}
	
}
