package chat.host;

import java.awt.TrayIcon.MessageType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Stack;
import java.util.TreeSet;

import chat.messages.Message;
import chat.messages.SystemMessage;
import chat.messages.SystemMessageType;
import chat.threads.ClientThread;
import chat.threads.ServerReceive;
import chat.threads.ServerThread;

public class Server {
	private ServerSocket connection;
	private Socket dns;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Hashtable<String,ClientThread> Clients;
	private Hashtable <Integer,ClientThread> ClientsId;
	private int count = 0;
	private ServerReceive rThread;
	
	public Server(String dns, int port) throws UnknownHostException, IOException{
		
			
			connection = new ServerSocket (port);
			this.dns = new Socket(dns,6000);
			in = new ObjectInputStream(this.dns.getInputStream());
			out = new ObjectOutputStream(this.dns.getOutputStream());
			
			Clients =  new Hashtable<>();
			ClientsId = new Hashtable<>();
			rThread = new ServerReceive(this,in);
			rThread.start();
			
			
			Socket client;
			while(true)
			{
				
					client = connection.accept();
					ClientThread ct ;
					ClientsId.put(++count, ct = new ClientThread(count,this,client));
					ct.start();
				
			}
		
		

	}
	
	public synchronized void join(int id, String username)
	{
		System.out.println("Server:"+username);
		try {
			out.writeObject(new SystemMessage(username,SystemMessageType.LOGIN, ""+id));
			out.flush();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public synchronized void joinResponse(int id,String username)
	{
		String s = username;
		System.out.println("Server response" + username);
		ClientThread ct = ClientsId.get(id);
		if(!s.equals("Failed"))
		{
			Clients.put(username,ct);
			try {
				ct.getOut().writeObject(new SystemMessage("Successfully Connected.",SystemMessageType.LOGIN,s));
				System.out.println("Server response" + "Successfully Connected.");
				ct.getOut().flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else
		{
			
			try {
				ct.getOut().writeObject(new SystemMessage("Username already used. Please try again.",SystemMessageType.LOGIN));
				ct.getOut().flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void route(Message msg)
	{
		ClientThread ct = Clients.get(msg.getReceiver());
		if(ct != null)
		{
			try {
				ct.getOut().writeObject(msg);
				ct.getOut().flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else
		{
			System.out.println("Server Routes to DNS");
			try {
				out.writeObject(msg);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public synchronized void MembersListResponse(TreeSet<String> t )
	{
		for ( String user : Clients.keySet())
		{
			t.remove(user);
			ClientThread ct = Clients.get(user);
			try {
				ct.getOut().writeObject(new SystemMessage(t,SystemMessageType.MEMBERS));
				ct.getOut().flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			t.add(user);
		}
		
	}
	
	public synchronized void logoff(String username) throws IOException
	{
		System.out.println("Server Logout: "+ username);
			Clients.get(username).getClient().close();
			Clients.remove(username);
			out.writeObject(new SystemMessage(username, SystemMessageType.LOGOUT));
			out.flush();
		
	}
	
	public synchronized TreeSet<String> getMembersList()
	{
		TreeSet<String> t = new TreeSet<>();
		for ( String s : Clients.keySet())
			t.add(s);
		return t;
		
	}

	public ServerReceive getRThread() {
		return rThread;
	}
	
}
