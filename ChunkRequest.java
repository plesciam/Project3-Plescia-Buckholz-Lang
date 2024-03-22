import com.google.gson.Gson;

public class ChunkRequest implements JSONSerialiazable {
    private String fileName;
    private int chunkId;

    public ChunkRequest(String fileName, int chunkId){
        this.fileName = fileName;
        this.chunkId = chunkId;
    }

    public String getFileName(){
        return fileName;
    }

    public int getChunkid(){
        return ChunkId;
    }

    @Override
    public String toJSON(){
        Gson gson = new Gson();
        return gson.toJSON(this);
    }
    
}
