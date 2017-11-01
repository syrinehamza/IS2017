package chat.threads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import chat.host.DNSserver;
import chat.messages.Message;
import chat.messages.SystemMessage;
import chat.messages.SystemMessageType;

public class ServerThread extends Thread{
	private DNSserver DNSserver; 
	private Socket serverSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private int ServerId;
	
	public ServerThread(DNSserver dns, Socket serverSocket,int id) {
		this.DNSserver=dns;
		this.serverSocket=serverSocket;
		this.ServerId = id;
		try {
			this.out=new ObjectOutputStream(serverSocket.getOutputStream());
		    this.in=new ObjectInputStream(serverSocket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}

	public void run(){
		while(true)
		{
			try {
				Object message =in.readObject();
				Message mesg;
				SystemMessage sysm;
				if(message instanceof Message)
				{
					mesg = (Message) message;
					if(mesg.getTTL()!=0)
					{
					mesg.setTTL(mesg.getTTL()-1);
					System.out.println("ServerThread-> DNS.Route");
					DNSserver.route(mesg);
					}
					else if(mesg.getSender()!= null)
					{
						DNSserver.route(new Message("System Message: Message faild to deliever",null,mesg.getSender(),2));
					}
				}else 
				{
					sysm = (SystemMessage)message;
					switch (sysm.getType()){
					case LOGIN : DNSserver.login(sysm, this); System.out.println("SERVERTHREAD:Login"); break;
					case LOGOUT: DNSserver.logOff((String)sysm.getContent()); break;
					
				}
				
				
				}
			}
			catch(IOException e)
			{
				 break;
			} catch (ClassNotFoundException e) {
				System.out.println("Problem in SerevrThread");
			}
		
		}
	}
	public ObjectOutputStream getOut() {
		return out;
	}
	public int getServerId() {
		return ServerId;
	}


}
