package chat.messages;

import java.io.Serializable;
import java.util.TreeSet;

public class SystemMessage implements Serializable{
private Object content;
private SystemMessageType type;
private String sender;

public Object getContent() {
	return content;
}

public void setContent(String content) {
	this.content = content;
}

public SystemMessageType getType() {
	return type;
}

public void setType(SystemMessageType type) {
	this.type = type;
}

public SystemMessage(Object t,SystemMessageType members ) {
	this.type = members;
	this.content = t;
}

public SystemMessage(Object content, SystemMessageType type,String sender) {
	this.content = content;
	this.type = type;
	this.sender = sender;
}
public String getSender()
{
	return sender;
}

}

