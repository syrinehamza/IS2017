package chat.host;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.TreeSet;

import chat.messages.Message;
import chat.messages.SystemMessage;
import chat.messages.SystemMessageType;
import chat.threads.ServerReceive;
import chat.threads.ServerThread;

public class DNSserver {
private ServerSocket DNS;
private int port;
private Hashtable<Integer,ServerThread> servers;
private Hashtable<String,Integer> clients;
private int count = 0;

public DNSserver()
{
	try {
		port = 6000;
		DNS = new ServerSocket(6000);
		servers = new Hashtable<>();
		clients = new Hashtable<>();
		
		Socket serverSocket;
		while(true)
		{
			try {
				serverSocket = DNS.accept();
				ServerThread s;
				servers.put(++count, s = new ServerThread(this,serverSocket,count));
				s.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}



public synchronized void logOff(String name)
{
	clients.remove(name);
//	System.out.println("DNS: Logout: "+ name);
	TreeSet<String> t = members();
//	System.out.println(clients.toString());
	for(int server : servers.keySet())
	{
		try {
			servers.get(server).getOut().writeObject(new SystemMessage(t,SystemMessageType.MEMBERS));
			servers.get(server).getOut().flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

public synchronized void login(SystemMessage msg, ServerThread server)
{
	ServerThread st = server;
	String s = (String)msg.getContent();
	if(clients.containsKey(s))
	{
		try {
			st.getOut().writeObject(new SystemMessage("Failed",SystemMessageType.LOGIN,msg.getSender()));
			st.getOut().flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}else{
//		System.out.println("DNS login:"+(String)msg.getContent());
		try {
			st.getOut().writeObject(new SystemMessage(msg.getContent(),SystemMessageType.LOGIN,msg.getSender()));
//			System.out.println("DNS login:"+(String)msg.getContent()+"After");
			st.getOut().flush();
			TreeSet<String> t = members();
			t.add((String) msg.getContent());
			clients.put((String)msg.getContent(),st.getServerId());
//			System.out.println(clients.toString());
			for(int server1 : servers.keySet())
			{
				try {
					servers.get(server1).getOut().writeObject(new SystemMessage(t,SystemMessageType.MEMBERS));
					servers.get(server1).getOut().flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

public synchronized void route(Message msg)
{
	try {
		servers.get(clients.get(msg.getReceiver())).getOut().writeObject(msg);
		System.out.println("DNS.Route -> ServerReceive");
		servers.get(clients.get(msg.getReceiver())).getOut().flush();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}


public TreeSet<String> members()
{
	TreeSet<String> t = new TreeSet<>();
	for(String user : clients.keySet())
	{
			t.add(user);
	}
	return t;
}


}
