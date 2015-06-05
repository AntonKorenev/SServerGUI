/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectors;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ao
 */
public class ServerConnector implements Runnable {
    Socket mSocket;
    ObjectInputStream mReader;
    ObjectOutputStream mWriter;
    
    public ServerConnector(){
        setUpNetworkParameters();
    }
    
    private void setUpNetworkParameters(){
        try {
            mSocket = new Socket("127.0.0.1",5000);
            mReader = new ObjectInputStream(mSocket.getInputStream());
            mWriter = new ObjectOutputStream(mSocket.getOutputStream());
            System.out.println("Connection established");
        } catch (IOException ex) {
            Logger.getLogger(ServerConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        Message receivedMessage;
        try {
            while((receivedMessage = (Message) mReader.readObject()) != null){
                //System.out.println(receivedMessage);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void sendMessage(Message message) throws IOException {
        mWriter.writeObject(message);
    }
}
