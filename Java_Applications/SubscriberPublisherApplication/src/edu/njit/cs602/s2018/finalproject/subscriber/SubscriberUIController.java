package edu.njit.cs602.s2018.finalproject.subscriber;

import edu.njit.cs602.s2018.finalproject.common.TopicMessage;


/**
 */
public interface SubscriberUIController {

    /**
     * Initiate request to get published topics from the server
     */
    void getPublishedTopics();

    /**
     * Initiate request to get subscribed topics from the server
     */
    void getSubscribedTopics();

    /**
     * Initiate request to subscribe to a topic
     * @param topic
     */
    void subscribeTopic(String topic);

    /**
     * Initiate request to unsubscribe to a topic
     * @param topic
     */
    void unsubscribeTopic(String topic);

    /**
     * Handle topic message received
     * @param topicMessage
     */
    void onReceivedTopicMessage(TopicMessage topicMessage);

}
