/*
Copyright (C) 2004 Geoffrey Alan Washburn
    
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.
    
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
    
You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
USA.
*/

import java.lang.*;
import java.util.*;
import java.io.*;
import java.net.*;

/**
 * An abstract class for {@link Client}s in a {@link Maze} that local to the 
 * computer the game is running upon. You may choose to implement some of 
 * your code for communicating with other implementations by overriding 
 * methods in {@link Client} here to intercept upcalls by {@link GUIClient} and 
 * {@link RobotClient} and generate the appropriate network events.
 * @author Geoffrey Washburn &lt;<a href="mailto:geoffw@cis.upenn.edu">geoffw@cis.upenn.edu</a>&gt;
 * @version $Id: LocalClient.java 343 2004-01-24 03:43:45Z geoffw $
 */


public abstract class LocalClient extends Client {

	// queue to store the incoming packets from server
	public static final Queue<MazewarPacket> inQueue = new LinkedList<MazewarPacket>();
		 
	// map of containing active players on maze (Client.name<String> --> <Client Object>)
	public static final Map<String, Client> clients = new HashMap();

	// ***Lab3*** map containing connections to all clients
	public static final Map<String, Socket> clientsconn = new HashMap();
	
	// ***Lab3*** Socket to communicate to the next client in the ring
	public Socket nextclientSkt = null;
	
	// ***Lab3*** Queue to store outgoing packets (to be multicasted on token receive)
	public static final Queue<MazewarPacket> outQueue = new LinkedList<MazewarPacket>();
	
	// thread to listen from server and enqueue packets
	ClientReceiverThread enquethread;
		
	// thread to dequeue and process packets
	ClientExecutionThread dequethread;
	
	// Host name and port number of the Mazewar server
	String hostname;
	int port;
	
	// Socket and streams to communicate to the server with
	Socket srvSocket = null;
        public ObjectOutputStream outStream = null;
        public ObjectInputStream inStream = null;

        /** 
         * Create a {@link Client} local to this machine.
         * @param name The name of this {@link Client}.
         * @param hostname The hostname of the the Mazewar server
         * @param port The port number of the Mazewar server
         */

        public LocalClient(String name, int ctype, String host, int port_num) {
            super(name, ctype);
            assert(name != null);
            
            hostname = host;
            port = port_num;
            try {
            	// Initialize the socket to the server's host name and port #
            	srvSocket = new Socket(hostname,port);

            	System.out.println("Socket created.");
            	outStream = new ObjectOutputStream(srvSocket.getOutputStream());
            	inStream = new ObjectInputStream(srvSocket.getInputStream());

		System.out.println("Socket & input stream created.");
            } catch (UnknownHostException e) {
		System.err.println("ERROR: Don't know where to connect!!");
		System.exit(1);
	    } catch (IOException e) {
		System.err.println("ERROR: Couldn't get I/O for the connection.");
		System.exit(1);
	    }
	    enquethread = new ClientReceiverThread(srvSocket, inQueue, inStream);
	    dequethread = new ClientExecutionThread(inQueue, outQueue, clients, name); 

        }

	public void startthreads(){
	    enquethread.start();
	    dequethread.start();
	}

		
		/*
		*  TODO: 1. Send packages to server
		*			- Create functions to handle key events
		*			- Create MW_REQUEST package
		*			- Send to server via "outStream"
		*
		*		 2. Read and execute packages from queue
		*			- Modify ClientExecutionThread.java
		*			- Dequeue package, lookup package.cID in map 'clients'
		*			- Execute package.KeyEvent on that client
		*
		*		 3.	Handle Fire events
		*			- If a client dies, send MW_INIT package
		*			  to server for that client
		*			
		*		 4. ???
		*/
}
