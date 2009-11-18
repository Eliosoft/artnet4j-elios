package artnet4j;

import java.net.InetAddress;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class InetAddressAdapter extends XmlAdapter<String, InetAddress> {

	@Override
	public String marshal(InetAddress adr) throws Exception {
		return adr.getHostAddress();
	}

	@Override
	public InetAddress unmarshal(String adr) throws Exception {
		return InetAddress.getByName(adr);
	}

}
