package artnet4j.packets;

public enum PortType {
	DMX512(0),
	MIDI(1),
	AVAB(2),
	COLORTRAN(3),
	ADB62_5(4),
	ARTNET(5);

	private int id;

	private PortType(int id) {
		this.id=id;
	}

	public int getPortID() {
		return id;
	}
}
