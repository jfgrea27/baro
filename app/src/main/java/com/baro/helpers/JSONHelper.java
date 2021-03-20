package com.baro.helpers;

import com.baro.constants.JSONEnum;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JSONHelper {


    /**
     * This method creates a JSONObject using a HashMap<Constant, String>. Only valid Constant are
     * possible for the key of the JSON key-value pairs.
     * @param keyValuePairs This is a set of key-value pairs to be added to the JSONObject.
     * @return JSONObject This returns the corresponding JSONObject for keyValuePairs if valid;
     * null if an error has been caught.
     */
    public static JSONObject createJSONFromHashMap(HashMap<String, String> keyValuePairs) {
        JSONObject json = new JSONObject();

        try {

            for(Map.Entry<String, String> entry: keyValuePairs.entrySet()) {
                json.put(entry.getKey(), entry.getValue());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }


    /**
     * This method creates a JSONObject from String.
     * @param jsonString This is a string representation of a JSON.
     * @return JSONObject This returns the corresponding JSONObject for the jsonString if a valid
     * JSON can be generated; otherwise null.
     */
    public static JSONObject createJSONFromString(String jsonString) {
        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
