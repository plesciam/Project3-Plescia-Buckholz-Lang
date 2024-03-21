package fileshare;

import java.io.IOException;
import model.Configuration;
import rdt.ReliableSocket;

public class FileShareThread implements Runnable
{

    private static final int MAX_CHUNK_SIZE = 50;       // 50 byte chunk.

    private Configuration config;       // The configuration.
    private ReliableSocket sock;        // The reliable sock to talk over.
   
    /**
     * Construct a new file sharing thread.
     * @param config the configuration information.
     * @throws IOException 
     */
    public FileShareThread(Configuration config) throws IOException
    {
        this.config = config;
        this.sock = new ReliableSocket(config.getServePort());
        this.sock.setFailProbability(config.getFailureProb());
    }

    /**
     * This file sharing thread will service requests for file chunks one 
     * at a time using the RDT protocol.
     */
    @Override
    public void run() 
    {
       // TODO: Implement the run method.
    }

  
    
}