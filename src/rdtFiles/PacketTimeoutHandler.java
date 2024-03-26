package rdtFiles;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * PacketTimeoutHandler
 */
    public class PacketTimeoutHandler implements Runnable 
    {
        private DatagramSocket Socket;
        private DatagramPacket Packet;
        private Timer resendTimer;

    public PacketTimeoutHandler(DatagramSocket Socket, DatagramPacket Packet, Timer resendTimer) 
    {
        this.Packet = Packet;
        this.Socket = Socket;
        this.resendTimer = resendTimer;
    }  
    
    TimerTask task = new TimerTask() { //Creates a new timer, cancels the old and calls PacketTimeoutHandler once more
        @Override
        public void run() {
            Timer newTimer = new Timer();
            resendTimer.cancel();
            new PacketTimeoutHandler(Socket, Packet, newTimer);
        }
        
    };

    @Override
    public void run() 
    {
        try
        {
            Socket.send(Packet);
        
            try 
            {
                PacketTimeoutHandler.schedule(new PacketTimeoutHandler(Socket, Packet, resendTimer), 1000);
                Socket.send(Packet);
            }
            catch(IllegalStateException ex)
            {
                return;
            }

        }
        catch(IOException e)
        {
            System.out.println("Timer has failed to send for packet" + Packet.getAddress() + Packet.getPort());
            e.printStackTrace();
        }
    }

    private static void schedule(PacketTimeoutHandler packetTimeoutHandler, int i) {
        throw new UnsupportedOperationException("Unimplemented method 'schedule'");
    }
  
}