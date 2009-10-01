package artnet4j;

import artnet4j.packets.ArtNetPacket;

public interface ArtNetServerListener {

	void artNetPacketBroadcasted(ArtNetPacket packet);

	void artNetPacketReceived(ArtNetPacket packet);

	void artNetPacketSent(ArtNetPacket packet);

	void artNetServerStarted(ArtNetServer artNetServer);

	void artNetServerStopped(ArtNetServer artNetServer);

}
