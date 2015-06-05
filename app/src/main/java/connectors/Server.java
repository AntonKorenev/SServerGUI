/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectors;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

/**
 *
 * @author ao
 */
/*public class MobileConnector implements Runnable{
    ArrayList<PrintWriter> mClientOutputStreams;
    boolean mStopCondition;
    
    public MobileConnector(){
        mClientOutputStreams = new ArrayList<>();
        mStopCondition = false;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            
            while(!mStopCondition){
                Socket clientSocket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                mClientOutputStreams.add(writer);
                
                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
                System.out.println("new connection with "+clientSocket.getLocalAddress().getHostAddress());
            }
        } catch (IOException ex) {
            Logger.getLogger(MobileConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void broadcast(Message message){
        for(PrintWriter writer: mClientOutputStreams){
            try{
                writer.println(message);
                writer.flush();
            } catch(Exception ex){
                Logger.getLogger(MobileConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    
    public class ClientHandler implements Runnable{
        ObjectInputStream mReader;
        Socket mSocket;
        
        public ClientHandler(Socket clientSocket){
            mSocket = clientSocket;
            try {
                InputStreamReader isReader = new InputStreamReader ( mSocket.getInputStream() ) ;
            } catch (IOException ex) {
                Logger.getLogger(MobileConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run(){
            Message receivedMessage;
            try {
                while((receivedMessage = mReader.) != null){
                    System.out.println(receivedMessage);//вместо этого действие на enum
                    PrintWriter writer = new PrintWriter(mSocket.getOutputStream());
                    writer.println("");
                    writer.flush();
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }
    
   
    
}*/

public class Server extends Thread{
    ServerSocket mServerSocket;
    HashSet<Socket> mClientSockets;
    int mPortNumber;
    boolean mStopCondition = false;

    public Server(int portNumber) {
        mClientSockets = new HashSet<>();
        mPortNumber = portNumber;
        establishConnection();
    }

    private void establishConnection(){
        try {
            System.out.println("Waiting for a connection on " + mPortNumber);
            mServerSocket = new ServerSocket(mPortNumber);
            System.out.println("Success");
        } catch (IOException e) {
            System.out.println("Connection error!");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!isInterrupted()){
            try {
                Socket sock = mServerSocket.accept();
                System.out.println("socket added");
                mClientSockets.add(sock);

                ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());

                Message msg;
                while ((msg = (Message) ois.readObject()) != null) {
                    switch (msg.getAction()){
                        case GET:
                            switch (msg.getParameter()){
                                case TEMPERATURE:
                                    //oos.writeObject();
                                    break;
                                case HUMIDITY:
                                    //oos.writeObject();
                                    break;
                                case LUMINOSITY:
                                    //oos.writeObject();
                                    break;
                                case PRESSURE:
                                    //oos.writeObject();
                                    break;
                                case ENERGY_USAGE:
                                    //oos.writeObject();
                                    break;
                            }
                            break;
                        case SET:
                            switch (msg.getParameter()){
                                case TEMPERATURE:
                                    msg.getSetValue();
                                    break;
                                case HUMIDITY:
                                    msg.getSetValue();
                                    break;
                                case LUMINOSITY:
                                    msg.getSetValue();
                                    break;
                            }
                            break;
                        case TURN:
                            switch (msg.getParameter()){
                                case TEMPERATURE:
                                    msg.getSwitchValue();
                                    break;
                                case HUMIDITY:
                                    msg.getSwitchValue();
                                    break;
                                case LUMINOSITY:
                                    msg.getSwitchValue();
                                    break;
                                case ENERGY_USAGE:
                                    msg.getSwitchValue();
                                    break;
                            }
                            break;
                    }
                    //oos.writeObject();
                    break;
                }

                oos.close();
                ois.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}