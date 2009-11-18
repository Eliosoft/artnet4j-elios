package artnet4j.packets;

import java.net.DatagramPacket;
import java.util.logging.Logger;


public class ArtNetPacketParser {

	public static final Logger logger = Logger.getLogger(ArtNetPacketParser.class
			.getClass().getName());

	public static ArtNetPacket createPacketForOpCode(int opCode, byte[] data) {
		logger.finer("creating packet instance for opcode: 0x"+ByteUtils.hex(opCode, 4));
		ArtNetPacket packet=null;
		for(PacketType type : PacketType.values()) {
			if (opCode==type.getOpCode()) {
				packet=type.createPacket();
				if (packet!=null) {
					packet.parse(data);
					break;
				} else {
					logger.fine("packet type valid, but not yet supported: "+type);
				}
			}
		}
		return packet;
	}

	private static ArtNetPacket parse(byte[] raw, int offset, int length) {
		ArtNetPacket packet=null;
		ByteUtils data=new ByteUtils(raw);
		if (data.length>10) {
			if (data.compareBytes(ArtNetPacket.HEADER,0,8)) {
				int opCode=data.getInt16LE(8);
				packet=createPacketForOpCode(opCode,raw);
			} else {
				logger.warning("invalid header");
			}
		} else {
			logger.warning("invalid packet length: "+data.length);
		}
		return packet;
	}

	public static ArtNetPacket parse(DatagramPacket receivedPacket) {
		return parse(receivedPacket.getData(),receivedPacket.getOffset(),receivedPacket.getLength());
	}
}
