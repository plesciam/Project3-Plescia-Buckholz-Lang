package fileShareFiles;

import java.util.ArrayList;
import java.util.HashMap;

//import model.Host;

public class DownloadThread<Host> implements Runnable
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

    /**
     * Download the requested chunks from the given host.
     */
    @Override
    public void run() 
    {
        // TODO: Implement the run method
    }
    
}
