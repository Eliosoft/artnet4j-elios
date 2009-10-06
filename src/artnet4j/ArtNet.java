package artnet4j;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Logger;

import artnet4j.packets.ArtNetPacket;
import artnet4j.packets.ArtPollReplyPacket;
import artnet4j.packets.PacketType;

public class ArtNet implements ArtNetServerListener {

	public static final Logger logger = Logger.getLogger(ArtNet.class
			.getClass().getName());

	protected static final long ARTPOLL_REPLY_TIMEOUT = 3000;
	protected static final String VERSION = "0.1";

	protected ArtNetServer server;
	protected ArtNetNodeDiscovery discovery;

	public ArtNet() {
		logger.info("Art-Net v"+VERSION);
	}

	@Override
	public void artNetPacketBroadcasted(ArtNetPacket packet) {
	}

	@Override
	public void artNetPacketReceived(ArtNetPacket packet) {
		logger.fine("packet received: " + packet.getType());
		if (discovery!=null && packet.getType() == PacketType.ART_POLL_REPLY) {
			discovery.discoverNode((ArtPollReplyPacket) packet);
		}
	}

	@Override
	public void artNetPacketUnicasted(ArtNetPacket ap) {

	}

	@Override
	public void artNetServerStarted(ArtNetServer artNetServer) {
		logger.fine("server started callback");
	}

	@Override
	public void artNetServerStopped(ArtNetServer artNetServer) {
		logger.info("server stopped");
	}

	public void broadcastPacket(ArtNetPacket packet) {
		server.broadcastPacket(packet);
	}

	public ArtNetNodeDiscovery getNodeDiscovery() {
		if (discovery==null) {
			discovery = new ArtNetNodeDiscovery(this);
		}
		return discovery;
	}

	public void setBroadCastAddress(String ip) {
		server.setBroadcastAddress(ip);
	}

	public void start() throws SocketException, ArtNetException {
		server = new ArtNetServer();
		server.addListener(this);
		server.start();
	}

	public void startNodeDiscovery() {
		getNodeDiscovery().start();
	}

	public void unicastPacket(ArtNetPacket packet, InetAddress adr) {
		server.unicastPacket(packet,adr);
	}

	public void unicastPacket(ArtNetPacket packet, String adr) {
		server.unicastPacket(packet,adr);
	}
}