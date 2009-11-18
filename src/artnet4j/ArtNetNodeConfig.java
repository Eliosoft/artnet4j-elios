package artnet4j;
import java.net.InetAddress;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class ArtNetNodeConfig {

	@XmlAttribute
	public String id;

	@XmlAttribute
	@XmlJavaTypeAdapter(InetAddressAdapter.class)
	public InetAddress ip;

	@XmlAttribute(name = "universe")
	public int universeID;

	@XmlAttribute(name = "numchannels")
	public int numDmxChannels;

	@XmlAttribute(name = "port")
	public int serverPort;

	@Override
	public String toString() {
		return "nodeConfig: id=" + id + ", ip=" + ip + ", uid=" + universeID
		+ ", nc=" + numDmxChannels;
	}
}