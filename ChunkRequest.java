import java.io.InvalidObjectException;
import java.nio.charset.StandardCharsets;
import merrimackutil.json.*;
import merrimackutil.json.JSONSerializable.*;
import merrimackutil.json.types.JSONObject;
import merrimackutil.json.types.JSONType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ChunkRequest implements JSONSerializable {
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
        return chunkId;
    }

    public JSONType toJSONType(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("file-name", fileName);
        jsonObject.put("chunk-id", chunkId);
        return jsonObject;
    }

    @Override
    public String toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("file-name", fileName);
        jsonObject.put("chunk-id", chunkId);
        return jsonObject.toString();
    }
    @Override
    public byte[] serialize(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("file-name", fileName);
        jsonObject.put("chunk-id", chunkId);

        String jsonString = toJSON();
        return jsonString.getBytes(StandardCharsets.UTF_8);

        try{
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(jsonString.getBytes(StandardCharsets.UTF_8));
            return outputStream.toByteArray();
        } catch (IOException e){
            // Handle IOException
            e.printStackTrace(); // or log the exception
            return null;
        }
    }

    @Override
    public void deserialize(JSONType arg0) throws InvalidObjectException {
        if(!(arg0 instanceof JSONObject))
        throw new InvalidObjectException("Invalid JSONType for deserialization");
    }
        

}
