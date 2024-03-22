import java.io.InvalidObjectException;
import java.util.Base64;
import merrimackutil.json.JSONSerializable;
import merrimackutil.json.types.JSONType;

public class ChunkResponse implements JSONSerializable{
    private String fileName;
    private int chunkId;
    private byte[] chunkData;

    public ChunkResponse(String fileName, int chunkId, byte[] chunkData){
        this.fileName = fileName;
        this.chunkId = chunkId;
        this.chunkData = chunkData;
    }

    public String getFileName(){
        return fileName;
    }

    public int getChunkid(){
        return chunkId;
    }

    public byte[] getChunkData(){
        return chunkData;
    }

    @Override
    public String toJSON(){
        return "{\"file-name\": \"" + fileName + "\", \"chunk-id\": " + chunkId + ", \"chunk-data\": \"" + Base64.getEncoder().encodeToString(chunkData) + "\"}";
    }

    @Override
    public void deserialize(JSONType arg0) throws InvalidObjectException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deserialize'");
    }

    @Override
    public String serialize() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'serialize'");
    }

    @Override
    public JSONType toJSONType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toJSONType'");
    }
}
