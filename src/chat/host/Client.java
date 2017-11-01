package chat.host;

import java.awt.TrayIcon.MessageType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.TreeSet;

import javax.swing.JOptionPane;

import chat.controller.Controller;
import chat.messages.Message;
import chat.messages.SystemMessage;
import chat.messages.SystemMessageType;
import chat.threads.ClientReceive;

public class Client {
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private String name;
	private Controller control;
	private ClientReceive rThread;
	
	
	
	public boolean Connect(String host, int port)
	{
			
			try {
				socket = new Socket(host,port);
				in = new ObjectInputStream(socket.getInputStream());
				out = new ObjectOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Wrong Input");
				return false;
			}
			
			rThread = new ClientReceive(this, in,control);
			rThread.start();
			return true;
		
	}
	
	

	public synchronized void join(String name1)
	{
			
			if(name1 != null && name1.length() != 0)
			{
					try {
						out.writeObject(new SystemMessage(name1,SystemMessageType.LOGIN));
						out.flush();
						
					} catch ( IOException e) {
						JOptionPane.showMessageDialog(null, "Something went wrong. Please try again", "OOPs!!", 
								JOptionPane.PLAIN_MESSAGE);
					}
				
			}

		
	}
	
	public void getMembersOfServer()
	{
		try {
			out.writeObject(new SystemMessage(name, SystemMessageType.SERVER));
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public synchronized void LoginSuccess(String s)
	{
		name = s;
		System.out.println("Client Login Success: " + s);
	}
	
	public void Chat (String Source, String Destination, int TTL, String Message)
	{
		try {
			out.writeObject(new Message(Message, Source,Destination, TTL));
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void LogOff()
	{
		try {
			System.out.println("Client Logout: "+ name);
			System.out.println(out);
			out.writeObject(new SystemMessage(name,SystemMessageType.LOGOUT));
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Socket getSocket() {
		return socket;
	}

	public ObjectInputStream getIn() {
		return in;
	}

	public ObjectOutputStream getOut() {
		return out;
	}

	public String getName() {
		return name;
	}

	public Controller getControl() {
		return control;
	}
	public void setControl(Controller control) {
		this.control = control;
		rThread.setControl(control);
	}

	
}
	