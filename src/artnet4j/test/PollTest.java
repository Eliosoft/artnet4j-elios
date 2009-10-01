package artnet4j.test;

import java.net.SocketException;
import java.util.List;

import junit.framework.TestCase;
import artnet4j.ArtNet;
import artnet4j.ArtNetDiscoveryListener;
import artnet4j.ArtNetException;
import artnet4j.ArtNetNode;
import artnet4j.packets.ArtDmxPacket;

public class PollTest extends TestCase implements ArtNetDiscoveryListener {

	private ArtNetNode netLynx;
	private int sequenceID;

	@Override
	public void discoveryCompleted(List<ArtNetNode> nodes) {
		System.out.println(nodes.size()+" nodes found:");
		for(ArtNetNode n : nodes) {
			System.out.println(n);
		}
		netLynx=nodes.get(0);
	}

	@Override
	public void discoveryFailed(Throwable t) {
		System.out.println("discovery failed");
	}

	public void testDiscovery() {
		ArtNet artnet=new ArtNet();
		try {
			artnet.start();
			//artnet.setBroadCastAddress("239.0.0.1");
			artnet.startNodeDiscovery(this);
			while(true) {
				if (netLynx!=null) {
					ArtDmxPacket dmx=new ArtDmxPacket();
					dmx.setUniverse(0, 0x0b);
					dmx.setSequenceID(sequenceID%255);
					byte[] buffer=new byte[24];
					for(int i=0; i<buffer.length; i++) {
						buffer[i]=(byte)(Math.sin(sequenceID*0.05+i*0.2)*127+128);
					}
					dmx.setDMX(buffer,buffer.length);
					artnet.broadCastPacket(dmx);
					sequenceID++;
				}
				Thread.sleep(30);
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (ArtNetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
