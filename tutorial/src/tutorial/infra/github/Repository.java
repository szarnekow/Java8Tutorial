package tutorial.infra.github;

import java.time.LocalDateTime;
import java.util.Optional;

public class Repository {

	private final String name;
	private LocalDateTime createdAt;
	
	public Repository(String name, LocalDateTime createdAt) {
		this.name = name;
		this.createdAt = createdAt;
	}

	public String getName() {
		return name;
	}
	
	void setDateIfAbsent(Optional<LocalDateTime> nullableDate) {
		if (createdAt == null && nullableDate.isPresent()) {
			createdAt = nullableDate.get();
		}
	}
	
	public Optional<LocalDateTime> getCreatedAt() {
		return Optional.ofNullable(createdAt);
	}

	LocalDateTime unsafeGetCreatedAt() {
		return createdAt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Repository other = (Repository) obj;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.equals(other.createdAt))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GitHubRepository [name=" + name + ", createdAt=" + createdAt
				+ "]";
	}
	
}
