package artnet4j;

public class ArtNetPacket {

	public static final byte[] HEADER = "Art-Net\0".getBytes();

	/**
	 * Converts the byte into an unsigned int.
	 * 
	 * @param b
	 * @return
	 */
	public static final int byteToUint(byte b) {
		return (b < 0 ? 256 + b : b);
	}

	protected byte[] data = new byte[768];

	protected final ArtNetPacketType type;

	public ArtNetPacket(ArtNetPacketType type) {
		this.type = type;
	}

	/**
	 * Gets a 16bit int (Big Endian, MSB first) from the buffer at the given
	 * offset.
	 * 
	 * @param offset
	 * @return
	 */
	protected final int getInt16(int offset) {
		return byteToUint(data[offset]) << 8 + byteToUint(data[offset + 1]);
	}

	/**
	 * Gets a 16bit int (Little Endian, LSB first) from the buffer at the given
	 * offset.
	 * 
	 * @param offset
	 * @return
	 */
	protected final int getInt16LE(int offset) {
		return byteToUint(data[offset + 1]) << 8 + byteToUint(data[offset]);
	}

	/**
	 * Gets an 8bit int from the buffer at the given offset.
	 * 
	 * @param offset
	 * @return
	 */
	protected final int getInt8(int offset) {
		return byteToUint(data[offset]);
	}

	/**
	 * Returns the type of this packet.
	 * 
	 * @return the type
	 */
	public ArtNetPacketType getType() {
		return type;
	}

	protected final void setInt16(int val, int offset) {
		data[offset] = (byte) (val >> 8 & 0xff);
		data[offset + 1] = (byte) (val & 0xff);
	}

	protected final void setInt16LE(int val, int offset) {
		data[offset] = (byte) (val & 0xff);
		data[offset + 1] = (byte) (val >> 8 & 0xff);
	}

	protected final void setInt8(int val, int offset) {
		data[offset] = (byte) (val & 0xff);
	}
}
