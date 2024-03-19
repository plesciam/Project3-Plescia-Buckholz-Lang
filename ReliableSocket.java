import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Timer;

public class ReliableSocket {
    private DatagramSocket socket;
    private HashMap<ConnectionIdentifier, SenderState> senderStates;
    private HashMap<ConnectionIdentifier, ReceiverState> receiverStates;
    private HashMap<ConnectionIdentifier, Timer> timers;

    // Constructor to connect to a remote machine
    public ReliableSocket(InetAddress remoteAddress, int remotePort) throws SocketException {
        socket = new DatagramSocket();
        socket.connect(remoteAddress, remotePort);
        senderStates = new Hashmap<>();
    }
}