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
	private final int sendPort;

	protected DatagramSocket socket;

	protected final List<ArtNetServerListener> listeners = new ArrayList<ArtNetServerListener>();

	protected InetAddress broadCastAddress;

	private Thread serverThread;

	private int receiveBufferSize;

	private boolean isRunning;

	public ArtNetServer() {
		this(DEFAULT_PORT,DEFAULT_PORT);
	}

	public ArtNetServer(int port, int sendPort) {
		super(NodeStyle.ST_SERVER);
		this.port = port;
		this.sendPort=sendPort;
		setBufferSize(2048);
	}

	public void addListener(ArtNetServerListener l) {
		synchronized (listeners) {
			listeners.add(l);
		}
	}

	public void broadcastPacket(ArtNetPacket ap) {
		try {
			DatagramPacket packet = new DatagramPacket(ap.getData(), ap
					.getLength(), broadCastAddress, sendPort);
			socket.send(packet);
			for (ArtNetServerListener l : listeners) {
				l.artNetPacketBroadcasted(ap);
			}
		} catch (IOException e) {
			logger.warning(e.getMessage());
		}
	}

	public void removeListener(ArtNetServerListener l) {
		synchronized (listeners) {
			listeners.remove(l);
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
				}
			}
			socket.close();
			logger.info("server thread terminated.");
			for(ArtNetServerListener l : listeners) {
				l.artNetServerStopped(this);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendArtPollReply(InetAddress inetAddress, ArtPollPacket packet) {

	}

	public void setBroadcastAddress(String address) {
		try {
			broadCastAddress = InetAddress.getByName(address);
			logger.fine("broadcast IP set to: " + broadCastAddress);
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
					.getLength(), targetAdress, sendPort);
			socket.send(packet);
			logger.finer("sent packet to: "+targetAdress);
			for (ArtNetServerListener l : listeners) {
				l.artNetPacketUnicasted(ap);
			}
		} catch (IOException e) {
			logger.warning(e.getMessage());
		}
	}
}