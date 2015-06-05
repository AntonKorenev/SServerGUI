package ArduinoTelnet;

import java.net.Socket;

/**
 * Connector class
 * Using for connecting to arduino and sending commands
 *
 * @author Artem Chernatsky
 * @version 1.0.0
 */

public class Connector {
    /** DNS name or IP address of the host  */
    private String host;

    /** Number of arduino's port  */
    private int port;

    /**
     * Constructor with default parameters
     * Use locallhost as address and port number 23
     */
    public Connector() {
        host="locallhost";
        port=23;
    }

    /**
     * Constructor with custom address
     * Port will be set as default number 23
     *
     * @param hostname is the address of arduino
     */
    public Connector(String hostname) {
        host = hostname;
        port = 23;
    }

    /**
     * Constructor with custom address and port
     *
     * @param hostname is the address of arduino
     * @param hostport is the port number of arduino
     */
    public Connector(String hostname, int hostport) {
        host = hostname;
        port = hostport;
    }

    /**
     * Method for sending commands to arduino
     *
     * @param command contains text of the command, which will be sent
     * @return the answer of the arduino to the command
     */
    public String sendCommand(String command) {
        String result = "";
        String com = command + "\n";
        try {
            Socket s = new Socket(host, port);
            s.getOutputStream().write(com.getBytes());
            byte buf[] = new byte [64 * 1024];
            int r = s.getInputStream().read(buf);
            String data = new String(buf, 0, r);
            result = result + data;
            s.close();
        } catch (Exception e) {
            System.out.println("init error: " + e);
        }
        return result;
    }
}
