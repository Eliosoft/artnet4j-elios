package artnet4j.packets;

public class ArtPollPacket extends ArtNetPacket {

	private static int ARTPOLL_LENGTH = 14;

	private boolean replyOnce;
	private boolean replyDirect;

	public ArtPollPacket() {
		this(true, true);
	}

	public ArtPollPacket(boolean replyOnce, boolean replyDirect) {
		super(PacketType.ART_POLL);
		setData(new byte[ARTPOLL_LENGTH]);
		setHeader();
		setProtocol();
		setTalkToMe(replyOnce, replyDirect);
	}

	@Override
	public int getLength() {
		return data.getLength();
	}

	@Override
	public boolean parse(byte[] raw) {
		setData(raw, ARTPOLL_LENGTH);
		int talk = data.getInt8(12);
		replyOnce = 0 == (talk & 0x02);
		replyDirect = 1 == (talk & 0x01);
		return true;
	}

	private void setTalkToMe(boolean replyOnce, boolean replyDirect) {
		this.replyOnce = replyOnce;
		this.replyDirect = replyDirect;
		data.setInt8((replyOnce ? 0 : 2) | (replyDirect ? 1 : 0), 12);
	}

	@Override
	public String toString() {
		return type + ": reply once:" + replyOnce + " direct: " + replyDirect;
	}
}
