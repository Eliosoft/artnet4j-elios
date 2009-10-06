package artnet4j;

import java.net.InetAddress;
import java.util.logging.Logger;

import artnet4j.packets.ArtPollReplyPacket;
import artnet4j.packets.ByteUtils;
import artnet4j.packets.PortDescriptor;

public class ArtNetNode {

	protected static final Logger logger = Logger.getLogger(ArtNetNode.class
			.getClass().getName());

	protected final NodeStyle nodeStyle;

	private InetAddress ip;

	private int subSwitch;

	private int oemCode;

	private int nodeStatus;
	private NodeReportCode reportCode;

	private String shortName;
	private String longName;

	private int numPorts;
	private PortDescriptor[] ports;
	private byte[] dmxIns;
	private byte[] dmxOuts;

	public ArtNetNode() {
		this(NodeStyle.ST_NODE);
	}

	public ArtNetNode(NodeStyle style) {
		nodeStyle=style;
	}

	public void extractConfig(ArtPollReplyPacket source) {
		setIPAddress(source.getIPAddress());
		subSwitch=source.getSubSwitch();
		oemCode=source.getOEMCode();
		nodeStatus=source.getNodeStatus();
		shortName=source.getShortName();
		longName=source.getLongName();
		ports=source.getPorts();
		numPorts=ports.length;
		reportCode=source.getReportCode();
		dmxIns=source.getDmxIns();
		dmxOuts=source.getDmxOuts();
		logger.info("updated node config");
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
		return ip;
	}

	/**
	 * @return the nodeStatus
	 */
	public int getNodeStatus() {
		return nodeStatus;
	}

	/**
	 * @return the nodeStyle
	 */
	public NodeStyle getNodeStyle() {
		return nodeStyle;
	}

	/**
	 * @return the oemCode
	 */
	public int getOemCode() {
		return oemCode;
	}

	/**
	 * @return the reportCode
	 */
	public NodeReportCode getReportCode() {
		return reportCode;
	}

	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}

	public int getSubNet() {
		return subSwitch;
	}

	public void setIPAddress(InetAddress ip) {
		this.ip=ip;
	}

	@Override
	public String toString() {
		return "node: "+nodeStyle+" "+ip+" "+longName+", "+numPorts+" ports, "+ByteUtils.hex(subSwitch,2)+" subswitch";
	}
}
