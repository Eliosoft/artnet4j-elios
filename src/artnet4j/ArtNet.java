package artnet4j;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.logging.Logger;

import artnet4j.packets.ArtNetPacket;
import artnet4j.packets.ArtPollReplyPacket;
import artnet4j.packets.PacketType;

public class ArtNet implements ArtNetServerListener {

	public static final Logger logger = Logger.getLogger(ArtNet.class
			.getClass().getName());

	protected HashMap<InetAddress, ArtNetNode> discoveredNodes = new HashMap<InetAddress, ArtNetNode>();

	private ArtNetServer server;

	public ArtNet() {
	}

	@Override
	public void artNetPacketBroadcasted(ArtNetPacket packet) {
		logger.info("packet bc'd: "+packet.getType());
	}

	@Override
	public void artNetPacketReceived(ArtNetPacket packet) {
		logger.info("packet received: "+packet.getType());
		if (packet.getType()==PacketType.ART_POLL_REPLY) {
			ArtPollReplyPacket reply=(ArtPollReplyPacket) packet;
			InetAddress nodeIP = reply.getIPAddress();
			ArtNetNode node=discoveredNodes.get(nodeIP);
			if (node==null) {
				logger.info("discovered new node: "+nodeIP);
				node=reply.getNodeStyle().createNode();
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

	public boolean start() {
		server=new ArtNetServer();
		server.addListener(this);
		try {
			server.start();
			return true;
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (ArtNetException e) {
			e.printStackTrace();
		}
		return false;
	}
}
