package artnet4j;

import java.util.List;

public interface ArtNetDiscoveryListener {

	void discoveryCompleted(List<ArtNetNode> nodes);

	void discoveryFailed(Throwable t);
}
