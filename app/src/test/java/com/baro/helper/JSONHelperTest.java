package com.baro.helper;

import com.baro.model.Constant;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class JSONHelperTest {


    @Test
    public void createJSONShouldReturnJSONObject() throws JSONException {
        HashMap<Constant, String> sampleHashMap = new HashMap<>();
        sampleHashMap.put(Constant.COURSE_NAME_KEY, "Course Name");
        sampleHashMap.put(Constant.COURSE_UUID_KEY, "Course UUID");

        JSONObject jsonTest = JSONHelper.createJSON(sampleHashMap);

        assertEquals(jsonTest.length(), 2);
        assertEquals(jsonTest.get(Constant.COURSE_NAME_KEY.toString()), "Course Name");
        assertEquals(jsonTest.get(Constant.COURSE_UUID_KEY.toString()), "Course UUID");

    }
}
