package edu.njit.cs602.s2018.finalproject.publisher;


import edu.njit.cs602.s2018.finalproject.common.TopicMessage;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;


/**
 */
public class SimplePublisher {

    private final Socket cs;
    private final DataInputStream dis;
    private final DataOutputStream dos;

    public SimplePublisher(String serverHost, int serverPort) throws IOException {
        this.cs = new Socket(serverHost, serverPort);
        dis = new DataInputStream(cs.getInputStream());
        dos = new DataOutputStream(cs.getOutputStream());
        System.out.println("Publisher connected to server " + serverHost+":" + serverPort);
    }

    public void send(TopicMessage msg) throws IOException {
        msg.put(dos);
        dos.flush();
    }

    public static void main(String [] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("helloooo i'm in the main for simple publisher");
            SimplePublisher publisher = new SimplePublisher(args[0], Integer.parseInt(args[1]));
            for (; ;) {
                System.out.print("Topic:");
                String topic = scanner.nextLine();
                System.out.print("Msg:");
                String msg = scanner.nextLine();
                publisher.send(new TopicMessage(topic, msg, new Date(System.currentTimeMillis())));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
