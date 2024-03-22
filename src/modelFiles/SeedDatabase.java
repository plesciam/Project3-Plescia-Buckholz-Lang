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

import java.io.FileNotFoundException;
import java.io.InvalidObjectException;
import java.util.HashMap;
import merrimackutil.json.JSONSerializable;
import merrimackutil.json.types.JSONArray;
import merrimackutil.json.types.JSONObject;
import merrimackutil.json.types.JSONType;


public class SeedDatabase implements JSONSerializable
{
    private HashMap<String, Seed> seeds;    // List of fortunes.
   
    /**
     * Construct a new database from a database object.
     * @param obj the database as a JSON object.
     * @throws InvalidObjectException when obj is not a valid database object.
     */
    public SeedDatabase(JSONObject obj) throws InvalidObjectException
    {
        deserialize(obj);
    }

    /**
     * Get the seed for the given file name.
     * @param fileName the name of the file to get the seed for.
     * @return the seed for {@code fileName}.
     * @throws FileNotFoundException if the file name is unknown (i.e. there is no seed).
     */
    public Seed getSeed(String fileName) throws FileNotFoundException
    {
        if (seeds.containsKey(fileName))
            return seeds.get(fileName);
        else 
            throw new FileNotFoundException("Seed file was not found.");
    }

    /**
     * Uses a JSON object representing the fortune database to populate 
     * this object.
     * @param obj the object representing the database.
     * @throws InvalidObjectException if {@code obj} is not a valid database.
     */
    public void deserialize(JSONType obj) throws InvalidObjectException 
    {
        JSONObject db = null;

        if (!(obj instanceof JSONObject))
            throw new InvalidObjectException("Database should be object.");
            
        db = (JSONObject) obj;

        if (db.containsKey("seeds"))
        {
            JSONArray array = db.getArray("seeds");
            seeds = new HashMap<>();
            for (int i = 0; i < array.size(); i++)
            {
                Seed tmp = new Seed(array.getObject(i));
                seeds.put(tmp.getFileName(), tmp);

            }
        }
        else 
            throw new InvalidObjectException("Database missing array of file seeds.");

        if (db.size() > 1)
            throw new InvalidObjectException("Unexpected fields.");
    }

    /**
     * Converts this database to a JSON string.
     * @return A string representing the object.
     */
    public String serialize() 
    {
        return toJSONType().toJSON();    
    }

    /**
     * Converts {@code this} object to a JSON representation.
     * @return A JSONObject representing this object.
     */
    public JSONType toJSONType() 
    {
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();

        for (String key : seeds.keySet())
            array.add(seeds.get(key));
        obj.put("seeds", array);

        return obj;
    }
    
}