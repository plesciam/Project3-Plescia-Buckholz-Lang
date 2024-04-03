package fileShareFiles;
import java.io.InvalidObjectException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import merrimackutil.json.JSONSerializable;
import merrimackutil.json.types.JSONObject;
import merrimackutil.json.types.JSONType;


public class ChunkResponse implements JSONSerializable{
    private String fileName;
    private int chunkId;
    private String chunkData;

    public ChunkResponse(String fileName, int chunkId, String chunkData){
        this.fileName = fileName;
        this.chunkId = chunkId;
        this.chunkData = chunkData;
    }

    public String getFileName(){
        return fileName;
    }

    public int getChunkID(){
        return chunkId;
    }

    public String getChunkData(){
        return chunkData;
    }

    public JSONType toJSONType(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("file-name", fileName);
        jsonObject.put("chunk-id", chunkId);
        jsonObject.put("chunk-data", chunkData);
        return jsonObject;
    }

    public String toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("file-name", fileName);
        jsonObject.put("chunk-id", chunkId);
        jsonObject.put("chunk-data", chunkData);
        return jsonObject.toString();
    }

    @Override
    public void deserialize(JSONType arg0) throws InvalidObjectException {
        if(!(arg0 instanceof JSONObject)){
            throw new InvalidObjectException("Invalid JSONType: Expected JSONObject");
        }

        JSONObject jsonObject = (JSONObject) arg0;
        fileName = jsonObject.getString("file-name");
        chunkId = jsonObject.getInt("chunk-id");
        chunkData = jsonObject.getString("chunk-data");
    }

    @Override
    public byte[] serialize() {
        String jsonString = toJSON();
        return jsonString.getBytes(StandardCharsets.UTF_8);
    }
}
