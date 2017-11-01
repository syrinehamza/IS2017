package chat.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import chat.host.DNSserver;
import chat.host.Server;
import chat.messages.Message;
import chat.messages.SystemMessage;
import chat.messages.SystemMessageType;

// Thread for each client. created on server side
public class ClientThread extends Thread {
	private Server server; 
	private Socket client;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private int ClientId; 
	
	public ClientThread (int id,Server server, Socket serverSocket){
			this.server=server;
			this.ClientId=id;
			this.client=serverSocket;
			try {
				this.out=new ObjectOutputStream(client.getOutputStream());
				this.in=new ObjectInputStream(client.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		}
    
	public void run(){
		boolean on = true;
		while(on)
		{
			try {
				Object message =in.readObject();
				Message mesg;
				SystemMessage sysm;
				if(message instanceof Message){
					mesg = (Message) message;
					server.route(mesg);
				}else {	
					sysm = (SystemMessage) message;
					System.out.println("Client Thread: Switch");
					switch (sysm.getType()){
					case LOGIN : server.join(ClientId,(String)sysm.getContent()); break;
					case LOGOUT: on = false;System.out.println("ClientThread Logout: "+(String)sysm.getContent() );server.logoff((String)sysm.getContent()); break;
					case SERVER: out.writeObject(new SystemMessage(server.getMembersList(), SystemMessageType.SERVER));break;
					}
				
				}
			} catch (IOException e) {
				break;
			}catch(ClassNotFoundException e)
			{
				System.out.println("Problem in class not found");
			}
		}
		
		
	}
	
	public ObjectOutputStream getOut(){
		return out;
		
	}

	public Server getServer() {
		return server;
	}

	public Socket getClient() {
		return client;
	}

	public ObjectInputStream getIn() {
		return in;
	}

	public int getClientId() {
		return ClientId;
	}
}
