package com.baro.helper;

import com.baro.model.Constant;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JSONHelperTest {


    @Test
    public void createJSONFromHashMapShouldReturnJSONObject() throws JSONException {
        HashMap<Constant, String> sampleHashMap = new HashMap<>();
        sampleHashMap.put(Constant.COURSE_NAME_KEY, "Course Name");
        sampleHashMap.put(Constant.COURSE_UUID_KEY, "Course UUID");

        JSONObject jsonTest = JSONHelper.createJSONFromHashMap(sampleHashMap);

        assertEquals(jsonTest.length(), 2);
        assertEquals(jsonTest.get(Constant.COURSE_NAME_KEY.toString()), "Course Name");
        assertEquals(jsonTest.get(Constant.COURSE_UUID_KEY.toString()), "Course UUID");

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
