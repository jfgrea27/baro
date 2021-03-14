package com.baro.helper;

import com.baro.model.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JSONHelper {


    public static JSONObject createJSON(HashMap<Constant, String> keyValuePairs) throws JSONException {
        JSONObject json = new JSONObject();

        for(Map.Entry<Constant, String> entry: keyValuePairs.entrySet()) {
            json.put(entry.getKey().toString(), entry.getValue());
        }
        return json;
    }
}
