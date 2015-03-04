package tutorial.infra.github;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Represents an event that happened in a eclipse repository on GitHub.
 */
public class Event {

	private final EventType eventType;
	private LocalDateTime time;
	private Repository repo;
	private Optional<User> user;
	private int repoSize;
	private int watchers;
	private int forks;
	private Optional<String> branchName;
	private Optional<String> message;

	public Event(EventType eventType, LocalDateTime time, Repository repo, Optional<User> user, int size, int watchers, int forks, Optional<String> refType, Optional<String> message) {
		this.eventType = eventType;
		this.time = time;
		this.repo = repo;
		this.user = user;
		this.repoSize = size;
		this.watchers = watchers;
		this.forks = forks;
		this.branchName = refType;
		this.message = message;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public Repository getRepo() {
		return repo;
	}

	public void setRepo(Repository repo) {
		this.repo = repo;
	}

	public Optional<User> getUser() {
		return user;
	}

	public void setUser(Optional<User> user) {
		this.user = user;
	}

	public int getRepoSize() {
		return repoSize;
	}

	public void setRepoSize(int repoSize) {
		this.repoSize = repoSize;
	}

	public int getWatchers() {
		return watchers;
	}

	public void setWatchers(int watchers) {
		this.watchers = watchers;
	}

	public int getForks() {
		return forks;
	}

	public void setForks(int forks) {
		this.forks = forks;
	}

	public Optional<String> getBranchName() {
		return branchName;
	}

	public void setRefType(Optional<String> refType) {
		this.branchName = refType;
	}

	public Optional<String> getMessage() {
		return message;
	}

	public void setMessage(Optional<String> message) {
		this.message = message;
	}

	public EventType getEventType() {
		return eventType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((eventType == null) ? 0 : eventType.hashCode());
		result = prime * result + forks;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((branchName == null) ? 0 : branchName.hashCode());
		result = prime * result + ((repo == null) ? 0 : repo.hashCode());
		result = prime * result + repoSize;
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + watchers;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (eventType != other.eventType)
			return false;
		if (forks != other.forks)
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (branchName == null) {
			if (other.branchName != null)
				return false;
		} else if (!branchName.equals(other.branchName))
			return false;
		if (repo == null) {
			if (other.repo != null)
				return false;
		} else if (!repo.equals(other.repo))
			return false;
		if (repoSize != other.repoSize)
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (watchers != other.watchers)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Event [eventType=" + eventType + ", time=" + time + ", repo="
				+ repo + ", user=" + user + ", repoSize=" + repoSize
				+ ", watchers=" + watchers + ", forks=" + forks + ", refType="
				+ branchName + ", message=" + message + "]";
	}
	
}
