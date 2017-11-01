package chat.threads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.TreeSet;

import chat.host.Server;
import chat.messages.Message;
import chat.messages.SystemMessage;

public class ServerReceive extends Thread {
	private Server server;
	private ObjectInputStream in;
//	Created by server to receive from DNS.
	public ServerReceive(Server server,ObjectInputStream in) {
		this.in = in;
		this.server = server;
	}
	
	@SuppressWarnings("unchecked")
	public void run()
	{
		while(true)
		{
			try {
				Object x = in.readObject();
				if ( x instanceof SystemMessage)
				{
					SystemMessage msg = (SystemMessage) x;
					switch(msg.getType())
					{
						case LOGIN: server.joinResponse(Integer.parseInt(msg.getSender()),(String)msg.getContent());break;
						case MEMBERS: server.MembersListResponse((TreeSet<String>)msg.getContent()); break;
					}
				}
				else
				{
					// chat message
					System.out.println("ServerReceive -> Server.Route");
					server.route((Message) x);
				}
			}
			 catch (IOException e) {
				break;
			
			} catch (ClassNotFoundException e) {
				System.out.println("Problem in ServerReceive");
			}
		}
	}

	

}
