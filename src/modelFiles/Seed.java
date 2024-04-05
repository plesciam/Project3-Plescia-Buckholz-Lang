/*
 * Copyright (C) 2023 -- 2024 Zachary A. Kissel
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
package modelFiles;

import java.io.InvalidObjectException;
import java.util.ArrayList;

import merrimackutil.json.JSONSerializable;
import merrimackutil.json.types.JSONObject;
import merrimackutil.json.types.JSONType;
import merrimackutil.json.types.JSONArray;

public class Seed implements JSONSerializable
{
    private String fileName;    // The name of the file.
    private int numChunks;      // The number of chunks.
    private ArrayList<Host> hosts;        // Hosts that have some or all of the file.

    /**
     * Load a fortune from an JSON Object.
     * @param obj the JSON object representing the fortune.
     * @throws InvalidObjectException if the object is not a valid 
     * fortune.
     */
    public Seed(JSONObject obj) throws InvalidObjectException
    {
        deserialize(obj);
    }

    /**
     * Get the file name for this seed.
     * @return the file name of the seed.
     */
    public String getFileName() 
    {
        return fileName;
    }

    /**
     * Get the number of chunks associated with this 
     * file.
     * @return the number of chuks.
     */
    public int getNumChunks() 
    {
        return numChunks;
    }

    /**
     * Get the arary of hosts that possess a chunk or the entire file.
     * @return an array list of hosts that have parts or all of the file.
     */
    public ArrayList<Host> getHosts() 
    {
        return hosts;
    }
    
    /**
     * Converts a JSON object repersenting a fortune to populate {@code this}
     * fortune object.
     * @param obj the fortune object.
     */
    public void deserialize(JSONType obj) throws InvalidObjectException 
    {
        JSONObject seed = null;

        if (!(obj instanceof JSONObject))
            throw new InvalidObjectException("Seed should be an object");

        seed = (JSONObject) obj;

        // Get the quote.
        if (seed.containsKey("filename"))
            this.fileName = seed.getString("filename");
        else 
            throw new InvalidObjectException("Seed should have a file name.");
        
        // Get the author.
        if (seed.containsKey("chunks"))
            this.numChunks = seed.getInt("chunks");
        else 
            throw new InvalidObjectException("Seed should have a number of chunks.");

        // Get the array of hostnames.
        if (seed.containsKey("hosts"))
        {
          JSONArray array = seed.getArray("hosts");
          hosts = new ArrayList<>();
          for (int i = 0; i < array.size(); i++)
          {
            hosts.add(new Host(array.getObject(i)));
          }

        }

        if (seed.size() > 3)
            throw new InvalidObjectException("Unknown fields, bad fortune.");
    }

    /**
     * Converts this object to a JSON representation.
     * @return a string representing the object.
     */
    public String serialize() 
    {
        return toJSONType().toJSON();
    }

    /**
     * Converts a fortune object to a JSON representation.
     * @return a JSONObject representing the fortune object.
     */
    public JSONType toJSONType() 
    {
        JSONObject obj = new JSONObject();

        obj.put("filename", fileName);
        obj.put("chunks", numChunks);

        JSONArray array = new JSONArray();
        for (int i = 0; i < hosts.size(); i++)
            array.add(hosts.get(i).toJSONType());

        obj.put("hosts", array);

        return obj;
    }
}