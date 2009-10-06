package artnet4j;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import artnet4j.packets.ArtNetPacket;
import artnet4j.packets.ArtNetPacketParser;
import artnet4j.packets.ArtPollPacket;
import artnet4j.packets.PacketType;

public class ArtNetServer extends ArtNetNode implements Runnable {

	public static final int DEFAULT_PORT = 0x1936;

	public static final String DEFAULT_BROADCAST_IP = "2.255.255.255";

	protected final int port;

	protected DatagramSocket socket;

	protected final List<ArtNetServerListener> listeners = new ArrayList<ArtNetServerListener>();

	protected InetAddress broadCastAddress;

	private Thread serverThread;

	private int receiveBufferSize;

	private boolean isRunning;

	protected final List<ArtNetNode> nodes = new ArrayList<ArtNetNode>();

	public ArtNetServer() {
		this(DEFAULT_PORT);
	}

	public ArtNetServer(int port) {
		super(NodeStyle.ST_SERVER);
		this.port = port;
		setBufferSize(2048);
	}

	public void addListener(ArtNetServerListener l) {
		listeners.add(l);
	}

	public void broadcastPacket(ArtNetPacket ap) {
		try {
			DatagramPacket packet = new DatagramPacket(ap.getData(), ap
					.getLength(), broadCastAddress, port);
			socket.send(packet);
			for (ArtNetServerListener l : listeners) {
				l.artNetPacketBroadcasted(ap);
			}
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		}
	}

	@Override
	public void run() {
		byte[] receiveBuffer = new byte[receiveBufferSize];
		DatagramPacket receivedPacket = new DatagramPacket(receiveBuffer,
				receiveBuffer.length);
		try {
			while (isRunning) {
				socket.receive(receivedPacket);
				logger.finer("received new packet");
				ArtNetPacket packet = ArtNetPacketParser.parse(receivedPacket);
				if (packet != null) {
					if (packet.getType() == PacketType.ART_POLL) {
						sendArtPollReply(receivedPacket.getAddress(),
								(ArtPollPacket) packet);
					}
					for (ArtNetServerListener l : listeners) {
						l.artNetPacketReceived(packet);
					}
				} else {
					logger
					.warning("received invalid Art-Net data, packet discarded");
				}
			}
			socket.close();
			logger.info("server thread terminated.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendArtPollReply(InetAddress inetAddress, ArtPollPacket packet) {

	}

	public void setBroadcastAddress(String address) {
		try {
			broadCastAddress = InetAddress.getByName(address);
			logger.info("broadcast IP set to: " + broadCastAddress);
		} catch (UnknownHostException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		}
	}

	private void setBufferSize(int size) {
		if (!isRunning) {
			receiveBufferSize = size;
		}
	}

	public void start() throws SocketException, ArtNetException {
		if (broadCastAddress == null) {
			setBroadcastAddress(DEFAULT_BROADCAST_IP);
		}
		if (socket == null) {
			socket = new DatagramSocket(port);
			logger.info("Art-Net server started at port: " + port);
			for (ArtNetServerListener l : listeners) {
				l.artNetServerStarted(this);
			}
			isRunning = true;
			serverThread = new Thread(this);
			serverThread.start();
		} else {
			throw new ArtNetException(
			"Couldn't create server socket, server already running?");
		}
	}

	public void stop() {
		isRunning = false;
	}

	/**
	 * Sends the given packet to the specified IP address.
	 * 
	 * @param ap
	 * @param targetAdress
	 */
	public void unicastPacket(ArtNetPacket ap, InetAddress targetAdress) {
		try {
			DatagramPacket packet = new DatagramPacket(ap.getData(), ap
					.getLength(), targetAdress, port);
			socket.send(packet);
			logger.info("sent packet to: "+targetAdress);
			for (ArtNetServerListener l : listeners) {
				l.artNetPacketUnicasted(ap);
			}
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		}
	}
	/**
	 * Sends the given packet to the specified IP address.
	 * 
	 * @param ap
	 * @param targetIP
	 */
	public void unicastPacket(ArtNetPacket ap, String targetIP) {
		InetAddress targetAdress;
		try {
			targetAdress = InetAddress.getByName(targetIP);
			unicastPacket(ap, targetAdress);
		} catch (UnknownHostException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		}
	}
}
