package artnet4j.test;

import java.net.SocketException;
import java.util.List;

import artnet4j.ArtNet;
import artnet4j.ArtNetDiscoveryListener;
import artnet4j.ArtNetException;
import artnet4j.ArtNetNode;
import artnet4j.packets.ArtDmxPacket;

public class PollTest implements ArtNetDiscoveryListener {

	public static void main(String[] args) {
		new PollTest().test();
	}
	private ArtNetNode netLynx;

	private int sequenceID;

	@Override
	public void discoveredNewNode(ArtNetNode node) {
		if (netLynx==null) {
			netLynx=node;
			System.out.println("found net lynx");
		}
	}

	@Override
	public void discoveredNodeDisconnected(ArtNetNode node) {
		System.out.println("node disconnected: "+node);
		if (node==netLynx) {
			netLynx=null;
		}
	}

	@Override
	public void discoveryCompleted(List<ArtNetNode> nodes) {
		System.out.println(nodes.size()+" nodes found:");
		for(ArtNetNode n : nodes) {
			System.out.println(n);
		}
	}

	@Override
	public void discoveryFailed(Throwable t) {
		System.out.println("discovery failed");
	}

	private void test() {
		ArtNet artnet=new ArtNet();
		try {
			artnet.start();
			artnet.getNodeDiscovery().addListener(this);
			artnet.startNodeDiscovery();
			while(true) {
				if (netLynx!=null) {
					ArtDmxPacket dmx=new ArtDmxPacket();
					dmx.setUniverse(netLynx.getSubNet(), netLynx.getDmxOuts()[0]);
					dmx.setSequenceID(sequenceID%255);
					byte[] buffer=new byte[510];
					for(int i=0; i<buffer.length; i++) {
						buffer[i]=(byte)(Math.sin(sequenceID*0.05+i*0.8)*127+128);
					}
					dmx.setDMX(buffer,buffer.length);
					artnet.unicastPacket(dmx,netLynx.getIPAddress());
					dmx.setUniverse(netLynx.getSubNet(), netLynx.getDmxOuts()[1]);
					artnet.unicastPacket(dmx,netLynx.getIPAddress());
					sequenceID++;
				}
				Thread.sleep(30);
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (ArtNetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
