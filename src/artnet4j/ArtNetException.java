package artnet4j;

public class ArtNetException extends Exception {

	private static final long serialVersionUID = 1L;

	public ArtNetException(String message) {
		super(message);
	}

	public ArtNetException(String message, Throwable cause) {
		super(message, cause);
	}
}
