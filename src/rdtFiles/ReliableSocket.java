package rdtFiles;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import fileShareFiles.ChunkRequest;
import fileShareFiles.ChunkResponse;


public class ReliableSocket {
    private DatagramSocket socket;
    private HashMap<String, ConnectionState> senderStates;
    private HashMap<String, ConnectionState> receiverStates;
    private HashMap<String, Timer> timers;
    private final int TIMEOUT = 1000; // Timeout value in milliseconds
    private enum SenderState {
        WAIT_FOR_CALL, WAIT_FOR_ACK, RETRANSMISSION_WAIT_FOR_ACK
    }

    private SenderState senderState;
    private Message currentMessage; // Store the current message being sent

    public ReliableSocket(){
        //Initialize sender state
        senderState = SenderState.WAIT_FOR_CALL;
        senderStates = new HashMap<>();
        timers = new HashMap<>();
    }

    // Constructor to connect to a remote machine
    public ReliableSocket(InetAddress remoteAddress, int remotePort) {
        try{
            this.socket = new DatagramSocket();
            this.socket.connect(remoteAddress, remotePort);
            this.socket.setSoTimeout(TIMEOUT); //Set socket timeout
            this.senderStates = new HashMap<>();
            this.receiverStates = new HashMap<>();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //Constructor for creating a socket with a local port
    public ReliableSocket(int localPort){
        try{
            this.socket = new DatagramSocket(localPort);
            this.socket.setSoTimeout(TIMEOUT); //Set socket timeout
            this.senderStates = new HashMap<>();
            this.receiverStates = new HashMap<>();
            this.timers = new HashMap<>();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //Method to receive a message
    public void send(Message cRes) {
        switch (senderState){
            case WAIT_FOR_CALL:
            sendPacket(cRes);
            currentMessage = cRes;
            senderState = SenderState.WAIT_FOR_ACK;
            break;
            case WAIT_FOR_ACK:
            //Ignore send request as we're waiting for ACK
            break;
            case RETRANSMISSION_WAIT_FOR_ACK:
            //Retransmit message and stay in the same state
            sendPacket(currentMessage); //Retransmit the current message
            break;
        }
        //Receive a DatagramPacket from UDP socket, process it, return Messgae object
        return;
    }

    //Method to handle receiving ACK
    private void receiveACK(){
        switch(senderState){
            case WAIT_FOR_ACK:
            //Transition to WAIT_FOR_CALL state upon receiving ACK
            senderState = SenderState.WAIT_FOR_CALL;
            break;
            case RETRANSMISSION_WAIT_FOR_ACK:
            //Transition to WAIT_FOR_CALL state upon receiving ACK
            senderState = SenderState.WAIT_FOR_CALL;
            break;
            default:
            //Ignore ACK in other states
        }
    }

    //Method to handle timeout
    private void handleTimeout(Message currentMessage){
        switch (senderState){
            case WAIT_FOR_ACK:
            //Retransmit message and stay in WAIT_FOR_ACK state
            sendPacket(currentMessage); //Retransmit the current message
            break;
            case RETRANSMISSION_WAIT_FOR_ACK:
            //Retransmit message and stay in RETRANSMISSION_WAIT_FOR_ACK state
            sendPacket(currentMessage); //Retransmit the current message
            break;
            default:
            //Ignore timeout in other states
            break;
        }
    }

    private void sendPacket(Message message) {
        try{
            //Serialize the message and construct a DatagramPcket
            byte[] data = serializeMessage(message);
            DatagramPacket packet = new DatagramPacket(data, data.length, message.getAddr(), message.getPort());
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
            //Handle exception
        }
    }

    //Method to start timer for messgae
    private void startTimer(Message message){
        //Create a timer for this message and schedule timeout task
        Timer timer = new Timer();
        timers.put(messageKey(message), timer);
        timer.schedule(new TimeoutTask(message), TIMEOUT);
    }

    //Method to handle reception of ACK
    private void receiveACK(Message message){
        //Transmition sender state to WAIT_FOR_CALL upon receiving ACK
        senderState = SenderState.WAIT_FOR_ACK;
        //Cencel timer for this message
        Timer timer = timers.remove(messageKey(message));
        if(timer != null){
            timer.cancel();
        }
    }

    //Helper method to serialize Message object into byte array
    private byte[] serializeMessage(Message message){
        //Implement serialization logic
        return null;
    }

    private String messageKey(Message message){
        //Construct a unique key using message properties
        return message.getAddr().getHostAddress() + ":" + message.getPort();
    }

    //Inner class representing a task to handle message timeout
    private class TimeoutTask extends TimerTask{
        private Message message;

        public TimeoutTask(Message message2) {
            //TODO Auto-generated constructor stub
        }

        public void TimoutTask(Message message){
            this.message = message;
        }

        @Override
        public void run(){
            handleTimeout(message);
        }
    }

    //Method to close the socket
    public void close(){
        //Stop all timers associated with the socket
        for(Timer timer : timers.values()){
            timer.cancel();
        }
        timers.clear();

        //Close the DatagramSocket
        if(socket != null && !socket.isClosed()){
            socket.close();
        }
    }

    //Inner class to represent the connection state
    private class ConnectionState{
        //Deefine the fields representing the state of the conneection
        private int sequnceNumber;
        private boolean waitingForAck;

        //Getter methods
        public int getSequenceNumber(){
            return sequnceNumber;
        }

        public boolean isWaitingForAck(){
            return waitingForAck;
        }

        //Setter methods
        public void setSequenceNumber(int sequnceNumber){
            this.sequnceNumber = sequnceNumber;
        }

        public void setWaitingForAck(boolean waitingForAck){
            this.waitingForAck = waitingForAck;
        }
    }


    //Inner class to represent a timer
    private class Timer{
        private TimerTask task;
        private long timeout;

        public Timer(){
            this.task = task;
            this.timeout = timeout;
        }

        public void schedule(TimeoutTask timeoutTask, int tIMEOUT2){
            java.util.Timer timer = new java.util.Timer();
            timer.schedule(task, timeout);
        }

        public void cancel(){
            task.cancel();
        }
    }

    public void setFailProbability(double failureProb) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setFailProbability'");
    }

  

}