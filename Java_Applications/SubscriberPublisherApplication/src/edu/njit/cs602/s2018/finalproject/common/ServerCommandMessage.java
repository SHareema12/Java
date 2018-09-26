package edu.njit.cs602.s2018.finalproject.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *Modified by Safia Hareema and Aditi
 * Sharma
 */
public class ServerCommandMessage {

	public static final String SUBSCRIBED="Subscribed";
	public static final String ALREADY_SUBSCRIBED="Already Subscribed";
	public static final String UNSUBSCRIBED="Unsubscribed";
	public static final String NEVER_SUBSCRIBED="Never Subscribed";
	public enum MessageType {
		RESP_END, SUBSCRIBE_REQ, SUBSCRIBE_RESP, UNSUBSCRIBE_REQ, UNSUBSCRIBE_RESP, SUBSCRIBER_LOGIN_REQ, SUBSCRIBER_LOGIN_RESP_OK, SUBSCRIBER_LOGIN_RESP_ERR, GET_PUBLISHED_TOPICS_REQ, GET_PUBLISHED_TOPICS_RESP, GET_SUBSCRIBED_TOPICS_REQ, GET_SUBSCRIBED_TOPICS_RESP, SERVER_INTERNAL_ERR, TOPIC_MESSAGE;
	}

	private final MessageType msgType;
	public final String message;

	public ServerCommandMessage(MessageType msgType, String message) {
		this.msgType = msgType;
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

	public MessageType getMessageType() {
		return this.msgType;
	}
	
	public String toString(){
		return message;
	}

	public static ServerCommandMessage get(DataInputStream dis) throws IOException {
		//synchronized (dis) {
			//System.out.println("Entered .get method");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String temp = dis.readUTF();
			//System.out.println("Message type " + temp);

			MessageType msgType = MessageType.valueOf(temp);

			String msg = dis.readUTF();
			//System.out.println("Message: " + msg);
			return new ServerCommandMessage(msgType, msg);
		//}
	}

	// Serialize and write to socket
	public void put(DataOutputStream dos) throws IOException {
		//synchronized (dos) {
			//System.out.println("in put method");
			//System.out.println("Message type: " + this.getMessageType().name());
			//System.out.println("Message: " + this.getMessage());
			dos.writeUTF(this.getMessageType().name());
			dos.writeUTF(this.getMessage());
		//}

	}
}