package artnet4j;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import artnet4j.packets.ArtNetPacket;
import artnet4j.packets.ArtPollReplyPacket;
import artnet4j.packets.PacketType;

public class ArtNet implements ArtNetServerListener {

	public static final Logger logger = Logger.getLogger(ArtNet.class
			.getClass().getName());

	protected static final long ARTPOLL_REPLY_TIMEOUT = 3000;
	protected static final String VERSION = "0001";

	protected ArtNetServer server;
	protected ArtNetNodeDiscovery discovery;

	public ArtNet() {
		logger.info("Art-Net v" + VERSION);
	}

	public void addServerListener(ArtNetServerListener l) {
		server.addListener(l);
	}

	@Override
	public void artNetPacketBroadcasted(ArtNetPacket packet) {
	}

	@Override
	public void artNetPacketReceived(ArtNetPacket packet) {
		logger.fine("packet received: " + packet.getType());
		if (discovery != null && packet.getType() == PacketType.ART_POLL_REPLY) {
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
		if (discovery == null) {
			discovery = new ArtNetNodeDiscovery(this);
		}
		return discovery;
	}

	public void init() {
		server = new ArtNetServer();
		server.addListener(this);
	}

	public void removeServerListener(ArtNetServerListener l) {
		server.removeListener(l);
	}

	public void setBroadCastAddress(String ip) {
		server.setBroadcastAddress(ip);
	}

	public void start() throws SocketException, ArtNetException {
		if (server==null) {
			init();
		}
		server.start();
	}

	public void startNodeDiscovery() {
		getNodeDiscovery().start();
	}

	public void unicastPacket(ArtNetPacket packet, InetAddress adr) {
		server.unicastPacket(packet, adr);
	}

	/**
	 * Sends the given packet to the specified IP address.
	 * 
	 * @param packet
	 * @param adr
	 */
	public void unicastPacket(ArtNetPacket packet, String adr) {
		InetAddress targetAdress;
		try {
			targetAdress = InetAddress.getByName(adr);
			server.unicastPacket(packet, targetAdress);
		} catch (UnknownHostException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		}
	}
}