package fileShareFiles;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;

import modelFiles.Host;
import rdtFiles.*;



public class DownloadThread implements Runnable
{
    private Host host;                      // The host to download from.
    private ArrayList<Integer> chunks;      // The list of chunks to donwload.
    private HashMap<Integer, byte[]>  data; // The downloaded data.
    private String fileName;                // the name of the file to download.

    public DownloadThread(Host host, ArrayList<Integer> chunks, String fileName)
    {
        this.host = host;
        this.chunks = chunks;
        data = new HashMap<>();
        this.fileName = fileName;
    }

    /**
     * Returns the downloaded chunks to the caller.
     * @return the downlaoded chunks.
     */
    public HashMap<Integer, byte[]> getResults()
    {
        return data;
    }
    public ArrayList<Integer> getResult() 
    {
        return chunks;
    }
    public Host getRes() 
    {
        return host;
    }
    public String getR() 
    {
        return fileName;
    }
    /**
     * Download the requested chunks from the given host.
     */
    @Override
    public void run() 
    {
        try {
            for (int i = 0; i < this.chunks.size(); i++)
            {
                constructRequest(i, host); //Sends request to host for chunks
                //receive chunk...
                byte[] chunkData; //= packet.getPacketData()
                data.put(i * 50, chunkData);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void constructRequest(int chunkID, Host h) throws IOException
    {
        chunkID *= 50;
        ChunkRequest c = new ChunkRequest(fileName, chunkID); //Creates new chunk request
        InetAddress newAdd = InetAddress.getByName(host.getAddress()); //Sets InetAddress for use below
        Message cMessage = new Message(newAdd, host.getPort(), c.serialize(), 500); 
        //Creates the message to be sent
        ReliableSocket sock = new ReliableSocket(newAdd, host.getPort()); //Makes new socket to host
        sock.send(cMessage); //Sends host the request
    }
    
}
