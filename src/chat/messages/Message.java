package chat.messages;

import java.io.Serializable;

public class Message implements Serializable {
	private String msg;
	private String sender;
	private String receiver;
	private int TTL;
	
	public Message(String m, String sender, String receiver, int TTL)
	{
		msg = m;
		this.sender = sender;
		this.receiver = receiver;
		this.TTL = TTL;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public int getTTL() {
		return TTL;
	}

	public void setTTL(int tTL) {
		TTL = tTL;
	}
	
	
}
