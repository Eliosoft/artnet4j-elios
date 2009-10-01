package artnet4j;

import java.net.InetAddress;
import java.util.logging.Logger;

import artnet4j.packets.ArtPollReplyPacket;
import artnet4j.packets.PortDescriptor;

public class ArtNetNode {

	protected static final Logger logger = Logger.getLogger(ArtNetNode.class
			.getClass().getName());

	protected final NodeStyle nodeStyle;

	private InetAddress ip;

	private int subSwitch;

	private int oemCode;

	private int nodeStatus;

	private String shortName;
	private String longName;

	private int numPorts;
	private PortDescriptor[] ports;

	public ArtNetNode(NodeStyle style) {
		nodeStyle=style;
	}

	public void extractConfig(ArtPollReplyPacket source) {
		ip=source.getIPAddress();
		subSwitch=source.getSubSwitch();
		oemCode=source.getOEMCode();
		nodeStatus=source.getNodeStatus();
		shortName=source.getShortName();
		longName=source.getLongName();
		ports=source.getPorts();
		numPorts=ports.length;
		logger.info("updated node config");
	}

	/**
	 * @return the nodeStyle
	 */
	public NodeStyle getNodeStyle() {
		return nodeStyle;
	}

	public void setIPAddress(InetAddress ip) {
		this.ip=ip;
	}
}
