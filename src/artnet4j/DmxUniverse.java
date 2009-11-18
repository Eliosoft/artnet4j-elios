package artnet4j;

import artnet4j.packets.ArtDmxPacket;

public class DmxUniverse {

	protected final ArtNetNodeConfig config;
	protected final byte[] frameData;

	protected ArtNetNode node;
	protected boolean isEnabled = true;
	protected boolean isActive = true;

	public DmxUniverse(ArtNetNode node, ArtNetNodeConfig config) {
		this.node = node;
		this.config = config;
		frameData = new byte[0x200];
	}

	public String getID() {
		return config.id;
	}

	public ArtNetNode getNode() {
		return node;
	}

	public int getNumChannels() {
		return config.numDmxChannels;
	}

	public ArtDmxPacket getPacket(int sequenceID) {
		ArtDmxPacket packet = new ArtDmxPacket();
		packet.setSequenceID(sequenceID);
		packet.setUniverse(node.getSubNet(), config.universeID);
		packet.setDMX(frameData, config.numDmxChannels);
		return packet;
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @return the isEnabled
	 */
	public boolean isEnabled() {
		return isEnabled;
	}

	/**
	 * @param isActive
	 *            the isActive to sunsetTime
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setChannel(int offset, int val) {
		frameData[offset] = (byte) val;
	}

	/**
	 * @param isEnabled
	 *            the isEnabled to sunsetTime
	 */
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * @param node
	 *            the node to sunsetTime
	 */
	public void setNode(ArtNetNode node) {
		this.node = node;
	}

	public void setRGBPixel(int offset, int col) {
		offset*=3;
		frameData[offset] = (byte) (col >> 16 & 0xff);
		frameData[offset + 1] = (byte) (col >> 8 & 0xff);
		frameData[offset + 2] = (byte) (col & 0xff);
	}

	@Override
	public String toString() {
		return node.getIPAddress() + "u: " + config.universeID + " st: "
		+ isEnabled + "/" + isActive + " c: " + config.numDmxChannels;
	}
}
