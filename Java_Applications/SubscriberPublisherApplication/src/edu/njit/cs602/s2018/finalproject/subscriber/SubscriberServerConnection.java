package edu.njit.cs602.s2018.finalproject.subscriber;

/*
 * Created by Safia Hareema
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import edu.njit.cs602.s2018.finalproject.common.ClientServerConnection;
import edu.njit.cs602.s2018.finalproject.common.ServerCommandMessage;
import edu.njit.cs602.s2018.finalproject.common.ServerCommandMessage.MessageType;
import edu.njit.cs602.s2018.finalproject.common.TopicMessage;

public class SubscriberServerConnection implements ClientServerConnection, Runnable {
	private Socket conn = null;
	private DataInputStream dis;
	private DataOutputStream dos;
	private String subscriberID = "";
	private boolean isAlive = false;
	private volatile boolean needServerCommand = false;
	private Thread newsThread;
	private volatile ServerCommandMessage serverMessage;
	private volatile ServerCommandMessage topicMessage = null;
	private Set<String> waitingObj = new TreeSet<String>();
	private Subscriber subscriber;

	public SubscriberServerConnection(String hostName, int serverPort) throws IOException {

		try {
			this.conn = new Socket(hostName, serverPort);
			isAlive = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// server down
			System.out.println(ErrorMessage.SERVER_DOWN.toString());
		}

		if (this.conn != null) {
			dis = new DataInputStream(conn.getInputStream());
			dos = new DataOutputStream(conn.getOutputStream());
		}
	}

	// set subscriber id
	public void setSubscriberID(String subscriberID) {
		this.subscriberID = subscriberID;
	}

	// get topic message
	public ServerCommandMessage getTopicMessage() {
		return this.topicMessage;
	}

	// link subscriber object
	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	@Override
	public void start() {
		this.newsThread = new Thread(this);
		newsThread.setName("newsThread");
		newsThread.start();
	}

	@Override
	public boolean isAlive() {
		return this.isAlive;
	}

	@Override
	public boolean login(String clientId) {
		boolean result;
		ServerCommandMessage command = new ServerCommandMessage(MessageType.SUBSCRIBER_LOGIN_REQ, clientId);
		synchronized (dis) {
			try {
				this.send(command, dos);
				this.serverMessage = ServerCommandMessage.get(dis);
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (this.serverMessage.getMessageType().name()
					.equalsIgnoreCase(MessageType.SUBSCRIBER_LOGIN_RESP_OK.name())) {
				result = true;
			} else {
				result = false;
			}
		}

		return result;

	}

	@Override
	public Set<String> getPublishedTopics() {
		Set<String> res = new TreeSet<>();
		this.needServerCommand = true;

		ServerCommandMessage command = new ServerCommandMessage(MessageType.GET_PUBLISHED_TOPICS_REQ, "test");
		try {
			this.send(command, dos);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		synchronized (waitingObj) {
			try {
				waitingObj.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (String serverResp : waitingObj) {
			res.add(serverResp);
		}
		waitingObj.clear();
		return res;
	}

	@Override

	public Set<String> getSubscribedTopics() {
		Set<String> res = new TreeSet<>();
		this.needServerCommand = true;

		ServerCommandMessage command = new ServerCommandMessage(MessageType.GET_SUBSCRIBED_TOPICS_REQ, "test");
		try {
			this.send(command, dos);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		synchronized (waitingObj) {
			try {
				waitingObj.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (String serverResp : waitingObj) {
			res.add(serverResp);
		}
		waitingObj.clear();

		return res;

	}

	@Override
	public boolean subscribe(String topic) {
		boolean subscribed = false;
		this.needServerCommand = true;

		ServerCommandMessage command = new ServerCommandMessage(MessageType.SUBSCRIBE_REQ, topic);
		try {
			this.send(command, dos);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		synchronized (waitingObj) {
			try {
				waitingObj.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (String serverResp : waitingObj) {
			if (serverResp.equals(ServerCommandMessage.SUBSCRIBED)) {
				subscribed = true;
			} else {
				subscribed = false;
			}
		}
		waitingObj.clear();

		return subscribed;
	}

	@Override
	public boolean unsubscribe(String topic) {
		boolean unsubscribed = false;
		this.needServerCommand = true;

		ServerCommandMessage command = new ServerCommandMessage(MessageType.UNSUBSCRIBE_REQ, topic);
		try {
			this.send(command, dos);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		synchronized (waitingObj) {
			try {
				waitingObj.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (String serverResp : waitingObj) {
			if (serverResp.equals(ServerCommandMessage.UNSUBSCRIBED)) {
				unsubscribed = true;
			} else {
				unsubscribed = false;
			}
		}
		waitingObj.clear();

		return unsubscribed;
	}

	@Override
	public void sendTopicMessages(List<TopicMessage> topicMessages) {
		// NOT NEEDED FOR CLIENT SIDE

	}

	@Override

	public void stop() {
	}

	public void send(ServerCommandMessage command, DataOutputStream dos) throws IOException {
		command.put(dos);
		dos.flush();
	}

	@Override
	public void run() {
		for (;;) {
			boolean istopicmessage = false;
			ServerCommandMessage topmessage = null;
			try {
				topmessage = ServerCommandMessage.get(dis);
			} catch (IOException e) {
				System.out.println("Server down");
				try {
					conn.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if (this.conn != null) {

				}
				System.exit(0);
			}
			if (topmessage.getMessageType().equals(ServerCommandMessage.MessageType.TOPIC_MESSAGE)) {
				istopicmessage = true;
			}
			this.topicMessage = topmessage;
			if (!istopicmessage) {
				synchronized (waitingObj) {
					switch (topmessage.getMessageType()) {
					case SUBSCRIBER_LOGIN_RESP_OK:
						break;
					case GET_PUBLISHED_TOPICS_RESP:
						while (!(topmessage.getMessageType().equals(ServerCommandMessage.MessageType.RESP_END))) {
							waitingObj.add(topmessage.message);
							try {
								topmessage = ServerCommandMessage.get(dis);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						break;
					case GET_SUBSCRIBED_TOPICS_RESP:
						while (!(topmessage.getMessageType().equals(ServerCommandMessage.MessageType.RESP_END))) {
							waitingObj.add(topmessage.message);
							try {
								topmessage = ServerCommandMessage.get(dis);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						break;
					case SUBSCRIBE_RESP:
						waitingObj.add(topmessage.message);
						break;
					case UNSUBSCRIBE_RESP:
						waitingObj.add(topmessage.message);
						break;
					default:
						break;
					}
					waitingObj.notify();
				}
			} else {
				if (!((topmessage == null) && (subscriber.getUserInterface().topicPosted(topmessage)))) {
					subscriber.getUserInterface().postTopicMessage(topmessage);
				}
			}
		}
	}

}