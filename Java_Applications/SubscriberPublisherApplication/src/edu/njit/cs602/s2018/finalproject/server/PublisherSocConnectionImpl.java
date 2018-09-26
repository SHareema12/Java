package edu.njit.cs602.s2018.finalproject.server;

import edu.njit.cs602.s2018.finalproject.common.ClientServerConnection;
import edu.njit.cs602.s2018.finalproject.common.TopicMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Set;

/**
 */
public class PublisherSocConnectionImpl implements Runnable, ClientServerConnection {
	private final Socket connSock;
	protected Thread receiveThread;
	private final ServerController controller;
	private final ServerConnectionManager connMgr;
	private final int clientId;
	private boolean loggedIn;

	public PublisherSocConnectionImpl(int clientId, Socket clientSock, ServerController controller,
			ServerConnectionManager connMgr) {
		//System.out.println("inside the cons for pub impl class");
		this.clientId = clientId;
		this.connSock = clientSock;
		this.controller = controller;
		this.connMgr = connMgr;
		this.loggedIn = true;
	}

	@Override
	public void start() {
		//System.out.println("Cam inside start of recv thread");
		receiveThread = new Thread(this);
		receiveThread.start();
	}

	@Override
	public boolean isAlive() {
		return false;
	}

	@Override
	public boolean login(String clientId) {
		return false;
	}

	@Override
	public Set<String> getPublishedTopics() {
		return this.controller.getAllPublishedTopics();
	}

	@Override
	public Set<String> getSubscribedTopics() {
		return null;
	}

	@Override
	public boolean subscribe(String topic) {
		return false;
	}

	@Override
	public boolean unsubscribe(String topic) {
		return false;
	}

	@Override
	public void sendTopicMessages(List<TopicMessage> topicMessages) {

	}

	@Override
	public void stop() {

	}

	public int getClientId() {
		return this.clientId;
	}

	@Override
	public void run() {
		try {
			//System.out.println("came inside the run method of the publisher");
			DataInputStream is = new DataInputStream(connSock.getInputStream());
			DataOutputStream os = new DataOutputStream(connSock.getOutputStream());
			for (;;) {
				TopicMessage msg = TopicMessage.get(is);
				//System.out.println("Msg: " + msg.toString());
				this.controller.onReceivedTopicMessage(msg);
			}
		} catch (EOFException e) {
			System.out.println("Publisher " + clientId + " closed");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				connSock.close();
				connMgr.removeConnection(clientId);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}