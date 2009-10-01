package artnet4j.packets;

public enum PacketType {

	ART_POLL(0x2000, ArtPollPacket.class),
	ART_POLL_REPLY(0x2100, ArtPollReplyPacket.class),
	ART_OUTPUT(0x5000, null),
	ART_ADDRESS(0x6000, null),
	ART_INPUT(0x7000, null),
	ART_TOD_REQUEST(0x8000, null),
	ART_TOD_DATA(0x8100, null),
	ART_TOD_CONTROL(0x8200, null),
	ART_RDM(0x8300, null),
	ART_RDMSUB(0x8400, null);

	private final int opCode;
	private final Class<? extends ArtNetPacket> packetClass;

	private PacketType(int code, Class<? extends ArtNetPacket> clazz) {
		opCode=code;
		packetClass=clazz;
	}

	public ArtNetPacket createPacket() {
		ArtNetPacket p = null;
		try {
			p=packetClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return p;
	}

	/**
	 * Returns the opcode for this packet type.
	 * 
	 * @return the opCode
	 */
	public int getOpCode() {
		return opCode;
	}
}