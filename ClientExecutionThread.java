import java.net.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class ClientExecutionThread extends Thread {
	
	public int tID;
	public boolean isleader;
	public MazewarTickerThread ticker;	
	private Queue<MazewarPacket> inQueue;
	private Queue<MazewarPacket> outQueue;
	private Map<String, Client> players;


	// ***Lab3*** map containing connections to all clients
	public static final Map<String, Socket> clientsconn = new HashMap(); 
	
	// ***Lab3*** Socket to communicate to the next client in the ring
	Socket nextclientSkt = null;
	
	
	
	public ClientExecutionThread (Queue<MazewarPacket> inQueue, Queue<MazewarPacket> outQueue, Map<String, Client> clients) 
	{
		super("ClientExecutionThread");
		this.inQueue = inQueue;
		this.outQueue = outQueue;
		this.players = clients;
		this.isleader = false;
		this.ticker = NULL;
	}
	
	


	public void run() {
		
		// poll inQueue for packets, read packet, executePacket()
		while(true){
			if(inQueue.size() != 0) { // Something is in the queue
			
				MazewarPacket head = inQueue.remove();
				if(head != null) {
					executePacket(head);
				}
			}
		}
    }
	
	public void executePacket(MazewarPacket pkt) {
	
		String cID = pkt.cID;
		Client c = players.get(cID);
		assert(c != null);
		
		if(pkt.type == MazewarPacket.MW_REPLY) { // Client event to process
			KeyEvent e = pkt.event;
			// c.execute_command(e);
			
                        // Up-arrow moves forward.
                        if(e.getKeyCode() == KeyEvent.VK_UP) {
                                c.forward();
                        // Down-arrow moves backward.
                        } else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                                c.backup();
                        // Left-arrow turns left.
                        } else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                                c.turnLeft();
                        // Right-arrow turns right.
                        } else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                                c.turnRight();
                        // Spacebar fires.
                        } else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                                c.fire();
                        }
		}
		else if(pkt.type == MazewarPacket.MW_BYE) { // Client wants to quit the game
			// Remove the client from the hash map of players active in the game and from the maze
			players.remove(cID);
			c.maze.removeClient(c);
			
			if(c.getType() == 25) { // Local client is quitting; exit from the Mazewar application
				Mazewar.quit();
			}
		}
		else if(pkt.type == MazewarPacket.MW_TICK){
			c.maze.missiletick();
		}
		else if(pkt.type == MazewarPacket.JOIN_SERV) {
			
		}
		else if(pkt.type == MazewarPacket.RING_INIT) {

		}
		else if(pkt.type == MazewarPacket.RING_PAUSE) {
			
		}
		else if(pkt.type == MazewarPacket.RING_UNPAUSE) {

		}
		else if(pkt.type == MazewarPacket.RING_TOKEN) {
			sendmcast();
		}
		else if(pkt.type == MazewarPacket.MW_ELECTION) {
			// start a ticker thread
			ticker = new MazewarTickerThread();
			// ticker.start();
			// isleader = true;
		}


		else { // Other types have no actions
			return;
		}
	}

	public void createring(MazewarPacket initpkt) {
		// Initialize the sockets to the clients' host names and port #s
		// Initialize socket to next client in ring

	}
	public void sendmcast() {
		// Dequeue and multicast the head of outqueue
		// Send RING_TOKEN to next client
	}
	public void sendmcast(MazewarPacket pkt) {
		// Special multicast to be used only by leader
		// send RING_STOP and RING_RESUME
	}

}
