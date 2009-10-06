package artnet4j;

import artnet4j.packets.ArtNetPacket;

public interface ArtNetServerListener {

	void artNetPacketBroadcasted(ArtNetPacket packet);

	void artNetPacketReceived(ArtNetPacket packet);

	void artNetPacketUnicasted(ArtNetPacket ap);

	void artNetServerStarted(ArtNetServer artNetServer);

	void artNetServerStopped(ArtNetServer artNetServer);

}
