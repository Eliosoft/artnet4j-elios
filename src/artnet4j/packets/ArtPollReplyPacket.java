package artnet4j.packets;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;

import artnet4j.NodeStyle;

public class ArtPollReplyPacket extends ArtNetPacket {

	private InetAddress ip;

	private int subSwitch;

	private int oemCode;

	private int nodeStatus;

	private String shortName;
	private String longName;

	private int numPorts;
	private PortDescriptor[] ports;

	private NodeStyle nodeStyle;

	public ArtPollReplyPacket() {
		super(PacketType.ART_POLL_REPLY);
	}

	public ArtPollReplyPacket(byte[] data) {
		super(PacketType.ART_POLL_REPLY);
		setData(data);
	}

	/**
	 * @return the ip
	 */
	public InetAddress getIPAddress() {
		InetAddress ipClone = null;
		try {
			ipClone=InetAddress.getByAddress(ip.getAddress());
		} catch (UnknownHostException e) {
		}
		return ipClone;
	}

	public String getLongName() {
		return longName;
	}

	public int getNodeStatus() {
		return nodeStatus;
	}

	public NodeStyle getNodeStyle() {
		return nodeStyle;
	}

	public int getOEMCode() {
		return oemCode;
	}

	public PortDescriptor[] getPorts() {
		return ports;
	}

	public String getShortName() {
		return shortName;
	}

	public int getSubSwitch() {
		return subSwitch;
	}

	@Override
	public boolean parse(byte[] raw) {
		setData(raw);
		//		System.out.println(toString());
		System.out.println(raw[172]+" "+raw[173]);
		setIPAddress(data.getByteChunk(null, 10, 4));
		subSwitch=data.getInt16(18);
		oemCode=data.getInt16(20);
		nodeStatus=data.getInt8(23);
		shortName=new String(data.getByteChunk(null, 26, 17));
		longName=new String(data.getByteChunk(null, 44, 64));
		numPorts=data.getInt16(172);
		ports=new PortDescriptor[numPorts];
		for(int i=0; i<numPorts; i++) {
			ports[i]=new PortDescriptor(data.getInt8(174+i));
		}
		int styleID=data.getInt8(200);
		for(NodeStyle s : NodeStyle.values()) {
			if (styleID==s.getStyleID()) {
				nodeStyle=s;
			}
		}
		return true;
	}

	private void setIPAddress(byte[] address) {
		try {
			ip=InetAddress.getByAddress(address);
			logger.fine("setting ip address: "+ip);
		} catch (UnknownHostException e) {
			logger.log(Level.WARNING,e.getMessage(),e);
		}
	}
}
