package fileShareFiles;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import modelFiles.Host;
import rdtFiles.*;



public class DownloadThread implements Runnable
{
    private Host host;                      // The host to download from.
    private ArrayList<Integer> chunks;      // The list of chunks to donwload.
    private HashMap<Integer, byte[]>  data; // The downloaded data.
    private String fileName;                // the name of the file to download.
    private ReliableSocket sock;  //Makes new socket to host

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

    private void setSocket(InetAddress n)
    {
        this.sock = new ReliableSocket(n, host.getPort());
    }
    /**
     * Download the requested chunks from the given host.
     */
     //Sets InetAddress for use below
    @Override
    public void run() 
    {
        try {
            InetAddress newAdd = InetAddress.getByName(getRes().getAddress());
            setSocket(newAdd); //Sets the currently connected socket to the required address
            for (int i = 0; i < this.chunks.size(); i++)
            {
                constructRequest(i, host); //Sends request to host for chunks
                Message m = sock.receive(); //Receives message
                byte[] chunkData = m.getData(); //Collects data
                chunkData = Base64.getDecoder().decode(chunkData); //Decodes data from encoder
                String cDataString = new String(chunkData, StandardCharsets.UTF_8); //Puts data into 
                // a string to be turned into a JSON object.
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
        InetAddress newAdd = InetAddress.getByName(getRes().getAddress());
        Message cMessage = new Message(newAdd, host.getPort(), c.serialize(), 500); 
        //Creates the message to be sent
        sock.send(cMessage); //Sends host the request
    }
    
}
