package fileShareFiles;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import modelFiles.Configuration;
import rdtFiles.Message;
import rdtFiles.ReliableSocket;
import fileShareFiles.*;
import java.io.FileInputStream;
import java.util.Base64;

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
    private BlockingQueue<ReliableSocket> sockQueue;
   
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
        this.sockQueue = new LinkedBlockingQueue<>();
    }

    /**
     * This file sharing thread will service requests for file chunks one 
     * at a time using the RDT protocol.
     */
    @Override
    public void run() 
    {
       try {
        while (true)
        {
            ChunkRequest c; //= sock.receiveChunk(); Need to implement chunk receiving
            sendChunkResponse(c);
        }
       } catch(IOException e){
        e.printStackTrace(); //Records the error captured
       }
    }

    private void sendChunkResponse(ChunkRequest c) throws IOException
    {
        String filePath = c.getFileName(); //Receives the file name
        try (FileInputStream fStream = new FileInputStream(config.getFileDirectory() + filePath)) {
            long start = c.getChunkid();
            fStream.skip(start); //Skips to the starting point of the chunk
            byte[] chunk = new byte[50]; //Sets the size of our chunk
            fStream.read(chunk); //Reads info into array chunk
            String chunkEncoded = Base64.getEncoder().encodeToString(chunk); //Encodes chunk
            ChunkResponse cRes = new ChunkResponse(filePath, c.getChunkid(), chunkEncoded);
            //New chunk response created above
            //Chunk needs to be placed into message
        }
    }
}     //Connect to the network and listen for incoming connections
        try{
            ReliableSocket serverSocket = new ReliableServerSocket(config.getLocalPort());
            System.out.println("FileShareThread: Listening for incoming connections...");

            while(true){
                ReliableSocket sock = serverSocket.accept();
                System.out.println("FileShareThread: Connection established with " + sock.getRemoteSocketAddress());
                sockQueue.add(sock);

                //Spawn a new thread to handle the incoming connection
                Thread handlerThread = new Thread(new FileShareHandler(sock));
                handlerThread.start();
            }
        } catch (SocketException e){
            System.out.println("FileShareThread: Socket closed, exiting...");
        } catch (IOException e){
            e.printStackTrace();
        }
    

    public ReliableSocket getNextSocket() throws InterruptedException{
        return sockQueue.take();
    }


  
    
}
