package com.baro.helpers;

import com.baro.constants.JSONEnum;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JSONHelperTest {


    @Test
    public void createJSONFromHashMapShouldReturnJSONObject() throws JSONException {
        HashMap<String, String> sampleHashMap = new HashMap<>();
        sampleHashMap.put(JSONEnum.COURSE_NAME_KEY.key, "Course Name");
        sampleHashMap.put(JSONEnum.COURSE_UUID_KEY.key, "Course UUID");

        JSONObject jsonTest = JSONHelper.createJSONFromHashMap(sampleHashMap);

        assertEquals(jsonTest.length(), 2);
        assertEquals(jsonTest.get(JSONEnum.COURSE_NAME_KEY.key), "Course Name");
        assertEquals(jsonTest.get(JSONEnum.COURSE_UUID_KEY.key), "Course UUID");

    }


    @Test
    public void createJSONFromStringShouldReturnJSONObjectWhenValidString() throws JSONException {
        String validJsonString = "{\"key1\": \"value\", \"key2\": 1}";

        JSONObject jsonTest = JSONHelper.createJSONFromString(validJsonString);

        assertEquals(jsonTest.length(), 2);
        assertEquals(jsonTest.get("key1"), "value");
        assertEquals(jsonTest.get("key2"), 1);
    }


    @Test
    public void createJSONFromStringShouldReturnNullWhenInvalidString()  {
        String invalidJsonString = "\"key1\": ";

        JSONObject jsonTest = JSONHelper.createJSONFromString(invalidJsonString);;
        assertNull(jsonTest);
    }
}
