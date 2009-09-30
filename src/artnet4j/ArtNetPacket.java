package artnet4j;

public class ArtNetPacket {

	private final ArtNetPacketType type;

	public ArtNetPacket(ArtNetPacketType type) {
		this.type = type;
	}

	/**
	 * Returns the type of this packet.
	 * 
	 * @return the type
	 */
	public ArtNetPacketType getType() {
		return type;
	}

}
