package artnet4j;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import artnet4j.packets.ArtNetPacket;
import artnet4j.packets.ArtPollPacket;
import artnet4j.packets.ArtPollReplyPacket;
import artnet4j.packets.PacketType;

public class ArtNet implements ArtNetServerListener {

	public class NodeDiscovery extends Thread {
		private final ArtNetDiscoveryListener l;
		private final boolean isActive=true;

		public NodeDiscovery(ArtNetDiscoveryListener l) {
			this.l = l;
		}

		@Override
		public void run() {
			try {
				while(isActive) {
					ArtPollPacket poll = new ArtPollPacket();
					server.broadcastPacket(poll);
					Thread.sleep(ARTPOLL_REPLY_TIMEOUT);
					if (isActive) {
						l.discoveryCompleted(new ArrayList<ArtNetNode>(
								discoveredNodes.values()));
					}
				}
			} catch (InterruptedException e) {
			}
		}
	}

	public static final Logger logger = Logger.getLogger(ArtNet.class
			.getClass().getName());

	protected static final long ARTPOLL_REPLY_TIMEOUT = 3000;

	protected ConcurrentHashMap<InetAddress, ArtNetNode> discoveredNodes;

	private ArtNetServer server;

	private Thread discoveryThread;

	public ArtNet() {
		discoveredNodes=new ConcurrentHashMap<InetAddress, ArtNetNode>();
	}

	@Override
	public void artNetPacketBroadcasted(ArtNetPacket packet) {
		logger.info("packet bc'd: " + packet.getType());
	}

	@Override
	public void artNetPacketReceived(ArtNetPacket packet) {
		logger.info("packet received: " + packet.getType());
		if (packet.getType() == PacketType.ART_POLL_REPLY) {
			ArtPollReplyPacket reply = (ArtPollReplyPacket) packet;
			InetAddress nodeIP = reply.getIPAddress();
			ArtNetNode node = discoveredNodes.get(nodeIP);
			if (node == null) {
				logger.info("discovered new node: " + nodeIP);
				node = reply.getNodeStyle().createNode();
				discoveredNodes.put(nodeIP, node);
			}
			node.extractConfig(reply);
		}
	}

	@Override
	public void artNetPacketSent(ArtNetPacket packet) {
		logger.info("packet sent callback");
	}

	@Override
	public void artNetServerStarted(ArtNetServer artNetServer) {
		logger.info("server started callback");
	}

	@Override
	public void artNetServerStopped(ArtNetServer artNetServer) {
		logger.info("server stopped");
	}

	public void broadCastPacket(ArtNetPacket packet) {
		server.broadcastPacket(packet);
	}

	public void setBroadCastAddress(String ip) {
		server.setBroadcastAddress(ip);
	}

	public void start() throws SocketException, ArtNetException {
		server = new ArtNetServer();
		server.addListener(this);
		server.start();
	}

	public void startNodeDiscovery(ArtNetDiscoveryListener l) throws ArtNetException {
		if (discoveryThread==null) {
			discoveryThread = new NodeDiscovery(l);
			discoveryThread.start();
		} else {
			throw new ArtNetException("Node discovery already active");
		}
	}
}
