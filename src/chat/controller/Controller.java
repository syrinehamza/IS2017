package chat.controller;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import chat.gui.ChatGUI;
import chat.host.Client;

public class Controller implements ActionListener {
	private Client client;
	private ChatGUI gui;
	private String destination;
	
	public Controller()
	{
		destination = null;
		client = new Client();
		boolean success = false;
		String host = (String) JOptionPane.showInputDialog(null, "Please Enter The Host's IP Address", "Connecting", 
				JOptionPane.PLAIN_MESSAGE, null, null, null);
		if(host!= null && host.length() != 0)
		{
			String port = (String) JOptionPane.showInputDialog(null, "Please Enter The Host's port number", "Connecting", 
					JOptionPane.PLAIN_MESSAGE, null, null, null);
			
			
			success = client.Connect(host, Integer.parseInt(port));
			if(success)
			{
				InitiateNameRequest();
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Something went wrong. Please try again.", "OOPs!!", 
						JOptionPane.PLAIN_MESSAGE);
			}
		}
	}
	
	public void InitiateNameRequest()
	{
		String name = (String) JOptionPane.showInputDialog(null, "Please Enter A Nickname", "Logging in", 
				JOptionPane.PLAIN_MESSAGE, null, null, null);
		client.setControl(this);
		client.join(name);
	}
	
	public void Join(Boolean a)
	{
		if (a)
		{
			gui = new ChatGUI(this);
			gui.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent windowEvent) {
					client.LogOff();
				}
			});
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Username already Taken", "OOPs!!", 
					JOptionPane.PLAIN_MESSAGE);
			InitiateNameRequest();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == gui.getLogOff())
		{
			client.LogOff();
			System.exit(0);
		}
		else if( e.getSource() == gui.getSend())
		{
			if ( destination == null)
			{
				JOptionPane.showMessageDialog(null, "Please choose a user to chat with.", "OOPs!!", 
						JOptionPane.PLAIN_MESSAGE);
			}else
			{	
				gui.getChatHistory().append(client.getName()+" :"+gui.getChatMessage().getText()+"\n");
				client.Chat(client.getName(), destination, 2, gui.getChatMessage().getText());
				gui.getChatMessage().setText("");
				gui.getChatHistory().repaint();
				gui.getChatHistory().validate();
			}
		}else
			if(e.getSource() == gui.getMembersOfServer())
			{
				client.getMembersOfServer();

			}
		else
		{
			String member = ((JButton) e.getSource()).getText();
			gui.getChatHistory().append("You Are Now Sending Messages to: "+member+"\n");
			destination = member;
		}
		
	}

	public void ShowMessage(String s) {
		gui.getChatHistory().append(s+"\n");
		gui.getChatHistory().repaint();
		gui.getChatHistory().validate();
	}

	public void UpdateMembers(TreeSet<String> treeSet) {
		gui.getMembers().removeAll();
		for (String n : treeSet)
		{
			JButton b = new JButton(n);
			b.addActionListener(this);
			gui.getMembers().add(b);
			b.setVisible(true);
		}
		if(treeSet != null && destination != null && !treeSet.contains(destination))
		{
			gui.getChatHistory().append(destination+" Logged out.\n" );
			gui.getChatHistory().repaint();
			gui.getChatHistory().validate();
			destination = null;
		}
		gui.repaint();
		gui.validate();
		
	}

	public void ShowMembersOfServer(TreeSet<String> content) {
		JPanel p = new JPanel(new GridLayout());
		for ( String user : content)
		{
			if(!user.equals(client.getName()))
			{
				JButton b = new JButton(user);
				b.addActionListener(this);
				p.add(b);
				b.setVisible(true);
			}
				
		}
		JOptionPane.showMessageDialog(null,p,"Members on your server",JOptionPane.INFORMATION_MESSAGE);
		
	}
	
	
	
	

}
