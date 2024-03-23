package fileShareFiles;

import java.io.IOException;
import modelFiles.Configuration;
import rdtFiles.ReliableSocket;

public class FileShareThread implements Runnable
{

    private static final int MAX_CHUNK_SIZE = 50;       // 50 byte chunk.

    public static int getMaxChunkSize() {
        return MAX_CHUNK_SIZE;
    }

    private Configuration config;       // The configuration.
    public Configuration getConfig() {
        return config;
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

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
