package artnet4j;


public enum NodeStyle {
	ST_NODE(0,ArtNetNode.class),
	ST_SERVER(1,ArtNetServer.class),
	ST_MEDIA(2,ArtNetNode.class),
	ST_ROUTER(3,ArtNetNode.class),
	ST_BACKUP(4,ArtNetNode.class),
	ST_CONFIG(5,ArtNetNode.class);

	private int id;
	private Class<? extends ArtNetNode> nodeClass;

	private NodeStyle(int id, Class<? extends ArtNetNode> nodeClass) {
		this.id=id;
		this.nodeClass=nodeClass;
	}

	public ArtNetNode createNode() {
		ArtNetNode node = null;
		try {
			node=nodeClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return node;
	}

	public int getStyleID() {
		return id;
	}
}
