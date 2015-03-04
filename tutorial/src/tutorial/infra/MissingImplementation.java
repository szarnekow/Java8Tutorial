package tutorial.infra;

/**
 * Can be used to indicate a missing implementation.
 * Usage pattern is something like
 * {@code String result = $implementMe$()} along with
 * a static import.
 */
public class MissingImplementation extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public static <T> T $implementMe$() {
		throw new MissingImplementation();
	}
	
	public static int $returnInt$() {
		throw new MissingImplementation();
	}
	
	public static double $returnDouble$() {
		throw new MissingImplementation();
	}
	
}
