package artnet4j.packets;

public class PortDescriptor {

	protected boolean canOutput;
	protected boolean canInput;
	protected PortType type;

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

	/**
	 * @return the canInput
	 */
	public boolean canInput() {
		return canInput;
	}

	/**
	 * @return the canOutput
	 */
	public boolean canOutput() {
		return canOutput;
	}

	/**
	 * @return the type
	 */
	public PortType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "PortDescriptor: " + type + " out: " + canOutput + " in: "
		+ canInput;
	}
}
