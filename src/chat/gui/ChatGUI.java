package chat.gui;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import chat.controller.Controller;

public class ChatGUI extends JFrame{
	
	private JTextField chatMessage;
	private JTextArea chatHistory;
	private JPanel members;
	private JButton LogOff;
	private JButton Send;
	private JButton MembersOfServer;
	
	public ChatGUI(Controller c)
	{
		super("CHAT");
		setSize(1000, 630);
		setResizable(false);
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		members = new JPanel();
		members.setLayout(new GridLayout(0, 1));
		members.setBackground(Color.WHITE);
		members.setVisible(true);
		getContentPane().add(members);
		members.setBounds(830, 10, 150, 500);
		
		chatHistory=new JTextArea();
		chatHistory.setEditable(false);
		chatHistory.setBackground(Color.white);
		chatHistory.setVisible(true);
		getContentPane().add(chatHistory);
		chatHistory.setBounds(10, 10, 800, 500);
		
		chatMessage = new JTextField();
		chatMessage.setBackground(Color.WHITE);
		chatMessage.setVisible(true);
		getContentPane().add(chatMessage);
		chatMessage.setBounds(5, 515, 810, 70);
		
		Send = new JButton("Send");
		Send.addActionListener(c);
		getContentPane().add(Send);
		Send.setBounds(830, 515, 150, 30);
		Send.setVisible(true);
		
		LogOff = new JButton("Log Off");
		LogOff.addActionListener(c);
		LogOff.setVisible(true);
		getContentPane().add(LogOff);
		LogOff.setBounds(830, 545, 150, 30);
		
		MembersOfServer = new JButton("On My Server");
		MembersOfServer.addActionListener(c);
		MembersOfServer.setVisible(true);
		getContentPane().add(MembersOfServer);
		MembersOfServer.setBounds(830,575,150,30);
		
		setVisible(true);
		
		repaint();
		validate();
		
	}
	
	public JButton getMembersOfServer() {
		return MembersOfServer;
	}

	public JTextField getChatMessage() {
		return chatMessage;
	}

	public JTextArea getChatHistory() {
		return chatHistory;
	}

	public JPanel getMembers() {
		return members;
	}

	public JButton getLogOff() {
		return LogOff;
	}

	public JButton getSend() {
		return Send;
	}
	
	
	
}
