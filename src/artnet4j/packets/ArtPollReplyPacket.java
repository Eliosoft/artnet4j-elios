package artnet4j.packets;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;

import artnet4j.NodeReportCode;
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
	private NodeReportCode reportCode;

	private byte[] dmxIns;
	private byte[] dmxOuts;

	public ArtPollReplyPacket() {
		super(PacketType.ART_POLL_REPLY);
	}

	public ArtPollReplyPacket(byte[] data) {
		super(PacketType.ART_POLL_REPLY);
		setData(data);
	}

	/**
	 * @return the dmxIns
	 */
	public byte[] getDmxIns() {
		return dmxIns;
	}

	/**
	 * @return the dmxOuts
	 */
	public byte[] getDmxOuts() {
		return dmxOuts;
	}

	/**
	 * @return the ip
	 */
	public InetAddress getIPAddress() {
		InetAddress ipClone = null;
		try {
			ipClone = InetAddress.getByAddress(ip.getAddress());
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

	/**
	 * @return the reportCode
	 */
	public NodeReportCode getReportCode() {
		return reportCode;
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
		//		System.out.println(data.toHex(256));
		setIPAddress(data.getByteChunk(null, 10, 4));
		subSwitch = data.getInt16(18);
		oemCode = data.getInt16(20);
		nodeStatus = data.getInt8(23);
		shortName = new String(data.getByteChunk(null, 26, 17));
		longName = new String(data.getByteChunk(null, 44, 64));
		reportCode = NodeReportCode.getForID(new String(data.getByteChunk(null,
				108, 5)));
		numPorts = data.getInt16(172);
		ports = new PortDescriptor[numPorts];
		for (int i = 0; i < numPorts; i++) {
			ports[i] = new PortDescriptor(data.getInt8(174 + i));
		}
		dmxIns = data.getByteChunk(null, 186, 4);
		dmxOuts = data.getByteChunk(null, 190, 4);
		for (int i = 0; i < 4; i++) {
			dmxIns[i] &= 0x0f;
			dmxOuts[i] &= 0x0f;
		}
		int styleID = data.getInt8(200);
		for (NodeStyle s : NodeStyle.values()) {
			if (styleID == s.getStyleID()) {
				nodeStyle = s;
			}
		}
		return true;
	}

	/**
	 * @param dmxIns
	 *            the dmxIns to set
	 */
	public void setDmxIns(byte[] dmxIns) {
		this.dmxIns = dmxIns;
	}

	/**
	 * @param dmxOuts
	 *            the dmxOuts to set
	 */
	public void setDmxOuts(byte[] dmxOuts) {
		this.dmxOuts = dmxOuts;
	}

	private void setIPAddress(byte[] address) {
		try {
			ip = InetAddress.getByAddress(address);
			logger.fine("setting ip address: " + ip);
		} catch (UnknownHostException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		}
	}

	/**
	 * @param reportCode
	 *            the reportCode to set
	 */
	public void setReportCode(NodeReportCode reportCode) {
		this.reportCode = reportCode;
	}
}
