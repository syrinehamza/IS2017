package chat.threads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.TreeSet;

import chat.controller.Controller;
import chat.host.Client;
import chat.messages.Message;
import chat.messages.SystemMessage;

// Thread for receiving from server. created at client.
public class ClientReceive extends Thread{
     private Client client;
     private ObjectInputStream in;
     private Controller control;
     
    public ClientReceive(Client c, ObjectInputStream in, Controller co)
    {
    	this.client = c;
    	this.in = in;
    	this.control = co;
    }
    
    @SuppressWarnings("unchecked")
	public void run()
    {
    	while(true)
    	{
    		try {
    			Object m;
    			m= in.readObject();
				
				if(m instanceof Message)
				{
					Message msg = (Message) m;
					String s = msg.getSender() +": "+ msg.getMsg();
					client.getControl().ShowMessage(s);
				}
				else
				{
					SystemMessage msg = (SystemMessage) m;
					switch(msg.getType())
					{
					case LOGIN: if(((String)msg.getContent()).charAt(0) == 'S') 
						{
							client.LoginSuccess(msg.getSender());
							control.Join(true);
						}
						else 
						{
							control.Join(false);
						} 
					break;
					case MEMBERS: System.out.println(control);control.UpdateMembers(((TreeSet<String>)msg.getContent()));break;
					case SERVER : control.ShowMembersOfServer((TreeSet<String>)msg.getContent());break;
					}
				}
			} catch (ClassNotFoundException | IOException e) {
				break;
			}
    		
    	}
    }

	public void setControl(Controller control2) {
		this.control = control2;
		
	}
}
