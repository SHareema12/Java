package edu.njit.cs602.s2018.finalproject.server;


import edu.njit.cs602.s2018.finalproject.common.ClientServerConnection;

/**
 */
public interface ServerConnectionManager {

    /**
     * Start
     */
    void start();

    /**
     * Set controller for the server connection manager
     * @param controller
     */
    void setServerController(ServerController controller);

    /**
     * Create a subscriber client connection
     * @param connHandle connection implementation object
     * @return
     */
    ClientServerConnection createSubscriberConnection(Object connHandle);

    /**
     * Create a publisher client connection
     * @param connHandle connection implementation object
     * @return
     */
    ClientServerConnection createPublisherConnection(Object connHandle);


    /**
     * Clean up resources associated with a connection
     * @param connectionId
     */
    void removeConnection(int connectionId);

    /**
     * Stop
     */
    void stop();
}
