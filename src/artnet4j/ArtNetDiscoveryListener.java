package artnet4j;

import java.util.List;

public interface ArtNetDiscoveryListener {

	void discoveredNewNode(ArtNetNode node);

	void discoveredNodeDisconnected(ArtNetNode node);

	void discoveryCompleted(List<ArtNetNode> nodes);

	void discoveryFailed(Throwable t);
}
