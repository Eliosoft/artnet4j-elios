package artnet4j.packets;

public class PortDescriptor {

	boolean canOutput;
	boolean canInput;
	PortType type;

	public PortDescriptor(int id) {
		canOutput = (id & 0x80) > 0;
		canInput = (id & 0x40) > 0;
		id &= 0x3f;
		for (PortType t : PortType.values()) {
			if (id == t.getPortID()) {
				type = t;
			}
		}
	}

	@Override
	public String toString() {
		return "PortDescriptor: " + type + " out: " + canOutput + " in: "
		+ canInput;
	}
}
