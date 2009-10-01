package artnet4j.test;

import artnet4j.packets.ArtPollPacket;

public class PollTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArtPollPacket p=new ArtPollPacket();
		System.out.println(p);
	}

}
