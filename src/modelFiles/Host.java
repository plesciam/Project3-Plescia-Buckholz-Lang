/**
 * Copyright (C) 2023 - 2024  Zachary A. Kissel 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License 
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package modelFiles;

import merrimackutil.json.JSONSerializable;
import merrimackutil.json.types.JSONType;
import merrimackutil.json.types.JSONObject;
import java.io.InvalidObjectException;

/**
 * This class represents a host entry in the hosts collection.
 * 
 * @author Zach Kissel
 */
public class Host implements JSONSerializable 
{
    private String address; // Address of the host.
    private int port; // Port number of the host.

    /**
     * Construct a host entry from the corresponding JSON object.
     * 
     * @param obj a JSON object representing a host entry.
     */
    public Host(JSONObject obj) throws InvalidObjectException 
    {
        deserialize(obj);
    }

    /**
     * Gets the address.
     * 
     * @return the address as a string.
     */
    public String getAddress() 
    {
        return address;
    }

    /**
     * Gets the port number.
     * 
     * @return the port number.
     */
    public int getPort() 
    {
        return port;
    }

    /**
     * Serializes the object into a JSON encoded string.
     * 
     * @return a string representing the JSON form of the object.
     */
    public String serialize() 
    {
        return toJSONType().getFormattedJSON();
    }

    /**
     * Coverts json data to an object of this type.
     * 
     * @param obj a JSON type to deserialize.
     * @throws InvalidObjectException the type does not match this object.
     */
    public void deserialize(JSONType obj) throws InvalidObjectException 
    {
        JSONObject entry;
        if (obj instanceof JSONObject) {
            entry = (JSONObject) obj;

            if (!entry.containsKey("address"))
                throw new InvalidObjectException("Host needs an address address.");
            else
                address = entry.getString("address");

            if (!entry.containsKey("port"))
                throw new InvalidObjectException("Host  needs a port.");
            else
                port = entry.getInt("port");

            if (entry.size() > 2)
                throw new InvalidObjectException("Superflous fields");

        } else
            throw new InvalidObjectException(
                    "Host Entry -- recieved array, expected Object.");
    }

    /**
     * Converts the object to a JSON type.
     * 
     * @return a JSON type either JSONObject or JSONArray.
     */
    public JSONType toJSONType() 
    {
        JSONObject obj = new JSONObject();

        obj.put("address", address);
        obj.put("port", port);

        return obj;
    }
}
