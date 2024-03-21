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
    
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Timer newTimer = resendTimer;
            resendTimer.cancel();
            PacketTimeoutHandler pT = new PacketTimeoutHandler(Socket, Packet, newTimer);
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
                this.resendTimer.schedule(task,1000);
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
  
}