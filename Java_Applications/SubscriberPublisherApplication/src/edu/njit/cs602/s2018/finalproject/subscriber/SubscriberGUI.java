/*
* Written by Safia Hareema
*/

package edu.njit.cs602.s2018.finalproject.subscriber;

import java.awt.EventQueue;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.swing.JFrame;

import edu.njit.cs602.s2018.finalproject.common.ServerCommandMessage;
import edu.njit.cs602.s2018.finalproject.common.TopicMessage;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextPane;
import java.awt.TextArea;
import javax.swing.JTextArea;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 * Created by Safia Hareema
 */

public class SubscriberGUI implements SubscriberUserInterface {

	private SubscriberUIController subscriber;
	private JFrame frame;
	private JButton btnSubscribe;
	private JComboBox<String> comboBoxPublished;
	private JTextArea serverFeed;
	private JTextArea newsFeed;
	private JButton btnUnsubscribe;
	private JComboBox<String> comboBoxSubbed;
	private JButton btnRefresh;
	private boolean serverAlive = false;
	private ConcurrentSkipListSet<String> publishedTopics = new ConcurrentSkipListSet<>();
	private ConcurrentSkipListSet<String> subbedTopics = new ConcurrentSkipListSet<>();
	private ConcurrentSkipListSet<String> topicMessages = new ConcurrentSkipListSet<>();
	private JScrollPane sb2;
	private JScrollPane sb1;

	/**
	 * Create the application.
	 */
	public SubscriberGUI() {
		setUIController(subscriber);
		initialize();
	}

	// set window to visible
	public void viewWindow() {
		this.frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(200, 200, 585, 340);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JLabel lblNewsFeed = new JLabel("News Feed");
		lblNewsFeed.setBounds(248, 180, 62, 14);
		panel.add(lblNewsFeed);

		JLabel lblServerFeed = new JLabel("Server Feed");
		lblServerFeed.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblServerFeed.setBounds(248, 76, 70, 14);
		panel.add(lblServerFeed);

		btnSubscribe = new JButton("Subscribe");
		btnSubscribe.setIcon(null);
		btnSubscribe.setBounds(10, 11, 118, 23);

		panel.add(btnSubscribe);

		btnSubscribe.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!(comboBoxPublished.getItemAt(0) == null)) {
					subscribe(comboBoxPublished.getSelectedItem().toString());
				}
			}
		});

		btnUnsubscribe = new JButton("Unsubscribe");
		btnUnsubscribe.setBounds(328, 11, 118, 23);
		panel.add(btnUnsubscribe);

		btnUnsubscribe.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!(comboBoxSubbed.getItemCount() == 0)) {
					unsubscribe(comboBoxSubbed.getSelectedItem().toString());
				}
			}
		});

		comboBoxPublished = new JComboBox<>();
		comboBoxPublished.setBounds(138, 12, 103, 20);
		panel.add(comboBoxPublished);

		comboBoxSubbed = new JComboBox<>();
		comboBoxSubbed.setBounds(456, 12, 103, 20);
		panel.add(comboBoxSubbed);

		btnRefresh = new JButton("Refresh");
		btnRefresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				refresh();
			}
		});
		btnRefresh.setToolTipText("Refresh");
		btnRefresh.setBounds(248, 42, 80, 23);
		panel.add(btnRefresh);

		serverFeed = new JTextArea();
		serverFeed.setEditable(false);
		serverFeed.setBackground(Color.LIGHT_GRAY);
		serverFeed.setBounds(0, 97, 576, 83);
		sb1 = new JScrollPane(serverFeed);
		sb1.setBounds(0, 93, 569, 87);
		panel.add(sb1);

		newsFeed = new JTextArea();
		newsFeed.setEditable(false);
		newsFeed.setBounds(0, 197, 566, 104);
		sb2 = new JScrollPane(newsFeed);
		sb2.setBounds(0, 201, 569, 100);
		panel.add(sb2);

	}

	// subscribe to a topic
	public void subscribe(String topic) {
		this.subscriber.subscribeTopic(topic);
		if (this.publishedTopics.contains(topic)) {
			updatePubAndSub(topic, true);
		}
	}

	// remove topic from published and add it to subscribed
	private void updatePubAndSub(String topic, boolean subscribedFlag) {
		DefaultComboBoxModel<String> model1 = new DefaultComboBoxModel<>();
		DefaultComboBoxModel<String> model2 = new DefaultComboBoxModel<>();
		if (subscribedFlag) {
			publishedTopics.remove(topic);
			subbedTopics.add(topic);
		} else {
			publishedTopics.add(topic);
			subbedTopics.remove(topic);
		}
		for (String t : this.publishedTopics) {
			model1.addElement(t);
		}
		for (String t : this.subbedTopics) {
			model2.addElement(t);
		}
		comboBoxPublished.setModel(model1);
		comboBoxSubbed.setModel(model2);
	}

	// refresh page aka get new topics published if there are any
	public void refresh() {
		this.subscriber.getPublishedTopics();
	}

	// unsubscribe from a topic
	public void unsubscribe(String topic) {
		this.subscriber.unsubscribeTopic(topic);
		if (this.subbedTopics.contains(topic)) {
			updatePubAndSub(topic, false);
		}
	}

	@Override
	/**
	 * Set controller to invoke for user inputs
	 * 
	 * @param controller
	 */
	public void setUIController(SubscriberUIController subscriber) {

		this.subscriber = subscriber;
	}

	@Override
	/**
	 * Display server status in UI
	 * 
	 * @param errMsg
	 */
	public void setServerStatus(ErrorMessage errMsg) {
		serverFeed.setText(errMsg.toString() + serverFeed.getText());
		return;

	}

	@Override
	/**
	 * Get subscriber id input from UI
	 * 
	 * @return
	 */
	public String getSubscriberId() {
		return null;
	}

	@Override
	/**
	 * Display all topics publsihed
	 * 
	 * @param topics
	 */
	public void setPublishedTopics(Set<String> topics) {
		for (String topic : topics) {
			if (!this.subbedTopics.contains(topic)) {
				if (!(this.publishedTopics.contains(topic)) || this.publishedTopics.isEmpty()) {
					this.publishedTopics.add(topic);
				}
			}
		}

		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
		for (String t : this.publishedTopics) {
			model.addElement(t);
		}
		comboBoxPublished.setModel(model);
		postServerMessage("Published Topics Updated");
	}

	@Override
	/**
	 * Display topics subscribed by the client
	 * 
	 * @param topics
	 */
	public void setSubscribedTopics(Set<String> topics) {
		for (String topic : topics) {
			if (!(this.subbedTopics.contains(topic)) || this.subbedTopics.isEmpty()) {
				this.subbedTopics.add(topic);
			}
			if (this.publishedTopics.contains(topic)) {
				this.publishedTopics.remove(topic);
			}
		}

		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
		for (String t : this.subbedTopics) {
			model.addElement(t);
		}
		comboBoxSubbed.setModel(model);
		setPublishedTopics(this.publishedTopics);
		postServerMessage("Subscribed Topics Updated");
	}

	@Override
	/**
	 * Start user interface
	 * 
	 * @param user
	 */
	public void startUser(String user) {
		frame.setTitle(user + "'s News Client");
		viewWindow();

	}

	@Override
	/**
	 * Set published messages received from the server
	 * 
	 * @param messages
	 */
	public void setPublishedMessages(List<TopicMessage> messages) {
		for (TopicMessage message : messages) {
			serverFeed.setText(serverFeed.getText() + "\n" + message.toString());
		}
		serverFeed.update(serverFeed.getGraphics());
	}

	// post new serverFeed message
	public void postServerMessage(String message) {
		serverFeed.setText(message + "\n" + serverFeed.getText());
	}

	// check if topic message has already been posted
	public boolean topicPosted(ServerCommandMessage topicMessage) {
		if (this.topicMessages == null || this.topicMessages.isEmpty()) {
			return false;
		}
		if (this.topicMessages.contains(topicMessage)) {
			return true;
		}
		return false;
	}

	// post new topic message on newsFeed
	public void postTopicMessage(ServerCommandMessage topicMessage) {
		String parsedMessage = parseMessage(topicMessage.message);
		if (parsedMessage != null && !(this.topicMessages.contains(parsedMessage))) {
			this.topicMessages.add(parsedMessage);
			newsFeed.setText(parsedMessage + "\n" + newsFeed.getText());
		}
	}

	// disable buttons if server goes down
	public void disableButtons() {
		btnSubscribe.setEnabled(false);
		btnUnsubscribe.setEnabled(false);
	}

	// parse topic messages to fit <topic><timestamp><message> format
	public String parseMessage(String topicMessage) {
		StringBuilder sb = new StringBuilder();
		// extract topic
		sb.append("<");
		sb.append(topicMessage.substring(6, topicMessage.indexOf(',')));
		sb.append("><");
		// extract timestamp
		sb.append(topicMessage.substring((topicMessage.indexOf("time:") + 5),
				topicMessage.indexOf(',', (topicMessage.indexOf(',')) + 1)));
		sb.append("><");
		// extract message
		sb.append(topicMessage.substring((topicMessage.indexOf("msg:") + 4)));
		sb.append(">");
		return sb.toString();
	}
}