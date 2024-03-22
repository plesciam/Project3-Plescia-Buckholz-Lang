package modelFiles;
/*
 * Copyright (C) 2022 -- 2023 Zachary A. Kissel
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
import java.io.InvalidObjectException;

import merrimackutil.json.JSONSerializable;
import merrimackutil.json.types.JSONObject;
import merrimackutil.json.types.JSONType;

public class Configuration implements JSONSerializable
{
    private int servePort;              // The port to listen on.
    private double failProb = 0.0;      // A probability for channel faults.
    private String fileDir;             // The file share directory.
    private String seedFile;            // The name of the seed file.

    /**
     * Builds a configuration from a JSON object.
     * @throws InvalidObjectException if the configuration object is invalid.
     */
    public Configuration(JSONObject obj) throws InvalidObjectException
    {
        deserialize(obj);
    }

    /**
     * Get the probability of failure for the channel.
     * @return the probability of channel failure.
     */
    public double getFailureProb()
    {
        return failProb;
    }

    /**
     * Gets the port for the server.
     * @return the server port.
     */
    public int getServePort()
    {
        return servePort;
    }

    /**
     * Get the file directory.
     * @return the file directory as a string.
     */
    public String getFileDirectory()
    {
        return fileDir;
    }

    /**
     * Get the seed file name from the configuration file.
     * @return the name of the seed file.
     */
    public String getSeedFile() 
    {
        return seedFile;
    }

    /**
     * Deserialize the object into a configuration.
     */
    public void deserialize(JSONType obj) throws InvalidObjectException 
    {
        JSONObject config = null;

        if (!(obj instanceof JSONObject))
            throw new InvalidObjectException("Expected configuration object.");
        
        config = (JSONObject) obj;
        
        // Try to read the port number.
        if (config.containsKey("serve-port"))
            this.servePort = config.getInt("serve-port");
        else 
            throw new InvalidObjectException("Configuration missing serve port.");

        // Try to read the location of the file data directory.
        if (config.containsKey("file-dir"))
            this.fileDir = config.getString("file-dir");
        else 
            throw new InvalidObjectException("Configuration missing file directory.");

        // Try to read the seed file location.
        if (config.containsKey("seed-file"))
            this.seedFile = config.getString("seed-file");
        else 
            throw new InvalidObjectException("Configuration missing seed file name.");

        // Try to get the size of the pool, this is optional.
        if (config.containsKey("failure-prob")) {
            this.failProb = config.getDouble("failure-prob");

            // Make sure the pool size is sane.
            if (this.failProb < 0.0) {
                System.out.println("Warning: failure probability is negative defaulting to 0.0.");
                this.failProb = 0.0;
            }
            else if (this.failProb > 1.0)
            {
                System.out.println("Warning: failure probability is larger than 1.0 defaulting to 0.0.");
                this.failProb = 0.0;
            }

            if (config.size() > 4)
                throw new InvalidObjectException("Invalid configuration -- superflouous fields.");
            return;
        }

        if (config.size() > 3)
            throw new InvalidObjectException("Invalid configuration -- superflouous fields.");
    }

    /**
     * Serialize the object into a JSON string.
     */
    public String serialize() 
    {
        return toJSONType().toJSON();
    }

    /**
     * Constructs a JSON object representing the configuration.
     */
    public JSONType toJSONType() 
    {
        JSONObject obj = new JSONObject();
        obj.put("serve-port", servePort);
        obj.put("failure-prob", failProb);
        obj.put("file-dir", fileDir);
        obj.put("seed-file", seedFile);
        return obj;
        
    }
}