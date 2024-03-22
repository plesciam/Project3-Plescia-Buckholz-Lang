import merrimackutil.json.JSONSerializable;
import merrimackutil.json.types.JSONObject;
import merrimackutil.json.types.JSONType;


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

    @Override
    public JSONType toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("file-name", fileName);
        jsonObject.put("chunk-id", chunkId);
        return jsonObject.toString();
    }

}
