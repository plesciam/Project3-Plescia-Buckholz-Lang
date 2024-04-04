package rdtFiles;

import java.net.InetAddress;
import java.util.Arrays;

/**
 * Message
 */
 public class Message 
{
    private InetAddress addr;
    private Message message;
    private int port;
    private byte[] data;
    private int dataLen;      

    public Message(InetAddress addr,int port, byte[] data, int dataLen) 
        {
            this.addr = addr;
            this.port = port;
            this.data = data;    
            this.dataLen = dataLen;
        }

            public void getInetAddress() 
            {
                setInetAddress(0);
                
            }
            
            public int setInetAddress(int inetAddress) {
                return inetAddress;
            }
            public int getDataLenth() 
            {
                return this.dataLen;
            }
            public byte[] getData()
            {
                return Arrays.copyOf(data, dataLen);
            }
            public InetAddress getAddr() 
            {
                return addr;
            }
            public int getPort() 
            {
                return port;
            }
            
            @Override
            public String toString() 
            {
                return addr + ":" + message + port + "send" + dataLen + "bytes";
            }
        }
