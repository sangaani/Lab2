import java.io.Serializable;

public class MazewarPacket implements Serializable {

	/* define packet formats */
	public static final int MW_NULL    = 0;
	public static final int MW_REQUEST = 100;
	public static final int MW_REPLY   = 200;
	public static final int MW_BYE     = 300;
	
	/* the packet payload */
	
	/* initialized to be a null packet */
	public int type = MW_NULL;
	
	/* send your message here */
	public String cID;
	public keyEvent event;	

	/* send your message here */
	public String message;
	
}
