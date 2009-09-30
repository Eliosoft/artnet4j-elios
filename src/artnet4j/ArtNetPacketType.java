package artnet4j;

public enum ArtNetPacketType {

	ART_POLL(0x2000),
	ART_POLL_REPLY(0x2100),
	ART_OUTPUT(0x5000),
	ART_ADDRESS(0x6000),
	ART_INPUT(0x7000),
	ART_TOD_REQUEST(0x8000),
	ART_TOD_DATA(0x8100),
	ART_TOD_CONTROL(0x8200),
	ART_RDM(0x8300),
	ART_RDMSUB(0x8400);

	private final short opCode;

	ArtNetPacketType(int code) {
		opCode=(short)code;
	}

	/**
	 * Returns the opcode for this packet type.
	 * 
	 * @return the opCode
	 */
	public short getOpCode() {
		return opCode;
	}

}