package com.baro.helpers


import org.json.JSONException
import org.json.JSONObject
import java.util.*

object JSONHelper {
    /**
     * This method creates a JSONObject using a HashMap<String, String.
     * @param keyValuePairs This is a set of key-value pairs to be added to the JSONObject.
     * @return JSONObject This returns the corresponding JSONObject for keyValuePairs if valid;
     * null if an error has been caught.
     */
    fun createJSONFromHashMap(keyValuePairs: HashMap<String?, String?>): JSONObject {
        val json = JSONObject()
        for ((key, value) in keyValuePairs) {
            json.put(key, value)
        }
        return json
    }

    /**
     * This method creates a JSONObject from String.
     * @param jsonString This is a JString representation of a JSON.
     * @return JSONObject This returns the corresponding JSONObject for the jsonString if a valid
     * JSON can be generated; otherwise null.
     */
    fun createJSONFromString(jsonString: String): JSONObject? {
        return try {
            JSONObject(jsonString)
        } catch (e: JSONException) {
            e.printStackTrace()
            null
        }
    }
}